package core.dataStructure;

/**
 * @author 16307110325
 * Created on 2017/10/19.
 */
public class CodeHuffTree {

    /* fields and constructor */
    private ByteNode root;
    private String[] codeTable;

    public CodeHuffTree(int[] frequencyList) {
        if (frequencyList.length != 256)
            throw new IllegalArgumentException
                    ("Byte frequency list must be 256 length");

        // build a ByteNode list
        ByteNode[] byteNodeList = new ByteNode[256];
        for (int i = 0; i < 256; i++) {
            byteNodeList[i] = new ByteNode((byte)i, frequencyList[i]);
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
        this.codeTable = new String[256];
        for (int i = 0; i < 256; i++) {
            codeTable[i] = byteNodeList[i].getCode();
        }
    }

    public CodeHuffTree(int[] frequencyList, int length) {
        if (frequencyList.length != length)
            throw new IllegalArgumentException
                    ("Byte frequency list must be 256 length");

        // build a ByteNode list
        ByteNode[] byteNodeList = new ByteNode[length];
        for (int i = 0; i < length; i++) {
            byteNodeList[i] = new ByteNode((byte)i, frequencyList[i]);
        }

        // build a Huffman tree
        // 这里可能有误，因为不知道ArrayCopy干了什么
//        core.dataStructure.MinHeap<ByteNode> pqueue = new core.dataStructure.MinHeap<>(byteNodeList);
        MinHeap<ByteNode> pqueue = new MinHeap<>();
        for (int i = 0; i < length; i++) {
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
        this.codeTable = new String[length];
        for (int i = 0; i < length; i++) {
            codeTable[i] = byteNodeList[i].getCode();
        }
    }

    public ByteNode getRoot() {
        return this.root;
    }

    /* get code Table */

    public String[] getCodeTable() {
        return codeTable;
    }

    /* ByteNode nested class */

    private static class ByteNode implements Comparable<ByteNode> {

        /* fields and constructor */

        byte value;
        int frequency;
        ByteNode leftChild, rightChild;
        StringBuilder code = new StringBuilder(""); // HUffman code build
        boolean isLeaf = true; // a real byte node if true, otherwise just a connection node

        public ByteNode(byte value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }

        public ByteNode(int frequency) {
            this.frequency = frequency;
        }

        public ByteNode(byte value, ByteNode leftChild, ByteNode rightChild, boolean isLeaf) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.isLeaf = isLeaf;
        }

        /* getter and setter */

        public String getCode() {
            return code.toString();
        }

        public byte getValue() {
            return value;
        }

        public void setValue(byte value) {
            this.value = value;
        }

        public ByteNode getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(ByteNode leftChild) {
            this.leftChild = leftChild;
            setLeaf(false);
            insertZeroBeforeAllChildCode(leftChild);
        }

        public ByteNode getRightChild() {
            return rightChild;
        }

        public void setRightChild(ByteNode rightChild) {
            this.rightChild = rightChild;
            setLeaf(false);
            insertOneBeforeAllChildCode(rightChild);
        }

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            this.isLeaf = leaf;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        private void insertZeroBeforeAllChildCode(ByteNode node) {
            if (node == null)
                return;
            node.code.insert(0,0);
            ByteNode left = node.getLeftChild();
            ByteNode right = node.getRightChild();
            if (left != null) {
                insertZeroBeforeAllChildCode(left);
            }
            if (right != null) {
                insertZeroBeforeAllChildCode(right);
            }
        }
        private void insertOneBeforeAllChildCode(ByteNode node) {
            if (node == null)
                return;
            node.code.insert(0,1);
            ByteNode left = node.getLeftChild();
            ByteNode right = node.getRightChild();
            if (left != null) {
                insertOneBeforeAllChildCode(left);
            }
            if (right != null) {
                insertOneBeforeAllChildCode(right);
            }
        }
        /* CompareTo for implements Comparable */

        @Override
        public int compareTo(ByteNode o) {
            return Integer.compare(this.frequency, o.frequency);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (isLeaf())
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
