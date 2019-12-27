springboot集成schedule（深度理解）
背景
 在项目开发过程中，我们经常需要执行具有周期性的任务。通过定时任务可以很好的帮助我们实现。

我们拿常用的几种定时任务框架做一个比较：



从以上表格可以看出，Spring Schedule框架功能完善，简单易用。对于中小型项目需求，Spring Schedule是完全可以胜任的。

 

1、springboot集成schedule
1.1 添加maven依赖包
由于Spring Schedule包含在spring-boot-starter基础模块中了，所有不需要增加额外的依赖。

复制代码
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
复制代码
 

1.2 启动类，添加启动注解
在springboot入口或者配置类中增加@EnableScheduling注解即可启用定时任务。

@EnableScheduling
@SpringBootApplication
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }
}
　　

1.3.添加定时任务
我们将对Spring Schedule三种任务调度器分别举例说明。

1.3.1 Cron表达式

类似于Linux下的Cron表达式时间定义规则。Cron表达式由6或7个空格分隔的时间字段组成，如下图：


常用表达式：


举个栗子：

添加一个work()方法，每10秒执行一次。

注意：当方法的执行时间超过任务调度频率时，调度器会在下个周期执行。

如：假设work()方法在第0秒开始执行，方法执行了12秒，那么下一次执行work()方法的时间是第20秒。

@Component
public class MyTask {
    @Scheduled(cron = "0/10 * * * * *")
    public void work() {
        // task execution logic
    }
}
 

1.3.2 固定间隔任务

 下一次的任务执行时间，是从方法最后一次任务执行结束时间开始计算。并以此规则开始周期性的执行任务。

举个栗子：

添加一个work()方法，每隔10秒执行一次。

例如：假设work()方法在第0秒开始执行，方法执行了12秒，那么下一次执行work()方法的时间是第22秒。

@Scheduled(fixedDelay = 1000*10)
public void work() {
    // task execution logic
}
　　

1.3.3 固定频率任务

 按照指定频率执行任务，并以此规则开始周期性的执行调度。

举个栗子：

添加一个work()方法，每10秒执行一次。

注意：当方法的执行时间超过任务调度频率时，调度器会在当前方法执行完成后立即执行下次任务。

例如：假设work()方法在第0秒开始执行，方法执行了12秒，那么下一次执行work()方法的时间是第12秒。

@Scheduled(fixedRate = 1000*10)
public void work() {
    // task execution logic
}
 

 

2、配置TaskScheduler线程池
 在实际项目中，我们一个系统可能会定义多个定时任务。那么多个定时任务之间是可以相互独立且可以并行执行的。

通过查看org.springframework.scheduling.config.ScheduledTaskRegistrar源代码，发现spring默认会创建一个单线程池。这样对于我们的多任务调度可能会是致命的，当多个任务并发（或需要在同一时间）执行时，任务调度器就会出现时间漂移，任务执行时间将不确定。

protected void scheduleTasks() {
    if (this.taskScheduler == null) {
        this.localExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
    }
    //省略...
}
　　

2.1 自定义线程池
新增一个配置类，实现SchedulingConfigurer接口。重写configureTasks方法，通过taskRegistrar设置自定义线程池。

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
     
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }
}
 



3、实际应用中的问题
 3.1 Web应用中的启动和关闭问题
我们知道通过spring加载或初始化的Bean，在服务停止的时候，spring会自动卸载（销毁）。但是由于线程是JVM级别的，如果用户在Web应用中启动了一个线程，那么这个线程的生命周期并不会和Web应用保持一致。也就是说，即使Web应用停止了，这个线程依然没有结束（死亡）。

解决方法：

1）当前对象是通过spring初始化

spring在卸载（销毁）实例时，会调用实例的destroy方法。通过实现DisposableBean接口覆盖destroy方法实现。在destroy方法中主动关闭线程。

@Component
public class MyTask implements DisposableBean{
    @Override
    public void destroy() throws Exception {
        //关闭线程或线程池
        ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler)applicationContext.getBean("scheduler");
        scheduler.shutdown();
    }
    //省略...
}
　

2）当前对象不是通过spring初始化（管理）

那么我们可以增加一个Servlet上下文监听器，在Servlet服务停止的时候主动关闭线程。

1
2
3
4
5
6
7
public class MyTaskListenter implements ServletContextListener{
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        //关闭线程或线程池
    }
    //省略...
}
 

 

3.2 分布式部署问题
在实际项目中，我们的系统通常会做集群、分布式或灾备部署。那么定时任务就可能出现并发问题，即同一个任务在多个服务器上同时在运行。

解决方法（分布式锁）：

1）通过数据库表锁

2）通过缓存中间件

3）通过Zookeeper实现

 

总结：

spring schedule给我们提供了一套简单、快速、高效、稳定的定时任务框架。但需要考虑线程的生命周期及分布式部署问题。
