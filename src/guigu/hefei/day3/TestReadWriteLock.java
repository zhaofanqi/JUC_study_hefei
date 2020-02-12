package guigu.hefei.day3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: ReadWriteLock  简单案例  1个写，多个读 。 读写锁  ：允许多线程读，仅仅允许一个线程写
 * @author:zhaofanqi
 * @create:2020/2/12 14:20
 **/
public class TestReadWriteLock {
    public static void main(String[] args) {


        CountDownLatch latch = new CountDownLatch(102);

        // 线程操作资源类
        RWDemo rwDemo = new RWDemo(latch);
        for (int i = 0; i <2 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rwDemo.set(10);
                }
            }).start();
        }



        for (int i = 0; i <100 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rwDemo.get();
                }
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finish");

    }
}

class RWDemo {

    private int num = 0;
    private CountDownLatch latch;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public RWDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    public void get() {
        lock.readLock().lock();
        try {
            Thread.sleep(1000); // 输出时 ，没有停顿，而是一次性直接输出
            System.out.println( System.currentTimeMillis() + ":" + num);
            latch.countDown();
        } catch (Exception e){
           e.printStackTrace();
        }  finally{
            lock.readLock().unlock();
        }
    }

    public void set(int num) {
        lock.writeLock().lock();
        try {
            this.num = num;
            Thread.sleep(2000);
            System.out.println(System.currentTimeMillis() +"----write done---"); //输出时，时间上存在差异
            latch.countDown();
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}