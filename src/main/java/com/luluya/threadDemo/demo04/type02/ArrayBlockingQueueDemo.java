package com.luluya.threadDemo.demo04.type02;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞式队列
 * 有边界的阻塞队列，内部实现是一个数组。以先进先出的方式存储数据
 *
 * @author jairy
 * @date 2019/5/12
 */
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        //设置最多支持3队列总数
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        boolean b1 = blockingQueue.offer("李四");//添加非阻塞式
        System.out.println(b1);
        boolean b2 = blockingQueue.offer("张军",1, TimeUnit.SECONDS);//添加阻塞式
        System.out.println(b2);
        boolean b3 = blockingQueue.offer("李四");//添加非阻塞式
        System.out.println(b3);
        boolean b4 = blockingQueue.offer("张军", 1, TimeUnit.SECONDS);//添加阻塞式
        System.out.println(b4);

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll(1, TimeUnit.SECONDS));
    }

}
