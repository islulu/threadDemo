package com.luluya.threadDemo.demo04.type02;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 非阻塞式队列
 * @author jairy
 * @date 2019/5/12
 */
public class ConcurrentLinkedQueueDemo {
    //阻塞式队列的好处：防止队列容器溢出，防止丢失数据
    public static void main(String[] args) {
        //非阻塞队列，无界
        ConcurrentLinkedQueue q = new ConcurrentLinkedQueue();
        q.offer("余胜军");
        q.offer("码云");
        q.offer("蚂蚁课堂");
        q.offer("张杰");
        q.offer("艾姐");
        //从头获取元素,删除该元素（只能获取一个
        System.out.println(q.poll());
        //从头获取元素,不刪除该元素
        System.out.println(q.peek());
        //获取总长度
        System.out.println(q.size());
    }

}
