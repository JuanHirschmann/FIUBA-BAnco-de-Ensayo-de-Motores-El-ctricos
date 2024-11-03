package Views;

import static Model.Constants.AVAILABLE_TORQUE_MODES;
import static Model.Constants.BROWSE_FILE_BUTTON_LABEL;
import static Model.Constants.CSV_DELIMITER;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Model.Constants.commands;
import Model.Constants.testTypes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.scilab.forge.jlatexmath.TeXFormula;

public class Views {
    private JFrame frame = new JFrame("Banco de ensayo - FIUBA");
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel controlPanel = new JPanel(new GridBagLayout());
    private JPanel inputPanel = new JPanel(new FlowLayout());
    private JLabel targetIPLabel = new JLabel("IP objetivo:");
    private JTextField targetIPInput = new JTextField(20);

    private LabeledInput startTime=new LabeledInput("Tiempo de inicio [s]:");
    private LabeledInput stopTime=new LabeledInput("Tiempo de fin [s]:");
    private JLabel errorMsgLabel = new JLabel("");

    private JTextField filename = new JTextField(40);
    // MeasurementVector measurements;

    private JButton startPlotButton = new JButton("Gráfico");
    private JButton saveCSVButton = new JButton(WRITE_CSV);
    private JButton openFileButton = new JButton(BROWSE_FILE_BUTTON_LABEL);
    private JComboBox<testTypes> torqueTestModeComboBox = new JComboBox<testTypes>();
    // Grafico
    private XYSeriesCollection dataset = new XYSeriesCollection();
    private XYSeries torque_data = new XYSeries(commands.TORQUE.seriesName);
    private XYSeries speed_data = new XYSeries(commands.SPEED.seriesName);
    private XYSeries voltage_data = new XYSeries(commands.VOLTAGE.seriesName);
    private XYSeries power_data = new XYSeries(commands.POWER.seriesName);
    private XYSeries current_data = new XYSeries(commands.CURRENT.seriesName);
    private XYSeries torque_command = new XYSeries(commands.TORQUE_COMMAND.seriesName);

    // Mediciones
    private MeasurementBuffer measurementsBufferedValues = new MeasurementBuffer();
    private ScheduledExecutorService measurementPollTimer = Executors.newScheduledThreadPool(40);
    private onScreenMeasurements displayedMeasurements = new onScreenMeasurements();
    private TorqueEquation torqueEquationText = new TorqueEquation();
    private JLabel torqueEquation = new JLabel(torqueEquationText.toString());
    private TorqueEquationParameters torqueEquationParameters =new TorqueEquationParameters();

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
    }
    private class LabeledInput
    {
        JLabel inputLabel =new JLabel();
        JTextField input=new JTextField(20);
        LabeledInput(String label)
        {
            inputLabel.setText(label);
        }
        public void set(JPanel panel)
        {
            panel.add(inputLabel);
            panel.add(input);
        }
        public void setVisible(boolean visible)
        {
            inputLabel.setVisible(visible);
            input.setVisible(visible);
        }
        public String getValue()
        {
            return input.getText();
        }
    }
    private class updateGraphMeasurements implements Runnable {
        public void run() {
            for (String key : measurementsBufferedValues.getKeySet()) {
                float average_value = 0;
                ArrayList<Float> value = new ArrayList<Float>(measurementsBufferedValues.getBufferedData(key));
                ArrayList<Float> timestamp = new ArrayList<Float>(
                        measurementsBufferedValues.getBufferedDataTimestamp(key));
                for (int i = 0; i < value.size(); i++) {
                    dataset.getSeries(key).add(timestamp.get(i), value.get(i));
                    average_value += value.get(i);
                }
                average_value /= value.size();
                displayedMeasurements.addMeasurement(key, average_value);

            }
            measurementsBufferedValues.clearBuffer();
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
        this.dataset.addSeries(torque_command);
        this.displayedMeasurements.addMeasuredVariable(commands.TORQUE);
        this.displayedMeasurements.addMeasuredVariable(commands.POWER);
        this.displayedMeasurements.addMeasuredVariable(commands.VOLTAGE);
        this.displayedMeasurements.addMeasuredVariable(commands.CURRENT);
        this.displayedMeasurements.addMeasuredVariable(commands.SPEED);
        this.displayedMeasurements.addMeasuredVariable(commands.TORQUE_COMMAND);
        this.torqueTestModeComboBox.addItem(testTypes.TORQUE_VS_SPEED);
        this.torqueTestModeComboBox.addItem(testTypes.TORQUE_VS_TIME);

        // this.torqueEquation.setContent("$T(v)=A+Bv+Cv^2+D\\frac{dv}{dt}$");

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
        inputPanel.add(torqueTestModeComboBox);
        inputPanel.add(openFileButton);
        inputPanel.add(filename);
        inputPanel.add(torqueEquation);
        startTime.set(inputPanel);
        stopTime.set(inputPanel);
        displayedMeasurements.setMeasurements(inputPanel);
        torqueEquationParameters.setParameters(inputPanel);
        inputPanel.add(startPlotButton);
        inputPanel.add(saveCSVButton);
        startPlotButton.addActionListener(new ButtonHandler());
        saveCSVButton.addActionListener(new ButtonHandler());
        openFileButton.addActionListener(new ButtonHandler());
        torqueTestModeComboBox.addItemListener(new TestTypeHandler());
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

    public void alert(String message) {
        errorMsgLabel.setText(message);
    }

    private void plotCSV(String filepath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(CSV_DELIMITER);
                dataset.getSeries(commands.TORQUE_COMMAND.seriesName).add(Float.valueOf(values[0]),
                        Float.valueOf(values[1]));

            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dataset", e);
        }

    }

    public void torqueVsTimeVisibility(boolean visible) {
        startTime.setVisible(!visible);
        stopTime.setVisible(!visible);
        filename.setVisible(!visible);
        openFileButton.setVisible(!visible);
        torqueEquation.setVisible(visible);
        torqueEquationParameters.setVisible(visible);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private void storeDataSet(String filename) {
        int seriesCount = this.dataset.getSeriesCount();
        int itemCount = this.dataset.getItemCount(0);
        String header = "";
        String aux = "";
        System.out.println(seriesCount);
        System.out.println(itemCount);
        java.util.List<String> csv = new ArrayList<>();
        for (int j = 0; j < itemCount; j++) {
            for (int i = 0; i < seriesCount; i++) {
                Comparable key = this.dataset.getSeriesKey(i);
                Number x = this.dataset.getX(i, j);
                Number y = this.dataset.getY(i, j);
                // if (j == 0) {
                header += "Tiempo [ms],";
                header += String.format("%s,", key);
                // }
                // if (i == 0) {

                aux += String.format("%s,", x);
                // }

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

    private class TorqueEquation {
        String A;
        String B;
        String C;
        String D;
        String E;

        TorqueEquation() {
            this.reset();
        };

        public String toString() {
            return "<html>" + A + "+" + B + "v+" + C + "v<sup>2</sup>+" + D
                    + "<sup>dv</sup> &frasl; <sub>dt</sub></html>";
        }

        public void reset() {
            A = "A";
            B = "B";
            C = "C";
            D = "D";
            E = "E";
        }

    }

    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            if ("Gráfico".equals(cmd)) {
                measurementPollTimer.scheduleAtFixedRate(new updateGraphMeasurements(), 1000, 500,
                        TimeUnit.MILLISECONDS);

            } else if (WRITE_CSV.equals(cmd)) {
                String pattern = "yyyyMMdd HHmmss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                storeDataSet(CSV_FILEPATH + todayAsString);
            } else if (BROWSE_FILE_BUTTON_LABEL.equals(cmd)) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(Views.this.frame);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename.setText(c.getSelectedFile().getName());
                    String dir = c.getCurrentDirectory().toString();
                    String path = dir + "\\" + filename.getText();

                    plotCSV(path);
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    filename.setText("");
                }
            }

        }
    }

    private class TestTypeHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getSource() == torqueTestModeComboBox) {

                if (torqueTestModeComboBox.getSelectedItem() == testTypes.TORQUE_VS_SPEED) {
                    // Tiempo de inicio-fin
                    // Componentes de ecuación cupla
                    torqueVsTimeVisibility(false);

                } else if (torqueTestModeComboBox.getSelectedItem() == testTypes.TORQUE_VS_TIME) {
                    torqueVsTimeVisibility(true);
                }
                // l1.setText(c1.getSelectedItem() + " selected");
            }
        }

    }
}
