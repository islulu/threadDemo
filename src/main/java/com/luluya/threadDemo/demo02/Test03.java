package com.luluya.threadDemo.demo02;

/**
 * @author jairy
 * @date 2019/5/9
 */
public class Test03 implements Runnable {
    private int trainCount = 100;
    private Object oj = new Object();
    public boolean flag = true;

    /**
     * 多线程死锁
     *
     * 同步中嵌套同步,导致锁无法释放
     */
    public void run() {
        if (flag) {
            while (trainCount > 0) {
                synchronized (oj) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    sale();
                }
            }
        } else {
            while (trainCount > 0) {
                sale();
            }
        }
    }
    public synchronized void sale() {
        synchronized (oj) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
            if (trainCount > 0) {
                System.out.println(Thread.currentThread().getName() + "," + "出售第" + (100 - trainCount + 1) + "票");
                trainCount--;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Test03 threadTrain = new Test03();
        Thread t1 = new Thread(threadTrain, "窗口1");
        Thread t2 = new Thread(threadTrain, "窗口2");
        t1.start();
        Thread.sleep(40);
        threadTrain.flag = false;
        t2.start();
    }
    
}