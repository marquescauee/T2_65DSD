package models;

import java.sql.Time;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreMethod implements Method{

    Semaphore semaphore;

    public SemaphoreMethod() {
        semaphore = new Semaphore(1);
    }

    @Override
    public boolean tryAcquire(int i, TimeUnit timeUnit) {
        try {
            return this.semaphore.tryAcquire(i, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void release() {
        try {
            semaphore.release();
        }catch (Exception e){

        }
    }
}
