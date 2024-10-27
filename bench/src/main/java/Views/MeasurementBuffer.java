package Views;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.jfree.data.general.Dataset;

import java.util.Collections;
import static Model.Constants.GRAPH_BUFFER_SIZE;

public class MeasurementBuffer {
    Map<String, ArrayList<Float>> data = new HashMap<>();
    Set <String> keyset=new HashSet<>();

    public void addTimeSeries(String time_series_name) {
        keyset.add(time_series_name);
        data.put(time_series_name, new ArrayList<Float>());
        data.put(time_series_name + "_timestamp", new ArrayList<Float>());
    };

    public void addValue(String time_series_name, float measurement, double timestamp) {
        if (data.get(time_series_name) == null) {
            this.addTimeSeries(time_series_name);
            System.out.println(time_series_name);
        }
        //System.out.println(time_series_name);
        //System.out.println(measurement);
        //System.out.println(timestamp);
        data.get(time_series_name).add(measurement);
        data.get(time_series_name + "_timestamp").add((float) timestamp);
    }

    public void clearBuffer() {
        for (String key : data.keySet()) {

            data.get(key).clear();
        }

    }

    public void clearBuffer(String time_series_name) {
        this.data.get(time_series_name).clear();
        this.data.get(time_series_name + "_timestamp").clear();
    }

    public ArrayList<Float> getBufferedData(String time_series_name) {
        return this.data.get(time_series_name);
    }

    public ArrayList<Float> getBufferedDataTimestamp(String time_series_name) {
        return this.data.get(time_series_name + "_timestamp");
    }

    public Set<String> getKeySet() {
        return this.keyset;
    }

    public static void main(String[] args) {
        MeasurementBuffer buffer = new MeasurementBuffer();

        System.out.println("aca");
        buffer.addTimeSeries("speed");

        double time = 1;
        float value = 1;
        for (int i = 0; i < 100; i++) {
            buffer.addValue("speed", value * i, time * i);
            buffer.addValue("speed2", value * i*2, time * i);
        }
        // Dictionary<String, ArrayList<Float>> speed_buffer= new Hashtable<>();

        for (String key : buffer.getKeySet()) {
            ArrayList<Float> speed = new ArrayList<Float>(buffer.getBufferedData(key));
            //ArrayList<Float> timestamp = new ArrayList<Float>(buffer.getBufferedDataTimestamp(key+""));
            for (int i = 0; i < speed.size(); i++) {
                System.out.println(speed.get(i));
                //System.out.println(timestamp.get(i));

            }

        }
        System.out.println("termine");
    }
}
