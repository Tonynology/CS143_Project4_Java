
/*
 * Huffman.java
 */
package huffman;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.DecimalFormat;

/**
 * This class encodes (compresses) and decodes (decompresses) ASCII text files
 * using the Huffman compression algorithm and data structures.<br>
 * File name of text file to encode can be entered from the command line.<br>
 *      alice.txt<br>
 * File name of file to decode can be entered from the command line with the
 * -d option parameter. -d alice.huf (or) -d alice.txt<br>
 * If no file name is entered from the command line, the user is prompted for
 * the file.<br>
 * <pre>
 * Encoding
 *    Input  - file to encode (alice.txt)
 *    Output - encoded file (alice.huf), huffman data file (alice.cod)
 *       Name of encoded file and compression ratio is written to stdout.
 * Decoding - must add -d option argument from the command line
 *    Input  - file name of encoded file (alice.huf or alice.txt). Compression 
 * data file must also be present (alice.cod).
 *    Output - decoded file (alice.dec.txt)
 * </pre>
 * 
 * @author Tommy Tran
 * @version 1.0 6/9/18 Test Environment: NetBeans 8.2 
 */
public class Huffman {

    public static final int CHARMAX = 128;
    public final int START_ARRAY_SIZE = 2;
    public final int NUM_BITS = 8;
    private HuffmanTree<Character> theTree;
    private SortedMap<Character, String> codeMap;
    private SortedMap<String, Character> keyMap;
    HuffmanChar[] charCountArray;
    ArrayList<String> fileArray;
    int[] frequencyArray;
    static boolean decode = false;
    /**
     * Creates a new instance of main
     */
    public Huffman() {
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
        try {            
            String textFileName = "";
            if (args.length > 0) {
                if (args[0].substring(0,
                                    2).toLowerCase().equalsIgnoreCase("-d")) {
                    decode = true;
                    if (args.length > 1) {
                        textFileName = args[1];
                    }
                } else {
                    textFileName = args[0];
                }
            }
            textFileName = checkFileValidity(textFileName);
            Huffman coder = new Huffman();
            if (!decode) {
                if ( textFileName.substring(textFileName.lastIndexOf('.')
                                                ).equalsIgnoreCase(".huf") ) {
                    throw new Exception("cannot encode a .huf file");
                }
                coder.encode(textFileName);
            } else {
                coder.decode(textFileName);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * encode file using Huffman compression<br>
     * <pre>
     * entry - text file
     * exit - compressed file (.huf extension)
     *        frequency data needed for decode (.cod extension)</pre>
     * @param fileName the file to encode
     * @throws IOException if error reading or writing to a file
     * @throws Exception if keyCode cannot find a match; passes through
     * exception from charFrequencyArray
     */
    public void encode(String fileName) throws IOException, Exception {
        int numCharsInputFile = 0;
        int numBytesOutputFile = 0;
        String inLine = "";
        
        // create new output (.huf) file name
        String newOutName = fileName.substring(0,
                fileName.lastIndexOf('.')).concat(".huf");
        // if it exists from previous effort, delete
        File fileOut = new File(newOutName);
        if (fileOut.exists()) {
            fileOut.delete();
        }
        // create buffered output stream for output file
        FileOutputStream outstream = new FileOutputStream(fileOut);
        BufferedOutputStream fout = new BufferedOutputStream(outstream);
        
        // create File reference for input file
        File inFile = new File(fileName);
        // safety check - should be valid when passed to method
        if (!inFile.exists()) {
            throw new IOException("file not found");
        }
        // read each line of the test file into an ArrayList<String>
        readTextFile(inFile);
        // create frequency array
        createFrequencyArray();
        // create HuffmanData array
        charFrequencyArray();

        // create huffman tree and maps
        theTree = new HuffmanTree(charCountArray);
        codeMap = theTree.getCodeMap();
        Scanner fin = new Scanner(inFile);
        // loop thru every line in input file
        int bitNum = NUM_BITS;
        short outValue = 0;
        boolean done = false;
        while (!done) {
            char ch = '\0';
            String code = "";
            if (fin.hasNextLine()) {
                inLine = fin.nextLine();
            } else {
                // EOF marker
                inLine = "\b";
                done = true;
            }
            // loop thru every char in inLine
            for (int i = 0; i <= inLine.length(); i++) {
                // convert char to code
                if (i == inLine.length()) {
                    ch = '\n';
                } else {
                    ch = inLine.charAt(i);
                }
                numCharsInputFile++;
                // get the huffman code string for that char
                code = codeMap.get(ch);
                if (code == null) {
                    fout.close();
                    fin.close();
                    throw new Exception("invalid key code");
                }
                // loop thru every char in code string 
                // convert to binary data
                for (int j = 0; j < code.length(); j++) {
                    char codeCh = code.charAt(j);
                    if (codeCh == '1') {
                        outValue |= 1;
                    }
                    if (bitNum > 1) {
                        outValue <<= 1;
                        bitNum--;
                    } else {
                        fout.write(outValue);
                        numBytesOutputFile++;
                        bitNum = NUM_BITS;
                        outValue = 0;
                    }
                }
            }
        }
        // cleanup stray bits, pad the last byte
        if (bitNum < NUM_BITS) {
            outValue <<= bitNum - 1;
            fout.write(outValue);
            numBytesOutputFile++;
        }
        fout.close();
        fin.close();
        
        // print compression ratio to stdout
        float compression
                = ((float) numBytesOutputFile / (float) numCharsInputFile) * 100;
        DecimalFormat fmt = new DecimalFormat("##.##");
        System.out.println(newOutName
                + ": " + fmt.format(compression) + "% compression");
        // write the .cod file
        writeKeyFile(fileName);
    }

    /**
     * decode (decompress) a huffman encoded file<br>
     * entry - .huf encoded file<br>
     * exit  - decoded file with .dec.txt extension<br>
     * @param inFileName the file to decode
     * @throws IOException if error reading or writing to a file
     */
    public void decode(String inFileName) throws IOException {
        // 1. read .cod file
        String codFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".cod");
        File codFile = new File(codFileName);
        if (!codFile.exists()) {
            throw new IOException(".cod file does not exist");
        }
        FileInputStream codInstream = new FileInputStream(codFile);
        BufferedInputStream codFin = new BufferedInputStream(codInstream);
        // 3-byte input buffer for reading .huf file
        byte in[] = new byte[3];
        // first read HuffmanChar elements into an ArrayList
        ArrayList<HuffmanChar> huffData = new ArrayList<>();
        while (codFin.available() >= 3) {
            // read 3 bytes into buffer
            codFin.read(in);
            // covert the 3 bytes to a HuffmanChar
            HuffmanChar huffChar = new HuffmanChar(in);
            // place into ArrayList
            huffData.add(huffChar);
        }
        // convert ArrayList into HuffmanChar []
        charCountArray = new HuffmanChar[START_ARRAY_SIZE];
        charCountArray = huffData.toArray(charCountArray);

        // 2. create huffman tree and tree maps
        theTree = new HuffmanTree(charCountArray);
        keyMap = theTree.getKeyMap();

        // 3. create input .huf file name, check for existence
        String hufFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".huf");
        File inputFile = new File(hufFileName);
        if (!inputFile.exists()) {
            throw new IOException("input .huf file does not exist");
        }
        FileInputStream finstream = new FileInputStream(inputFile);
        BufferedInputStream fin = new BufferedInputStream(finstream);

        // 4. create decoded output file (.dec.txt)
        String decodeFileName = inFileName.substring(0,
                inFileName.lastIndexOf('.')).concat(".dec.txt");
        File decodeFileWriter = new File(decodeFileName);
        if (decodeFileWriter.exists()) {
            decodeFileWriter.delete();
        }
        FileWriter fWriter = new FileWriter(decodeFileWriter, true);
        BufferedWriter fout = new BufferedWriter(fWriter);

        // 5. decode the .huf file, write to .dec.txt
        Character value = '\0';
        String code = "";
        boolean done = false;
        while (!done) {
            // read one byte from .huf file
            char inValue = (char) fin.read(); // 6/7: changed byte to char
            // -1 is the end of the file
            if (inValue == -1) {
                done = true;
                break;
            }
            for (int i = 0; i < NUM_BITS; i++) {
                // check the high bit: if '1', add "1" to code, else add "0"
                if ((0x80 & inValue) == 0x80) {
                    code = code.concat("1");
                } else {
                    code = code.concat("0");
                }
                // left bit shift
                inValue <<= 1;
                // is there a match?
                value = keyMap.get(code);
                if (value != null) {
                    if (value == '\b') {
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
     * Write freqency data to file with .cod extension.<br>
     * Data is used when decoding the file.
     *
     * @param fileName the name of the file to write to (after adding .cod)
     * @throws IOException if error writing to file
     */
    public void writeKeyFile(String fileName) throws IOException {
        // create new (.cod) file name
        String newName = fileName.substring(0,
                fileName.lastIndexOf('.')).concat(".cod");
        File codFile = new File(newName);
        if (codFile.exists()) {
            codFile.delete();
        }
        FileOutputStream outstream = new FileOutputStream(codFile);
        BufferedOutputStream fout = new BufferedOutputStream(outstream);
        byte huffOut[];
        for (int i = 0; i < charCountArray.length; i++) {
            HuffmanChar huffChar = charCountArray[i];
            huffOut = huffChar.toThreeBytes();
            fout.write(huffOut);
        }
        fout.close();
    }

    /**
     * checkFileValidity checks for existence of user-entered file. If no file
     * name entered, or if the entered file does not exist, then prompt the
     * user up to 5 times for a file name.
     *
     * @param fileName name of the user entered file, empty string if no name
     * entered on command line
     * @return String name of validated file name
     * @throws IOException if file cannot be found after 5 prompts to user.
     */
    public static String checkFileValidity(String fileName) throws IOException {
        File fin = null;

        if (fileName.length() > 0) {
            fin = new File(fileName);
            if (fin.exists()) {
                return fileName;
            } else {
                System.out.println("File '" + fileName + "' not found.");
            }
        }

        // give the user 5 attempts to enter a correct file name
        Scanner scn = new Scanner(System.in);
        int attempts = 0;
        while (attempts < 5) {
            System.out.print("Enter file name with extension please: ");
            fileName = scn.nextLine();
            if (fileName.length() > 0) {
                if (fileName.substring(0,
                                    2).toLowerCase().equalsIgnoreCase("-d")) {
                    decode = true;
                    if (fileName.length() > 1) {
                        fileName = fileName.substring(3, fileName.length());
                    }
                } else {
                    fileName = fileName.substring(0, fileName.length());
                }
            }
            
            File secondFile = new File(fileName);
            if (secondFile.exists()) {
                return fileName;
            } else {
                System.out.println("File '" + fileName + "' not found.");
                attempts++;
            }
        }
        
        throw new IOException("File Not Found");
    }

    /**
     * ReadTextFile reads the text file and converts it to an array for use in
     * the program
     *
     * @param file the text file to be read
     * @throws IOException if the file cannot be read
     */
    public void readTextFile(File file) throws IOException, Exception {
        fileArray = new ArrayList<String>();
        FileReader read = new FileReader(file);
        BufferedReader reader = new BufferedReader(read);
        String line = reader.readLine();
        while (line != null) {
            fileArray.add(line);
            line = reader.readLine();
        }
        createFrequencyArray();
    }

    /**
     * createFrequencyArray creates an array of integers that tallies the number
     * of character frequencies using ASCII as an index for each character.
     * @throws Exception if a byte encountered with high bit set (valid ASCII
     * files never have the high bit set)
     */
    public void createFrequencyArray() throws Exception {
        frequencyArray = new int[CHARMAX];
        for (String line : fileArray) {
            for (int i = 0; i < line.length(); i++) {
                int freq = (int) line.charAt(i);
                if (freq >= CHARMAX) {
                    throw new Exception("non-ASCII character in input file");
                }
                frequencyArray[freq]++;
            }
            // New line character for each line 
            frequencyArray['\n']++;
        }
        // bell character for end of text.
        frequencyArray['\b']++;
    }

    /**
     * charFrequencyArray<br>
     * creates an arraylist to store the frequencies from the frequencyArray
     * and to sort them. The arraylist is then sorted and stored in global
     * charCountArray
     */
    public void charFrequencyArray() {
        //arraylist to store the values from frequencyArray
        ArrayList<HuffmanChar> huffList = new ArrayList<HuffmanChar>();
        //goes through the frequencyArray and checks for values greater than 0
        for (int i = 0; i < frequencyArray.length; i++) {
            if (frequencyArray[i] > 0) {
                //cast i to the char because ascii.
                HuffmanChar huffChar = new HuffmanChar((char) i,
                        frequencyArray[i]);
                //add to arraylist
                huffList.add(huffChar);
            }
        }
        //sorts the arraylist
        Collections.sort(huffList);

        //stores the arraylist values into charCount Array
        charCountArray = new HuffmanChar[huffList.size()];
        // now sort
        huffList.toArray(charCountArray);
    }
}
