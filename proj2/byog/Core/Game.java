package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private boolean isQuit = false;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        char playerChoice;
        String seedStr = new String("");
        long seed;

        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //generate the intro page of the game
        drawFrame();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                playerChoice = StdDraw.nextKeyTyped();
                break;
            }
        }

        switch (playerChoice) {
            case 'n':
            case 'N':
                StdDraw.clear(Color.black);
                StdDraw.setPenColor(Color.white);
                StdDraw.setFont(font);
                StdDraw.text(WIDTH/2, HEIGHT/2, "Input your seed: ", 0);
                StdDraw.show();
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char numChar = StdDraw.nextKeyTyped();
                        int num = Character.getNumericValue(numChar);
                        if (num <= 9 && num >= 0) {
                            seedStr += numChar;
                            StdDraw.clear(Color.black);
                            StdDraw.text(WIDTH/2, HEIGHT/2, "Input your seed: " + seedStr, 0);
                            StdDraw.show();
                        } else if (numChar == 's' || numChar == 'S') {
                            break;
                        }
                    }
                }
                seed = Long.parseLong(seedStr, 10);
                MapGenerator randomMap = new MapGenerator(seed);
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char command = StdDraw.nextKeyTyped();
                        if (command != 'q' && command != 'Q') {
                            randomMap.moveEntity(command);
                            randomMap.updateWorld();
                        } else {
                            isQuit = true;
                            break;
                        }
                    }
                }
                saveMap(randomMap);
                break;
            case 'l':
            case 'L':
                MapGenerator savedMap = loadSavedMap();
                savedMap.drawWorld();
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char command = StdDraw.nextKeyTyped();
                        if (command != 'q' && command != 'Q') {
                            savedMap.moveEntity(command);
                            savedMap.updateWorld();
                        } else {
                            isQuit = true;
                            break;
                        }
                    }
                }
                saveMap(savedMap);
                break;
            case 'q':
            case 'Q':
                isQuit = true;
                break;
        }
    }

    public void drawFrame() {
        StdDraw.clear(Color.black);
        Font font_1 = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font_1);
        StdDraw.text(WIDTH/2, HEIGHT * 3/4, "CS61B: THE GAME", 0);
        Font font_2 = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font_2);
        StdDraw.text(WIDTH/2, HEIGHT/2, "New Game (N)", 0);
        StdDraw.text(WIDTH/2, HEIGHT/2 - 2, "Load Game (L)", 0);
        StdDraw.text(WIDTH/2, HEIGHT/2 - 4, "Quit (Q)", 0);

        StdDraw.show();
    }

    public void saveMap(MapGenerator inputGenerator) {
        //quit and save
        String fileName = "file.ser";
        try {
            //save the object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            //serialization
            out.writeObject(inputGenerator);
            out.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught.");
            System.out.println(ex);
        }
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
                handleInput(input, randomMap, commandIdx);
                return finalWorldFrame;
                //break;
            case 'l':
            case 'L':
                MapGenerator savedMap = loadSavedMap();
                finalWorldFrame = savedMap.frame();
                savedMap.drawWorld();
                handleInput(input, savedMap, 1);
                //return the world frame
                return finalWorldFrame;
        }
        return null;
    }


    //method to parse the input (including the motion command and the quit statement)
    public void handleInput(String input, MapGenerator inputGenerator, int commandIdx) {
        for (int j = commandIdx; j < input.length(); j++) {
            char currentCommand = input.charAt(j);
            if (currentCommand != 'Q' && currentCommand != 'q') {
                inputGenerator.moveEntity(currentCommand);
                inputGenerator.updateWorld();
            } else {
                //quit and save
                StdDraw.pause(1000);
                String fileName = "file.ser";
                    try {
                        //save the object in a file
                        FileOutputStream file = new FileOutputStream(fileName);
                        ObjectOutputStream out = new ObjectOutputStream(file);

                        //serialization
                        out.writeObject(inputGenerator);
                        out.close();
                        file.close();
                    } catch (IOException ex) {
                        System.out.println("IOException is caught.");
                        System.out.println(ex);
                    }
                    isQuit = true;
            }
        }
    }

    //method to load the previously saved map
    public MapGenerator loadSavedMap() {
        //load the saved game
        MapGenerator savedMap = null;
        String filename = "file.ser";
        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            savedMap = (MapGenerator) in.readObject();

            in.close();
            file.close();
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught:");
            System.out.print(ex);
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return savedMap;
    }

    public boolean quitStatus() {
        return isQuit;
    }
}


