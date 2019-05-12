package com.luluya.threadDemo.demo04.type02;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 阻塞式队列
 * 队列大小的配置是可选的，以先进先出的方式存储数据
 *
 * @author jairy
 * @date 2019/5/12
 */
public class LinkedBlockingQueueDemo {

    public static void main(String[] args) {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(3);
        linkedBlockingQueue.add("张三");
        linkedBlockingQueue.add("李四");
        linkedBlockingQueue.add("李四");
        //linkedBlockingQueue.add("李四"); 超过长度会报错
        System.out.println(linkedBlockingQueue.size());

        LinkedBlockingQueue linkedBlockingQueue2 = new LinkedBlockingQueue();
        linkedBlockingQueue2.add("张三");
        linkedBlockingQueue2.add("李四");
        System.out.println(linkedBlockingQueue2.size());
    }

}
