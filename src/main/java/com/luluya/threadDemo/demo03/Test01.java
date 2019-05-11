package com.luluya.threadDemo.demo03;


/**
 * @author jairy
 * @date 2019/5/10
 */
public class Test01 {

    public static void main(String[] args) {
        Res res = new Res();
        InThread inThread = new InThread(res);
        OutThread outThread = new OutThread(res);
        inThread.start();
        outThread.start();
    }

}

class Res {

    public String name;

    public String sex;
    //线程通讯标识
    public boolean flag = false;

}

class InThread extends Thread {
    private Res res;

    public InThread(Res res) {
        this.res = res;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            synchronized (res) {
                if (res.flag) {
                    try {
                        res.wait();//当前线程变为等待，释放锁
                    } catch (InterruptedException e) {

                    }
                }
                if (count == 0) {
                    res.name = "小红";
                    res.sex = "女";
                } else {
                    res.name = "小绿";
                    res.sex = "男";
                }
                count = (count + 1) % 2;
                res.flag = true;
                res.notify();//唤醒当前线程
            }
        }
    }
}

class OutThread extends Thread {
    private Res res;

    public OutThread(Res res) {
        this.res = res;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (res) {
                if (!res.flag) {
                    try {
                        res.wait();
                    } catch (InterruptedException e) {

                    }
                }
                System.out.println(res.name + "---" + res.sex);
                res.flag = false;
                res.notify();
            }
        }
    }
}