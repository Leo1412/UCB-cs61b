package byog.Core;

import byog.TileEngine.TETile;
import byog.lab5.Position;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Map;

public class Room {
    private TETile[][] MapToDraw;

    public int w;
    public int h;
    public Position p; //lower left corner of the room
    public int ExitNo = 0;

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

    //p1, p2 are the end points on the same line(vertical or horizontal), p3 and p4 are on the same line(v or h)
    //assume two lines are either adjacent vertically or horizontally, this method aims to return the overlapping positions, if any
    //(p1, p2)
    //(p3, p4)
    public ArrayList<Position> isLineOverlap(Position p1, Position p2, Position p3, Position p4) {
        ArrayList<Position> OverlapPos = new ArrayList<>();
        if(p1.y == p2.y) {
            for (int i = 1; i < (p2.x - p1.x); i++) {
                if(((p1.x + i) < p4.x) && ((p1.x + i) > p3.x)) {
                    OverlapPos.add(new Position(p1.x + i, p1.y));
                }
            }
        } else {
            for (int i = 1; i < (p2.y - p1.y); i++) {

                if(((p1.y + i) < p4.y) && ((p1.y + i) > p3.y)) {
                    OverlapPos.add(new Position(p1.x, p1.y + i));
                }
        }
    }
        return OverlapPos;
    }

    //determine whether two rooms are adjacent
    public ArrayList<Position> isAdjacent(Room BRoom) {
        ArrayList<Position> OverlapPos = new ArrayList<>();
        //x range: [p.x, xExtreme1]
        //y range: [p.y, yExtreme1]
        int xExtreme1 = p.x + w + 1;
        int yExtreme1 = p.y + h + 1;
        //BRoom x range: [BRoom.p.x, xExtreme2]
        //BRoom y range: [BRoom.p.y, yExtreme2]
        int xExtreme2 = BRoom.p.x + BRoom.w + 1;
        int yExtreme2 = BRoom.p.y + BRoom.h + 1;

        if(BRoom.p.x == (xExtreme1 + 1)) {
            //right
            OverlapPos = isLineOverlap(new Position(xExtreme1, p.y), new Position(xExtreme1, yExtreme1), BRoom.p, new Position(BRoom.p.x, yExtreme2));
        } else if(p.x == (xExtreme2 + 1)) {
            //left
            OverlapPos = isLineOverlap(p, new Position(p.x, yExtreme1), new Position(xExtreme2, BRoom.p.y), new Position(xExtreme2, yExtreme2));
        } else if(BRoom.p.y == (yExtreme1 + 1)) {
            //up
            OverlapPos = isLineOverlap(new Position(p.x, yExtreme1), new Position(xExtreme1, yExtreme1), BRoom.p, new Position(xExtreme2, BRoom.p.y));
        } else if(p.y == (yExtreme2 + 1)) {
            //down
            OverlapPos = isLineOverlap(p, new Position(xExtreme1, p.y), new Position(BRoom.p.x, yExtreme2), new Position(xExtreme2, yExtreme2));
        }
        return OverlapPos;
    }


    //1-right, rotating counter-clockwise
    public int[] distanceTo(Room BRoom) {
        Position p1 = new Position(p.x + w + 1, p.y);
        Position p2 = new Position(p.x, p.y + h + 1);

        Position p3 = new Position(BRoom.p.x + BRoom.w + 1, BRoom.p.y);
        Position p4 = new Position(BRoom.p.x, BRoom.p.y + BRoom.h + 1);
        int[] resultArr = new int[2];

        if(isLineOverlap(p, p1, BRoom.p, p3).size() != 0) {
            if(p.y > BRoom.p.y) {
                resultArr[0] = 7;
                resultArr[1] = p.y - BRoom.p.y - BRoom.h - 1 - 1;
                return resultArr;
            } else{
                resultArr[0] = 3;
                resultArr[1] = BRoom.p.y - p.y - h -1 -1;
                return resultArr;
            }
        } else if(isLineOverlap(p, p2, BRoom.p, p4).size() != 0) {
            if(p.x > BRoom.p.x) {
                resultArr[0] = 5;
                resultArr[1] = p.x - BRoom.p.x - BRoom.w - 1 -1;
                return resultArr;
            } else{
                resultArr[0] = 1;
                resultArr[1] = BRoom.p.x - p.x - w - 1 - 1;
                return resultArr;
            }
        } else {
            //return this.distanceTo2(BRoom);
            if(p.x < BRoom.p.x) {
                if(p.y < BRoom.p.y) {
                    resultArr[0] = 2;
                    int yDistance = Math.max(BRoom.p.y - p.y - h - 1 - 1, 0);
                    int xDistance = Math.max(BRoom.p.x - p.x - w - 1 - 1, 0);
                    if ((xDistance == 0) || (yDistance == 0)) {
                        resultArr[1] = yDistance + xDistance + 2;
                    } else {
                        resultArr[1] = yDistance + xDistance;
                    }
                    return resultArr;
                } else{
                    resultArr[0] = 8;
                    int yDistance = Math.max(p.y - BRoom.p.y - BRoom.h - 1 - 1, 0);
                    int xDistance = Math.max(BRoom.p.x - p.x - w - 1 - 1, 0);
                    if ((xDistance == 0) || (yDistance == 0)) {
                        resultArr[1] = yDistance + xDistance + 2;
                    } else {
                        resultArr[1] = yDistance + xDistance;
                    }
                    return resultArr;
                }
            } else{
                if(p.y < BRoom.p.y) {
                    resultArr[0] = 4;
                    int yDistance = Math.max(BRoom.p.y - p.y - h - 1 - 1, 0);
                    int xDistance = Math.max(p.x - BRoom.p.x - BRoom.w - 1 - 1, 0);
                    if ((xDistance == 0) || (yDistance == 0)) {
                        resultArr[1] = yDistance + xDistance + 2;
                    } else {
                        resultArr[1] = yDistance + xDistance;
                    }
                    return resultArr;
                } else{
                    resultArr[0] = 6;
                    int yDistance = Math.max(p.y - BRoom.p.y - BRoom.h - 1 - 1, 0);
                    int xDistance = Math.max(p.x - BRoom.p.x - BRoom.w - 1 - 1, 0);
                    if ((xDistance == 0) || (yDistance == 0)) {
                        resultArr[1] = yDistance + xDistance + 2;
                    } else {
                        resultArr[1] = yDistance + xDistance;
                    }
                    return resultArr;
                }
            }
        }
        }
}

