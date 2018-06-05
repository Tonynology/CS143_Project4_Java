/*
 * Huffman.java
 *
 * Created on May 21, 2007, 1:01 PM
 */

package huffman;
import java.util.*;
import java.lang.*;
import java.io.*;
import javax.swing.JFileChooser;
/**
 *
 * @author pbladek
 */
public class Huffman
{  
    public static final int CHARMAX = 128;
    public static final byte CHARBITS = 7;
    public static final short CHARBITMAX = 128;
    private HuffmanTree<Character> theTree;
    private byte[] byteArray;
    private SortedMap<Character, String> keyMap;
    private SortedMap<String, Character> codeMap;
    HuffmanChar[] charCountArray;
    byte[] saveDataArray;
    int[] charCount = new int[128];
    public static String fileName = "x";
    public static File fileObject = new File("");
    
    /**
     * Creates a new instance of Main
     */
    public Huffman() {}
    
    /**
     * main
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[1];
//        args[0] = "alice.txt";
//----------------------------------------------------
// used for debugging encoding
//----------------------------------------------------
//        args = new String[2];
//        args[0] = "-d";
//        args[1] = "alice.txt";  
//----------------------------------------------------        
        boolean decode = false;
        String textFileName = "";
        if(args.length > 0)
        {
            if(args[0].substring(0,2).toLowerCase().equals("-d"))
            {
                decode = true;
                if(args.length > 1)
                    textFileName = args[1];
            }
            else
                textFileName = args[0];
        }
        Huffman coder = new Huffman();
        if(!decode)
            coder.encode(textFileName);
        else
            coder.decode(textFileName);
       
    }

    /*
     * encode
     * @param fileName the file to encode
     */
    public void encode(String fileName) {
        Scanner fileInputScanner;
        String temp;

        try {
            getFileDirectory();
            fileInputScanner = new Scanner(fileObject);
            fileInputScanner.useDelimiter("\n");
            while (fileInputScanner.hasNextLine()) {
                temp = fileInputScanner.nextLine() + "\n";
                
                for (int i = 0; i < temp.length(); i++) {
                    charCount[(int) temp.charAt(i)]++;
                }
                
                
                count++;
            }
            fileInputScanner.close();
            //now we have to sort the data by count, but the instruction said
            // "It should be put into an array holding a class designed to hold that data."
            // Anyone has idea for this? HashMap?
        } catch (Exception ex) {
            System.out.println("There was an error");
        }

        writeEncodedFile(byteArray, fileName);
        writeKeyFile(fileName);
    }
 
    /*
     * decode
     * @param inFileName the file to decode
     */   
    public void decode(String inFileName)
    { 
     
    }
      
    /**
     * writeEncodedFile
     * @param bytes bytes for file
     * @param fileName file input
     */ 
    public void writeEncodedFile(byte[] bytes, String fileName)
    {
      

    }
   
    /**
     * writeKeyFile
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName)
    {
  
    }
    
    public void getFileDirectory() {
        JFileChooser chooser = new JFileChooser();
        while (!fileObject.exists()) {
            chooser.setCurrentDirectory(
                    fileObject.getAbsoluteFile().getParentFile());
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileObject = chooser.getSelectedFile();
                fileName = fileObject.getName().trim();
                JOptionPane.showMessageDialog(null, "Done!"
                        + " The file has been copied and modified!", "Info",
                        JOptionPane.PLAIN_MESSAGE);
            } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                System.exit(0);
            }
        }
    }

}

