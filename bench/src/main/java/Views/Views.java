package Views;

import static Model.Constants.DEFAULT_SERVER_ADDRESS;
import static Model.Constants.GRAPH_BUFFER_SIZE;
import static Model.Constants.WRITE_CSV;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.net.ConnectException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.ArrayList;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;

public class Views {
    JFrame frame = new JFrame("Banco de ensayo - FIUBA");
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel controlPanel = new JPanel(new GridBagLayout());
    JPanel inputPanel = new JPanel(new FlowLayout());
    JLabel targetIPLabel = new JLabel("IP objetivo:");
    JTextField targetIPInput = new JTextField(20);
    JLabel speedTimeFunctionLabel = new JLabel("Función de velocidad en función del tiempo:");
    JTextField speedTimeFunctionInput = new JTextField(20);
    JLabel torqueTimeFunctionLabel = new JLabel("Función de cupla en función del tiempo:");
    JTextField torqueTimeFunctionInput = new JTextField(20);
    JLabel torqueSpeedFunctionLabel = new JLabel("Función de velocidad en función de la cupla:");
    JTextField torqueSpeedFunctionInput = new JTextField(20);
    JLabel startTimeLabel = new JLabel("Tiempo de inicio [s]:");
    JTextField startTimeInput = new JTextField(10);
    JLabel stopTimeLabel = new JLabel("Tiempo de fin [s]:");
    JTextField stopTimeInput = new JTextField(10);
    JLabel errorMsgLabel = new JLabel("");
    JLabel varValueLabel = new JLabel("");
    JLabel targetVarNameLabel = new JLabel("VarName");
    JTextField targetVarNameInput = new JTextField(10);
    JLabel targetVarPathLabel = new JLabel("VarPath");
    JTextField targetVarPathInput = new JTextField(10);
    JLabel targetVarValueLabel = new JLabel("VarValue");
    JTextField targetVarValueInput = new JTextField(10);
    MeasurementVector measurements;
    // Medicion de variables

    JLabel simulatorTorqueLabel = new JLabel("Torque en eje simulador: ");
    JLabel simulatorSpeedLabel = new JLabel("Velocidad en eje simulador: ");
    JLabel simulatorVoltageLabel = new JLabel("Tensión en eje simulador: ");
    JLabel simulatorCurrentLabel = new JLabel("Corriente en eje simulador: ");
    JLabel simulatorPowerLabel = new JLabel("Potencia en eje simulador: ");
    JLabel simulatorTorqueValueLabel = new JLabel("-- Nm");
    JLabel simulatorSpeedValueLabel = new JLabel("-- RPM");
    JLabel simulatorVoltageValueLabel = new JLabel("-- V");
    JLabel simulatorCurrentValueLabel = new JLabel("-- A");
    JLabel simulatorPowerValueLabel = new JLabel("-- kW");
    JButton startPlotButton = new JButton("Gráfico");
    JButton saveCSVButton = new JButton(WRITE_CSV);

    // Grafico
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries torque_data = new XYSeries("Torque");
    XYSeries speed_data = new XYSeries("Speed");
    XYSeries voltage_data = new XYSeries("Voltage");
    XYSeries power_data = new XYSeries("Power");
    XYSeries current_data = new XYSeries("Current");

    // Mediciones

    private String[] measured_simulator_torque = new String[GRAPH_BUFFER_SIZE];
    private String[] measured_simulator_speed = new String[GRAPH_BUFFER_SIZE];
    private String[] measured_simulator_voltage = new String[GRAPH_BUFFER_SIZE];
    private String[] measured_simulator_current = new String[GRAPH_BUFFER_SIZE];
    private String[] measured_simulator_power = new String[GRAPH_BUFFER_SIZE];
    private long[] timestamp = new long[GRAPH_BUFFER_SIZE];
    private int measurement_buffer_index = 0;
    ScheduledExecutorService measurementPollTimer = Executors.newScheduledThreadPool(20);

    /**
     * Actualiza las mediciones en pantalla, agrega la unidad de medida al final de
     * la
     * Cadena de texto
     * 
     * @param simulator_torque  Torque medido en Nm
     * @param simulator_speed   velocidad medida en RPM
     * @param simulator_current Corriente medida en A
     * @param simulator_voltage Tensión medida en V
     * @param simulator_power   Potencia medida en W
     */

    public void updateMeasurements(long timestamp_value, String simulator_torque, String simulator_speed,
            String simulator_current,
            String simulator_voltage, String simulator_power) {
        timestamp[measurement_buffer_index] = timestamp_value;
        measured_simulator_torque[measurement_buffer_index] = simulator_torque;
        measured_simulator_speed[measurement_buffer_index] = simulator_speed;
        measured_simulator_voltage[measurement_buffer_index] = simulator_voltage;
        measured_simulator_current[measurement_buffer_index] = simulator_current;
        measured_simulator_power[measurement_buffer_index] = simulator_power;
        measurement_buffer_index++;
        /*
         * simulatorTorqueValueLabel.setText(simulator_torque + " Nm");
         * simulatorSpeedValueLabel.setText(simulator_speed + " RPM");
         * simulatorCurrentValueLabel.setText(simulator_current + " A");
         * simulatorVoltageValueLabel.setText(simulator_voltage + " V");
         * simulatorPowerValueLabel.setText(simulator_power + " kW");
         */
    }

    private class updateGraphMeasurements implements Runnable {

        public void run() {
            float avgTorque = 0;
            float avgSpeed = 0;
            float avgCurrent = 0;
            float avgVoltage = 0;
            float avgPower = 0;
            float torque = 0;
            float speed = 0;
            float current = 0;
            float voltage = 0;
            float power = 0;

            for (int i = 0; i < measurement_buffer_index; i++) {
                torque = Float.valueOf(measured_simulator_torque[i]);
                speed = Float.valueOf(measured_simulator_speed[i]);
                current = Float.valueOf(measured_simulator_current[i]);
                voltage = Float.valueOf(measured_simulator_voltage[i]);
                power = Float.valueOf(measured_simulator_power[i]);
                dataset.getSeries("Torque").add(timestamp[i], torque);
                avgTorque += torque;
                dataset.getSeries("Speed").add(timestamp[i], speed);
                avgSpeed += speed;
                dataset.getSeries("Current").add(timestamp[i], current);
                avgCurrent += current;
                dataset.getSeries("Voltage").add(timestamp[i], voltage);
                avgVoltage += voltage;

                dataset.getSeries("Power").add(timestamp[i], power);
                avgPower += power;
            }

            System.err.println(timestamp[measurement_buffer_index - 1] - timestamp[0]);
            ;
            simulatorTorqueValueLabel.setText(String.format("%.3g%n",avgTorque / measurement_buffer_index) + " Nm");
            simulatorSpeedValueLabel.setText(String.format("%.3g%n",avgSpeed / measurement_buffer_index) + " RPM");
            simulatorCurrentValueLabel.setText(String.format("%.3g%n",avgCurrent / measurement_buffer_index) + " A");
            simulatorVoltageValueLabel.setText(String.format("%.3g%n",avgVoltage / measurement_buffer_index) + " Vrms");
            simulatorPowerValueLabel.setText(String.format("%.3g%n",avgPower / measurement_buffer_index) + " kW");

            measurement_buffer_index = 0;
        }
    }

    public void stopGraphUpdate() {
        measurementPollTimer.shutdown();
    };

    public void display() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setup();
        frame.setLayout(new GridLayout(0, 1));
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        ChartPanel chartPanel = new ChartPanel(this.createChart());
        // frame.add(chartPanel);
        frame.add(chartPanel);

    }

    public Views() {

    }

    public JPanel getControlPanel() {
        return controlPanel;
    }

    public void setupControlPanel() {
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; // El área de texto empieza en la columna cero.
        constraints.gridy = 3; // El área de texto empieza en la fila cero
        constraints.gridwidth = 8; // El área de texto ocupa dos columnas.
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(targetIPLabel);
        leftPanel.add(targetIPInput);
        leftPanel.add(errorMsgLabel, constraints);
        controlPanel.add(leftPanel);
        frame.pack();
    }

    private void setup() {
        inputPanel.add(speedTimeFunctionLabel);
        inputPanel.add(speedTimeFunctionInput);
        inputPanel.add(torqueTimeFunctionLabel);
        inputPanel.add(torqueTimeFunctionInput);
        inputPanel.add(torqueSpeedFunctionLabel);
        inputPanel.add(torqueSpeedFunctionInput);
        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeInput);
        inputPanel.add(stopTimeLabel);
        inputPanel.add(stopTimeInput);
        inputPanel.add(targetVarNameLabel);
        inputPanel.add(targetVarNameInput);
        inputPanel.add(targetVarPathLabel);
        inputPanel.add(targetVarPathInput);
        inputPanel.add(targetVarValueLabel);
        inputPanel.add(varValueLabel);
        inputPanel.add(targetVarValueInput);

        inputPanel.add(simulatorTorqueLabel);
        inputPanel.add(simulatorTorqueValueLabel);
        inputPanel.add(simulatorSpeedLabel);
        inputPanel.add(simulatorSpeedValueLabel);
        inputPanel.add(simulatorVoltageLabel);
        inputPanel.add(simulatorVoltageValueLabel);
        inputPanel.add(simulatorCurrentLabel);
        inputPanel.add(simulatorCurrentValueLabel);
        inputPanel.add(simulatorPowerLabel);
        inputPanel.add(simulatorPowerValueLabel);
        inputPanel.add(startPlotButton);
        inputPanel.add(saveCSVButton);
        startPlotButton.addActionListener(new ButtonHandler());
        saveCSVButton.addActionListener(new ButtonHandler());
        inputPanel.setBackground(Color.GRAY);
        targetIPInput.setText(DEFAULT_SERVER_ADDRESS);

        this.dataset.addSeries(torque_data);
        this.dataset.addSeries(speed_data);
        this.dataset.addSeries(voltage_data);
        this.dataset.addSeries(current_data);
        this.dataset.addSeries(power_data);

    }

    private JFreeChart createChart() {

        JFreeChart chart = ChartFactory.createXYLineChart("Grafico",
                "X", "Y", dataset);

        return chart;
    }

    public String getTargetIP() {
        System.err.println(this.targetIPInput.getText());
        return this.targetIPInput.getText();
    }

    public String getTargetVarName() {
        System.err.println(this.targetVarPathInput.getText());
        return this.targetVarPathInput.getText();
    }

    public String getTargetVarPath() {
        System.err.println(this.targetVarNameInput.getText());
        return this.targetVarNameInput.getText();
    }

    public String getTargetVarValue() {
        System.err.println(this.targetVarValueInput.getText());
        return this.targetVarValueInput.getText();
    }

    public void setVarValue(String value) {
        this.varValueLabel.setText(value);
    }

    public void alert(String message) {
        errorMsgLabel.setText(message);
    }

    private void storeDataSet(String filename) {
        java.util.List<String> csv = new ArrayList<>();
        int seriesCount = this.dataset.getSeriesCount();
        int itemCount = this.dataset.getItemCount(0);
        String header = "Time,";
        String aux = "";

        for (int j = 0; j < itemCount; j++) {
            for (int i = 0; i < seriesCount; i++) {
                Comparable key = this.dataset.getSeriesKey(i);
                Number x = this.dataset.getX(i, j);
                Number y = this.dataset.getY(i, j);
                if (j == 0) {
                    header += String.format("%s,", key);
                }
                if (i == 0) {

                    aux += String.format("%s,", x);
                }
                
                aux += String.format("%s,", y);
            }
            if (j == 0) {
                
                csv.add(header);
            }
            csv.add(aux);
            aux="";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".csv"));) {
            for (String line : csv) {
                writer.append(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dataset", e);
        }
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            if ("Gráfico".equals(cmd)) {
                measurementPollTimer.scheduleAtFixedRate(new updateGraphMeasurements(), 0, 500, TimeUnit.MILLISECONDS);

            } else if ("CSV".equals(cmd)) {
                storeDataSet("C:\\Users\\juanh\\OneDrive\\Escritorio\\TPP\\archivo");

            }

        }
    }
}
