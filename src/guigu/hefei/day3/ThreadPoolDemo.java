package guigu.hefei.day3;

/**
 * @description:  了解线程池
 * @author:zhaofanqi
 * @create:2020/2/12 16:17
 **/

import java.util.Random;
import java.util.concurrent.*;

/**
 *   1 线程池 ：提供了一个线程队列，队列中保存着已经创建好的线程，一定程度上避免了创建与销毁的开销，提高响应速度
 *   2 线程池的根接口  Executor ： 负责线程的使用与调度
 *                      \--** ExecutorService  子接口：线程池的主要接口
 *                          \-- ThreadPoolExecutor  线程池的实现类
 *                          \--** ScheduledExecutorService 子接口 负责线程的调度
 *                              \-- ScheduledThreadPoolExecutor : 继承了ThreadPoolExecutor 实现了ScheduledExecutorService
 *  3  工具类：Executors 提供了工厂方法
 *                  newFixedThreadPool()    固定大小的线程池
 *                  newCachedThreadPool()   缓存线程池，线程池的数量不固定，可以根据需求自动更改数量
 *                  newSingleThreadExecutor() 创建只有一个线程的线程池
 *                  newScheduledThreadPool()  创建固定大小的线程，并且可以调度
 *
 *
 */
public class ThreadPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //1 准备线程池
        //2 为线程池分配任务
        //3 关闭线程池
        ExecutorService es = Executors.newFixedThreadPool(5);
        ThreadPoolTest tpt = new ThreadPoolTest();
        for (int i = 0; i <10 ; i++) {
            /*es.submit(new Runnable() {
                @Override
                public void run() {
                    tpt.cal();
                }
            });*/
            Future<Integer> future = es.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    int sum = 0;
                    for (int j = 0; j <= 100; j++) {
                        sum += j;
                    }
                    return sum;
                }
            });
            System.out.println(Thread.currentThread().getName()+" : "+future.get());
        }

        es.shutdown();


        //------------------ 定时调度线程 newScheduledThreadExecutor------------------------------------------
        System.out.println("===========new  Test ==================");
        ScheduledExecutorService pools = Executors.newScheduledThreadPool(5);
        for (int i = 0; i <10 ; i++) {

            Future<Integer> fu = pools.schedule(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int n = new Random().nextInt(100);
                    System.out.println(Thread.currentThread().getName() + " : " + n);
                    return n;
                }
            },3,TimeUnit.SECONDS);
            System.out.println(fu.get());
        }

        pools.shutdown();


    }
}


class  ThreadPoolTest{

    public void cal(){
        int sum=0;
        for (int i = 0; i <=100 ; i++) {
            sum+=i;
        }
        System.out.println(Thread.currentThread().getName()+" ： "+sum);
    }
}