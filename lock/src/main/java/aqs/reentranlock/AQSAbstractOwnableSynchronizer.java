package aqs.reentranlock;

import java.io.Serializable;

/**
 * @author mawt
 * @description
 * @date 2020/1/17
 */
public abstract class AQSAbstractOwnableSynchronizer implements Serializable {

    protected AQSAbstractOwnableSynchronizer() {

    }

    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

}
