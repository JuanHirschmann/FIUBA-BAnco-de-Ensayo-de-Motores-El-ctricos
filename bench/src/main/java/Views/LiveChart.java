package Views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
public final class LiveChart {
    // Grafico
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries torque_data = new XYSeries("Torque");
    XYSeries speed_data = new XYSeries("Speed");
    XYSeries voltage_data = new XYSeries("Voltage");
    XYSeries power_data = new XYSeries("Power");
    XYSeries current_data = new XYSeries("Current");
    private static final String S = "0.000000000000000";
    private final JProgressBar progressBar = new JProgressBar();
    
    private JFreeChart create() {
        JFrame f = new JFrame("√2");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(progressBar, BorderLayout.NORTH);
        JFreeChart chart = ChartFactory.createXYLineChart("Grafico",
                "X", "Y", dataset);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRangeAxis().setRange(1.4, 1.51);
        plot.getDomainAxis().setStandardTickUnits(
               NumberAxis.createIntegerTickUnits());
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);
        f.add(new ChartPanel(chart));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        runCalc();
        return chart;
    }

    private void runCalc() {
        TwoWorker task = new TwoWorker();
        task.addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("progress".equals(e.getPropertyName())) {
                progressBar.setIndeterminate(false);
                progressBar.setValue((Integer) e.getNewValue());
            }
        });
        task.execute();
    }

    private class TwoWorker extends SwingWorker<Double, Double> {

        private static final int N = 100;
        //private final DecimalFormat df = new DecimalFormat(S);
        double x = 1;
        private int n;

        @Override
        protected Double doInBackground() throws Exception {
            for (int i = 1; i <= N; i++) {
                x = x - (((x * x - 2) / (2 * x)));
                setProgress(i * (100 / N));
                publish(x);
                Thread.sleep(1000); // simulate latency
            }
            return x;
        }

        @Override
        protected void process(List<Double> chunks) {
            for (double d : chunks) {
                //label.setText(df.format(d));
                torque_data.add(++n, d);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new LiveChart()::create);
    }
}
