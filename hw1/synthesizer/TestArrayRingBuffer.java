package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> x = new ArrayRingBuffer(4);
        x.enqueue(1);
        x.enqueue(2);
        x.enqueue(3);
        x.enqueue(4);
        assertTrue(x.isFull());
        assertTrue(x.dequeue() == 1);
        assertTrue(x.dequeue() == 2);
        assertTrue(x.dequeue() == 3);
        assertTrue(x.dequeue() == 4);
        //suppoesd to throw an exception
        x.dequeue();

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
