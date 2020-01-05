package aqs;

import java.io.Serializable;

/**
 * @author mawt
 * @description
 * @date 2020/1/5
 */
public abstract class BaseAQS implements Serializable {

    protected BaseAQS() {

    }

    private transient Thread exclusiveOwnerThread;

    public final void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }

    public Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
