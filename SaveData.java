/*
 * SaveDate.java
 *
 * Created on May 25, 2007, 11:09 AM
 */

package huffman;
import java.io.*;
/**
 * class for saving the initial sorted array
 *  of data.<br>
 * 
 * @author Tommy Tran, Danhiel T VU, TaeHoon Moon, Quan Dinh Tran 
 * @version 1.0 6/9/18 Test Environment: NetBeans 8.2 
 */
public class SaveData implements Serializable
{
    private char data = '\0';
    private short occurrances = 0;

    /**
     * Creates a new instance of SaveDate
     */
    public SaveData() {}
    
    /** 
     * Creates a new instance of SaveDate
     * @param c the data char
     * @param o the number of occurrences
     */
    public SaveData(char c, short o)
    {
        data = c;
        occurrances = o;
    }

    /**
     * accessors
     * @return data
     */
    public char getData()
    {
        return data;
    }

    /**
     * accessors
     * @return data
     */
    public short getOccurrances()
    {
        return occurrances;
    }
    /**
     * Show as String
     * @return a string version of this class
     */
    @Override
    public String toString()
    {
        return data + ":" + occurrances + " ";
    }   
}
