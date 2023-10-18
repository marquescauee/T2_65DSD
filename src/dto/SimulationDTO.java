package dto;

public class SimulationDTO {
    private int carQuantity;
    private int insertionInterval;
    private String meshFileName;
    private int maxCarInMeshQuantitySameTime;

    public SimulationDTO(int carQuantity, int insertionInterval, String meshFileName, int maxCarInMeshQuantitySameTime) {
        this.carQuantity = carQuantity;
        this.insertionInterval = insertionInterval;
        this.meshFileName = meshFileName;
        this.maxCarInMeshQuantitySameTime = maxCarInMeshQuantitySameTime;
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

