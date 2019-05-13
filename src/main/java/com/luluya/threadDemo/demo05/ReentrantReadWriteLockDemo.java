package com.luluya.threadDemo.demo05;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jairy
 * @date 2019/5/12
 */
public class ReentrantReadWriteLockDemo{

    static Map<String, String> map = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = rwl.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = rwl.writeLock();

    private static final String get(String key){
        readLock.lock();
        try {
            System.out.println("正在做读的操作,key:" + key + " 开始");
            Thread.sleep(100);
            String value = map.get(key);
            System.out.println("正在做读的操作,key:" + key + " 结束");
            return value;
        } catch (InterruptedException e) {

        } finally {
            readLock.unlock();
        }
        return key;
    }

    private static final String set(String key, String value){
        writeLock.lock();
        try {
            System.out.println("正在做写的操作,key:" + key + ",value:" + value + "开始.");
            Thread.sleep(100);
            String put = map.put(key, value);
            System.out.println("正在做写的操作,key:" + key + ",value:" + value + "结束.");
            System.out.println();
            return put;
        }catch (InterruptedException e){

        }finally {
            writeLock.unlock();
        }
        return value;
    }

    // 清空所有的内容
    public static final void clear() {
        writeLock.lock();
        try {
            map.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ReentrantReadWriteLockDemo.set(i + "", i + "");
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ReentrantReadWriteLockDemo.get(i + "");
                }

            }
        }).start();
    }

}
