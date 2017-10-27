package core.dataStructure;

/**
 * @author 16307110325
 * Created on 2017/10/18.
 */
public class MinHeap<T extends Comparable<? super T>> {
    private static final int DEFAULT_CAPACITY = 100;
    private int currentSize;
    private T[] array; // 0位置留空，第一个元素在array[1]
    public MinHeap() {
        currentSize = 0;
        array = (T[]) new Comparable[DEFAULT_CAPACITY];
    }

    public MinHeap(int capacity) throws Exception {
        currentSize = 0;
        if (capacity < 1)
            throw new Exception();
        array = (T[]) new Comparable[capacity];
    }

    public MinHeap(T[] items) {
        this.currentSize = items.length;
        this.array = (T[]) new Comparable[currentSize * 2 + 1];

        for (int i = 0; i < items.length; i++) {
            array[i + 1] = items[i];
        }
//        System.arraycopy(items, 0, this.array, 1, currentSize);
        buildHeap();
    }

    public int getCurrentSize() {
        return currentSize;
    }

    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i++)
            percolateDown(i);
    }

    public void insert(T x) {
        if (currentSize == array.length - 1)
            enlargeArray(array.length * 2 + 1);
        // percolate up
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2)
            array[hole] = array[hole / 2];
        array[hole] = x;
    }

    public T deleteMin() {
        if (isEmpty())
            throw new ArrayIndexOutOfBoundsException();

        T minItem = findMin();
        array[1] = array[currentSize--];
        percolateDown(1);

        return minItem;
    }

    private T findMin() {
        return array[1];
    }

    private void percolateDown(int hole) {
        int child;
        T tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            // if child is not the only left child, choose the smaller of them
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0)
                child++;
            if (array[child].compareTo(tmp) < 0)
                array[hole] = array[child];
            else
                break;
        }
        array[hole] = tmp;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    private void enlargeArray(int size) {
        if (size <= currentSize)
            return;
        T[] newArray = (T[]) new Comparable[size];
        for (int i = 1; i <= currentSize; i++) {
            newArray[i] = array[i];
        }
//        System.arraycopy(array, 1, newArray, 1, currentSize); // this will generate bug!!
        array = newArray;
    }
}
