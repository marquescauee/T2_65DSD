package dependency_injection;

import java.util.concurrent.TimeUnit;

public interface Method{
    boolean tryAcquire(int i, TimeUnit timeUnit);
    void release();
}
