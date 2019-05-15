package com.luluya.threadDemo.demo01;

/**
 * @author jairy
 * @date 2019/5/12
 */

//data公共数据接口
interface Data {
    public abstract String getRequest();
}

//获取真实数据
class RealData implements Data {
    private String requestResult;

    //data参数
    public RealData(String data) {
        System.out.println("正在使用data进行网络请求，data:" + data + ",开始。。。");
        try {
            //业务操作耗时
            Thread.sleep(3000);
        }catch (Exception e){

        }
        System.out.println("操作执行完毕。。。获取到结果");
        this.requestResult = "luluya";
    }

    @Override
    public String getRequest() {
        return requestResult;
    }
}

//当有线程想要获取RealData的时候，程序会被阻塞，等到RealData被注入才会使用getReal()方法
class FutureData implements Data {
    //读取结果
    public boolean Flag = false;
    private RealData realData;

    //读取data数据
    public synchronized void setRealData(RealData realData) {
        //如果一键获得到结果，直接返回
        if (Flag) {
            return;
        }
        //如果flag为false，没有获取到数据，传递RealData对象
        this.realData = realData;
        Flag = true;
        notify();
    }

    @Override
    public synchronized String getRequest() {
        //一直监听
        while (!Flag){
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        return realData.getRequest();
    }
}

class FutureClient{
    public Data request(String queryStr){
        FutureData futureData = new FutureData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RealData realData = new RealData(queryStr);
                futureData.setRealData(realData);
            }
        }).start();
        return futureData;
    }
}

public class FutureDemo {
    public static void main(String[] args) {
        FutureClient futureClient = new FutureClient();
        Data request = futureClient.request("请求参数.");
        System.out.println("请求发送成功!");
        System.out.println("执行其他任务...");
        String result = request.getRequest();
        System.out.println("获取到结果..." + result);
    }
}