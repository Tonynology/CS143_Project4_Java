/*
 * HuffmanTree.java
 */

package huffman;
import java.util.*;
/**
 * HuffmanTree
 * 
 * Binary tree for Huffman coding.<br>
 * 
 * @author Tommy Tran, Danhiel T VU, TaeHoon Moon, Quan Dinh Tran 
 * @version 1.0 6/9/18 Test Environment: NetBeans 8.2 
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
     * Creates a new instance of HuffmanTree from an array of Huffman Data.<br>
     * <pre>
     *   remove two elements with lowest counts (top of list)
     *   create new HuffmanTree node with elements
     *   insert this new node back into the tree in correct position
     *   continue until there is only one item left in the list,
     *     which now is the root of the completed HuffmanTree</pre>
     *
     * @param dataArray array of Huffman Data to convert into Huffman tree
     */
    public HuffmanTree(HuffmanData<T>[] dataArray) 
    {
         // convert the array into an ArrayList<BinaryNode<HuffmanData<Character>>
        ArrayList<BinaryNode<HuffmanData<Character>>> huffAList
                = new ArrayList<>();
        // add all the elements of dataArray into huffAList
        for (int i = 0; i < dataArray.length; i++) {
            BinaryNode<HuffmanData<Character>> node
                    = new BinaryNode(dataArray[i]);
            huffAList.add(node);
        }

        while (huffAList.size() > 1) {
            // two elements with fewest occurances removed
            BinaryNode element1 = huffAList.remove(0);
            BinaryNode element2 = huffAList.remove(0);
            // create new HuffmanTree
            add(element1, element2);
            // get the new node (root)
            BinaryNode curRoot = (BinaryNode) getRootNode();
            // insert back into the list at proper location
            reinsertNode(huffAList, curRoot);
        }
        
        // create maps
        keyMap = new TreeMap<String, T>();
        codeMap = new TreeMap<T, String>();
        setMaps(getRootNode(), "");
    }
    
    /**
     * insert BinaryNode into ArrayList in proper position (sorted ascending
     * order)<br>
     * simple linear search, worse case O(n)
     * @param huffAList ArrayList where node is inserted
     * @param curRoot BinaryNode to insert (root of new HuffmanTree)
     */
    private void reinsertNode(ArrayList<BinaryNode<HuffmanData<Character>>> huffAList,
            BinaryNode curRoot) {
        boolean inserted = false;
        for (int i = 0; i < huffAList.size(); i++) {
            if (((HuffmanData<Character>) curRoot.getData()).getOccurances()
                    <= huffAList.get(i).getData().getOccurances()) {
                huffAList.add(i, curRoot);
                inserted = true;
                break; // done
            }
        }
        // adds to end of list if largest value
        if (!inserted) {
            huffAList.add(curRoot);
        }
    }
    /** 
     * creates two new HuffmanTrees and adds them to the root of this tree
     * @param left 
     * @param right
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
        BinaryNodeInterface<HuffmanData<T>> leftNode = new BinaryNode<>(element1);
        BinaryNodeInterface<HuffmanData<T>> rightNode = new BinaryNode<>(element2);
        BinaryNodeInterface<HuffmanData<T>> rootNode = new BinaryNode<>(new HuffmanData<>(MARKER, leftNode.getData().getOccurances() + rightNode.getData().getOccurances()));
        rootNode.setLeftChild(leftNode);
        rootNode.setRightChild(rightNode);
        BinaryTree<HuffmanData<T>> tree = new BinaryTree<>();
        tree.setRootNode(rootNode);
        HuffmanData<T> data = new HuffmanData<>(MARKER, getRootData().getOccurances() + tree.getRootData().getOccurances());
        if(getRootData().getOccurances() < tree.getRootData().getOccurances())
            setTree(data, this, tree);
        else
            setTree(data, tree, this);
    }
    
    /** 
     * add a single element to the tree
     *  smaller on the left
     * @param element1
     */
     private void add(HuffmanData<T> element1)
     {
        BinaryTree<HuffmanData<T>> tree = new BinaryTree<>(element1);
        HuffmanData<T> data = new HuffmanData<>(MARKER, getRootData().getOccurances() + tree.getRootData().getOccurances());
        if(getRootData().getOccurances() < tree.getRootData().getOccurances())
            setTree(data, this, tree);
        else
            setTree(data, tree, this);       
     }
    
     /**
      * This Method sets up the<br>
      * Key Map and Code Map for encoding and decoding.<br>
      *
      * @param node the current node input
      * @param codeString String input
      */
     private void setMaps(BinaryNodeInterface<HuffmanData<T>> node,
             String codeString)
     { 
       if (node == null) {
            return;
        }

        if (node.getLeftChild() != null) {
            codeString += "0";
            setMaps(node.getLeftChild(), codeString);
            //shorten by 1
            codeString = (codeString.length() > 1) ? 
                        codeString.substring(0, codeString.length() - 1) : "";
        }
        //print node value (SortedTree)
        if (node.getData().getData() != null) {
            codeMap.put(node.getData().getData(), codeString);
            keyMap.put(codeString, node.getData().getData());
        }

        if (node.getRightChild() != null) {
            codeString += "1";
            setMaps(node.getRightChild(), codeString);
            codeString = (codeString.length() > 1) ? 
                        codeString.substring(0, codeString.length() - 1) : "";
        }      
     }
    
    /**
     * accessors for codeMap
     * @return codeMap
     */
    public SortedMap<T, String> getCodeMap()
    {
        return codeMap;
    }
    
    /**
     * accessors for keyMap
     * @return keyMap
     */
    public SortedMap<String, T> getKeyMap()
    {
        return keyMap;
    }

}

