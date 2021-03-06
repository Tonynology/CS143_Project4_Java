/*
 * HuffmanChar.java
 */

package huffman;
import java.io.*;
/**
 * class for read character of Huffman Code.<br>
 * 
 * @author Tommy Tran, Danhiel T VU, TaeHoon Moon, Quan Dinh Tran 
 * @version 1.0 6/9/18 Test Environment: NetBeans 8.2 
 */
public class HuffmanChar extends HuffmanData<Character>
        implements Serializable
{  
    public static final int BITS_IN_BYTE = 8;
    public static final int BYTE_SIZE_NUMBER = 256;
  
    /**
     * Creates a new instance of HuffmanChar
     */
    public HuffmanChar()
    {
        super();
    }
    
    /**
     *  Creates a new instance of HuffmanChar
     * @param c the character
     */
    public HuffmanChar(Character c)
    {
        super(c);
    }

    /**
     * Creates a new instance of HuffmanChar
     * @param c the character
     * @param oc the number of occurances
     */

    public HuffmanChar(Character c, int oc)
    {
        super(c, oc);
    }
    
     /**
      *  Creates a new instance of HuffmanChar
      * @param hc a HuffmanChar
      */
    public HuffmanChar(HuffmanChar hc)
    {
        super(hc.getData(), hc.getOccurances());
    }
   
    /**
     *  Creates a new instance of HuffmanChar
     * @param threeBytes an array of three bytes
     */
    public HuffmanChar(byte[] threeBytes)
    {
        super(new Character((char)threeBytes[0]),
            ((int)threeBytes[2]) >= 0 ? (int)threeBytes[2] |
                    ((int)threeBytes[1] << BITS_IN_BYTE)
            : ((BYTE_SIZE_NUMBER + (int)threeBytes[2]) +
                    ((int)threeBytes[1] << BITS_IN_BYTE)));
    }
    
    /**
     * returns the class converted to a 3-byte array
     * @return the class converted to a 3-byte array
     */
    public byte[] toThreeBytes()
    {
        byte[] ba = new byte[3];
        ba[0] = (byte)(getData().charValue());
        short oc = (short)getOccurances();
        ba[1] = (byte)(oc >> 8);
        ba[2] = (byte)(oc & (byte)(-1));
        return ba; 
    }

}
