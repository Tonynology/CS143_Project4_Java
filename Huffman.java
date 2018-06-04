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
    public static File fileObject = null;
    public static String name = "x";
    
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
    public void encode(String fileName)
    {
      // YOUR CODE HERE
        fileObject = new File(fileName);
        name = fileName;

            if (fileObject.exists() && fileObject.canRead()) {
                fileName = fileObject.getName().trim();
//                ArrayEachLine.fileArrayList(fileObject);
            } else {    // if fileObject doesn't exist & can't read
                TextFileScanner(name, fileObject);
            }

 System.out.println(theTree);

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

    private void TextFileScanner(String name, File fileObject) {
       Scanner keyboard = new Scanner(System.in);
        do {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(
                    fileObject.getAbsoluteFile().getParentFile());
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fileObject = chooser.getSelectedFile(); //choose file
                //get file name from chosen file
                name = fileObject.getName().trim(); 
                //put it in array each line
//                ArrayEachLine.fileArrayList(fileObject); 
//                Project1_CS143.fileObject = fileObject;
//                Project1_CS143.name = name;
            }
        } while (!fileObject.exists() || !fileObject.canRead());
//-----------------------------------------------------------------------------        
        //if file cannot read or doesn't exist
        while ((!fileObject.exists()) || (!fileObject.canRead())) {
            if (!fileObject.exists()) {
                System.out.println("No such file");
            } else // ! fileObject.canRead( )
            {
                System.out.println("That file is not readable.");
            }
            System.out.println("Enter file name again:");
            name = keyboard.next();
            fileObject = new File(name);
        }
    }
 
}

