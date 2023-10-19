package controllers;

import dto.SimulationDTO;
import models.Simulation;
import models.Vehicle;
import models.Road;
import observers.VehicleObserver;
import views.GridTable;
import views.MainView;
import views.SimulationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationController implements VehicleObserver {

    private LinkedList<Vehicle> vehiclesInQueue;
    private ArrayList<Vehicle> vehiclesOnGrid;
    private SimulationDTO dto;
    private SimulationView simulationView;
    private boolean closed = false;
    private Simulation simulationModel;
    private boolean stopAndWait = false;

    public SimulationController(SimulationDTO dto) {
        GridController grid = new GridController(dto);

        this.simulationView = new SimulationView(grid.getGridTable());
        initViewActions();

        this.dto = dto;

        simulationModel = new Simulation(
                this.simulationView.getGridTable().getRowCount(),
                this.simulationView.getGridTable().getColumnCount(),
                this.getGridTable().getMesh(), dto.getInsertionInterval(),
                this.dto.getmaxCarInMeshQuantitySameTime());

        this.vehiclesOnGrid = new ArrayList<>();
        this.vehiclesInQueue = this.loadVehicles();

        simulationModel.setVehiclesInQueue(vehiclesInQueue);
        simulationModel.setVehiclesOnGrid(vehiclesOnGrid);

        simulationModel.start();

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
        simulationModel.close();
        this.closed = true;
        for (Vehicle queuedVehicle : this.vehiclesInQueue) {
            queuedVehicle.close();
        }
        for (Vehicle vehicleOnGrid : this.vehiclesOnGrid) {
            vehicleOnGrid.close();
        }
        simulationModel.interrupt();
    }


    public synchronized void updateCell(Road road) {
        this.simulationView.dataChanged(this.vehiclesInQueue.size(), this.vehiclesOnGrid.size() );
        this.getGridTable().fireTableCellUpdated(road.getRow(), road.getColumn());
        this.getGridTable().fireTableDataChanged();
    }

    public GridTable getGridTable() {
        return (GridTable) this.simulationView.getGridTable().getModel();
    }

    public LinkedList<Vehicle> loadVehicles() {
        LinkedList<Vehicle> vehicles = new LinkedList<>();
        for (int i = 0; i < this.dto.getmaxCarInMeshQuantitySameTime(); i++) {
            Vehicle v = new Vehicle(simulationModel);
            v.addObserver(this);
            vehicles.add(v);
        }
        return vehicles;
    }

    public ArrayList<Vehicle> getVehiclesOnGrid() {
        return vehiclesOnGrid;
    }

    public LinkedList<Vehicle> getVehiclesInQueue() {
        return vehiclesInQueue;
    }

    public void stopAndWait(){
        stopAndWait = true;
        for (Vehicle queuedVehicle : this.vehiclesInQueue) {
            queuedVehicle.close();
        }
        vehiclesInQueue.clear();
    }

    @Override
    public void vehicleHasBeenRemoved(Vehicle vehicle) {
        this.getVehiclesOnGrid().remove(vehicle);
        if(!this.closed && !stopAndWait){
            Vehicle v = new Vehicle(simulationModel);
            v.addObserver(this);
            simulationModel.addVehicleToQueue(v);
        }
        updateCell(vehicle.getCurrentRoad());
        if ((this.getVehiclesOnGrid().isEmpty() && this.getVehiclesInQueue().isEmpty() && !this.closed) ) {
            this.close();
            this.simulationView.backToMenu();
            this.simulationView.alert("All vehicles have completed the route, simulation finished.");

        }
    }

    @Override
    public void vehicleHasMoved(Road newRoad) {
        updateCell(newRoad);
    }
}
