/* package Views;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

public class Chart {
    
    public Chart(String plotname,) {

        // private XYSeriesCollection torqueDataset = new XYSeriesCollection();
        // private XYSeriesCollection speedDataset = new XYSeriesCollection();
        // private XYSeriesCollection currentDataset = new XYSeriesCollection();
        // private XYSeriesCollection voltageDataset = new XYSeriesCollection();
        // private XYSeriesCollection powerDataset = new XYSeriesCollection();

        // create the datasets
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
        // dataset1.addSeries(series1);
        // dataset2.addSeries(series2);

        // construct the plot
        XYPlot plot = new XYPlot();
        plot.setDataset(0, mainDataset.get(commands.TORQUE.seriesName));
        plot.setDataset(1, mainDataset.get(commands.SPEED.seriesName));
        plot.setDataset(2, mainDataset.get(commands.VOLTAGE.seriesName));
        plot.setDataset(3, mainDataset.get(commands.CURRENT.seriesName));
        plot.setDataset(4, mainDataset.get(commands.POWER.seriesName));
        // customize the plot with renderers and axis
        // r1.setSeriesPaint(0, Color.BLACK);
        plot.setRenderer(0, new XYLineAndShapeRenderer());
        plot.setRenderer(1, new XYLineAndShapeRenderer());
        plot.setRenderer(2, new XYLineAndShapeRenderer());
        plot.setRenderer(3, new XYLineAndShapeRenderer());
        plot.setRenderer(4, new XYLineAndShapeRenderer());
        // plot.setRenderer(1, splinerenderer);
        plot.setRangeAxis(0, new NumberAxis("Torque [Nm]"));
        plot.setRangeAxis(1, new NumberAxis("Velocidad [RPM]"));
        plot.setRangeAxis(2, new NumberAxis("Tensión [Vrms]"));
        plot.setRangeAxis(3, new NumberAxis("Corriente [Arms]"));
        plot.setRangeAxis(4, new NumberAxis("Potencia [KW]"));
        plot.setDomainAxis(new NumberAxis("Tiempo[ms]"));

        // Map the data to the appropriate axis
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.mapDatasetToRangeAxis(2, 2);
        plot.mapDatasetToRangeAxis(3, 3);
        plot.mapDatasetToRangeAxis(4, 4);
        // generate the chart
        JFreeChart chart = new JFreeChart("MyPlot", null, plot, true);
        // JPanel jpanel = new ChartPanel(chart);
        // chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }
}
 */