package Controller;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class MeasurementBuffer {
    Map<String, ArrayList<Float>> data = new Hashtable<>();
    Set <String> keyset=new HashSet<>();
    public MeasurementBuffer()
    {

    }
    public MeasurementBuffer(MeasurementBuffer targetBuffer)
    {
        this.keyset.addAll(targetBuffer.keyset);
        for(String key: targetBuffer.data.keySet())
        {   
            
            this.data.put(key, new ArrayList<Float>(targetBuffer.data.get(key)));
        }
    }
    
    /** 
     * Adds a time series (value, timestamp) for the measurement buffer. 
     * @param time_series_name
     */
    public void addTimeSeries(String time_series_name) {
        keyset.add(time_series_name);
        data.put(time_series_name, new ArrayList<Float>());
        data.put(time_series_name + "_timestamp", new ArrayList<Float>());
    };

    
    /** 
     * Adds a measurement (value, timestamp) onto the measurement buffer. 
     * @param time_series_name 
     * @param measurement
     * @param timestamp
     */
    public void addValue(String time_series_name, float measurement, double timestamp) {
        if (data.get(time_series_name) == null) {
            this.addTimeSeries(time_series_name);
            //System.out.println(time_series_name);
        }
        data.get(time_series_name).add(measurement);
        data.get(time_series_name + "_timestamp").add((float) timestamp);
    }
    /* 
     * Clears the whole measurement buffer
     */
    public void clearBuffer() {
        for (String key : data.keySet()) {

            data.get(key).clear();
        }

    }

    public void clearBuffer(String time_series_name) {
        this.data.get(time_series_name).clear();
        this.data.get(time_series_name + "_timestamp").clear();
    }
    /* 
     * Gets certain time series values from the buffer as an ArrayList. 
     */
    public ArrayList<Float> getBufferedData(String time_series_name) {
        return new ArrayList<Float>(this.data.get(time_series_name));
    }
    /* 
     * Gets certain time series timestamps from the buffer as an ArrayList. 
     */
    public ArrayList<Float> getBufferedDataTimestamp(String time_series_name) {
        return new ArrayList<Float>(this.data.get(time_series_name + "_timestamp"));
    }
    /* 
     * Gets the timeseries's keyset
     */
    public Set<String> getKeySet() {
        return this.keyset;
    }
    /* 
     * Checks if measurement buffer is empty
     */
    public Boolean isEmpty()
    {
        Boolean output=true;
        for (String key : this.keyset) {
            if(data.get(key).size()!=0)
            {
                output=false;
            }

        }
        return output;
    }
    public static void main(String[] args) {
        MeasurementBuffer buffer = new MeasurementBuffer();

        //System.out.println("aca");
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
                //System.out.println(speed.get(i));
                //System.out.println(timestamp.get(i));

            }

        }
        System.out.println("termine");
    }
}
