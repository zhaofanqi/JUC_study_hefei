package guigu.hefei.day3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 按照顺序打印 ABC
 * @author:zhaofanqi
 * @create:2020/2/12 12:16
 **/
public class Alternative {
    public static void main(String[] args) {
        // 线程操作资源类
        AlternativeDemo alternativeDemo = new AlternativeDemo();
        Th th = new Th(alternativeDemo);

        try {
            new Thread(th, "C").start();
            Thread.sleep(2 * 100);
            new Thread(th, "B").start();
            Thread.sleep(2 * 100);
            new Thread(th, "A").start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

//资源类
class AlternativeDemo {
    private Lock lock = new ReentrantLock();
    private int num = 1;
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void soutA(int i) {
        lock.lock();
        try {
            if (num!=1) {
                try {
                    conditionA.await();
                } catch (InterruptedException e) {
                    //                e.printStackTrace();
                }
            }
            num=2;
            System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
            conditionB.signal();
        } finally {
            lock.unlock();
        }
    }

    public void soutB(int i) {
        lock.lock();
        try {
            if (num!=2) {
                try {
                    conditionB.await();
                } catch (InterruptedException e) {
                    //                    e.printStackTrace();
                }
            }
            num=3;
            System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
            conditionC.signal();
        } finally {
            lock.unlock();
        }
    }

    public void soutC(int i) {
        lock.lock();
        try {
//            if (Thread.currentThread().getName() != "C") {// 直接线程名称控制 会重复输出
            if (num!=3) {// 直接线程名称控制 会重复输出
                try {
                    conditionC.await();
                } catch (InterruptedException e) {
                    //                e.printStackTrace();
                }
            }
            num=1;
            System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
            conditionA.signal();
        } finally {
            lock.unlock();
        }
    }
    /*// 这种方法 得到的数据不能正确停止
    public void sout(int i) {
        lock.lock();
        try {
            if (Thread.currentThread().getName() == "A") {
                System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
                conditionB.signal();
                conditionA.await();
            } else if (Thread.currentThread().getName() == "B") {
                System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
                conditionC.signal();
                conditionB.await();
            } else if (Thread.currentThread().getName() == "C") {
                System.out.println(Thread.currentThread().getName() + "第 " + i + " 遍");
                conditionA.signal();
                conditionC.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }*/


}

//线程
class Th implements Runnable {
    private AlternativeDemo ad;

    public Th(AlternativeDemo ad) {
        this.ad = ad;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (Thread.currentThread().getName() == "A") {
                ad.soutA(i);
            } else if (Thread.currentThread().getName() == "B") {
                ad.soutB(i);
            } else if (Thread.currentThread().getName() == "C") {
                ad.soutC(i);
            }
        }
    }
}