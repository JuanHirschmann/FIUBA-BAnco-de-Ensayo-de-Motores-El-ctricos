package Controller;

import static Views.Constants.CSV_DELIMITER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class TorqueTimeValues {
    private int length=0;
    private int onePeriodLength=0;
    private Map<String, ArrayList<String>> data = new Hashtable<>();
    public TorqueTimeValues(TorqueTimeValues copyTarget)
    {
        this.length=copyTarget.length;
        System.out.println(this.length);
        this.onePeriodLength=copyTarget.onePeriodLength;
        for (String key: copyTarget.data.keySet())
        {  
            data.put(key, new ArrayList<String>(copyTarget.data.get(key)));
            

        }
    }
    public TorqueTimeValues()
    {
        data.put("value", new ArrayList<String>());
        data.put("timestamp", new ArrayList<String>());
    }
    public float getMaxTorque()
    {
        float max=0;
        for(int i =0;i<data.get("value").size();i++)
        {
            if(Float.valueOf(data.get("value").get(i))>max)
            {
                max=Float.valueOf(data.get("value").get(i));
            }
        }
        return max;
    }
    public int length()
    {
        return length;
    }
    public void extend(int periods)
    {
        String aux;
        Float time_delta;
        int initialLength=onePeriodLength;
        this.dropExtentions();
        for(int i=initialLength;i<(periods*initialLength);i++)
        {   
            aux=data.get("value").get(i-initialLength);
            data.get("value").add(aux);
            aux=data.get("timestamp").get(i-initialLength);
            time_delta=Float.valueOf(aux)+Float.valueOf(data.get("timestamp").get(initialLength-1));
            data.get("timestamp").add(String.valueOf(time_delta));
            length++;
        }
        System.err.println("extendi");
        System.err.println(length);
    }
    public void dropExtentions()
    {
        int initialLength=onePeriodLength;
        int counter=0;
        for(int i=initialLength;i<this.length;i++)
        {   
            data.get("value").remove(initialLength);
            data.get("timestamp").remove(initialLength);
            counter++;
        }
        this.length-=counter;
        System.err.println("reduje");
        System.err.println(length);
    }
    public void clear()
    {
        this.data.clear();
        this.length=0;
        this.onePeriodLength=0;
        
        data.put("value", new ArrayList<String>());
        data.put("timestamp", new ArrayList<String>());
    }
    public void fromCSV(String filepath)
    {
        this.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(CSV_DELIMITER);
                data.get("timestamp").add(values[0]);
                data.get("value").add(values[1]);
                length++;
                onePeriodLength++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dataset", e);
        }
    }
    public ArrayList<String> getTimestamp()
    {
        return this.data.get("timestamp");
    }
    
    public ArrayList<String> getValue()
    {
        return this.data.get("value");
    }
    public String getTimestamp( int index)
    {
        String timestamp;
        try
        { 
            timestamp=this.data.get("timestamp").get(index);
        }
        catch(IndexOutOfBoundsException e){
            timestamp=null;
        }
        return timestamp;
    }
    
    public String getValue(int index)
    {
        String value;
        try
        {

            value=this.data.get("value").get(index);
        }
        catch(IndexOutOfBoundsException e){
            value=null;
        }
        return value;
    }
}

