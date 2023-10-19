package models;

import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Road {

    private static final String ICONS_PATH = Paths.get("").toAbsolutePath() + "/src/icons/";
    protected Semaphore semaphore;
    protected String iconDirectory;
    protected boolean entry;
    protected boolean exit;
    protected int type;
    protected Vehicle vehicle;
    protected int row;
    protected int column;
    private String method;
    private Lock lock;

    public Road(int row, int column, int type, String method) {
        this.vehicle = null;
        this.type = type;
        this.row = row;
        this.column = column;
        this.method = method;
        if (method.equals("Monitor")){
            lock = new ReentrantLock();
        }else {
            semaphore = new Semaphore(1);
        }
        this.defineCurrentIcon();
    }

    public boolean tryAcquire() {
        boolean acquired = false;
        if(method.equals("Monitor")){
            try {
                acquired = this.lock.tryLock(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
            }
        }else {
            try {
                acquired = this.semaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
               // throw new RuntimeException(e);
            }
        }
        return acquired;
    }


    public void release() {
        //this.method.release();
        try {
        if(method.equals("Monitor")){

            this.lock.unlock();

        }else {
            this.semaphore.release();
        }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.defineCurrentIcon();
    }

    public void removeVehicle() {
        this.vehicle = null;
        this.defineCurrentIcon();
    }

    public void defineCurrentIcon() {
        if (this.getVehicle() != null) {
            this.setVehicleIconDirectory();
        } else {
            this.setIconDirectoryByType();
        }
    }

    public void setIconDirectoryByType() {
        this.setIconDirectory(ICONS_PATH + "malha" + this.type + ".png");
    }

    public void setVehicleIconDirectory() {
        this.setIconDirectory(ICONS_PATH + "veiculo" + this.vehicle.getType() + ".png");
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getIconDirectory() {
        return iconDirectory;
    }

    public void setIconDirectory(String iconDirectory) {
        this.iconDirectory = iconDirectory;
    }

    public int getType() {
        return type;
    }

    public boolean isEntry() {
        return entry;
    }

    public void setEntry(boolean isEntryCell) {
        this.entry = isEntryCell;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean isExitCell) {
        this.exit = isExitCell;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isIntersection() {
        return this.type > 4;
    }

    public boolean isEmpty() {
        return this.vehicle == null;
    }
    public boolean isRoad() {
        return this.getType() > 0;
    }

}
