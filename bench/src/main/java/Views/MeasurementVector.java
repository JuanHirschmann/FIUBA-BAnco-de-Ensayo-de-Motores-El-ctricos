package Views;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Collections;

public class MeasurementVector {
    Dictionary<String, ArrayList<Float>> data = new Hashtable<>();

    public MeasurementVector() {
        ArrayList<Float> timestamp = new ArrayList<Float>();
        this.addTimeSeries("timestamp");
    };

    public void addTimeSeries(String time_series_name)
    {
        data.put(time_series_name, null);
    };

    public void addValue(String time_series_name, Float[] measurement)
    {
        Collections.addAll(data.get(time_series_name),measurement);
    }
}
