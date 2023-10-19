package views;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationView extends JFrame {

    private JPanel containerPanel;
    private JTable gridTable;
    private JButton btnClose;
    private JTextField tfVehiclesInQueue;
    private JTextField tfVehiclesOnGrid;
    private JButton btnStopAndWait;

    public SimulationView(GridTable gridTable) {
        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setContentPane(this.containerPanel);

        this.renderGridTable(gridTable);
        super.setVisible(true);
    }

    private void renderGridTable(GridTable gridTable) {
        this.gridTable.setModel(gridTable);
        this.gridTable.setRowHeight(32);
        this.gridTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.gridTable.setIntercellSpacing(new Dimension(0, 0));
        this.gridTable.setDefaultRenderer(Object.class, new GridCellRenderer());

        TableColumnModel columnModel = this.gridTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setMinWidth(60);
        }
    }

    public JTable getGridTable() {
        return gridTable;
    }

    public void backToMenu(){
        btnClose.setText("Back to Menu");
    }

    public void alert(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void dataChanged(int vehiclesInQueue, int vehiclesOnGrid) {
        tfVehiclesInQueue.setText("Vehicles to be added: " + vehiclesInQueue);
        tfVehiclesOnGrid.setText("Running Vehicles: " + vehiclesOnGrid);
    }

    public void addActionCloseBtn(ActionListener actionListener){
        btnClose.addActionListener(actionListener);
    }

    public void addActionStopAndWaitBtn(ActionListener actionListener) {
        btnStopAndWait.addActionListener(actionListener);
    }
}
