package guigu.hefei.day2;

/**
 * @description: 创建执行线程  使用  callable
 * @author:zhaofanqi
 * @create:2020/2/11 18:59
 **/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 *  callable 与runnable 的区别： 1 callable 有返回值 ，如果接受线程的返回值，会让调用线程(本案例中为主线程) 阻塞
 *                               2  该接口泛型
 *                               3  可能会抛出异常
 *                      callable 需要使用 FutureTask 实现类的支持，用于接收返回结果
 */

public class CallableDemo {
    public static void main(String[] args) {
        CallTest callTest = new CallTest();

        FutureTask<Integer> ft = new FutureTask<Integer>(callTest);

        new Thread(ft).start();
        try {
            Integer sum = ft.get();
            System.out.println("final result :" + sum);
            System.out.println("------------------");// 改行代码一定在最后，因为 FutureTask 源代码明确指出 ft 获取结果要等待计算结束
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class CallTest implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            sum += i;
        }
        return sum;
    }
}