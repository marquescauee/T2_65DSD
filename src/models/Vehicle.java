package models;

import observers.VehicleObserver;

import java.util.ArrayList;
import java.util.Random;

public class Vehicle extends Thread {

    private Simulation simulation;
    private ArrayList<Road> route;
    private Road[][] roadConnection;
    private Random random = new Random();
    private int speed;
    private Road currentRoad;
    private boolean closed = false;
    private int type;

    private ArrayList<VehicleObserver> observers = new ArrayList<>();

    public Vehicle(Simulation simulation) {
        this.simulation = simulation;
        this.route = new ArrayList<>();
        this.roadConnection = simulation.getRoadConnection();
        this.speed = random.nextInt(100) + 500;
        this.type = random.nextInt(6) + 1;
        this.currentRoad = null;
    }

    public void addObserver(VehicleObserver observer){
        observers.add(observer);
    }

    public void notifyVehicleRemoved(){
        for (VehicleObserver v:
             observers) {
            v.vehicleHasBeenRemoved(this);
        }
    }

    public void notifyVehicleMoved(Road newRoad){
        for (VehicleObserver v :
                observers) {
            v.vehicleHasMoved(newRoad);
        }
    }

    public void close() {
        this.closed = true;
        this.interrupt();
    }

    @Override
    public void run() {
        while (!this.closed) {
            while (!route.isEmpty()) {
                int nextPosition = 0;
                if (route.get(nextPosition).isIntersection()) {
                    resolveIntersection();
                } else {
                    Road road = this.route.get(nextPosition);
                    move(road, true);
                }
            }
            this.getCurrentRoad().removeVehicle();
            this.getCurrentRoad().release();
            this.notifyVehicleRemoved();
            this.close();
        }
    }

    private void resolveIntersection() {
        ArrayList<Road> intersectionsToReserve = loadNecessaryIntersections();
        ArrayList<Road> reservedIntersections = attemptToReserveIntersections(intersectionsToReserve);

        if (reservedIntersections.size() == intersectionsToReserve.size()) {
            for (Road reservedIntersection : reservedIntersections) {
                this.move(reservedIntersection, false);
            }
        } else{
             releaseRoadList(reservedIntersections);
        }
    }

    private ArrayList<Road> loadNecessaryIntersections() {
        ArrayList<Road> intersectionsToReserve = new ArrayList<>();
        for (Road road : this.route) {
            intersectionsToReserve.add(road);
            if (!road.isIntersection()) {
                break;
            }
        }
        return intersectionsToReserve;
    }

    private ArrayList<Road> attemptToReserveIntersections(ArrayList<Road> intersectionsToReserve) {
        ArrayList<Road> reservedIntersections = new ArrayList<>();
        for (Road intersectionToReserve : intersectionsToReserve) {
            if (intersectionToReserve.tryAcquire()) {
                reservedIntersections.add(intersectionToReserve);
            } else {
                this.releaseRoadList(reservedIntersections);
                break;
            }
        }
        return reservedIntersections;
    }

    private void releaseRoadList(ArrayList<Road> roads) {
        for (Road road : roads) {
            road.release();
        }
    }

    private void move(Road nextRoad, boolean reserve) {
        if (nextRoad.isEmpty()) {
            boolean reserved = false;
            if (reserve) {
                while (!reserved){
                    if (nextRoad.tryAcquire()) {
                        reserved = true;
                    }
                }
            }
            nextRoad.addVehicle(this);
            Road previousRoad = this.getCurrentRoad();
            if (previousRoad != null) {
                previousRoad.removeVehicle();
                previousRoad.release();
            }
            this.setCurrentRoad(nextRoad);
            this.notifyVehicleMoved(nextRoad);
            this.delay();

            this.route.remove(nextRoad);
        }
    }

    public void setRoute(Road entry) throws Exception {
        boolean exitFound = false;
        Road nextRoad = entry;
        route.add(nextRoad);
        int intersectionsFound = 0;
        while (!exitFound) {
            int direction = nextRoad.getType();
            boolean oneWay = direction <= 4;
            if (oneWay) {
                nextRoad = this.chooseRoadByDirection(direction, nextRoad.getRow(), nextRoad.getColumn());
            } else {
                nextRoad = this.chooseIntersectionByDirection(direction, nextRoad.getRow(), nextRoad.getColumn(), intersectionsFound);
                if (nextRoad.isIntersection()) {
                    intersectionsFound++;
                } else {
                    intersectionsFound = 0;
                }
            }
            route.add(nextRoad);
            exitFound = nextRoad.isExit();
        }
    }

    public Road chooseRoadByDirection(int direction, int currentRow, int currentColumn) throws Exception {
        switch (direction) {
            case 1:
                return this.roadConnection[currentRow][currentColumn - 1];
            case 2:
                return this.roadConnection[currentRow + 1][currentColumn];
            case 3:
                return this.roadConnection[currentRow][currentColumn + 1];
            case 4:
                return this.roadConnection[currentRow - 1][currentColumn];
            default:
                throw new Exception("Error in road network assembly");
        }
    }

    private Road chooseIntersectionByDirection(int direction, int currentRow, int currentColumn, int intersectionsFound) throws Exception {
        int side = random.nextInt(2);
        switch (direction) {
            case 5: {
                return this.roadConnection[currentRow][currentColumn - 1];
            }
            case 6: {
                return this.roadConnection[currentRow + 1][currentColumn];
            }
            case 7: {
                return this.roadConnection[currentRow][currentColumn + 1];
            }
            case 8: {
                return this.roadConnection[currentRow - 1][currentColumn];
            }
            case 9: {
                if (intersectionsFound == 3) {
                    return this.roadConnection[currentRow + 1][currentColumn];
                }

                if (side == 0) {
                    return this.roadConnection[currentRow][currentColumn - 1];
                } else {
                    return this.roadConnection[currentRow + 1][currentColumn];
                }
            }
            case 10: {
                if (intersectionsFound == 3) {
                    return this.roadConnection[currentRow][currentColumn - 1];
                }

                if (side == 0) {
                    return this.roadConnection[currentRow][currentColumn - 1];
                } else {
                    return this.roadConnection[currentRow - 1][currentColumn];
                }
            }
            case 11: {
                if (intersectionsFound == 3) {
                    return this.roadConnection[currentRow][currentColumn + 1];
                }

                if (side == 0) {
                    return this.roadConnection[currentRow + 1][currentColumn];
                } else {
                    return this.roadConnection[currentRow][currentColumn + 1];
                }
            }
            case 12: {
                if (intersectionsFound == 3) {
                    return this.roadConnection[currentRow - 1][currentColumn];
                }

                if (side == 0) {
                    return this.roadConnection[currentRow][currentColumn + 1];
                }
                    return this.roadConnection[currentRow - 1][currentColumn];
            }
            default:
                throw new Exception("Error in road connection assembly");
        }
    }

    public int getType() {
        return type;
    }

    public Road getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
    }

    public void delay() {
        try {
            Thread.sleep(this.speed);
        } catch (InterruptedException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
