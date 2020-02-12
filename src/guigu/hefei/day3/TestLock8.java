package guigu.hefei.day3;

/**
 * @description:  线程8 锁 理解案例
 * @author:zhaofanqi
 * @create:2020/2/12 14:57
 **/

import javax.print.attribute.standard.NumberUp;

/**
 * 8 锁  理解的核心：  ① 静态方法的锁为  Class 实例 ；非静态方法的锁默认为 this 对象
 *                      ② 某一个时刻内，只有有一个线程拥有锁，无论几个方法
 *
 *   测试条件          方法                       方法                            sleep                          一个对象                   输出结果                   懂否
 *     1            Syn.getOne()                 Syn.getTwo                          无                             1                        one，two
 *     2            Syn.getOne()                 Syn.getTwo                      getOne Thread.sleep                 1                       1,2
 *     3           Syn.getOne()                 Syn.getTwo   getThree            getOne Thread.sleep                1                        312
 *     4            Syn.getOne()                 Syn.getTwo                      getOne Thread.sleep                 2                       21
 *     5          static Syn.getOne()           Syn.getTwo                       getOne Thread.sleep                 1                        21
 *     6          static Syn.getOne()     static Syn.getTwo                       getOne Thread.sleep                1                       12                        不
 *     7          static Syn.getOne()           Syn.getTwo                       getOne Thread.sleep                2                         21                        不
 *     8          static Syn.getOne()     static Syn.getTwo                       getOne Thread.sleep                2                       12                      不
 *
 */
public class TestLock8 {
    public static void main(String[] args) {
        //线程操作资源类
        Number number = new Number();
        Number number2 = new Number();

        new Thread(new Runnable() {
            @Override
            public void run() {
               number.getOne();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                number2.getTwo();
            }
        }).start();
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                number.getThree();
            }
        }).start();*/
    }
}

class Number{

    public static  synchronized   void  getOne(){
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public static synchronized void getTwo(){
        System.out.println("two");
    }

    public void getThree(){
        System.out.println("Three");
    }
}
