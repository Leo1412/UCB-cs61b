package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */


public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        final int F = 4;
        final int T = 3;
        final int E = 2;
        final int O = 1;

        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> x = new ArrayRingBuffer(F);
        x.enqueue(O);
        x.enqueue(E);
        x.enqueue(T);
        x.enqueue(F);
        assertTrue(x.isFull());
        assertTrue(x.dequeue() == O);
        assertTrue(x.dequeue() == E);
        assertTrue(x.dequeue() == T);
        assertTrue(x.dequeue() == F);
        //suppoesd to throw an exception
        x.dequeue();

    }



    /** Calls tests for ArrayRingBuffer.**/
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 

