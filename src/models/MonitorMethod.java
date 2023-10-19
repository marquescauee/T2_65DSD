package models;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorMethod implements Method{

    private Lock lock;

    public MonitorMethod() {
        lock = new ReentrantLock();
    }

    @Override
    public boolean tryAcquire(int i, TimeUnit timeUnit) {
        try {
            return lock.tryLock(i, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        lock.unlock();
    }
}
