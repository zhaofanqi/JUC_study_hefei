package guigu.hefei.day1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:${volatile 的原子性案例}
 * @author:zhaofanqi
 * @create:2020/2/10 21:02
 **/
public class TestVola_2 {
    public static void main(String[] args) {
        // volatile  的不保证原子性：同一个变量在同一时刻可以被多个线程操作
        /**
         *  原子性理解：某变量同一时刻只能被一个线程修改时，不可以有其他线程去修改
         *  典型的 i++  即使使用了 volatile  使 i 变得可见，但是 可能已经执行 ++ 结束，开始写回。这就不是原子性，因为读-改-写回 的过程里，读的值不是主内存中的值
         *
         *  对于 不能保证原子性的问题如何解决呢？
         *      使用原子变量：如             new AtomicInteger() 这些变量都使用volatile
         *      原子变量 是通过  CAS 算法保证的。CAS 包含了三个操作数
         *          内存值(此处使用的内存地址哦)  V
         *          预估值  A
         *          更新值  B
         *          当且仅当 V== A 时，才使用  V=B 更新内存中的值。否则不做任何操作（即在更新主内存中的变量值前，需要再次检查该值是否发生不同）
         *                      -- 后面还会有ABA 问题
         */
        // 先看 i++   ++i
        AtomicDemo ad = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }

    }
}

class AtomicDemo implements Runnable {

//    public int i;
    public AtomicInteger i=new AtomicInteger();

    @Override
    public void run() {
//        System.out.println(Thread.currentThread().getName() + " : " + getI());  //添加该行输出，方便理解，当前线程 getI 获取的值已经不是自己线程操作的目标结果了，而是主内存中的值
        i.getAndAdd(1);
        System.out.println(Thread.currentThread().getName() + " : " + getI());
    }

  /*  public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }*/

    public AtomicInteger getI() {
        return i;
    }

    public void setI(AtomicInteger i) {
        this.i = i;
    }
}
