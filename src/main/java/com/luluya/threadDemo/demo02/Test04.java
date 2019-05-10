package com.luluya.threadDemo.demo02;

/**
 * @author jairy
 * @date 2019/5/9
 */
public class Test04 extends Thread {
    private Res res;

    public Test04(Res res) {
        this.res = res;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "---" + "i---" + i + "--num:" + res.getNum());
        }
    }

    public static void main(String[] args) {
        Res res = new Res();
        Test04 threadLocaDemo1 = new Test04(res);
        Test04 threadLocaDemo2 = new Test04(res);
        Test04 threadLocaDemo3 = new Test04(res);
        threadLocaDemo1.start();
        threadLocaDemo2.start();
        threadLocaDemo3.start();
    }
}

class Res {
    // 生成序列号共享变量
    public static Integer count = 0;

    /**
     * 返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。
     *
     * 这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。
     * ThreadLocal中的缺省实现直接返回一个null。
     */
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    public Integer getNum() {
        int count = threadLocal.get() + 1;
        threadLocal.set(count);
        return count;
    }
}