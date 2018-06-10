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
import javax.swing.JOptionPane;

/**
 *
 * @author pbladek
 */
public class Huffman {

    public static final int CHARMAX = 128;
    public static final byte CHARBITS = 7;
    public static final short CHARBITMAX = 128;
    private HuffmanTree<Character> theTree;
    private byte[] byteArray;
    private SortedMap<Character, String> keyMap;
    private SortedMap<String, Character> codeMap;
    HuffmanChar[] charCountArray;
    int[] saveDataArray;
    public static String fileName;
    public static File fileObject;

    /**
     * Creates a new instance of Main
     */
    public Huffman() {
        theTree = new HuffmanTree<Character>();
        keyMap = new TreeMap<>();
        codeMap = new TreeMap();
        saveDataArray = new int[128];
        byteArray = new byte[128];
        fileName = "x";
        fileObject = new File("");
    }

    /**
     * main
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        if (args.length > 0) {
            if (args[0].substring(0, 2).toLowerCase().equals("-d")) {
                decode = true;
                if (args.length > 1) {
                    textFileName = args[1];
                }
            } else {
                textFileName = args[0];
            }
        }
        Huffman coder = new Huffman();
        if (!decode) {
            coder.encode(textFileName);
        } else {
            coder.decode(textFileName);
        }
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
                    saveDataArray[(int) temp.charAt(i)]++;
                }
            }
            fileInputScanner.close();
            saveCharData();
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
    public void decode(String inFileName) {
       // takes in .cod file
        String codFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".cod");
        File codFile = new File(codFileName);
        if (!codFile.exists()) {
            throw new IOException(".cod file does not exist");
        }
        FileInputStream codInstream = new FileInputStream(codFile);
        BufferedInputStream codFin = new BufferedInputStream(codInstream);
        byte in[] = new byte[3];
        // first read HuffmanChar elements into an ArrayList
        ArrayList<HuffmanChar> huffData = new ArrayList<>();
        while (codFin.available() >= 3) {
            // read 3 bytes into buffer
            codFin.read(in);
            // covert the 3 bytes to a HuffmanChar
            HuffmanChar huffChar = new HuffmanChar(in);
            // store int array
            huffData.add(huffChar);
        }
        // convert ArrayList into HuffmanChar []
        charCountArray = new HuffmanChar[2];
        charCountArray = huffData.toArray(charCountArray);

        //  huffman tree and tree maps
        theTree = new HuffmanTree(charCountArray);
        keyMap = theTree.getKeyMap();

        //  .huf file
        String hufFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".huf");
        File inputFile = new File(hufFileName);
        if (!inputFile.exists()) {
            throw new IOException("input .huf file does not exist");
        }
        FileInputStream finstream = new FileInputStream(inputFile);
        BufferedInputStream fin = new BufferedInputStream(finstream);

        // create output file (.dec.txt)
        String decodeFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".dec.txt");
        File decodeFileWriter = new File(decodeFileName);
        if (decodeFileWriter.exists()) {
            decodeFileWriter.delete();
        }
        FileWriter fWriter = new FileWriter(decodeFileWriter, true);
        BufferedWriter fout = new BufferedWriter(fWriter);
        //might need to add a space between the printing out
        //System.out.println();
        //decode the .huf file, write to .dec.txt
        Character value = '\0';
        String code = "";
        boolean done = false;
        while (!done) {
            // read one byte from .huf file
            char inValue = (char) fin.read(); // 6/7: changed byte to char
            // -1 is the end of the file
            if (inValue == -1) {
                System.out.println("-1: end of .huf file");
                done = true;
                break;
            }
            for (int i = 0; i < NUM_BITS; i++) {
                if ((0x80 & inValue) == 0x80) {
                    code = code.concat("1");
                } else {
                    code = code.concat("0");
                }
                // left bit shift
                inValue <<= 1;
                // is there a match?]
                value = keyMap.get(code);
                if (value != null) {
                    if (value == '\b') {
                        System.out.println("EOF marker");
                        done = true;
                        break;
                    } else {
                        fout.write(value);
                        code = "";
                                    }
                                }
                            }
                        }
                fin.close();
                fout.close();
        
        System.out.println(hufFileName + " converted to "
                                       + decodeFileName + "."); 
       
    }

    /**
     * writeEncodedFile
     *
     * @param bytes bytes for file
     * @param fileName file input
     */
    public void writeEncodedFile(byte[] bytes, String fileName) {
        String codeFileName = fileName.substring(0, fileName.indexOf(".")) + ".huf";
    
    ObjectOutputStream outputStream = null;
    try
    {
      outputStream = new ObjectOutputStream(new FileOutputStream(codeFileName));
      
      outputStream.write(bytes);
      outputStream.close();
    }
    catch (IOException e)
    {
      System.out.println("Could not open file" + codeFileName + " " + e.toString());
    }
    
    return new File(codeFileName);
    }

    /**
     * writeKeyFile
     *
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName) {
        String codeFileName = fileName.substring(0, fileName.indexOf(".")) + ".cod";
    
    ObjectOutputStream outputStream = null;
    saveDataArray = new byte[charCountArray.length * 3];
    for (int i = 0; i < charCountArray.length; i++)
    {
      byte[] threeBytes = charCountArray[i].toThreeBytes();
      saveDataArray[(3 * i)] = threeBytes[0];
      saveDataArray[(3 * i + 1)] = threeBytes[1];
      saveDataArray[(3 * i + 2)] = threeBytes[2];
    }
    try
    {
      outputStream = new ObjectOutputStream(new FileOutputStream(codeFileName));
      
      for (int i = 0; i < saveDataArray.length; i++)
        outputStream.writeByte(saveDataArray[i]);
      outputStream.close();
    }
    catch (IOException e)
    {
      System.out.println("Could not open file " + codeFileName + e.getClass() + e.toString());
    }
    }

    public void saveCharData() {

        charFrequencyArray();

        for (int i = 0; i < saveDataArray.length; i++) {
            if (saveDataArray[i] > 0) {
                //keyMap.put(charCountArray, );
            }
        }
    }

    public void charFrequencyArray() {
        ArrayList<HuffmanChar> huffList = new ArrayList<HuffmanChar>();
        for (int i = 0; i < saveDataArray.length; i++) {
            if (saveDataArray[i] > 0) {
                HuffmanChar huffChar = new HuffmanChar((char) i, saveDataArray[i]);
                huffList.add(huffChar);
            }
        }
        Collections.sort(huffList);
        charCountArray = new HuffmanChar[huffList.size()];
        huffList.toArray(charCountArray);
        System.out.println(Arrays.toString(charCountArray));
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
