package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

//the class Entity will keep track of the entity controlled by the player using W, A, S, D
public class Entity {
    private TETile[][] mapToDraw = null;
    private Position currentPos = new Position(0,0);
    private char command;

    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;
    private TETile entity = Tileset.FLOWER;

    public Entity(TETile[][] map, Position pos) {
        mapToDraw = map;
        currentPos = pos;
    }

    //method to move the entity controlled by player using W,A,S,D.
    public void moveEntity(char c) {
        command  = c;
        mapToDraw[currentPos.x][currentPos.y] = floor;
        switch(command) {
            case 'w':
            case 'W':
                if (mapToDraw[currentPos.x][currentPos.y + 1] != wall) {
                    mapToDraw[currentPos.x][currentPos.y + 1] = entity;
                    currentPos.y += 1;
                }
                break;
            case 'a':
            case 'A':
                if (mapToDraw[currentPos.x - 1][currentPos.y] != wall) {
                    mapToDraw[currentPos.x - 1][currentPos.y] = entity;
                    currentPos.x -= 1;
                }
                break;
            case 'd':
            case 'D':
                if (mapToDraw[currentPos.x + 1][currentPos.y] != wall) {
                    mapToDraw[currentPos.x + 1][currentPos.y] = entity;
                    currentPos.x += 1;
                }
                break;
            case 's':
            case 'S':
                if (mapToDraw[currentPos.x][currentPos.y - 1] != wall) {
                    mapToDraw[currentPos.x][currentPos.y - 1] = entity;
                    currentPos.y -= 1;
                }
                break;
        }
    }
    public void drawWorld() {
        TERenderer ter = new TERenderer();
        ter.initialize(mapToDraw.length, mapToDraw[0].length);
        ter.renderFrame(mapToDraw);

    }
}
