package dto;

import models.Method;
import models.MonitorMethod;
import models.SemaphoreMethod;

public class SimulationDTO {
    private int carQuantity;
    private int insertionInterval;
    private String meshFileName;
    private int maxCarInMeshQuantitySameTime;
    private Method method;

    public SimulationDTO(int carQuantity, int insertionInterval, String meshFileName, int maxCarInMeshQuantitySameTime, String methodString) {
        this.carQuantity = carQuantity;
        this.insertionInterval = insertionInterval;
        this.meshFileName = meshFileName;
        this.maxCarInMeshQuantitySameTime = maxCarInMeshQuantitySameTime;
        if(methodString.equals("Monitor")){
           method = new MonitorMethod();
        }else {
            method = new SemaphoreMethod();
        }
    }

    public Method getMethod() {
        return method;
    }

    public int getCarQuantity() {
        return carQuantity;
    }

    public int getInsertionInterval() {
        return insertionInterval;
    }

    public String getMeshFileName() {
        return meshFileName;
    }

    public int getmaxCarInMeshQuantitySameTime() {
        return maxCarInMeshQuantitySameTime;
    }
}

