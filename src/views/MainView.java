package views;

import controllers.SimulationController;
import dto.SimulationDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {

    private JPanel containerPanel;
    private JTextField tfDelayInsertingNewVehicle;
    private JTextField tfMaxVehiclesAtSameTime;
    private JTextField tfNumVehiclesInSimulation;
    private JButton btnStartSimulation;
    private JRadioButton rbMesh1;
    private JRadioButton rbMesh2;
    private JRadioButton rbMesh3;
    private JLabel lbMaxNumVehiclesOnRoad;
    private JLabel lbDelayInsertNewVehicle;
    private JLabel lbNumVehiclesInSimulation;
    private JLabel lbChooseMash;
    private JComboBox comboBoxMethod;

    public MainView() {
        super.setSize(new Dimension(800, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.containerPanel);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        btnStartSimulation.addActionListener((ActionEvent e) -> {
            new SimulationController(new SimulationDTO(
                    Integer.parseInt(this.tfNumVehiclesInSimulation.getText()),
                    Integer.parseInt(tfDelayInsertingNewVehicle.getText()),
                    this.getSelectedMesh(),
                    Integer.parseInt(tfMaxVehiclesAtSameTime.getText()),
                    (String) comboBoxMethod.getSelectedItem()
            ));
            super.dispose();
        });
        super.setVisible(true);
    }

    public String getSelectedMesh() {
        if (this.rbMesh1.isSelected()) {
            return "malha1.txt";
        }
        if (this.rbMesh2.isSelected()) {
            return "malha2.txt";
        }
        if (this.rbMesh3.isSelected()) {
            return "malha3.txt";
        }
        return null;
    }

    public static void main(String[] args) {
        new MainView();
    }
}
