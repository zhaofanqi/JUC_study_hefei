package guigu.hefei.day1;

import java.util.Date;
import java.util.Timer;

/**
 * @description: 开始学习volatile    volatile 的主要特征：可见性，不保证原子性，禁止指令重排
 * @author:zhaofanqi
 * @create:2020/2/10 18:52
 **/
public class TestVola_1
{
    public static void main(String[] args) {
        TestDemo td = new TestDemo();// 执行类加载器的时候，这个属性也被读取了
        //  执行完start 后，该对象的run()才被执行
        new Thread(td).start();
        while (true){
//            System.out.println(td.isFlag());
            /*synchronized (td){
                此处使用 synchronized 包含核心代码块也可以，只是实际使用需要考虑锁的效率不是很高
            }*/
            if (td.isFlag()){
                System.out.println(System.currentTimeMillis());
                System.out.println("--------------");
                break;
                /**
                 * 当使用volatile 时 ，会中断
                 * 当不使用volatile时，程序一直运行
                 */
            }

        }
    }

}


class TestDemo implements Runnable{

//    public volatile   boolean flag=false; // 此处的  flag 是否使用volatile  体现在多线程调用时，是否被可见
    public    boolean flag=false;
    @Override
    public void run() {
        System.out.println("start new thread "+ flag);
        try {
            Thread.sleep(1*1000);// 为了保证 main 先读到的是flag 是 false
            flag=true;
            System.out.println(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}