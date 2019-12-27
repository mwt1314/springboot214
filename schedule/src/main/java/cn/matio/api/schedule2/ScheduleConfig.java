package cn.matio.api.schedule2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */
/*
 在实际项目中，我们一个系统可能会定义多个定时任务。那么多个定时任务之间是可以相互独立且可以并行执行的。

通过查看org.springframework.scheduling.config.ScheduledTaskRegistrar源代码，发现spring默认会创建一个单线程池。
这样对于我们的多任务调度可能会是致命的，当多个任务并发（或需要在同一时间）执行时，任务调度器就会出现时间漂移，任务执行时间将不确定。
protected void scheduleTasks() {
    if (this.taskScheduler == null) {
            // 从这一行可以看出, 定时调度任务初始化时候初始化了一个单线程的线程池
            // 所以在定时任务调度时候, 如果定时任务过多, 就会存在线程争抢
            // 而且每一次也只会有一个定时任务运行
        this.localExecutor = Executors.newSingleThreadScheduledExecutor();
        this.taskScheduler = new ConcurrentTaskScheduler(this.localExecutor);
    }
    //省略...
}

自定义线程池:新增一个配置类，实现SchedulingConfigurer接口。重写configureTasks方法，通过taskRegistrar设置自定义线程池
 */
@Component
@ConditionalOnProperty(name = "schedule.pool.enable", value = "true", matchIfMissing = true)
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }

}
