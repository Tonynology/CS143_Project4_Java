/*
 * HuffmanTree.java
 *
 * Created on May 21, 2007, 2:16 PM
 */

package huffman;
import java.util.*;
/**
 * binary tree for Huffman coding
 * @author pbladek
 */
public class HuffmanTree<T extends Comparable<? super T>>
        extends BinaryTree<HuffmanData<T>>
{
    private final T MARKER = null;
    SortedMap<T, String> codeMap;
    SortedMap<String, T> keyMap;
    
    private int leafCount = 0;
    
    /**
     * Creates a new instance of HuffmanTree
     */
    public HuffmanTree() 
    {
        super();
    }
   
    /**
     * Creates a new instance of HuffmanTree
     * from an array of Huffman Data
     * @param dataArray n array of Huffman Data
     */
    public HuffmanTree(HuffmanData<T>[] dataArray) 
    {
        // your code here
        ArrayList<BinaryNode<HuffmanData<T>>> nodeList = new ArrayList();
    



        for (HuffmanData<T> item : dataArray) {
                nodeList.add(new BinaryNode(item));
        }
        while (nodeList.size() > 1)
        {
                clear();
                add((BinaryNode)nodeList.remove(0), (BinaryNode)nodeList.remove(0));
                int i = 0;
      
                while ((i < nodeList.size()) && (((HuffmanData)((BinaryNode)nodeList.get(i)).getData()).compareTo((HuffmanData)getRootData()) < 0))
                        i++;
                nodeList.add(i, (BinaryNode)getRootNode());
        }


        
         keyMap = new TreeMap<String, T>();
         codeMap = new TreeMap<T, String>();
         setMaps(getRootNode(), "");
    }
    
    /** 
     * creates two new HuffmanTrees and adds them to the root of this tree
     * @param left 
     * @param rightt
     */
    private void add(BinaryNode<HuffmanData<T>> left,
            BinaryNode<HuffmanData<T>> right)
    {
         HuffmanTree<T> leftTree = new HuffmanTree<T>();
         leftTree.setRootNode(left); 
         HuffmanTree<T> rightTree = new HuffmanTree<T>();
         rightTree.setRootNode(right);
         setTree(new HuffmanData<T>
                 (MARKER, left.getData().getOccurances()
                 + right.getData().getOccurances()), leftTree, rightTree);
    }
    
    /** 
     * adds 2 new elements to this tree<br>
     *  smaller on the left
     * @param element1
     * @param element2
     */
    private void firstAdd(HuffmanData<T> element1, HuffmanData<T> element2)
    {
        

    }
    
    /** 
     * add a single element to the tree
     *  smaller on the left
     * @param element1
     */
     private void add(HuffmanData<T> element1)
     {
          HuffmanTree<T> leftTree = new HuffmanTree();
          leftTree.setRootNode(left);
          HuffmanTree<T> rightTree = new HuffmanTree();
          rightTree.setRootNode(right);
          setTree(new HuffmanData(MARKER, ((HuffmanData)left.getData()).getOccurances() + ((HuffmanData)right.getData()).getOccurances()), leftTree, rightTree);
       
     }
    
     /** 
      * set up the 2 maps
      * @param node
      * @param codeString
      */
     private void setMaps(BinaryNodeInterface<HuffmanData<T>> node,
             String codeString)
     { 
       if (node == null)
          return;
       if (codeString == null)
          codeString = "";
       if (((HuffmanData)node.getData()).getData() != null)
       {
           codeMap.put(((HuffmanData)node.getData()).getData(), codeString);
           keyMap.put(codeString, ((HuffmanData)node.getData()).getData());
           leafCount += 1;
           return;
       }
    
       if (node.hasLeftChild())
       {
           setCodeMaps(node.getLeftChild(), codeString + "0");
       }
       if (node.hasRightChild())
       {
          setCodeMaps(node.getRightChild(), codeString + "1");
       }
              
     }
    
    /*
     * accessor for codeMap
     * @ return codeMap
     */
    public SortedMap<T, String> getCodeMap()
    {
        return codeMap;
    }
    
    /*
     * accessor for keyMap
     * @ return keyMap
     */
    public SortedMap<String, T> getKeyMap()
    {
        return keyMap;
    }

}
