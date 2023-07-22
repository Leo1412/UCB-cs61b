package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */

    private class BufferIterator implements Iterator<T> {
        private int ptr;
        private BufferIterator() {
            ptr = first;
        }
        public boolean hasNext() {
            return (ptr != last);
        }
        public T next() {
            T returnItem = rb[ptr];
            ptr += 1;
            return returnItem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow.");
        }
        rb[last] = x;
        fillCount += 1;
        if (last == this.capacity - 1) {
            last = 0;
        } else {
            last += 1;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        int oldFirst = first;
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow.");
        }
        fillCount -= 1;
        if (first == this.capacity - 1) {
            first = 0;
        } else {
            first += 1;
        }
        return rb[oldFirst];
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return rb[first];
    }


}
