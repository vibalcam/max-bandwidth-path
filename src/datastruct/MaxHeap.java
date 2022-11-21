package datastruct;

import java.util.Arrays;

public class MaxHeap {
    // Heap array to store the order
    private final int[] heap;
    // Used to store the values of the elements in the heap.
    private final double[] values;
    // Used to store the position of the elements in the heap.
    private final int[] position;
    // Used to store the index of the last element in the heap.
    private int last = -1;

    public MaxHeap(int size) {
        heap = new int[size];
        position = new int[size];
        values = new double[size];
    }

    /**
     * Returns the maximum value element
     * @return maximum value element
     */
    public int max() { // O(1)
        if(last < 0)
            throw new IndexOutOfBoundsException();
        return heap[0];
    }

    public boolean isEmpty() {
        return last < 0;
    }

    /**
     * Return the value of element x.
     *
     * @param x the element whose value we want to get
     * @return The value of element x.
     */
    public double getValue(int x){
        // imp check if x is in heap
        return values[x];
    }

    /**
     * Return the maximum value element and delete it from heap
     * @return maximum value element
     */
    public int pop() { // O(log n)
        int i = max();
        deleteIndex(0);
        return i;
    }

    /**
     * This function inserts a new element
     * 
     * @param x the element to insert
     */
    public void add(int x, double value) { // O(log n)
        if(x < 0 || x >= heap.length)
            throw new IndexOutOfBoundsException();

        // add element to end of heap
        last++;
        setElementHeap(x, last);
        values[x] = value;
        // fix the heap by making element "float up" to the right position
        // variables are indices in heap
        int idx = last;
        int parent = getParent(idx);
        while(idx > 0 && getValueIndex(parent) < value) {
            swap(parent, idx);
            idx = parent;
            parent = getParent(idx);
        }
    }

    /**
     * This function deletes the element at index i
     * 
     * @param idx the index of the item to be deleted
     */
    public void deleteIndex(int idx) { // O(log n)
        if(idx < 0 || idx > last)
            throw new IndexOutOfBoundsException();

        // replace element to be deleted
        setElementHeap(heap[last], idx);
        last--;
        // push up to fix heap
        int parent = getParent(idx);
        double value = getValueIndex(idx);
        if(idx > 0 &&  value > getValueIndex(parent)) {
            // until the parent > idx
            while (idx > 0 && value > getValueIndex(parent)) {
                swap(idx, parent);
                idx = parent;
                parent = getParent(idx);
            }
        } else { // push down to fix heap
            int right = getRight(idx);
            int left = getLeft(idx);
            // until idx > children
            while((left <= last && getValueIndex(left) > value) || (right <= last && getValueIndex(right) > value)) {
                // left value is largest of the three (parent and children)
                if(left == last || getValueIndex(left) > getValueIndex(right)) {
                    swap(left, idx);
                    idx = left;
                } else { // right value is largest of the three (parent and children)
                    swap(right, idx);
                    idx = right;
                }
                // update left and right values for next iteration
                left = getLeft(idx);
                right = getRight(idx);
            }
        }
    }

    /**
     * Return the value of the element at index idx in the heap.
     *
     * @param idx the index of the element in the heap
     * @return The value of the element at the given index.
     */
    private double getValueIndex(int idx) {
        return values[heap[idx]];
    }

//    /**
//     * It returns the position of the element at index x
//     *
//     * @param x the index of the element to be searched
//     * @return The position of the element in the array.
//     */
//    private int getPosition(int x) {
//        return position[x] - 1;//imp apply
//    }

//    private boolean isInHeap(int x) {
//        //imp check if in heap
//    }

    /**
     * Delete the element at the given index.
     *
     * @param x the element to be deleted
     */
    public void deleteElement(int x) { // O(log n)
        // imp check if element is in heap (position index 0 non existant + 1 exists)
        deleteIndex(position[x]);
    }


    /**
     * Swap the elements at indices i1 and i2 in the heap
     *
     * @param i1 index of the first element to swap
     * @param i2 the index of the element to be swapped
     */
    private void swap(int i1, int i2) { // O(1)
        // swap elements
        int tmp = heap[i1];
        setElementHeap(heap[i2], i1);
        setElementHeap(tmp, i2);
    }

    /**
     * Sets an index of the heap to the given element
     * @param x the element
     * @param idx the index in which the element will go
     */
    private void setElementHeap(int x, int idx) { // O(1)
        heap[idx] = x;
        position[x] = idx;
    }

    /**
     * Returns the parent of node i
     * @param i node whose parent we want to find
     * @return index of the parent node
     */
    private int getParent(int i) { // O(1)
        return (i - 1) / 2;
    }

    /**
     * Returns the left of node i
     * @param i node whose left we want to find
     * @return index of the left node
     */
    private int getLeft(int i) { // O(1)
        return 2 * i + 1;
    }

    /**
     * Returns the right of node i
     * @param i node whose right we want to find
     * @return index of the right node
     */
    private int getRight(int i) { // O(1)
        return 2 * i + 2;
    }

    /**
     * Sort the array using heapsort.
     * Build a max heap, then pop the max element n times.
     *
     * @param values the array of values to sort
     * @param asc true if you want the array to be sorted in ascending order, false if you want it to be sorted in
     * descending order.
     * @return The indices of the given array sorted
     */
    public static int[] heapSort(double[] values, int length, boolean asc) { // O(n log n)
//        todo use implementation from book build heap
//        int[] indices = new int[values.length];
//        int[] position = new int[values.length];
//        for (int i = 0; i < indices.length; i++) {
//            indices[i] = i;
//            position[i] = i;
//        }
//        MaxHeap maxHeap = new MaxHeap(indices, values, position);

        // build the max heap O(n log n)
        MaxHeap maxHeap = new MaxHeap(length);
        for(int k=0; k < length; k++)    // n times
            maxHeap.add(k, values[k]); // O(log n)

        // for each element in tree get max and delete O(n log n)
        int[] sorted = new int[length];
        if(asc) {
            for(int k=length-1; k >=0; k--)    // n times
                sorted[k] = maxHeap.pop();  // O(log n)
        } else {
            for(int k=0; k < length; k++) {    // n times
                sorted[k] = maxHeap.pop();  // O(log n)
            }
        }

        return sorted;
    }

    // TEST METHOD
    public static void main(String[] arg)
    {
        MaxHeap heap = new MaxHeap(6);
        heap.add(5, 5);
        heap.add(4, 4);
        heap.add(3, 3);
        heap.add(2, 2);
        heap.add(1, 1);
        System.out.println(heap.max());
        heap.deleteElement(4);
        System.out.println(heap.max());
        heap.deleteElement(5);
        System.out.println(heap.max());
        heap.add(5, 5);
        heap.add(0, 0);
        System.out.println(heap.max());

        // test heapsort
        double[] test = {3,4,2,0,1};
        System.out.println(Arrays.toString(heapSort(test, test.length, true)));
        System.out.println(Arrays.toString(heapSort(test, test.length, false)));
    }
}
