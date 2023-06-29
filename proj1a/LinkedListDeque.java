public class LinkedListDeque<T> {
    //use the circular sentinel node approach for implementation
    private IntNode sentinel;
    private int size;


    //class IntNode
    private class IntNode {
        public IntNode prev;
        public T item;
        public IntNode next;
        public IntNode(IntNode p, T i, IntNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    //constructor 
    public LinkedListDeque() {
        //create an empty LinkedListDeque
        sentinel = new IntNode(sentinel, null, sentinel);
        size = 0;
    }


    public void addFirst(T item) {
        if (sentinel.next == null) {
            IntNode new_node = new IntNode(sentinel, item, sentinel);
            sentinel.next = new_node;
            sentinel.prev = new_node;
        }
        else {
            IntNode new_node = new IntNode(sentinel, item, sentinel.next);
            sentinel.next.prev = new_node;
            sentinel.next = new_node;
        }
        size += 1;
    }

    public void addLast(T item) {
        if (sentinel.prev == null) {
            IntNode new_node = new IntNode(sentinel, item, sentinel);
            sentinel.next = new_node;
            sentinel.prev = new_node;
        }
        else {
            IntNode new_node = new IntNode(sentinel.prev, item, sentinel);
            sentinel.prev.next = new_node;
            sentinel.prev = new_node;
        }
        size += 1;
    }

    public boolean isEmpty() {
        if (size == 0)
             return true;
        return false;
    }

    public int size() {
        return size;
    }

    //print all items in the deque.
    public void printDeque() {
        IntNode testNode = sentinel;
        if(size == 0) {
            System.out.println("");
        }
        else {
            while(testNode.next != sentinel) {
                testNode = testNode.next;
                System.out.print(testNode.item);
                System.out.print(" ");
            }
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        else if (size == 1) {
            size -= 1;
            T firstItem = sentinel.next.item;
            sentinel.next = null;
            sentinel.prev = null;
            return firstItem;
        }
        else {
            size -= 1;
            T firstItem = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            return firstItem;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        else if (size == 1) {
            size -= 1;
            T lastItem = sentinel.prev.item;
            sentinel.next = null;
            sentinel.prev = null;
            return lastItem;
        }
        else {
            size -= 1;
            T lastItem = sentinel.prev.item;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            return lastItem;
        }
    }

    //get the ith item using iteration
    public T get(int index) {
        IntNode testNode = sentinel;
        int count = 0;

        while(testNode.next != sentinel) {
            testNode = testNode.next;
            if (count == index) {
                return testNode.item;
            }
            count += 1;
        }
        return null;
    }


    //get the ith item using recursion
    public T getRecursive(int index) {
        if (index > (size - 1)) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }

        //make a copy of the current object
        LinkedListDeque<T> tempLLDeque = new LinkedListDeque<>();
        tempLLDeque.size = size - 1;
        tempLLDeque.sentinel.next = sentinel.next.next;
        return tempLLDeque.getRecursive(index - 1);
    }
}