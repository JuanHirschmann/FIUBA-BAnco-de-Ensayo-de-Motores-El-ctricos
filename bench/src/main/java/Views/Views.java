package Views;

import static Views.Constants.AVAILABLE_TORQUE_MODES;
import static Views.Constants.BROWSE_FILE_BUTTON_LABEL;
import static Views.Constants.CONNECT_BUTTON_LABEL;
import static Views.Constants.CSV_DELIMITER;
import static Views.Constants.CSV_FILEPATH;
import static Views.Constants.EMERGENCY_RELEASE_BUTTON_LABEL;
import static Views.Constants.EMERGENCY_STOP_BUTTON_LABEL;
import static Views.Constants.PAUSE_BUTTON_LABEL;
import static Views.Constants.POWER_ON_BUTTON_LABEL;
import static Views.Constants.SHUTDOWN_BUTTON_LABEL;
import static Views.Constants.START_BUTTON_LABEL;
import static Views.Constants.SET_TEST_PARAMETERS_BUTTON_LABEL;
import static Views.Constants.WRITE_CSV;

//import java.awt.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFileChooser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Views.Constants.testStates;
import Model.Constants.testTypes;
import Swing.MainFrame;
import Swing.TorqueEquationParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.scilab.forge.jlatexmath.TeXFormula;

import Controller.Controller;
import Controller.MeasurementBuffer;
import Controller.TorqueTimeValues;
import Controller.ViewListener;
import Model.Constants.commands;

public class Views implements ViewListener {

    Controller appController = null;

    private MainFrame frame = new MainFrame();
    private SwingPlotWorker plotUpdater = new SwingPlotWorker();

    public void setController(Controller controller) {
        appController = controller;
    };

    public void shutdownRequest() {
    }

    public void connectRequest() {
    }

    public void connectPowerRequest() {
    }

    public void startTestRequest() {
    }

    public void pauseTestRequest() {
    }

    public void emergencyStopRequest() {
    }

    public Controller getController() {
        return this.appController;
    }

    private Map<String, XYSeriesCollection> mainDataset = new Hashtable<>();

    private XYSeries torque_data = new XYSeries(commands.TORQUE.seriesName);
    private XYSeries speed_data = new XYSeries(commands.SPEED.seriesName);
    private XYSeries voltage_data = new XYSeries(commands.VOLTAGE.seriesName);
    private XYSeries power_data = new XYSeries(commands.POWER.seriesName);
    private XYSeries current_data = new XYSeries(commands.CURRENT.seriesName);
    private XYSeries torque_command = new XYSeries(commands.TORQUE_COMMAND.seriesName);

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

    private class SwingPlotWorker extends SwingWorker<Boolean, MeasurementBuffer> {
        private static int DISPLAYED_MEASUREMENTS_UPDATE_RATIO = 5;// Cada cuanto chunks actualiza mediciones
        private int chunkCounter = 0;
        private static int MEAN_SAMPLE_SIZE = 50;

        @Override
        protected Boolean doInBackground() throws Exception {

            while (!isCancelled()) {
                if (!getController().getMeasurementBuffer().isEmpty()) {
                    MeasurementBuffer buffer = new MeasurementBuffer(getController().getMeasurementBuffer());
                    publish(buffer);
                    getController().clearMeasurementBuffer();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        // eat it. caught when interrupt is called
                        System.out.println("MySwingWorker shut down.");
                    }
                }

            }
            return true;

        }

        @Override
        protected void process(List<MeasurementBuffer> chunks) {

            for (MeasurementBuffer data : chunks) {

                for (String key : data.getKeySet()) {
                    ArrayList<Float> value = new ArrayList<Float>(data.getBufferedData(key));
                    ArrayList<Float> timestamp = new ArrayList<Float>(data.getBufferedDataTimestamp(key));

                    for (int i = 0; i < value.size(); i++) {
                        mainDataset.get(key).getSeries(key).add(timestamp.get(i), value.get(i));
                    }

                }
                chunkCounter++;
                if (chunkCounter == DISPLAYED_MEASUREMENTS_UPDATE_RATIO) {
                    chunkCounter = 0;
                    for (String series : mainDataset.keySet()) {
                        float accumValue = 0;
                        int seriesLength = 0;
                        seriesLength = mainDataset.get(series).getSeries(series).getItemCount();
                        int sampleSize = seriesLength;
                        if (seriesLength > MEAN_SAMPLE_SIZE) {
                            sampleSize = MEAN_SAMPLE_SIZE;
                        }
                        for (int i = 0; i < sampleSize; i++) {
                            accumValue += mainDataset.get(series).getSeries(series).getY(seriesLength - i - 1)
                                    .floatValue();
                        }

                        frame.getInputPanel().displayedMeasurements.addMeasurement(series,
                                accumValue / sampleSize);
                        accumValue = 0;
                    }
                    ;
                }

            }

            /*
             * try {
             * Thread.sleep(250);
             * } catch (InterruptedException e) {
             * // eat it. caught when interrupt is called
             * System.out.println("MySwingWorker shut down.");
             * }
             */

        }

    }

    public Views() {

        this.setup();

        this.torqueVsTimeVisibility(true);

    }

    private void setup() {
        frame.getInputPanel().startButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().emergencyButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().buttonConnect.addActionListener(new ButtonHandler());
        frame.getInputPanel().powerOnButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().shutdownButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().setParametersButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().saveCSVButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().openFileButton.addActionListener(new ButtonHandler());
        frame.getInputPanel().torqueTestModeComboBox.addItemListener(new TestTypeHandler());
        frame.getInputPanel().testPeriodsSpinner.addChangeListener(new PeriodExtensionHandler());
        createChart();
        this.blockInput(testStates.INITIAL);

    }

    private void blockInput(testStates currentStep) {
        if (currentStep == testStates.INITIAL) {
            frame.getInputPanel().buttonConnect.setEnabled(true);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(false);
            frame.getInputPanel().emergencyButton.setEnabled(false);
            frame.getInputPanel().shutdownButton.setEnabled(false);
            frame.getInputPanel().targetIP.setEnabled(true);

            frame.getInputPanel().variablesPanel.setEnabled(false);

        } else if (currentStep == testStates.PLC_CONNECTED) {
            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(true);
            frame.getInputPanel().startButton.setEnabled(false);
            frame.getInputPanel().emergencyButton.setEnabled(true);
            frame.getInputPanel().shutdownButton.setEnabled(false);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(false);
        } else if (currentStep == testStates.POWER_CONNECTED) {

            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(false);
            frame.getInputPanel().emergencyButton.setEnabled(true);
            frame.getInputPanel().shutdownButton.setEnabled(true);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(true);
        } else if (currentStep == testStates.TEST_PARAMETER_LOAD) {
            // Tengo que asegurar que no hayan metido cualquier cosa
            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(false);
            frame.getInputPanel().emergencyButton.setEnabled(true);
            frame.getInputPanel().shutdownButton.setEnabled(true);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(true);

        } else if (currentStep == testStates.TEST_PARAMETER_READY) {
            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(true);
            frame.getInputPanel().emergencyButton.setEnabled(true);
            frame.getInputPanel().shutdownButton.setEnabled(true);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(false);

        } else if (currentStep == testStates.TEST_RUNNING) {
            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(true);
            frame.getInputPanel().emergencyButton.setEnabled(true);
            frame.getInputPanel().shutdownButton.setEnabled(true);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(false);

        }
    }

    private JFreeChart createChart() {

        mainDataset.put(commands.TORQUE.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.VOLTAGE.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.SPEED.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.CURRENT.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.POWER.seriesName, new XYSeriesCollection());

        mainDataset.get(commands.TORQUE.seriesName).addSeries(torque_data);
        mainDataset.get(commands.TORQUE.seriesName).addSeries(torque_command);
        mainDataset.get(commands.POWER.seriesName).addSeries(power_data);
        mainDataset.get(commands.SPEED.seriesName).addSeries(speed_data);
        mainDataset.get(commands.VOLTAGE.seriesName).addSeries(voltage_data);
        mainDataset.get(commands.CURRENT.seriesName).addSeries(current_data);

        // construct the plot
        XYPlot plot = new XYPlot();
        plot.setDataset(0, mainDataset.get(commands.TORQUE.seriesName));
        plot.setDataset(1, mainDataset.get(commands.SPEED.seriesName));
        plot.setDataset(2, mainDataset.get(commands.VOLTAGE.seriesName));
        plot.setDataset(3, mainDataset.get(commands.CURRENT.seriesName));
        plot.setDataset(4, mainDataset.get(commands.POWER.seriesName));
        // customize the plot with renderers and axis
        // r1.setSeriesPaint(0, Color.BLACK);
        plot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(3, new XYLineAndShapeRenderer(true, false));
        plot.setRenderer(4, new XYLineAndShapeRenderer(true, false));
        // plot.setRenderer(1, splinerenderer);
        plot.setRangeAxis(0, new NumberAxis("Torque [Nm]"));
        plot.setRangeAxis(1, new NumberAxis("Velocidad [RPM]"));
        plot.setRangeAxis(2, new NumberAxis("Tensión [Vrms]"));
        plot.setRangeAxis(3, new NumberAxis("Corriente [Arms]"));
        //plot.setRangeAxis(4, new NumberAxis("Potencia [KW]"));
        
        plot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRangeAxisLocation(3, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRangeAxisLocation(4, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        // Map the data to the appropriate axis
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.mapDatasetToRangeAxis(2, 2);
        plot.mapDatasetToRangeAxis(3, 3);
        plot.mapDatasetToRangeAxis(4, 4);
        // generate the chart
        JFreeChart chart = new JFreeChart("Variables en tiempo real", null, plot, true);
        frame.setChart(chart);

        // JPanel jpanel = new ChartPanel(chart);
        // chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private class PeriodExtensionHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (getController().getTorqueTimeValues().length() != 0) {

                getController().extendTorqueTimeValues((int) frame.getInputPanel().testPeriodsSpinner.getValue());
                plotTorqueTime();
            }
        }
    }

    public void torqueVsTimeVisibility(boolean visible) {
        frame.getInputPanel().stopTime.setVisible(!visible);
        frame.getInputPanel().filename.setVisible(visible);
        frame.getInputPanel().openFileButton.setVisible(visible);
        frame.getInputPanel().torqueEquation.setVisible(!visible);
        frame.getInputPanel().torqueEquationParameters.setVisible(!visible);
        frame.getInputPanel().testPeriodLabel.setVisible(visible);
        frame.getInputPanel().testPeriodsSpinner.setVisible(visible);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public String getTargetIP() {
        System.err.println(frame.getInputPanel().targetIP.getText());
        return frame.getInputPanel().targetIP.getText();
    }

    public void dismissAlert() {

        frame.userMessagePanel.setBackground(Color.WHITE);
        frame.getInputPanel().userMessageLabel.setText("");
    }

    public void alert(String message) {
        frame.userMessagePanel.setBackground(Color.RED);
        frame.getInputPanel().userMessageLabel.setText(message);
    }

    public Map<String, TorqueEquationParameter> getTorqueEquationParameters() {
        return frame.getInputPanel().torqueEquationParameters.getParameterValues();
    }

    public testTypes getTestType() {

        testTypes selectedTest = (testTypes) frame.getInputPanel().torqueTestModeComboBox.getSelectedItem();
        return selectedTest;
    }

    private void plotTorqueTime() {

        TorqueTimeValues torqueTime = new TorqueTimeValues(getController().getTorqueTimeValues());
        mainDataset.get(commands.TORQUE.seriesName).getSeries(commands.TORQUE_COMMAND.seriesName).clear();
        for (int i = 0; i < torqueTime.length(); i++) {
            mainDataset.get(commands.TORQUE.seriesName).getSeries(commands.TORQUE_COMMAND.seriesName).add(
                    Float.valueOf(torqueTime.getTimestamp(i)),
                    Float.valueOf(torqueTime.getValue(i)));
            ;
        }

    }

    /*
     * private void storeDataSet(String filename) {
     * /*
     * int seriesCount = this.dataset.getSeriesCount();
     * int itemCount = this.dataset.getItemCount(0);
     * String header = "";
     * String aux = "";
     * System.out.println(seriesCount);
     * System.out.println(itemCount);
     * java.util.List<String> csv = new ArrayList<>();
     * for (int j = 0; j < itemCount; j++) {
     * for (int i = 0; i < seriesCount; i++) {
     * Comparable key = this.dataset.getSeriesKey(i);
     * Number x = this.dataset.getX(i, j);
     * Number y = this.dataset.getY(i, j);
     * // if (j == 0) {
     * header += "Tiempo [ms],";
     * header += String.format("%s,", key);
     * // }
     * // if (i == 0) {
     * 
     * aux += String.format("%s,", x);
     * // }
     * 
     * aux += String.format("%s,", y);
     * }
     * if (j == 0) {
     * 
     * csv.add(header);
     * }
     * csv.add(aux);
     * aux = "";
     * }
     * 
     * try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename +
     * ".csv"));) {
     * for (String line : csv) {
     * writer.append(line);
     * writer.newLine();
     * }
     * } catch (IOException e) {
     * throw new IllegalStateException("Cannot write dataset", e);
     * }
     * System.out.println("exporte como csv");
     * 
     * }
     *
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            System.err.println(cmd);
            System.err.println("aca");

            if (SET_TEST_PARAMETERS_BUTTON_LABEL.equals(cmd)) {

                try {
                    // Cargar tipo de parámetro
                    testTypes test = getTestType();
                    // int runtime=this.getTestRuntime();
                    System.out.println(test);

                    blockInput(testStates.TEST_PARAMETER_READY);

                    if (test == testTypes.TORQUE_VS_SPEED) {
                        System.out.println("torquevsspeed");
                        try {
                            getController().selectTorqueVsSpeed();
                            getController().setTestEndTime(frame.getInputPanel().stopTime.getText());
                            getController().setTorqueVsSpeedParameters(
                                    frame.getInputPanel().torqueEquationParameters.getParameterValues());

                            blockInput(testStates.TEST_RUNNING);
                        } catch (Exception e) {

                            System.err.println("Tire error");

                            System.err.println(e.getMessage());

                            blockInput(testStates.TEST_RUNNING);
                            // blockInput(testStates.TEST_PARAMETER_READY);
                        }
                    } else if (test == testTypes.TORQUE_VS_TIME) {

                        getController().selectTorqueVsTime();
                        blockInput(testStates.TEST_RUNNING);
                        // getController().setTestEndTime(frame.getInputPanel().stopTime.getText());

                        /*
                         * TorqueTimeValues torqueTimeValue = this.getTorqueTimeValues();
                         * // getController().selectTorqueVsTime();
                         * bufferTimer.scheduleAtFixedRate(new sendTorqueCommands(model,
                         * torqueTimeValue.getTimestamp(),
                         * torqueTimeValue.getTimestamp()), 0, 500, TimeUnit.MILLISECONDS);
                         */

                    }
                    blockInput(testStates.TEST_RUNNING);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    alert(e.getMessage());

                    // blockInput(testStates.TEST_PARAMETER_LOAD);
                    blockInput(testStates.TEST_RUNNING);
                }
            } else if (WRITE_CSV.equals(cmd)) {
                String pattern = "yyyyMMdd HHmmss";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                // storeDataSet(CSV_FILEPATH + todayAsString);
            } else if (BROWSE_FILE_BUTTON_LABEL.equals(cmd)) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(Views.this.frame);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    frame.getInputPanel().filename.setText(c.getSelectedFile().getName());
                    String dir = c.getCurrentDirectory().toString();
                    String path = dir + "\\" + frame.getInputPanel().filename.getText();

                    getController().createTorqueTimeFromCSV(path);
                    plotTorqueTime();
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    frame.getInputPanel().filename.setText("");
                }
            } else if (CONNECT_BUTTON_LABEL.equals(cmd)) {
                String url = getTargetIP();
                System.err.println(url);
                try {

                    getController().connect(url);
                    Thread.sleep(100);
                    getController().PLCStart();
                    blockInput(testStates.PLC_CONNECTED);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    alert(e.getMessage());

                    blockInput(testStates.PLC_CONNECTED);
                    // blockInput(testStates.INITIAL);
                }
            } else if (SHUTDOWN_BUTTON_LABEL.equals(cmd)) {
                getController().stopMeasurements();
                System.err.println("estoy en apagar");
                plotUpdater.cancel(true);
                try {

                    getController().powerOff();
                    // TODO: Necesita un retardo?
                    getController().PLCStop();
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());

                }

            } else if (EMERGENCY_STOP_BUTTON_LABEL.equals(cmd)) {
                frame.getInputPanel().emergencyButton.setText(EMERGENCY_RELEASE_BUTTON_LABEL);
                System.err.println("estoy en EMG Stop");

                try {

                    getController().emergencyStop();

                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                }
            } else if (EMERGENCY_RELEASE_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en EMG Stop");
                frame.getInputPanel().emergencyButton.setText(EMERGENCY_STOP_BUTTON_LABEL);
                try {

                    getController().emergencyRelease();

                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                }
            } else if (POWER_ON_BUTTON_LABEL.equals(cmd)) {

                System.err.println("estoy en potencia");

                try {

                    getController().powerOn();
                    blockInput(testStates.POWER_CONNECTED);

                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                    blockInput(testStates.POWER_CONNECTED);
                    // blockInput(testStates.PLC_CONNECTED);
                }
            } else if (START_BUTTON_LABEL.equals(cmd)) {
                System.err.println("estoy en iniciar");
                getController().startMeasurements();
                plotUpdater.execute();
                frame.getInputPanel().startButton.setText(PAUSE_BUTTON_LABEL);

                try {
                    getController().start();

                    blockInput(testStates.TEST_RUNNING);

                }

                catch (Exception e) {
                    System.err.println("Tire error");
                    // blockInput(testStates.TEST_LOAD_READY);
                    blockInput(testStates.TEST_RUNNING);
                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                }
                // TODO Agregar lógica de inicio de ensayo
            } else if (PAUSE_BUTTON_LABEL.equals(cmd))

            {
                frame.getInputPanel().startButton.setText(START_BUTTON_LABEL);
                System.err.println("estoy en pausa");
                try {
                    // getController().stop();
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                }
                // TODO Agregar lógica de inicio de ensayo

            }

        }

    }

    private class TestTypeHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getSource() == frame.getInputPanel().torqueTestModeComboBox) {

                if (frame.getInputPanel().torqueTestModeComboBox.getSelectedItem() == testTypes.TORQUE_VS_SPEED) {
                    // Tiempo de inicio-fin
                    // Componentes de ecuación cupla
                    torqueVsTimeVisibility(false);

                } else if (frame.getInputPanel().torqueTestModeComboBox
                        .getSelectedItem() == testTypes.TORQUE_VS_TIME) {

                    torqueVsTimeVisibility(true);
                }
                // l1.setText(c1.getSelectedItem() + " selected");
            }
        }

    }
}
