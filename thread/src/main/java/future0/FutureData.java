package future0;

public class FutureData implements Data {
    private RealData realdata = null;
    private boolean ready = false;
    public synchronized void setRealData(RealData realdata) {
        if (ready) {
            return;
        }
        this.realdata = realdata;
        this.ready = true;

        //唤醒当前实体对象锁的等待池中所有
        notifyAll();
    }

    @Override
    public synchronized String getContent() {
        //如果数据还没有准备好（即reday=false，就会进入wait等待池，等待被唤醒）
        //如果ready=true，直接返回数据
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return realdata.getContent();
    }
}