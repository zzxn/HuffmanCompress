package core.dataStructure;

import java.util.LinkedList;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class CodeHuffTree {

    /* fields and constructor */
    private ByteNode root;
    private int[][] codeTable;

    public CodeHuffTree(int[] frequencyList) {
        if (frequencyList.length != 256)
            throw new IllegalArgumentException
                    ("Byte frequency list must be 256 length");

        // build a ByteNode list
        ByteNode[] byteNodeList = new ByteNode[256];
        for (int i = 0; i < 256; i++) {
            byteNodeList[i] = new ByteNode((byte) i, frequencyList[i]);
        }

        // build a Huffman tree
        // 这里可能有误，因为不知道ArrayCopy干了什么
//        core.dataStructure.MinHeap<ByteNode> pqueue = new core.dataStructure.MinHeap<>(byteNodeList);
        MinHeap<ByteNode> pqueue = new MinHeap<>();
        for (int i = 0; i < 256; i++) {
            pqueue.insert(byteNodeList[i]);
        }
        while (pqueue.getCurrentSize() != 1) {
            ByteNode node1 = pqueue.deleteMin();
            ByteNode node2 = pqueue.deleteMin();
            ByteNode newNode = new ByteNode(node1.getFrequency() + node2.getFrequency());
            newNode.setLeftChild(node1);
            newNode.setRightChild(node2);
            pqueue.insert(newNode);
        }
        this.root = pqueue.deleteMin();

        // build a code table (array)
        this.codeTable = new int[256][];
        for (int i = 0; i < 256; i++) {
            codeTable[i] = byteNodeList[i].getCode();
        }
    }

    public ByteNode getRoot() {
        return this.root;
    }

    /* get code Table */

    public int[][] getCodeTable() {
        return codeTable;
    }

    /* ByteNode nested class */

    private static class ByteNode implements Comparable<ByteNode> {

        /* fields and constructor */

        byte value;
        int frequency;
        ByteNode leftChild, rightChild;
        LinkedList<Integer> code = new LinkedList<>(); // Huffman code build
        boolean isLeaf = true; // a real byte node if true, otherwise just a connection node

        ByteNode(byte value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }

        ByteNode(int frequency) {
            this.frequency = frequency;
        }

        /* getter and setter */

        int[] getCode() {
            int len = code.size();
            int[] rs = new int[len];
            for (int i = 0; i < len; i++)
                rs[i] = code.get(i);
            return rs;
        }

        byte getValue() {
            return value;
        }

        void setValue(byte value) {
            this.value = value;
        }

        ByteNode getLeftChild() {
            return leftChild;
        }

        void setLeftChild(ByteNode leftChild) {
            this.leftChild = leftChild;
            if (isLeaf)
                setLeaf(false);
            insertZeroBeforeAllChildCode(leftChild);
        }

        ByteNode getRightChild() {
            return rightChild;
        }

        void setRightChild(ByteNode rightChild) {
            this.rightChild = rightChild;
            if (isLeaf)
                setLeaf(false);
            insertOneBeforeAllChildCode(rightChild);
        }

        boolean isLeaf() {
            return isLeaf;
        }

        void setLeaf(boolean leaf) {
            this.isLeaf = leaf;
        }

        int getFrequency() {
            return frequency;
        }

        void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        private void insertZeroBeforeAllChildCode(ByteNode node) {
            if (node == null)
                return;
            if (node.isLeaf)
                node.code.addFirst(0);
            insertZeroBeforeAllChildCode(node.getLeftChild());
            insertZeroBeforeAllChildCode(node.getRightChild());
        }

        private void insertOneBeforeAllChildCode(ByteNode node) {
            if (node == null)
                return;
            if (node.isLeaf)
                node.code.addFirst(1);
            insertOneBeforeAllChildCode(node.getLeftChild());
            insertOneBeforeAllChildCode(node.getRightChild());
        }
        /* CompareTo for implements Comparable */

        @Override
        public int compareTo(ByteNode o) {
            return Integer.compare(this.frequency, o.frequency);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (isLeaf)
                sb.append("is leaf ");
            sb.append("value:");
            sb.append(getValue());
            sb.append(" ");
            sb.append("frequency:");
            sb.append(getFrequency());
            sb.append(" ");
            sb.append("code:");
            sb.append(getCode());
            sb.append(" ");
            return sb.toString();
        }
    }
}
