package com.luluya.threadDemo.demo03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jairy
 * @date 2019/5/10
 */
public class Test02 {

    public static void main(String[] args) {
        Resput res = new Resput();
        Condition newCondition = res.lock.newCondition();

        InputThread inThread = new InputThread(res,newCondition);
        OutputThread outThread = new OutputThread(res,newCondition);
        inThread.start();
        outThread.start();
    }

}

class Resput{

    public String name;

    public String sex;
    //线程通讯标识
    public boolean flag = false;

    Lock lock = new ReentrantLock();
}

class InputThread extends Thread{
    private Resput resput;
    Condition newCondition;

    public InputThread(Resput resput,Condition newCondition){
        this.resput = resput;
        this.newCondition = newCondition;
    }

    @Override
    public void run() {
        int count = 0;
        while (true){
            try {
                resput.lock.lock();
                if(resput.flag) {
                    try {
                        newCondition.await();//当前线程变为等待，释放锁
                    }catch (Exception e) {

                    }
                }
                if(count == 0){
                    resput.name = "小红";
                    resput.sex = "女";
                }else{
                    resput.name = "小绿";
                    resput.sex = "男";
                }
                count = (count+1)%2;
                resput.flag = true;
                newCondition.signal();//唤醒当前线程
            } catch (Exception e) {
                resput.lock.unlock();
            }

        }
    }
}

class OutputThread extends Thread{
    private Resput resput;
    Condition newCondition;

    public OutputThread(Resput resput,Condition newCondition){
        this.resput = resput;
        this.newCondition = newCondition;
    }

    @Override
    public void run() {
        while (true){
            try {
                resput.lock.lock();
                if(!resput.flag){
                    try {
                        newCondition.await();
                    } catch (InterruptedException e) {

                    }
                }
                System.out.println(resput.name + "---" + resput.sex);
                resput.flag = false;
                newCondition.signal();
            }finally {
                resput.lock.unlock();
            }
        }
    }
}