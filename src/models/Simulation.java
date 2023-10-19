package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Simulation extends Thread{
    private LinkedList<Vehicle> vehiclesInQueue;
    private ArrayList<Vehicle> vehiclesOnGrid;
    private int rowCount;
    private int columnCount;
    private Road[][] roadConnection;
    private int intersectionInterval;
    private boolean closed = false;
    private int maxVehiclesSameTime;


    public Simulation(int rowCount, int columnCount, Road[][] roadConnection,int intersectionInterval, int maxVehiclesSameTime){
        this.rowCount = rowCount;
        this.columnCount =columnCount;
        this.roadConnection = roadConnection;
        this.intersectionInterval = intersectionInterval;
        this.maxVehiclesSameTime = maxVehiclesSameTime;
    }

    public Road[][] getRoadConnection() {
        return this.roadConnection;
    }

    @Override
    public void run() {
        while (!this.closed) {
            while (!this.vehiclesInQueue.isEmpty()) {
                for (int row = 0; row < this.rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Road entry = this.getRoadConnection()[col][row];
                        if (entry.isEntry() && entry.isEmpty() && !this.vehiclesInQueue.isEmpty()
                                && this.vehiclesOnGrid.size() < maxVehiclesSameTime) {
                            try {
                                Vehicle vehicle = this.vehiclesInQueue.remove();
                                vehicle.setRoute(entry);
                                this.addVehicleToGrid(vehicle);
                                vehicle.start();
                                this.sleepNextVehicle();
                            } catch (Exception ignored) {

                            }
                        }
                    }
                }
            }
        }
    }

    public void addVehicleToGrid(Vehicle vehicle) {
        this.vehiclesOnGrid.add(vehicle);
    }

    private void sleepNextVehicle() throws InterruptedException {
        int sleepTime = 10;
        if (intersectionInterval > 0) {
            sleepTime = intersectionInterval * 1000;
        }
        sleep(sleepTime);
    }

    public void close(){
        closed = true;
    }

    public void setVehiclesInQueue(LinkedList<Vehicle> vehiclesInQueue) {
        this.vehiclesInQueue = vehiclesInQueue;
    }

    public void setVehiclesOnGrid(ArrayList<Vehicle> vehiclesOnGrid) {
        this.vehiclesOnGrid = vehiclesOnGrid;
    }
}
