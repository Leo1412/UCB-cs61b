package byog.Core;

import byog.TileEngine.TETile;
import byog.lab5.Position;
import byog.TileEngine.Tileset;

import java.util.Map;

public class Room {
    private TETile[][] MapToDraw;

    public int w;
    public int h;
    public Position p; //lower left corner of the room

    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;


    public Room(TETile[][] map, int width, int height, Position pos) {
        MapToDraw = map;
        w = width;
        h = height;
        p = pos;
    }

    //method to draw a horizontal line of wall or floor...
    //s denotes the length of the line; t denotes the tile to draw
    //pos is the leftmost point of the line
    public void DrawHorizontalLine(int s, Position pos, TETile t) {
        for(int i = 0; i < s; i++) {
            MapToDraw[pos.x + i][pos.y] = t;
        }
    }

    //method to draw a vertical line
    //pos is the bottom point of the line
    public void DrawVerticalLine(int s, Position pos, TETile t) {
        for(int i = 0; i < s; i++) {
            MapToDraw[pos.x][pos.y + i] = t;
        }
    }

    //generate a room based on the variables
    public void GenerateRoom() {
        //generate the floors
        for(int i = 0; i < h; i ++) {
            Position pNew = new Position(p.x + 1, p.y + 1 + i);
            DrawHorizontalLine(w, pNew, floor);
        }

        //generate the walls
        DrawHorizontalLine(w + 2, p, wall);
        Position pU = new Position(p.x, p.y + h + 1);
        DrawHorizontalLine(w + 2, pU, wall);

        DrawVerticalLine(h + 2, p, wall);
        Position pR = new Position(p.x + w + 1, p.y);
        DrawVerticalLine(h + 2, pR, wall);

    }
}
