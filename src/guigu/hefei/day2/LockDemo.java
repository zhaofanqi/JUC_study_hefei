package guigu.hefei.day2;

/**
 * @description: 解决多线程安全问题方式
 * @author:zhaofanqi
 * @create:2020/2/11 20:47
 **/

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 经典案例：售卖车票
 * 不加锁 会出现车票超卖的现象
 * 加锁 即可
 */
public class LockDemo {
    public static void main(String[] args) {
        LockTest lt = new LockTest();
        for (int i = 0; i < 5; i++) {
            new Thread(lt).start();
        }
    }
}

class LockTest implements Runnable {

    private int ori = 100;
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        lock.lock();
        try {
            while (true) {
                if (ori > 0) {
                    --ori;
                    System.out.println(Thread.currentThread().getName() + " 售卖 ，剩余 ：" + ori);
                } else {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
