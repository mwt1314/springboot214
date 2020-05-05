package chain;

/**
 * 组长
 *
 * @author lzy
 *
 */
public class GroupLeader implements Ratify {
 
     @Override
     public Result deal(Chain chain) {
          Request request = chain.request();
          System.out.println("GroupLeader=====>request:" + request.toString());
 
          if (request.days() > 1) {
              // 包装新的Request对象
              Request newRequest = new Request.Builder().newRequest(request)
                        .setManagerInfo(request.name() + "平时表现不错，而且现在项目也不忙")
                        .build();
              return chain.proceed(newRequest);
          }
 
          return new Result(true, "GroupLeader：早去早回");
     }
}
 
 
/**
 * 经理
 *
 * @author lzy
 *
 */
class Manager implements Ratify {
 
     @Override
     public Result deal(Chain chain) {
          Request request = chain.request();
          System.out.println("Manager=====>request:" + request.toString());
          if (request.days() > 3) {
              // 构建新的Request
              Request newRequest = new Request.Builder().newRequest(request)
                        .setManagerInfo(request.name() + "每月的KPI考核还不错，可以批准")
                        .build();
              return chain.proceed(newRequest);
 
          }
          return new Result(true, "Manager：早点把事情办完，项目离不开你");
     }
 
}
 
/**
 * 部门领导
 *
 * @author lzy
 *
 */
class DepartmentHeader implements Ratify {
 
     @Override
     public Result deal(Chain chain) {
          Request request = chain.request();
          System.out.println("DepartmentHeader=====>request:"
                   + request.toString());
          if (request.days() > 7) {
              return new Result(false, "你这个完全没必要");
          }
          return new Result(true, "DepartmentHeader：不要着急，把事情处理完再回来！");
     }
 
}