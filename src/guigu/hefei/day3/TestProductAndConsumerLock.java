package guigu.hefei.day3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 使用lock 代替synchronized 实现  生产者消费者模型
 * @author:zhaofanqi
 * @create:2020/2/12 11:49
 **/
public class TestProductAndConsumerLock {
    public static void main(String[] args) {
        // 线程操作资源类

        Clerk clerk = new Clerk();
        Productor productor = new Productor(clerk);
        Consumer consumer = new Consumer(clerk);


        new Thread(productor, "Pro_A").start();
        new Thread(consumer, "Con_B").start();
        new Thread(productor, "Pro_AA").start();
        new Thread(consumer, "Con_DD").start();


    }
}

//资源类
class Clerk {

    private int product = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void get() {
        lock.lock();
        try {
            while (product > 0) {// 多线程操作要用while
                System.out.println(Thread.currentThread().getName() + ": 已经堆满");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " 生产后有 ： " + ++product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void sale() {
        lock.lock();
        try {
            while (product <= 0) {
                System.out.println(Thread.currentThread().getName() + " 缺货：" + product);
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " 售卖完成，剩余" + --product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}

class Consumer implements Runnable {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}


class Productor implements Runnable {
    private Clerk clerk;

    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

