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
import static Views.Constants.SELF_SUSTAINED_TEST_IMPORT_LABEL;
import static Views.Constants.SHUTDOWN_BUTTON_LABEL;
import static Views.Constants.START_BUTTON_LABEL;
import static Views.Constants.SET_TEST_PARAMETERS_BUTTON_LABEL;
import static Views.Constants.WRITE_CSV;
import static Views.Constants.SELF_SUSTAINED_TEST_LABEL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.desktop.PrintFilesEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Views.Constants.testStates;
import Model.Constants.testTypes;
import Swing.MainFrame;
import Swing.SelfSustainedTestFrame;
import Swing.TorqueEquationParameter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.security.KeyException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.scilab.forge.jlatexmath.TeXFormula;

import Controller.Controller;
import Controller.MeasurementBuffer;
import Controller.TorqueTimeValues;
import Controller.ViewListener;
import Model.Constants.commands;
import Model.Constants.serverSideTestStatus;
import Model.Constants.testStatus;

public class Views implements ViewListener {

    Controller appController = null;

    private MainFrame frame = new MainFrame();
    private SelfSustainedTestFrame selfSustaintedFrame =new SelfSustainedTestFrame();
    private SwingPlotWorker plotUpdater = new SwingPlotWorker();

    /**
     * @param controller
     */
    public void setController(Controller controller) {
        appController = controller;
    };
    /**
    * @param controller
    */
   public boolean DUTModeSelected() {
       return this.frame.getInputPanel().selfSustainedTestSelection.isSelected();
   };

    /**
     * Returns controller instance
     * 
     * @return Controller
     */
    public Controller getController() {
        return this.appController;
    }

    private Map<String, XYSeriesCollection> mainDataset = new Hashtable<>();
    private Map<String, XYSeriesCollection> selfSustainedDataset = new Hashtable<>();

    private XYSeries torqueData = new XYSeries(commands.TORQUE.seriesName);
    private XYSeries speedData = new XYSeries(commands.SPEED.seriesName);
    private XYSeries voltageData = new XYSeries(commands.VOLTAGE.seriesName);
    private XYSeries powerData = new XYSeries(commands.POWER.seriesName);
    private XYSeries currentData = new XYSeries(commands.CURRENT.seriesName);
    private XYSeries torqueCommandData = new XYSeries(commands.TORQUE_COMMAND.seriesName);

    private XYSeries selfSustainedTorqueData = new XYSeries(commands.DUT_TORQUE.seriesName);
    private XYSeries selfSustainedSpeedData = new XYSeries(commands.DUT_SPEED.seriesName);
    private XYSeries selfSustainedVoltageData = new XYSeries(commands.DUT_VOLTAGE.seriesName);
    private XYSeries selfSustainedPowerData = new XYSeries(commands.DUT_POWER.seriesName);
    private XYSeries selfSustainedCurrentData = new XYSeries(commands.DUT_CURRENT.seriesName);

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
                        if (key == "speed") {
                            mainDataset.get(key).getSeries(key).add(timestamp.get(i), value.get(i));
                        } else {
                            try{
                                
                                mainDataset.get(key).getSeries(key).add(timestamp.get(i), value.get(i));
                            }
                            catch(Exception e)
                            {
                                System.out.println(key);
                                selfSustainedDataset.get(key).getSeries(key).add(timestamp.get(i), value.get(i));
                            }

                        }

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

        }

    }

    public Views() {

        this.setup();

        this.torqueVsTimeVisibility(true);

    }

    /**
     * Updates external's buffer load elements
     * 
     * @param currentValue current elements loaded on buffer
     * @param finalValue   total elements to load
     */
    public void updateTorqueTimeLoad(int currentValue, int finalValue) {
        String output = String.valueOf(currentValue) + " / " + String.valueOf(finalValue);
        frame.getInputPanel().itemsLoadedLabel.setText(output);

    }

    /**
     * Updates the connection to PLC status
     * 
     * @param isConnected
     */
    public void updateConnectionStatus(boolean isConnected) {
        if (isConnected) {

            frame.getInputPanel().connectionIndicator.green();
        } else {

            frame.getInputPanel().connectionIndicator.red();
        }
    }

    /**
     * Updates server-side test status on semaphore indicator
     * 
     * @param testState serverSideTestStatus (NOT_STARTED, RUNNING, STOPPED....)
     */
    public void updateTestStatus(serverSideTestStatus testState) {
        frame.getInputPanel().semaphoreIndicator.setColor(testState.getColor());
        frame.getInputPanel().semaphoreIndicator.setText(testState.name());

    }

    /**
     * Changes power connection power indicator
     * 
     * @param isConnected
     */
    public void updateALMStatus(boolean isConnected) {
        if (isConnected) {

            frame.getInputPanel().powerOnIndicator.green();
        } else {

            frame.getInputPanel().powerOnIndicator.red();
        }
    }

    /**
     * updates loaded test status on test indicator.
     * 
     * @param isLoaded
     */
    public void updateLoadedTestStatus(boolean isLoaded) {
        if (isLoaded) {

            frame.getInputPanel().loadedTestIndicator.green();
        } else {

            frame.getInputPanel().loadedTestIndicator.red();
        }
    }

    /*
     * Set up of main frame panels.
     */
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

        frame.getInputPanel().selfSustainedTestSelection.addActionListener(new ButtonHandler());
        frame.getInputPanel().openDUTFileButton.addActionListener(new ButtonHandler());

        createChart();
        createSelfSustainedChart();
        this.blockInput(testStates.INITIAL);

    }

    /**
     * Blocks user access considering current test step and input error
     * 
     * @param currentStep
     */
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

        } else if (currentStep == testStates.TEST_END) {
            frame.getInputPanel().buttonConnect.setEnabled(false);
            frame.getInputPanel().powerOnButton.setEnabled(false);
            frame.getInputPanel().startButton.setEnabled(true);
            frame.getInputPanel().emergencyButton.setEnabled(false);
            frame.getInputPanel().shutdownButton.setEnabled(false);
            frame.getInputPanel().targetIP.setEnabled(false);

            frame.getInputPanel().variablesPanel.setEnabled(false);
            frame.getInputPanel().saveCSVButton.setEnabled(true);
        }
        ;
    }

    /**
     * Creates Chart plot
     * 
     * @return JFreeChart
     */
    private JFreeChart createChart() {

        mainDataset.put(commands.TORQUE.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.VOLTAGE.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.SPEED.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.CURRENT.seriesName, new XYSeriesCollection());
        mainDataset.put(commands.POWER.seriesName, new XYSeriesCollection());

        mainDataset.get(commands.TORQUE.seriesName).addSeries(torqueData);
        mainDataset.get(commands.TORQUE.seriesName).addSeries(torqueCommandData);
        mainDataset.get(commands.VOLTAGE.seriesName).addSeries(voltageData);
        mainDataset.get(commands.SPEED.seriesName).addSeries(speedData);
        mainDataset.get(commands.CURRENT.seriesName).addSeries(currentData);
        mainDataset.get(commands.POWER.seriesName).addSeries(powerData);

        

        // construct the plot
        XYPlot topPlot = new XYPlot();
        topPlot.setDataset(0, mainDataset.get(commands.TORQUE.seriesName));
        topPlot.setDataset(1, mainDataset.get(commands.SPEED.seriesName));
        topPlot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        topPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        topPlot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        topPlot.getRenderer(0).setSeriesStroke(0, new BasicStroke(2f));
        topPlot.getRenderer(0).setSeriesStroke(1, new BasicStroke(2f));
        topPlot.getRenderer(1).setSeriesStroke(0, new BasicStroke(2f));

        /*
         * renderer.setSeriesStroke(1, new BasicStroke(2f));
         * XYItemRenderer renderer2=topPlot.getRenderer(1);
         * renderer2.setSeriesStroke(0, new BasicStroke(2f));
         */

        topPlot.setRangeAxis(0, new NumberAxis("Torque [Nm]"));
        topPlot.setRangeAxis(1, new NumberAxis("Velocidad [RPM]"));

        topPlot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
        topPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        topPlot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        topPlot.mapDatasetToRangeAxis(0, 0);
        topPlot.mapDatasetToRangeAxis(1, 1);
        // customize the plot with renderers and axis
        XYPlot bottomPlot = new XYPlot();
        bottomPlot.setDataset(0, mainDataset.get(commands.VOLTAGE.seriesName));
        bottomPlot.setDataset(1, mainDataset.get(commands.CURRENT.seriesName));
        bottomPlot.setDataset(2, mainDataset.get(commands.POWER.seriesName));
        bottomPlot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(0).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(1).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(2).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRangeAxis(0, new NumberAxis("Tensión [Vrms]"));
        bottomPlot.setRangeAxis(1, new NumberAxis("Corriente [Arms]"));
        bottomPlot.setRangeAxis(2, new NumberAxis("Potencia [KW]"));
        bottomPlot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.mapDatasetToRangeAxis(0, 0);
        bottomPlot.mapDatasetToRangeAxis(1, 1);
        bottomPlot.mapDatasetToRangeAxis(2, 2);
        bottomPlot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        // generate the chart
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Tiempo [ms]"));
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.add(topPlot);
        plot.add(bottomPlot);
        JFreeChart chart = new JFreeChart("Ensayo en tiempo real", null, plot, true);
        frame.setChart(chart);
        return chart;
    }
    /**
     * Creates Chart plot
     * 
     * @return JFreeChart
     */
    private JFreeChart createSelfSustainedChart() {


        selfSustainedDataset.put(commands.DUT_TORQUE.seriesName, new XYSeriesCollection());
        selfSustainedDataset.put(commands.DUT_VOLTAGE.seriesName, new XYSeriesCollection());
        selfSustainedDataset.put(commands.DUT_SPEED.seriesName, new XYSeriesCollection());
        selfSustainedDataset.put(commands.DUT_CURRENT.seriesName, new XYSeriesCollection());
        selfSustainedDataset.put(commands.DUT_POWER.seriesName, new XYSeriesCollection());

        selfSustainedDataset.get(commands.DUT_TORQUE.seriesName).addSeries(selfSustainedTorqueData);
        selfSustainedDataset.get(commands.DUT_VOLTAGE.seriesName).addSeries(selfSustainedVoltageData);
        selfSustainedDataset.get(commands.DUT_SPEED.seriesName).addSeries(selfSustainedSpeedData);
        selfSustainedDataset.get(commands.DUT_CURRENT.seriesName).addSeries(selfSustainedCurrentData);
        selfSustainedDataset.get(commands.DUT_POWER.seriesName).addSeries(selfSustainedPowerData);

        // construct the plot
        XYPlot topPlot = new XYPlot();
        topPlot.setDataset(0, selfSustainedDataset.get(commands.DUT_TORQUE.seriesName));
        topPlot.setDataset(1, selfSustainedDataset.get(commands.DUT_SPEED.seriesName));
        topPlot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        topPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        topPlot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        topPlot.getRenderer(0).setSeriesStroke(0, new BasicStroke(2f));
        topPlot.getRenderer(0).setSeriesStroke(1, new BasicStroke(2f));
        topPlot.getRenderer(1).setSeriesStroke(0, new BasicStroke(2f));

        /*
         * renderer.setSeriesStroke(1, new BasicStroke(2f));
         * XYItemRenderer renderer2=topPlot.getRenderer(1);
         * renderer2.setSeriesStroke(0, new BasicStroke(2f));
         */

        topPlot.setRangeAxis(0, new NumberAxis("Torque [Nm]"));
        topPlot.setRangeAxis(1, new NumberAxis("Velocidad [RPM]"));

        topPlot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
        topPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        topPlot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        topPlot.mapDatasetToRangeAxis(0, 0);
        topPlot.mapDatasetToRangeAxis(1, 1);
        // customize the plot with renderers and axis
        XYPlot bottomPlot = new XYPlot();
        bottomPlot.setDataset(0, selfSustainedDataset.get(commands.DUT_VOLTAGE.seriesName));
        bottomPlot.setDataset(1, selfSustainedDataset.get(commands.DUT_CURRENT.seriesName));
        bottomPlot.setDataset(2, selfSustainedDataset.get(commands.DUT_POWER.seriesName));
        bottomPlot.setRenderer(0, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(0).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(1).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRenderer(2, new XYLineAndShapeRenderer(true, false));
        bottomPlot.getRenderer(2).setSeriesStroke(0, new BasicStroke(2f));

        bottomPlot.setRangeAxis(0, new NumberAxis("Tensión [Vrms]"));
        bottomPlot.setRangeAxis(1, new NumberAxis("Corriente [Arms]"));
        bottomPlot.setRangeAxis(2, new NumberAxis("Potencia [KW]"));
        bottomPlot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
        bottomPlot.mapDatasetToRangeAxis(0, 0);
        bottomPlot.mapDatasetToRangeAxis(1, 1);
        bottomPlot.mapDatasetToRangeAxis(2, 2);
        bottomPlot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        // generate the chart
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Tiempo [ms]"));
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.add(topPlot);
        plot.add(bottomPlot);
        JFreeChart chart = new JFreeChart("Ensayo en tiempo real (DUT)", null, plot, true);
        selfSustaintedFrame.setChart(chart);
        return chart;
    }

    /**
     * Handles period extension for CSV waveform input
     */
    private class PeriodExtensionHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (getController().getTorqueTimeValues().length() != 0) {

                getController().extendTorqueTimeValues((int) frame.getInputPanel().testPeriodsSpinner.getValue());

                plotTorqueTime();
            }
        }
    }

    /**
     * set user input visibility for torque vs time test commands
     * 
     * @param visible
     */
    public void torqueVsTimeVisibility(boolean visible) {
        frame.getInputPanel().stopTime.setVisible(!visible);
        frame.getInputPanel().filename.setVisible(visible);
        frame.getInputPanel().openFileButton.setVisible(visible);
        frame.getInputPanel().torqueEquation.setVisible(!visible);
        frame.getInputPanel().torqueEquationParameters.setVisible(!visible);
        frame.getInputPanel().testPeriodLabel.setVisible(visible);
        frame.getInputPanel().testPeriodsSpinner.setVisible(visible);
        frame.getInputPanel().itemsLoadedLabel.setVisible(visible);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    /**
     * set user input visibility for mixed test commands
     * 
     * @param visible
     */
    public void mixedTestVisibility(boolean visible) {
        frame.getInputPanel().stopTime.setVisible(!visible);
        frame.getInputPanel().filename.setVisible(visible);
        frame.getInputPanel().openFileButton.setVisible(visible);
        frame.getInputPanel().torqueEquation.setVisible(visible);
        frame.getInputPanel().torqueEquationParameters.setVisible(visible);
        frame.getInputPanel().testPeriodLabel.setVisible(visible);
        frame.getInputPanel().testPeriodsSpinner.setVisible(visible);
        frame.getInputPanel().itemsLoadedLabel.setVisible(visible);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    /**
     * return user input target IP
     * 
     * @return String
     */
    public String getTargetIP() {
        System.err.println(frame.getInputPanel().targetIP.getText());
        return frame.getInputPanel().targetIP.getText();
    }

    /**
     * Triggers window popup
     * 
     * @param message
     */
    public void alert(String message) {
        frame.getInputPanel().userMessageAlert.showMessageDialog(null, message, "Alerta",
                JOptionPane.ERROR_MESSAGE);// setText(message);

    }

    /**
     * Returns torque equation parameters
     * 
     * @return Map<String, TorqueEquationParameter>
     */
    public Map<String, TorqueEquationParameter> getTorqueEquationParameters() {
        return frame.getInputPanel().torqueEquationParameters.getParameterValues();
    }

    /**
     * Gets test type
     * 
     * @return testTypes
     */
    public testTypes getTestType() {

        testTypes selectedTest = (testTypes) frame.getInputPanel().torqueTestModeComboBox.getSelectedItem();
        return selectedTest;
    }

    /**
     * Creates torque vs time plot
     */
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

    /**
     * Creates torque vs time plot
     */
    private void plotSpeedTime() {

        TorqueTimeValues speedTime = new TorqueTimeValues(getController().getSpeedTimeValues());
        selfSustainedDataset.get(commands.DUT_SPEED.seriesName).getSeries(commands.DUT_SPEED.seriesName).clear();
        for (int i = 0; i < speedTime.length(); i++) {
            selfSustainedDataset.get(commands.DUT_SPEED.seriesName).getSeries(commands.DUT_SPEED.seriesName).add(
                    Float.valueOf(speedTime.getTimestamp(i)),
                    Float.valueOf(speedTime.getValue(i)));
            ;
        }

    }

    /**
     * Stores chart data to CSV file
     * 
     * @param filename
     */
    //TODO: esto no guarda todos los datos por algun motivo
    private void storeDataSet(String filename) {

        java.util.List<String> csv = new ArrayList<>();
        int maxItemCount = 0;
        if(this.DUTModeSelected())
        {
            mainDataset.putAll(selfSustainedDataset);
        }
        for (String series : mainDataset.keySet()) {
            int itemCount = this.mainDataset.get(series).getSeries(series).getItemCount();
            System.out.print(series);
            System.out.print(" : ");
            System.out.print(itemCount);
            if (maxItemCount < itemCount) {
                maxItemCount = itemCount;
            }
        }
        String header = "";
        String aux = "";
        for (int j = 0; j < maxItemCount; j++) {
            for (String key : this.mainDataset.keySet()) {
                List<XYSeries> seriesList = this.mainDataset.get(key).getSeries();
                Iterator<XYSeries> it = seriesList.iterator();
                while (it.hasNext()) {
                    XYSeries currentSeries = it.next();

                    try {
                        Number x = currentSeries.getX(j);
                        Number y = currentSeries.getY(j);
                        aux += String.format("%s,", x);
                        aux += String.format("%s,", y);
                    } catch (IndexOutOfBoundsException e) {
                        aux += String.format("%s,", "");
                        aux += String.format("%s,", "");
                    }
                    header += "Tiempo [ms],";
                    header += String.format("%s,", currentSeries.getKey());

                }

            }
            if (j == 0) {

                csv.add(header);
            }
            csv.add(aux);
            aux = "";

        }
        try (

                BufferedWriter writer = new BufferedWriter(new FileWriter(filename +
                        ".csv"));) {
            for (String line : csv) {
                writer.append(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dataset", e);
        }
        System.out.println("exporte como csv");
    }

    /**
     * Implements behaviour for button input
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

                            //blockInput(testStates.TEST_RUNNING);
                        } catch (Exception e) {

                            alert(e.getMessage());

                            //blockInput(testStates.TEST_RUNNING);
                            blockInput(testStates.TEST_PARAMETER_LOAD);
                        }
                    } else if (test == testTypes.TORQUE_VS_TIME) {

                        getController().selectTorqueVsTime();
                        //blockInput(testStates.TEST_RUNNING);

                    } else if (test == testTypes.MIXED_TEST) {

                        try {
                            getController().selectMixedTest();
                            getController().setTorqueVsSpeedParameters(
                                    frame.getInputPanel().torqueEquationParameters.getParameterValues());

                            blockInput(testStates.TEST_RUNNING);
                        } catch (Exception e) {

                            System.err.println("Tire error");

                            System.err.println(e.getMessage());
                            blockInput(testStates.TEST_PARAMETER_LOAD);
                        }

                    }
                    if(frame.getInputPanel().selfSustainedTestSelection.isSelected())
                    {
                        getController().selectSelfSustainedMode();
                    }
                    //blockInput(testStates.TEST_RUNNING);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    alert(e.getMessage());

                    blockInput(testStates.TEST_PARAMETER_LOAD);
                    //blockInput(testStates.TEST_RUNNING);
                }
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
                    Thread.sleep(1000);
                    getController().PLCStart();
                    blockInput(testStates.PLC_CONNECTED);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    alert(e.getMessage());

                    //blockInput(testStates.PLC_CONNECTED);
                    blockInput(testStates.INITIAL);
                }
            } else if (SHUTDOWN_BUTTON_LABEL.equals(cmd)) {
                getController().stopMeasurements();
                System.err.println("estoy en apagar");
                plotUpdater.cancel(true);
                try {

                    getController().powerOff();
                    getController().PLCStop();
                    blockInput(testStates.TEST_END);
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                    // blockInput(testStates.TEST_RUNNING);
                    blockInput(testStates.TEST_END);

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
                frame.inputPanel.measurementsPanel.setVisible(true);
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
            } else if (PAUSE_BUTTON_LABEL.equals(cmd)) {
                frame.getInputPanel().startButton.setText(START_BUTTON_LABEL);
                System.err.println("estoy en pausa");
                try {
                    getController().stop();
                } catch (Exception e) {
                    System.err.println("Tire error");

                    System.err.println(e.getMessage());
                    alert(e.getMessage());
                }
                // TODO Agregar lógica de reinicio de ensayo

            } else if (SELF_SUSTAINED_TEST_LABEL.equals(cmd)) {
                if (frame.getInputPanel().selfSustainedTestSelection.isSelected()) {
                    selfSustaintedFrame.setVisible(true);
                    frame.getInputPanel().DUTFilename.setVisible(true);
                    frame.getInputPanel().openDUTFileButton.setVisible(true);
                    createSelfSustainedChart();
                } else {
                    selfSustaintedFrame.setVisible(false);
                    frame.getInputPanel().DUTFilename.setVisible(false);
                    frame.getInputPanel().openDUTFileButton.setVisible(false);

                }
            } else if (SELF_SUSTAINED_TEST_IMPORT_LABEL.equals(cmd)) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Open" dialog:
                int rVal = c.showOpenDialog(Views.this.frame);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    frame.getInputPanel().DUTFilename.setText(c.getSelectedFile().getName());
                    String dir = c.getCurrentDirectory().toString();
                    String path = dir + "\\" + frame.getInputPanel().DUTFilename.getText();

                    getController().createSpeedTimeFromCSV(path);
                    plotSpeedTime();
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    frame.getInputPanel().filename.setText("");
                }

            }
        }
    }
    /**
     * Implements behaviour for
     * different test types
     * 
     */
    private class TestTypeHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent event) {
            if (event.getSource() == frame.getInputPanel().torqueTestModeComboBox) {

                if (frame.getInputPanel().torqueTestModeComboBox.getSelectedItem() == testTypes.TORQUE_VS_SPEED) {

                    torqueVsTimeVisibility(false);

                } else if (frame.getInputPanel().torqueTestModeComboBox
                        .getSelectedItem() == testTypes.TORQUE_VS_TIME) {

                    torqueVsTimeVisibility(true);
                } else if (frame.getInputPanel().torqueTestModeComboBox
                        .getSelectedItem() == testTypes.MIXED_TEST) {

                    mixedTestVisibility(true);
                }
            }

        }

    }
}
