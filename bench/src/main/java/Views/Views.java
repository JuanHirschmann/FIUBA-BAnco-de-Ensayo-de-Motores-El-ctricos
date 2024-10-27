package Views;

import static Model.Constants.CSV_FILEPATH;
import static Model.Constants.DEFAULT_SERVER_ADDRESS;
import static Model.Constants.GRAPH_BUFFER_SIZE;
import static Model.Constants.WRITE_CSV;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Model.Constants.commands;

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
    // MeasurementVector measurements;
    
    // Medicion de variables
    JLabel simulatorTorqueLabel = new JLabel(commands.TORQUE.displayName+": ");
    JLabel simulatorSpeedLabel = new JLabel(commands.SPEED.displayName+": ");
    JLabel simulatorVoltageLabel = new JLabel(commands.VOLTAGE.displayName+": ");
    JLabel simulatorCurrentLabel = new JLabel(commands.CURRENT.displayName+": ");
    JLabel simulatorPowerLabel = new JLabel(commands.POWER.displayName+": ");
    
    //Map<String, JLabel> measuredVariables=new HashMap<>();
    JLabel simulatorTorqueValueLabel = new JLabel("-- "+commands.TORQUE.displayUnit);
    JLabel simulatorSpeedValueLabel = new JLabel("-- "+commands.SPEED.displayUnit);
    JLabel simulatorVoltageValueLabel = new JLabel("-- "+commands.VOLTAGE.displayUnit);
    JLabel simulatorCurrentValueLabel = new JLabel("-- "+commands.CURRENT.displayUnit);
    JLabel simulatorPowerValueLabel = new JLabel("-- "+commands.POWER.displayUnit);
    
    JButton startPlotButton = new JButton("Gráfico");
    JButton saveCSVButton = new JButton(WRITE_CSV);

    // Grafico
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries torque_data = new XYSeries(commands.TORQUE.seriesName);
    XYSeries speed_data = new XYSeries(commands.SPEED.seriesName);
    XYSeries voltage_data = new XYSeries(commands.VOLTAGE.seriesName);
    XYSeries power_data = new XYSeries(commands.POWER.seriesName);
    XYSeries current_data = new XYSeries(commands.CURRENT.seriesName);
   
    // Mediciones
    private MeasurementBuffer measurementsBufferedValues = new MeasurementBuffer();
    ScheduledExecutorService measurementPollTimer = Executors.newScheduledThreadPool(40);
    onScreenMeasurements displayedMeasurements= new onScreenMeasurements();
    
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

    public void updateMeasurements(long timestamp_value, String measured_value, String var_name) {

        measurementsBufferedValues.addValue(var_name, Float.valueOf(measured_value), timestamp_value);
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
            for (String key : measurementsBufferedValues.getKeySet()) {
                float average_value=0;
                ArrayList<Float> value = new ArrayList<Float>(measurementsBufferedValues.getBufferedData(key));
                ArrayList<Float> timestamp = new ArrayList<Float>(
                        measurementsBufferedValues.getBufferedDataTimestamp(key));
                for (int i = 0; i < value.size(); i++) {
                    dataset.getSeries(key).add(timestamp.get(i), value.get(i));
                    average_value+=value.get(i);
                }
                average_value/=value.size();
                displayedMeasurements.addMeasurement(key,average_value);

            }
            // Esto a lo mejor es un poquito lento
            measurementsBufferedValues.clearBuffer();
            // TODO Implementar la lógica de actualización del texto en pantalla
            /*
             * simulatorTorqueValueLabel.setText(String.format("%.3g%n",avgTorque /
             * measurement_buffer_index) + " Nm");
             * simulatorSpeedValueLabel.setText(String.format("%.3g%n",avgSpeed /
             * measurement_buffer_index) + " RPM");
             * simulatorCurrentValueLabel.setText(String.format("%.3g%n",avgCurrent /
             * measurement_buffer_index) + " A");
             * simulatorVoltageValueLabel.setText(String.format("%.3g%n",avgVoltage /
             * measurement_buffer_index) + " Vrms");
             * simulatorPowerValueLabel.setText(String.format("%.3g%n",avgPower /
             * measurement_buffer_index) + " kW");
             * 
             * measurement_buffer_index = 0;
             */
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
        this.dataset.addSeries(torque_data);
        this.dataset.addSeries(speed_data);
        this.dataset.addSeries(voltage_data);
        this.dataset.addSeries(current_data);
        this.dataset.addSeries(power_data);
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


        displayedMeasurements.addMeasuredVariable(commands.TORQUE.seriesName);
        displayedMeasurements.addMeasuredVariable(commands.SPEED.seriesName);
        displayedMeasurements.addMeasuredVariable(commands.VOLTAGE.seriesName);
        displayedMeasurements.addMeasuredVariable(commands.CURRENT.seriesName);
        displayedMeasurements.addMeasuredVariable(commands.POWER.seriesName);
        displayedMeasurements.setMeasurements(inputPanel);
        /* inputPanel.add(simulatorTorqueLabel);
        inputPanel.add(simulatorTorqueValueLabel);
        inputPanel.add(simulatorSpeedLabel);
        inputPanel.add(simulatorSpeedValueLabel);
        inputPanel.add(simulatorVoltageLabel);
        inputPanel.add(simulatorVoltageValueLabel);
        inputPanel.add(simulatorCurrentLabel);
        inputPanel.add(simulatorCurrentValueLabel);
        inputPanel.add(simulatorPowerLabel);
        inputPanel.add(simulatorPowerValueLabel); */
        inputPanel.add(startPlotButton);
        inputPanel.add(saveCSVButton);
        startPlotButton.addActionListener(new ButtonHandler());
        saveCSVButton.addActionListener(new ButtonHandler());
        inputPanel.setBackground(Color.GRAY);
        targetIPInput.setText(DEFAULT_SERVER_ADDRESS);

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
        String header = "";
        String aux = "";
        System.out.println(seriesCount);
        System.out.println(itemCount);
        for (int j = 0; j < itemCount; j++) {
            for (int i = 0; i < seriesCount; i++) {
                Comparable key = this.dataset.getSeriesKey(i);
                Number x = this.dataset.getX(i, j);
                Number y = this.dataset.getY(i, j);
                //if (j == 0) {
                header+="Tiempo [ms],";
                header += String.format("%s,", key);
                //}
                //if (i == 0) {

                    aux += String.format("%s,", x);
                //}

                aux += String.format("%s,", y);
            }
            if (j == 0) {

                csv.add(header);
            }
            csv.add(aux);
            aux = "";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".csv"));) {
            for (String line : csv) {
                writer.append(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dataset", e);
        }
        System.out.println("exporte como csv");
    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            if ("Gráfico".equals(cmd)) {
                measurementPollTimer.scheduleAtFixedRate(new updateGraphMeasurements(), 1000, 500, TimeUnit.MILLISECONDS);
                
            } else if (WRITE_CSV.equals(cmd)) {
                String pattern = "yyyyMMdd HHmmss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                storeDataSet(CSV_FILEPATH+todayAsString);
            }

        }
    }
}
