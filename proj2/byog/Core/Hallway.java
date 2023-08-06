package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import java.util.Map;


public class Hallway {
    private TETile[][] MapToDraw;

    public Position pos;
    public int length;
    public int direction;  //0:right, 1: up, 2: left, 3: down
    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;

    public Hallway(TETile[][] map, Position p, int l, int d) {
        MapToDraw = map;
        pos = p;
        length = l;
        direction = d;
    }

    public void GenerateHallway() {
        switch(direction) {
            case 0:
                Room HW1 = new Room(MapToDraw, length, 1, new Position(pos.x, pos.y - 1));
                HW1.GenerateRoom();
                MapToDraw[pos.x][pos.y] = floor;
                MapToDraw[pos.x + length + 1][pos.y] = floor;
                break;
            case 1:
                Room HW2 = new Room(MapToDraw, 1, length, new Position(pos.x - 1, pos.y));
                HW2.GenerateRoom();
                MapToDraw[pos.x][pos.y] = floor;
                MapToDraw[pos.x][pos.y + length + 1] = floor;
                break;
            case 2:
                Room HW3 = new Room(MapToDraw, length, 1, new Position(pos.x - length - 1, pos.y - 1));
                HW3.GenerateRoom();
                MapToDraw[pos.x][pos.y] = floor;
                MapToDraw[pos.x - length - 1][pos.y] = floor;
                break;
            case 3:
                Room HW4 = new Room(MapToDraw, 1, length, new Position(pos.x - 1, pos.y - length - 1));
                HW4.GenerateRoom();
                MapToDraw[pos.x][pos.y] = floor;
                MapToDraw[pos.x][pos.y - length - 1] = floor;
                break;
        }
    }
}
