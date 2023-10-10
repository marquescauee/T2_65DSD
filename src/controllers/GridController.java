package controllers;

import dto.SimulationDTO;
import models.Road;
import views.GridTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GridController {

    private GridTable gridTable;
    private RoadController roadController;

    public GridController(SimulationDTO simulationDTO){
        gridTable = new GridTable(simulationDTO.getMeshFileName());
        createMatrix();
    }

    public void createMatrix() {
        Scanner scanner = null;
        try {
            File meshFile = new File(gridTable.getFilesPath() + gridTable.getFileName());
            scanner = new Scanner(meshFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ;
        }
        while (scanner.hasNext()) {
            gridTable.setRows(scanner.nextInt());
            gridTable.setColumns(scanner.nextInt());
            gridTable.setMesh(new Road[gridTable.getColumns()][gridTable.getRows()]);
            for (int row = 0; row < gridTable.getRows(); row++) {
                for (int column = 0; column < gridTable.getColumns(); column++) {
                    int direction = scanner.nextInt();
                    Road road = new Road(column, row, direction);
                    if (road.isRoad()) {
                        roadController = new RoadController(road, gridTable);
                        roadController.defineEntryOrExit(gridTable);
                    }
                    gridTable.getMesh()[column][row] = road;
                }
            }
        }
        scanner.close();
    }

    public GridTable getGridTable() {
        return gridTable;
    }
}
