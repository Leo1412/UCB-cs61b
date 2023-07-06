import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void test1() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('g', 'h'));
        assertFalse(offByOne.equalChars('o', 'z'));
        assertFalse(offByOne.equalChars('f', 'h'));
    }

    @Test
    public void test2() {
        assertTrue(offByOne.equalChars('%', '&'));
        assertFalse(offByOne.equalChars('%', ')'));
        assertFalse(offByOne.equalChars('A', 'a'));
        assertFalse(offByOne.equalChars('C', 'c'));
    }
}
