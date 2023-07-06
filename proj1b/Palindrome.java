public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    public boolean isPalindrome(String word) {
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }

        Deque<Character> wordDeque = wordToDeque(word);
        String backWord = "";
        for (int i = 0; i < word.length(); i++) {
            backWord += wordDeque.removeLast();
        }
        return backWord.equals(word);
    }

    //helper function to convert Deque<Character> into a string
    private String d2s(Deque inDeque) {
        String word = "";
        int dequeSize = inDeque.size();
        for (int i = 0; i < dequeSize; i++) {
            word += inDeque.removeFirst();
        }
        return word;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() == 0 || word.length() == 1) {
            return true;
        }
        Deque<Character> wordDeque = wordToDeque(word);
        String backWord = "";
        char firstChar = wordDeque.removeFirst();
        char lastChar = wordDeque.removeLast();
        if (cc.equalChars(firstChar, lastChar)) {
            return isPalindrome(d2s(wordDeque), cc);
        } else {
            return false;
        }
    }
}
