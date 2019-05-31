import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author wsl
 * @date 2019/3/22
 */
public class TestLock {

    volatile AtomicReference<Thread> atomicReference = new AtomicReference<>();

    volatile LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();


    public void acquire(){
        while(!tryAcquire()){
            // 塞到等待锁的集合中
            waiters.offer(Thread.currentThread());
            // 挂起这个线程
            LockSupport.park();
            // 后续 等待其他线程释放锁，收到通知后继续循环
        }
    }

    public void release(){

    }

    public boolean tryAcquire(){
        return atomicReference.compareAndSet(null,Thread.currentThread());
    }

}
