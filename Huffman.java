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

    }

    /**
     * writeEncodedFile
     *
     * @param bytes bytes for file
     * @param fileName file input
     */
    public void writeEncodedFile(byte[] bytes, String fileName) {

    }

    /**
     * writeKeyFile
     *
     * @param fileName the name of the file to write to
     */
    public void writeKeyFile(String fileName) {

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
