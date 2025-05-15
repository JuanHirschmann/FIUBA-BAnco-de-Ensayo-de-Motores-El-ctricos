package Model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class CommandQueue{
    private ArrayList<String> readCommandQueue= new ArrayList<String>();
    private Map<String,String> writeCommandQueue= new Hashtable<String,String>();
    CommandQueue()
    {
        
    }
    public void queueReadCommand(String varName)
    {   
        readCommandQueue.add(varName);
    }
    public void queueWriteCommand(String varName,String varValue)
    {
        writeCommandQueue.put(varName,varValue);
    }
    public Map<String,String> getWriteQueue()
    {
        return writeCommandQueue;
    }
    
    public ArrayList<String>  getReadQueue()
    {
        return readCommandQueue;
    }
    public void clearWriteQueue()
    {
        writeCommandQueue.clear();
    }
    
    public void clearReadQueue()
    {
        readCommandQueue.clear();
    }
}
