package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/8
 */

/**
 * 1.什么是线程：线程是一条执行路径，每个线程都互不影响
 * 2.什么是多线程：多线程在一个进程中，有多条不同的执行路径，并行执行。目的为了提高程序效率。
 * 3.在一个进程中，一定会有主线程
 * 4.如果连线程、主线程都没有，怎么执行程序
 *     线程的分类：用户线程、守护线程
 *     主线程、子线程、GC线程
 */
public class Test01 {

    /**
     * 1,继承Thread类
     * 2,实现Runnable接口（比较好，可实现多个接口～
     * 3,使用匿名内部类方式
     * 4,使用线程池进行管理「推荐」
     *
     * 此为 主线程
     */
    public static void main(String[] args) {
        //1.创建线程
        System.out.println("-----main 主线程创建开始-----");
        ThreadDemo01 threadDemo01 = new ThreadDemo01();
        //2.启动线程
        System.out.println("-----main 主线程创建启动-----");
        threadDemo01.start();// 并行执行
//        threadDemo01.run();// 顺序执行
        for (int i = 0; i< 10; i++) {
            System.out.println("main 主线程 i:" + i);
        }
        System.out.println("-----main 主线程创建结束-----");

        /**
         * 同步：（单线程）
         *  代码从上往下执行
         *
         * 异步：（多线程包含异步概念。每个线程都有自己独立线程进行执行，互不影响）
         *  新的一条执行路径不会影响其他线程
         */
    }

}

/**
 * 1.继承Thread类，重写run方法，run方法中需要线程执行代码
 */
class ThreadDemo01 extends Thread{
    /**
     * run 方法中，需要线程需要执行代码
     *
     * 此为 子线程
     */
    @Override
    public void run() {
        super.run();
        for (int i = 0; i< 10; i++) {
            System.out.println("子线程 i:" + i);
        }
    }
}
