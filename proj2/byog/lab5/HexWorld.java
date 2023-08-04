package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.*;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final long SEED = 2873123;

    private static final Random RANDOM = new Random(SEED);

    /**
     * Computes the width of row i for a size s hexagon.
     *
     * @param s The size of the hex.
     * @param i The row number where i = 0 is the bottom row.
     * @return
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }

        return s + 2 * effectiveI;
    }

    /**
     * Computesrelative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     * xxxx
     * xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     * xxxxxx
     * xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return -effectiveI;
    }

    /**
     * Adds a row of the same tile.
     *
     * @param world the world to draw on
     * @param p     the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t     the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    /**
     * Adds a hexagon to the world.
     *
     * @param world the world to draw on
     * @param p     the bottom left coordinate of the hexagon
     * @param s     the size of the hexagon
     * @param t     the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;

            int xRowStart = p.x + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);
        }
    }

    public static Position upPos(Position pos, int s) {
        Position resultPos = new Position(0,0);
        resultPos.x = pos.x;
        resultPos.y = pos.y + 2 * s - 1;
        return resultPos;
    }

    public static Position leftPos(Position pos, int s) {
        Position resultPos = new Position(0,0);
        resultPos.x = pos.x - 2 * s + 1;
        resultPos.y = pos.y + s;
        return resultPos;
    }

    public static Position rightPos(Position pos, int s) {
        Position resultPos = new Position(0,0);
        resultPos.x = pos.x + 2 * s - 1;
        resultPos.y = pos.y + s;
        return resultPos;
    }

    public static void drawVerticalHex(TETile[][] world, Position p, int s, TETile t, int height) {
        for (int i = 0; i < height; i++) {
            addHexagon(world, p, s, t);
            p = upPos(p, s);
        }
    }

    //method to tesselate the hexagons in the specified pattern;
    //p is the position of the lower left corner of the bottom tile.
    public static void tesselateHex(TETile[][] world, Position p, int s, TETile t) {
        int h = 5;
        Position pos1 = new Position(p.x,p.y);
        Position pos2 = new Position(p.x,p.y);
        drawVerticalHex(world, p, s, t, h);
        for (int i = 0; i < 2; i++) {
            h -= 1;
            pos1 = leftPos(pos1, s);
            pos2 = rightPos(pos2, s);
            drawVerticalHex(world, pos1, s, t, h);
            drawVerticalHex(world, pos2, s, t, h);
        }
    }



    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 40);
        TETile[][] world = new TETile[80][40];
        Position pos = new Position(40,5);
        TETile tile = new TETile('a', Color.blue, Color.black, "the tile");
        //create the background
        for (int x = 0; x < 80; x += 1) {
            for (int y = 0; y < 40; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        //add the hexagon
        //addHexagon(world, pos, 5, tile);
        tesselateHex(world, pos, 3, tile);

        //render the world
        ter.renderFrame(world);
    }
}
