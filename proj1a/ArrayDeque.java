public class ArrayDeque<T> {
    private int size;
    private T[] items;
    //index of the first item
    private int nextFirst;
    //index of the last item
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[15];
        nextFirst = 4;
        nextLast = 5;
    }

    //method for resizing the array (up to a larger array)
    private void resize(int capacity) {
        int increase = capacity - size;
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, nextLast);
        System.arraycopy(items, nextLast + increase, a, nextLast, size - nextLast);
        items = a;
    }

    //method for resizing the array down to a smaller array
    private void resize_d() {
        int decrease = items.length - (size * 2);
        T[] a = (T[]) new Object[size * 2];
        if (nextLast > nextFirst) {
            System.arraycopy(items, nextFirst + 1, a, 1, size);
            nextFirst = 0;
            nextLast = size + 1;
        }
        else {
            System.arraycopy(items, 0, a, 0, nextLast);
            System.arraycopy(items, nextFirst + 1, a, a.length - items.length + 1 + nextFirst, items.length - 1- nextFirst);
            nextFirst -= decrease;
        }

        items = a;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            nextFirst += size;
            resize(size * 2);
        }
        items[nextFirst] = item;
        size += 1;

        //modify the nextFirst
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        }
        else {
            nextFirst -= 1;
        }
    }

    public void addLast(T item) {
        if (size == items.length) {
            nextFirst += size;
            resize(size * 2);
        }
        items[nextLast] = item;
        size += 1;

        //modify the nextLast
        if (nextLast == (items.length - 1)) {
            nextLast = 0;
        }
        else {
            nextLast += 1;
        }
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    //print the Deque in the conceptual order (not the order of the array!!)
    public void printDeque() {
        if ((nextFirst + size) > (items.length - 1)) {
            for (int i = (nextFirst + 1); i < items.length; i += 1) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
            for (int j = 0; j < nextLast; j += 1) {
                System.out.print(items[j]);
                System.out.print(" ");
        }
        }
        else {
            for (int i = (nextFirst + 1); i <= (nextFirst + size); i += 1) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
        }
    }


    //need to shrink the array if a lot of space is unused..........
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T firstItem = items[nextFirst + 1];
        if (nextFirst == (items.length - 1)) {
            nextFirst = 0;
        } else {
            nextFirst += 1;
        }
        size -= 1;
        //ensure efficient usage of the array
        if ((items.length > 15) && (items.length > (4 * size))) {
            resize_d();
        }
        return firstItem;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T lastItem = items[nextLast - 1];
        if (nextLast == 0) {
            nextLast = (items.length - 1);
        } else {
            nextLast -= 1;
        }
        size -= 1;
        //ensure efficient usage of the array
        if ((items.length > 15) && (items.length > (4 * size))) {
            resize_d();
        }
        return lastItem;
    }

    public T get(int index) {
        if ((nextFirst + 1 + index) <= (items.length - 1)) {
            return items[nextFirst + 1 + index];
        } else {
            return items[nextFirst + 1 + index - (items.length - 1) - 1];
        }
    }
}