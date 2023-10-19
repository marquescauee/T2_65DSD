package dto;

import dependency_injection.Method;
import dependency_injection.MonitorMethod;
import dependency_injection.SemaphoreMethod;

public class SimulationDTO {
    private int insertionInterval;
    private String meshFileName;
    private int maxCarInMeshQuantitySameTime;
    private String methodString;

    public SimulationDTO(int insertionInterval, String meshFileName, int maxCarInMeshQuantitySameTime, String methodString) {
        this.insertionInterval = insertionInterval;
        this.meshFileName = meshFileName;
        this.maxCarInMeshQuantitySameTime = maxCarInMeshQuantitySameTime;
        this.methodString = methodString;
    }

    public String getMethod() {
        return methodString;
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

