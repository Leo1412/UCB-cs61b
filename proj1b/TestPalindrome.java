import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("racecar"));
        assertTrue(palindrome.isPalindrome("oo"));
        assertFalse(palindrome.isPalindrome("tyler"));
        assertFalse(palindrome.isPalindrome("durden"));
        assertTrue(palindrome.isPalindrome("L"));
        assertTrue(palindrome.isPalindrome(""));
    }

    @Test
    public void test() {
        String word = "llllol";
        Deque<Character> wordDeque = palindrome.wordToDeque(word);
        System.out.print(wordDeque.toString());
    }

    @Test
    public void testD2S() {
        String word = "leolllll";
        Deque<Character> wordDeque = palindrome.wordToDeque(word);
        System.out.println(palindrome.d2s(wordDeque));
    }

    @Test
    public void isPalindromeCharacterComparator() {
        CharacterComparator testCharComp = new OffByOne();
        assertFalse(palindrome.isPalindrome("racecar", testCharComp));
        assertFalse(palindrome.isPalindrome("noon", testCharComp));
        assertTrue(palindrome.isPalindrome("flake", testCharComp));
        assertTrue(palindrome.isPalindrome("eghf", testCharComp));
        assertTrue(palindrome.isPalindrome("L"));
        assertTrue(palindrome.isPalindrome(""));
    }
}
