package datastruct;

public class LinkedList {
    public Node head;
    private int size;

    public static class Node {
        public int data;
        public double weight;
        public Node next;

        public Node(int data, double weight, Node next) {
            this.data = data;
            this.next = next;
            this.weight = weight;
        }
    }

    public LinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Create new node and add to front of list, with new node being the new head
     * @param x element to be added
     */
    public void add(int x, double weight) { // O(1)
        head = new Node(x, weight, head);
        size++;
    }

    /**
     * Retrieve the node at the given index
     *
     * @param idx the index of the node we want to get
     * @return The node at the given index.
     */
    public Node getNode(int idx) { // O(n)
        // if list empty or invalid index
        if(idx < 0 || head == null || idx >= size) {
            throw new IndexOutOfBoundsException();
        }

        // start at head and keep reducing idx while going to next node
        // when idx is 0 we have arrived at the node we want
        Node current = head;
        while(idx != 0) {
            // if too large index
            if(current.next == null) {
                throw new IndexOutOfBoundsException();
            }

            current = current.next;
            idx--;
        }
        return current;
    }

    /**
     * Get the data at the given index
     *
     * @param idx The index of the element to retrieve.
     * @return The data of the node at the given index.
     */
    public int get(int idx) { // O(n)
        return getNode(idx).data;
    }

    /**
     * Delete the node at a certain index
     *
     * @param idx the index of the node to delete
     */
    public void delete(int idx) { // O(n)
        // if deleting head, set null
        if(idx == 0 && head != null) {
            head = null;
            return;
        }
        // otherwise get node and delete
        Node prev = getNode(idx-1);
        prev.next = prev.next.next;
        size--;
    }

    /**
     * Returns the size of the linked list
     *
     * @return The size of the list.
     */
    public int size() { // O(1)
        return size;
//        int count = 0;
//        Node current = head;
//        while(current != null) {
//            count++;
//            current = current.next;
//        }
//        return count;
    }

    /**
     * Returns the index of the given element or -1 if it is not present in the LinkedList
     *
     * @param x the element to search for
     * @return The index of the element in the linked list.
     */
    public int getIndex(int x) {
        int idx = 0;
        Node current = head;
        // while there are elements left to check
        while(current != null) {
            // if found the element return its index
            if(current.data == x)
                return idx;
            // else increment counter and go to next element
            idx++;
            current = current.next;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("[");
        Node current = head;
        while(current != null) {
            str.append(current.data).append(", ");
            current = current.next;
        }
        return str.append("]").toString();
    }

    // TEST METHOD
    public static void main(String[] args)
    {
        /* Start with the empty list. */
        LinkedList list = new LinkedList();

        // Insert the values
        list.add(1, 1);
        list.add(2, 1);
        list.add(3, 1);
        System.out.println(list);
        System.out.println(list.size());

        // Delete
        list.delete(2);
        System.out.println(list);
        list.delete(1);
        System.out.println(list);
        list.delete(0);
        System.out.println(list);
        System.out.println(list.size());
    }
}
