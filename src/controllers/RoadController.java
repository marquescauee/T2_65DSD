package controllers;

import models.Road;
import views.GridTable;

public class RoadController {

    private Road road;

    public RoadController(Road road, GridTable grid){
        this.road = road;
        defineEntryOrExit(grid);
    }

    public boolean isUpperEntry() {
        return road.getColumn() - 1 < 0 && road.getType() == 3;
    }

    public boolean isUpperExit() {
        return road.getColumn() - 1 < 0 && road.getType() == 1;
    }

    public boolean isLowerEntry(GridTable gridTable) {
        return road.getColumn() + 1 >= gridTable.getRowCount() && road.getType() == 1;
    }

    public boolean isLowerExit(GridTable gridTable) {
        return road.getColumn() + 1 >= gridTable.getRowCount() && road.getType() == 3;
    }

    public boolean isLeftEntry() {
        return road.getRow() - 1 < 0 && road.getType() == 2;
    }

    public boolean isLeftExit() {
        return road.getRow() - 1 < 0 && road.getType() == 4;
    }

    public boolean isRightEntry(GridTable gridTable) {
        return road.getRow() + 1 >= gridTable.getColumnCount() && road.getType() == 4;
    }

    public boolean isRightExit(GridTable gridTable) {
        return road.getRow() + 1 >= gridTable.getColumnCount() && road.getType() == 2;
    }

    public void defineEntryOrExit(GridTable gridTable) {
        road.setEntry((isUpperEntry() || isLowerEntry(gridTable) || isLeftEntry() || isRightEntry(gridTable)));
        road.setExit((isUpperExit() || isLowerExit(gridTable) || isLeftExit() || isRightExit(gridTable)));
    }
}
