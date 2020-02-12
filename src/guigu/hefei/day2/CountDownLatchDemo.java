package guigu.hefei.day2;

/**
 * @description: CountDownLatch 用于等待一组线程执行完成以后才去执行：  可以 理解为 闭锁（上面执行结束以后才执行）
 * 如多个线程分别去操作数据，而主线程需要所有的操作完成以后才能去执行
 * @author:zhaofanqi
 * @create:2020/2/11 17:39
 **/


import java.util.concurrent.CountDownLatch;

/**
 * 案例：3 个线程按顺序输出  A  B  C.不能控制顺序，但是可以控制只输出指定次数
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {
        final int count=5;
        CountDownLatch latch = new CountDownLatch(count);

        LatchDemo ld = new LatchDemo(latch);

        for (int i = 0; i < count; i++) {// 创建的线程数 大于  计数的值3 此时  latch.await 失效. 所以使用 相同变量
            new Thread(ld).start();
        }

        try {
            latch.await();
            System.out.println("finish ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {// 此处添加 try  finally 是为了等输出结束以后才执行 countDown 的操作
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        } finally {
            latch.countDown();
        }

    }
}

