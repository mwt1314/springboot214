package cn.matio.api.test;

// 测试类
public class TestAtomicDemo{
    public static void main(String[] args){

        AtomicDemo ad = new AtomicDemo();

        for(int i=0; i < 10; i++){
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable{
    private int serialNumber = 0;

    @Override
    public void run(){

        try{
            Thread.sleep(200);
        }catch(InterruptedException e){

        }

        System.out.println(Thread.currentThread().getName() + ":" + getSerialNumber());
    }

    public int getSerialNumber(){
        return serialNumber++;
    }
}