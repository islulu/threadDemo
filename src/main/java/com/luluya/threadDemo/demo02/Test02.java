package com.luluya.threadDemo.demo02;

/**
 * @author jairy
 * @date 2019/5/9
 */
public class Test02 implements Runnable {
    private static int trainCount = 100;
//    private int trainCount = 100;
    private Object oj = new Object();
    public boolean flag = true;

    /**
     * 一个线程使用同步代码块(this明锁),另一个线程使用同步函数。如果两个线程抢票不能实现同步，那么会出现数据错误。
     */
    public void run() {
        if (flag) {
            while (trainCount > 0) {
                synchronized (this) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if (trainCount > 0) {
                        System.out.println(Thread.currentThread().getName() + "," + "出售第" + (100 - trainCount + 1) + "票");
                        trainCount--;
                    }
                }
            }
        } else {
            while (trainCount > 0) {
                sale();
            }
        }
    }

//    public synchronized void sale() {
//        try {
//            Thread.sleep(10);
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        if (trainCount > 0) {
//            System.out.println(Thread.currentThread().getName() + "," + "出售第" + (100 - trainCount + 1) + "票");
//            trainCount--;
//        }
//    }

    /**
     * synchronized 修饰方法使用锁是当前this锁。
     *
     * synchronized 修饰静态方法使用锁是当前类的字节码文件
     */
    public static void sale() {
        synchronized (Test02.class) {
            if (trainCount > 0) {
                System.out.println(Thread.currentThread().getName() + ",出售第" + (100 - trainCount + 1) + "张票");
                trainCount--;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Test02 threadTrain = new Test02();
        Thread t1 = new Thread(threadTrain, "窗口1");
        Thread t2 = new Thread(threadTrain, "窗口2");
        t1.start();
        Thread.sleep(40);
        threadTrain.flag = false;
        t2.start();
    }
}