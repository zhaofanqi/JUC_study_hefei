package guigu.hefei.day3;

/**
 * @description: 多线程去执行多个任务， 存在部分线程执行完任务，而部分线程阻塞，导致最终时间较长
 * @author:zhaofanqi
 * @create:2020/2/12 19:24
 **/

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 多线程计算  1~ 100 亿
 * 利用 fork/ join    就是将任务进行切分 后再并行计算  最后进行汇总
 */

public class ForkJoinTest {
    public static void main(String[] args) throws  Exception {

        System.out.println("当前电脑核数为 ： "+Runtime.getRuntime().availableProcessors());
        ForkJoinPool pool = new ForkJoinPool();
        // 线程操作资源类
        ForkJoinSum task = new ForkJoinSum(0L, 10  * 1000L);

        System.out.println("  方式一  ："+pool.submit(task).get());
        long sum = pool.invoke(task);
        System.out.println(" 方式 二  ："+sum);
    }
}

//求和
class ForkJoinSum extends RecursiveTask<Long> {

    private static final long THURSHOLD = 10 * 1000L; //  确定  进行任务分割的条件
    private Long start;
    private Long end;

    public ForkJoinSum(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {// 注意任务需要递归切分

        if ((end - start) < THURSHOLD) {
            long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            long mid = (start + end) / 2;
            ForkJoinSum left = new ForkJoinSum(start, mid);
            left.fork();//对左边进行递归分割任务
            ForkJoinSum right = new ForkJoinSum(mid + 1, end);
            right.fork(); //  对右边进行递归分割任务
            return left.join() + right.join();
        }

    }
}