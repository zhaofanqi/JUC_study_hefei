package guigu.hefei.day2;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 知识点为 ConcurrentHashMap  ,举例使用的是  CopyOnWriteArrayList
 * @author:zhaofanqi
 * @create:2020/2/11 13:23
 **/
public class ConCurrentHashMap {
    public static void main(String[] args) {
        ConC cc = new ConC();
        for (int i = 0; i < 10; i++) {
            new Thread(cc).start();
        }


    }
}

class ConC implements Runnable {

    // Collections 工具提供的方式存在并发写入问题：如在  it.hasNext() 中执行添加操作会报错

    //    public static   List<String> list= Collections.synchronizedList(new ArrayList<String>());
//    public static   List<String> list= new CopyOnWriteArrayList<String>(); // 2 此时虽然不报错，但是会有很多 DDD
    public static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>(); // 2 此时虽然不报错，但是会有很多 DDD

    static {
        list.add("AA");
        list.add("BB");
        list.add("CC");
    }

    @Override
    public void run() {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(Thread.currentThread().getName() + " :  " + it.next());
            list.add("DDD");//  1 此时即使是单个线程也会出错
            /**
             *  使用的 ReentrantLock  (可重入锁)
             public boolean add(E e) {
             final ReentrantLock lock = this.lock;
             lock.lock();
             try {
             Object[] elements = getArray();
             int len = elements.length;
             Object[] newElements = Arrays.copyOf(elements, len + 1);
             newElements[len] = e;
             setArray(newElements);
             return true;
             } finally {
             lock.unlock();
             }
             }
             */
        }
    }

    public boolean add(Object e ){
        final ReentrantLock lock=new ReentrantLock();
        lock.lock();
        try {
            String[] array= (String[]) list.toArray();
            int  len=array.length;
            Object[] newArray = Arrays.copyOf(array, len + 1);
            newArray[len]=e;
            Arrays.asList(newArray);
            return true;
        } finally {
            lock.unlock();
        }
    }
}
