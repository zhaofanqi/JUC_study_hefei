package guigu.hefei.day2;

/**
 * @description: 使用synchronized 关键字  实现生产者消费者模型
 * 存在的场景： 程序不结束   由于生产者中 加了 睡眠时间，最后是 生产者 生产出来了，可是没有消费者消费，而生产的次数还没打，this.wait() 无法停止
 *              所以要保证 每有一次生产就有一次消费
 * 使用if 判断而不是 while 循环判断 会导致为负数的情况(多个线程时)
 * @author:zhaofanqi
 * @create:2020/2/12 0:28
 **/
public class TestProductAndConsumer {

    public static void main(String[] args) {
        // 案例是： 销售员卖商品
        // 线程 控制 资源类
        Clerk clerk = new Clerk();
        Productor productor = new Productor(clerk);
        Consumer consumer = new Consumer(clerk);

        new Thread(productor,"Pro_A").start();
        new Thread(consumer,"Con_B").start();
        new Thread(productor, "Pro_C").start();
        new Thread(consumer, "Con_D").start();

    }
}


// 资源类
class Clerk {

    private int product = 0;

    // 进商品
    public synchronized void get() {
//        if (product > 0) {
       while (product > 0) {
            System.out.println(Thread.currentThread().getName() + "货物已经堆满 : "+product);
            try {
                this.wait();
            } catch (InterruptedException e) {}
        } //else {
            System.out.println(Thread.currentThread().getName() + "增加后有："+ ++product );
            this.notifyAll();
       // }
    }

    public synchronized void sale() {
//        if (product <= 0) {
        while (product <= 0) {
            System.out.println(Thread.currentThread().getName() + "缺货 : "+product);
            try {
                this.wait();
            } catch (InterruptedException e) {}
        }// else {
            System.out.println(Thread.currentThread().getName() + "销售后剩余："+ --product );
            this.notifyAll();
       // }
    }
}

//线程
class Productor implements Runnable {
    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    private Clerk clerk;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(2*100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clerk.get();
        }
    }
}

//线程
class Consumer implements Runnable {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        //消费商品
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }

    }
}