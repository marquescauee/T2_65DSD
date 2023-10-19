package dto;

import dependency_injection.Method;
import dependency_injection.MonitorMethod;
import dependency_injection.SemaphoreMethod;

public class SimulationDTO {
    private int carQuantity;
    private int insertionInterval;
    private String meshFileName;
    private int maxCarInMeshQuantitySameTime;
    private String methodString;

    public SimulationDTO(int carQuantity, int insertionInterval, String meshFileName, int maxCarInMeshQuantitySameTime, String methodString) {
        this.carQuantity = carQuantity;
        this.insertionInterval = insertionInterval;
        this.meshFileName = meshFileName;
        this.maxCarInMeshQuantitySameTime = maxCarInMeshQuantitySameTime;
//        if(methodString.equals("Monitor")){
//           method = new MonitorMethod();
//        }else {
//            method = new SemaphoreMethod();
//        }
        this.methodString = methodString;
    }

    public String getMethod() {
        //return method;
        return methodString;
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

