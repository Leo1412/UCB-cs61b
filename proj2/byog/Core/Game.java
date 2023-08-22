package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.util.Map;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        /*
        //ensure the input is in the format of "NxxxxS".
        if ((input.charAt(0) != 'N') && (input.charAt(0) != 'n')) {
            System.exit(0);
            return null;
        } else if ((input.charAt(input.length() - 1) != 'S') && (input.charAt(input.length() - 1) != 's')) {
            System.exit(0);
            return null;
        }
         */
        char firstChar = input.charAt(0);
        TETile[][] finalWorldFrame = null;
        int commandIdx = 0;
        switch(firstChar) {
            case 'n':
            case 'N':
                String seedStr = new String("");
                long seed;
                for (int i = 1; i < input.length(); i++) {
                    char nextChar = input.charAt(i);
                    int nextCharInt = Character.getNumericValue(nextChar);
                    if (nextCharInt <= 9 && nextCharInt >= 0) {
                        seedStr += nextChar;
                    } else {
                        commandIdx = i + 1;
                        break;
                    }
                }
                seed = Long.parseLong(seedStr, 10);
                MapGenerator randomMap = new MapGenerator(seed);
                finalWorldFrame = randomMap.frame();
                Entity e = new Entity(finalWorldFrame, randomMap.entityStartPos());

                for (int j = commandIdx; j < input.length(); j++) {
                    char currentCommand = input.charAt(j);
                    if (currentCommand != 'Q' && currentCommand != 'q') {
                        e.moveEntity(currentCommand);
                        e.drawWorld();
                    } else {
                        //to be implemented (quit and save)
                    }
                }
                break;
            case 'l':
            case 'L':
                //to be implemented (load the saved game)
                break;
        }

        //return the world frame
        return finalWorldFrame;
    }
}
