package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/8
 */
class Test02 {

    public static void main(String[] args) {
        //1.创建线程
        System.out.println("-----main 主线程创建开始-----");
        RunnableDemo threadDemo02 = new RunnableDemo();
        Thread t1 = new Thread(threadDemo02);
        //2.启动线程
        System.out.println("-----main 主线程创建启动-----");
        t1.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("main 主线程 i:" + i);
        }
        System.out.println("-----main 主线程创建结束-----");
    }

}

/**
 * 1.实现Runnable接口，重写run方法
 */
public class RunnableDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("子线程 i:" + i);
        }
    }
}
