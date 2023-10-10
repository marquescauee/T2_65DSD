package views;

import models.Road;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.nio.file.Paths;

public class GridTable extends AbstractTableModel{
    private static final String FILES_PATH =  Paths.get("").toAbsolutePath() +"/src/files/";

    private int rows;
    private int columns;
    private Road mesh[][];
    private String fileName;

    public GridTable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int getRowCount() {
        return this.getRows();
    }

    @Override
    public int getColumnCount() {
        return this.getColumns();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return new ImageIcon(this.mesh[columnIndex][rowIndex].getIconDirectory());
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public Road[][] getMesh() {
        return mesh;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public static String getFilesPath() {
        return FILES_PATH;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMesh(Road[][] mesh) {
        this.mesh = mesh;
    }

}
