package controllers;

import dto.SimulationDTO;
import models.Vehicle;
import models.Road;
import views.GridTable;
import views.MainView;
import views.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationController extends Thread {

    private LinkedList<Vehicle> vehiclesInQueue;
    private ArrayList<Vehicle> vehiclesOnGrid;
    private SimulationDTO dto;
    private SimulationView simulationView;
    private boolean closed = false;

    public SimulationController(SimulationDTO dto) {
        GridController grid = new GridController(dto);

        this.simulationView = new SimulationView(grid.getGridTable());
        initViewActions();

        this.dto = dto;

        this.vehiclesOnGrid = new ArrayList<>();
        this.vehiclesInQueue = this.loadVehicles();

        this.start();
    }

    private void initViewActions(){
        simulationView.addActionCloseBtn(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
                new MainView();
                simulationView.dispose();
            }
        });

        simulationView.addActionStopAndWaitBtn(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAndWait();
            }
        });

    }


    public void close() {
        this.closed = true;
        for (Vehicle queuedVehicle : this.vehiclesInQueue) {
            queuedVehicle.close();
        }
        for (Vehicle vehicleOnGrid : this.vehiclesOnGrid) {
            vehicleOnGrid.close();
        }
        this.interrupt();
    }

    @Override
    public void run() {
        while (!this.closed) {
            this.executeQueue();
        }
    }

    private void executeQueue() {
        while (!this.vehiclesInQueue.isEmpty()) {
            for (int row = 0; row < this.simulationView.getGridTable().getRowCount(); row++) {
                for (int col = 0; col < this.simulationView.getGridTable().getColumnCount(); col++) {
                    Road entry = this.getRoadConnection()[col][row];
                    if (entry.isEntry() && entry.isEmpty() && !this.vehiclesInQueue.isEmpty() && this.vehiclesOnGrid.size() < this.dto.getmaxCarInMeshQuantitySameTime()) {
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

    private void sleepNextVehicle() throws InterruptedException {
        int sleepTime = 10;
        if (this.dto.getInsertionInterval() > 0) {
            sleepTime = this.dto.getInsertionInterval() * 1000;
        }
        sleep(sleepTime);
    }

    public synchronized void updateCell(Road road) {
        this.simulationView.dataChanged(this.vehiclesInQueue.size(), this.vehiclesOnGrid.size() );
        this.getGridTable().fireTableCellUpdated(road.getRow(), road.getColumn());
        this.getGridTable().fireTableDataChanged();
    }

    public Road[][] getRoadConnection() {
        return this.getGridTable().getMesh();
    }

    public GridTable getGridTable() {
        return (GridTable) this.simulationView.getGridTable().getModel();
    }

    public LinkedList<Vehicle> loadVehicles() {
        LinkedList<Vehicle> vehicles = new LinkedList<>();
        for (int i = 0; i < this.dto.getCarQuantity(); i++) {
            vehicles.add(new Vehicle(this));
        }
        return vehicles;
    }

    public ArrayList<Vehicle> getVehiclesOnGrid() {
        return vehiclesOnGrid;
    }

    public LinkedList<Vehicle> getVehiclesInQueue() {
        return vehiclesInQueue;
    }

    public void addVehicleToGrid(Vehicle vehicle) {
        this.vehiclesOnGrid.add(vehicle);
    }

    public void removeVehicleFromGrid(Vehicle vehicle) {
        this.getVehiclesOnGrid().remove(vehicle);
        updateCell(vehicle.getCurrentRoad());
        if ((this.getVehiclesOnGrid().isEmpty() && this.getVehiclesInQueue().isEmpty() && !this.closed) ) {
            this.close();
            this.simulationView.backToMenu();
            this.simulationView.alert("All vehicles have completed the route, simulation finished.");

        }
    }

    public void stopAndWait(){
        for (Vehicle queuedVehicle : this.vehiclesInQueue) {
            queuedVehicle.close();
        }
        vehiclesInQueue.clear();
    }

}
