package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import javax.annotation.processing.SupportedSourceVersion;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import edu.princeton.cs.introcs.StdDraw;
import java.lang.Math;



public class MapGenerator implements Serializable {
    static private long seed;
    final static private int WIDTH = 90;
    final static private int HEIGHT = 40;
    private static Random RANDOM = null;
    private TETile[][] map = new TETile[WIDTH][HEIGHT];
    private ArrayList<Room> RoomList = new ArrayList<>();

    ArrayList<Position> PointList = new ArrayList<>();
    RoomManager manager = new RoomManager(WIDTH, HEIGHT);

    Position entityPos = new Position(0,0);
    Position desPos = new Position(0, 0);
    private int prevNo = 0; //previous number of times the player hits the wall
    private int hitNo = 0; //number of times the player hits the wall

    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;
    private TETile entity = Tileset.FLOWER;
    private TETile destination = Tileset.MOUNTAIN;

    // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
    TERenderer ter = new TERenderer();

    public MapGenerator(long s) {
        seed = s;
        RANDOM = new Random(seed);

        ter.initialize(WIDTH, HEIGHT);
        //background
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }

        GenerateMap();

        //render the generated map.
        ter.renderFrame(map);

    }

    public Position GetRandomPoint(Position OPos, int radius) {
        double a = 2 * Math.PI * RandomUtils.uniform(RANDOM);
        double d = RandomUtils.uniform(RANDOM);
        return new Position((int)(radius * d * Math.cos(a) + OPos.x), (int)(radius * d * Math.sin(a) + OPos.y));
    }

    public void GenerateRooms() {
        Position pos1 = new Position(15, 20);
        Position pos2 = new Position(45, 20);
        Position pos3 = new Position(75, 20);
        //no. of points within the two circles
        int no1;
        int no2;
        int no3;
        do {
        no1 = (int)RandomUtils.gaussian(RANDOM, 15, 1);
        no2 = (int)RandomUtils.gaussian(RANDOM, 15, 1);
        no3 = (int)RandomUtils.gaussian(RANDOM, 15, 1);
        } while((no1 <= 0) || (no2 <= 0) || (no3 <= 0));

        for (int i = 0; i < no1; i++) {
            PointList.add(GetRandomPoint(pos1, 15));
        }
        for (int j = 0; j < no2; j++) {
            PointList.add(GetRandomPoint(pos2, 15));
        }
        for (int t = 0; t < no3; t++) {
            PointList.add(GetRandomPoint(pos3, 15));
        }

        for(int z = 0; z < no1 + no2 +no3; z++) {
            //the final width and height of the added room
            int finalW;
            int finalH;
            Room NewRoom = null;
            int no = 0;  //testing: record the number of trials; if it is too large, skip the point.
            boolean isKeep = true;  //determine whether to keep the current point.

            do{
                if(no > 10) {
                    isKeep = false;
                    break;
                }
                //finalW = (int) RandomUtils.gaussian(RANDOM, 5, 1);
                //finalH = (int) RandomUtils.gaussian(RANDOM, 5, 1);
                finalW = RandomUtils.uniform(RANDOM, 4, 10);
                finalH = RandomUtils.uniform(RANDOM, 4, 10);
                //reserve the top line for displaying HUD
                if (PointList.get(z).y + finalH == HEIGHT - 1) {
                    isKeep = false;
                    break;
                }
                NewRoom = new Room(map, finalW, finalH, PointList.get(z));
                no += 1;
            } while((manager.isOverlap(NewRoom) || manager.isOutOfBound(NewRoom)));

            if(isKeep) {
                //store the new room in room manager and generate the new room
                manager.AddRoom(NewRoom);
                NewRoom.GenerateRoom();
                RoomList.add(NewRoom);
            }
        }
    }


    public void GenerateHallways() {
        ArrayList<Room> roomGroup = new ArrayList<>(); //the group of rooms considered
        roomGroup.add(RoomList.get(0));
        Room ThisRoom = null;

        while(roomGroup.size() < RoomList.size()) {
            Room closestRoomTot = null;    //the closest room outside the group to the group
            int sDistTot = 200;            //the smallest distance

            //go through all rooms outside the roomGroup to search for the closest room
            for(int j = 0; j < RoomList.size(); j++) {
                boolean isInGroup = false;
                Room TestRoom = RoomList.get(j);
                Room closestRoomInGroup = null;   //the closest room in the group to the TestRoom
                int sDist = 100;                  //the smallest distance between TestRoom to the group

                //check whether TestRoom is in roomGroup
                for (int z = 0; z < roomGroup.size(); z++) {
                    Room zRoom = roomGroup.get(z);
                    if (zRoom == TestRoom) {
                        isInGroup = true;
                    }
                }
                if (isInGroup) {
                    continue;
                }

                //search for the room in groupRoom that has the smallest distance to TestRoom
                for (int t = 0; t < roomGroup.size(); t++) {
                    Room chosenRoom = roomGroup.get(t);
                    int dist = chosenRoom.distanceTo(TestRoom)[1];
                    if (dist < sDist) {
                        sDist = dist;
                        closestRoomInGroup = chosenRoom;
                    }
                }

                if (sDist < sDistTot) {
                    sDistTot = sDist;
                    closestRoomTot = closestRoomInGroup;  //inside group
                    ThisRoom = TestRoom;                  //outside group
                }
            }

            //the direction of the closest room to the group: closestRoomTot
            int direc = ThisRoom.distanceTo(closestRoomTot)[0];
            //update the groupRoom
            roomGroup.add(ThisRoom);
            //the smallest distance: sDistTot


            //needs to be done: distinguish different cases of "overlap"
            switch(direc) {
                case 1:
                    ArrayList<Position> PointList1 = ThisRoom.isLineOverlap(new Position(ThisRoom.p.x + ThisRoom.w + 1, ThisRoom.p.y), new Position(ThisRoom.p.x + ThisRoom.w + 1, ThisRoom.p.y + ThisRoom.h + 1), closestRoomTot.p, new Position(closestRoomTot.p.x, closestRoomTot.p.y + closestRoomTot.h + 1));
                    int randomPos1 = RandomUtils.uniform(RANDOM, PointList1.size());
                    Position randomPoint1 = PointList1.get(randomPos1);
                    Hallway CHallway1 = new Hallway(map, randomPoint1, sDistTot, 0);
                    CHallway1.GenerateHallway();
                    break;
                case 2:
                    int xDistance2 = Math.max(closestRoomTot.p.x - ThisRoom.p.x - ThisRoom.w - 1 - 1, 0);
                    int yDistance2 = Math.max(closestRoomTot.p.y - ThisRoom.p.y - ThisRoom.h - 1 - 1, 0);
                    if (ThisRoom.p.x + ThisRoom.w + 2 <= closestRoomTot.p.x) {
                        xDistance2 += 2;
                    } else if (ThisRoom.p.x + ThisRoom.w + 1 == closestRoomTot.p.x) {
                        xDistance2 += 1;
                    }
                    Position startPos2 = new Position(ThisRoom.p.x + ThisRoom.w, ThisRoom.p.y + ThisRoom.h + 1);
                    Hallway CHallway21 = new Hallway(map, startPos2, yDistance2, 1);
                    CHallway21.GenerateHallway();
                    Position tempPoint2 = new Position(startPos2.x - 1, startPos2.y + yDistance2 + 2);
                    Hallway CHallway22 = new Hallway(map, tempPoint2, xDistance2, 0);
                    CHallway22.GenerateHallway();
                    map[tempPoint2.x][tempPoint2.y] = wall;
                    map[startPos2.x][tempPoint2.y - 1] = floor;
                    break;
                case 3:
                    ArrayList<Position> PointList3 = ThisRoom.isLineOverlap(new Position(ThisRoom.p.x, ThisRoom.p.y + ThisRoom.h + 1), new Position(ThisRoom.p.x + ThisRoom.w + 1, ThisRoom.p.y + ThisRoom.h + 1), closestRoomTot.p, new Position(closestRoomTot.p.x + closestRoomTot.w + 1, closestRoomTot.p.y));
                    int randomPos3 = RandomUtils.uniform(RANDOM, PointList3.size());
                    Position randomPoint3 = PointList3.get(randomPos3);
                    Hallway CHallway3 = new Hallway(map, randomPoint3, sDistTot, 1);
                    CHallway3.GenerateHallway();
                    break;
                case 4:
                    int xDistance4 = Math.max(ThisRoom.p.x - closestRoomTot.p.x - closestRoomTot.w - 1 - 1, 0);
                    int yDistance4 = Math.max(closestRoomTot.p.y - ThisRoom.p.y - ThisRoom.h - 1 - 1, 0);
                    if (ThisRoom.p.x - 1 >= (closestRoomTot.p.x + closestRoomTot.w + 1)) {
                        xDistance4 += 2;
                    } else if (ThisRoom.p.x == (closestRoomTot.p.x + closestRoomTot.w + 1)) {
                        xDistance4 += 1;
                    }
                    Position startPos4 = new Position(ThisRoom.p.x + 1, ThisRoom.p.y + ThisRoom.h + 1);
                    Hallway CHallway41 = new Hallway(map, startPos4, yDistance4, 1);
                    CHallway41.GenerateHallway();
                    Position tempPoint4 = new Position(startPos4.x + 1, startPos4.y + yDistance4 + 2);
                    Hallway CHallway42 = new Hallway(map, tempPoint4, xDistance4, 2);
                    CHallway42.GenerateHallway();
                    map[tempPoint4.x][tempPoint4.y] = wall;
                    map[startPos4.x][tempPoint4.y - 1] = floor;
                    break;
                case 5:
                    ArrayList<Position> PointList5 = ThisRoom.isLineOverlap(ThisRoom.p, new Position(ThisRoom.p.x, ThisRoom.p.y + ThisRoom.h + 1), new Position(closestRoomTot.p.x + closestRoomTot.w + 1, closestRoomTot.p.y), new Position(closestRoomTot.p.x + closestRoomTot.w + 1, closestRoomTot.p.y + closestRoomTot.h + 1));
                    int randomPos5 = RandomUtils.uniform(RANDOM, PointList5.size());
                    Position randomPoint5 = PointList5.get(randomPos5);
                    Hallway CHallway5 = new Hallway(map, randomPoint5, sDistTot, 2);
                    CHallway5.GenerateHallway();
                    break;

                case 6:
                    int xDistance6 = Math.max(ThisRoom.p.x - closestRoomTot.p.x - closestRoomTot.w - 1 - 1, 0);
                    int yDistance6 = Math.max(ThisRoom.p.y - closestRoomTot.p.y - closestRoomTot.h - 1 - 1, 0);
                    if (ThisRoom.p.y >= (closestRoomTot.p.y + closestRoomTot.h + 2)) {
                        yDistance6 += 2;
                    } else if (ThisRoom.p.y == (closestRoomTot.p.y + closestRoomTot.h + 1)) {
                        yDistance6 += 1;
                    }
                    Position startPos6 = new Position(ThisRoom.p.x, ThisRoom.p.y + 1);
                    Hallway CHallway61 = new Hallway(map, startPos6, xDistance6, 2);
                    CHallway61.GenerateHallway();
                    Position tempPoint6 = new Position(startPos6.x - xDistance6 - 2, startPos6.y + 1);
                    Hallway CHallway62 = new Hallway(map, tempPoint6, yDistance6, 3);
                    CHallway62.GenerateHallway();
                    map[tempPoint6.x][tempPoint6.y] = wall;
                    map[tempPoint6.x + 1][startPos6.y] = floor;
                    break;
                case 7:
                    ArrayList<Position> PointList7 = ThisRoom.isLineOverlap(ThisRoom.p, new Position(ThisRoom.p.x + ThisRoom.w + 1, ThisRoom.p.y), new Position(closestRoomTot.p.x, closestRoomTot.p.y + closestRoomTot.h + 1), new Position(closestRoomTot.p.x + closestRoomTot.w + 1, closestRoomTot.p.y + closestRoomTot.h + 1));
                    int randomPos7 = RandomUtils.uniform(RANDOM, PointList7.size());
                    Position randomPoint7 = PointList7.get(randomPos7);
                    Hallway CHallway7 = new Hallway(map, randomPoint7, sDistTot, 3);
                    CHallway7.GenerateHallway();
                    break;
                case 8:
                    int xDistance8 = Math.max(closestRoomTot.p.x - ThisRoom.p.x - ThisRoom.w - 1 - 1, 0);
                    int yDistance8 = Math.max(ThisRoom.p.y - closestRoomTot.p.y - ThisRoom.h - 1 - 1, 0);
                    if (ThisRoom.p.y >= (closestRoomTot.p.y + closestRoomTot.h + 2)) {
                        yDistance8 += 2;
                    } else if (ThisRoom.p.y == (closestRoomTot.p.y + closestRoomTot.h + 1)) {
                        yDistance8 += 1;
                    }
                    Position startPos8 = new Position(ThisRoom.p.x + ThisRoom.w + 1, ThisRoom.p.y + 1);
                    Hallway CHallway81 = new Hallway(map, startPos8, xDistance8, 0);
                    CHallway81.GenerateHallway();
                    Position tempPoint8 = new Position(startPos8.x + xDistance8 + 2, startPos8.y + 1);
                    Hallway CHallway82 = new Hallway(map, tempPoint8, yDistance8, 3);
                    CHallway82.GenerateHallway();
                    map[tempPoint8.x][tempPoint8.y] = wall;
                    map[tempPoint8.x - 1][startPos8.y] = floor;
                    break;
            }
        }
    }


    //method to randomly generate the whole map.
    public void GenerateMap() {
        GenerateRooms();
        GenerateHallways();
        //draw the initial position of the entity
        int entityX = 0;
        int entityY = 0;
        int desX = 0;
        int desY = 0;
        while (true) {
            entityX = RANDOM.nextInt(WIDTH);
            entityY = RANDOM.nextInt(HEIGHT);
            if (map[entityX][entityY] == floor) {
                map[entityX][entityY] = entity;
                entityPos.x = entityX;
                entityPos.y = entityY;
                break;
            }
        }
        while (true) {
            desX = RANDOM.nextInt(WIDTH);
            desY = RANDOM.nextInt(HEIGHT);
            if (map[desX][desY] == floor) {
                map[desX][desY] = destination;
                desPos.x = desX;
                desPos.y = desY;
                break;
            }
        }
    }

    public Position entityStartPos() {
        return entityPos;
    }

    public TETile[][] frame() {
        return map;
    }


    //method to move the entity controlled by player using W,A,S,D.
    public void moveEntity(char command) {
        switch(command) {
            case 'w':
            case 'W':
                if (map[entityPos.x][entityPos.y + 1] != wall) {
                    map[entityPos.x][entityPos.y] = floor;
                    map[entityPos.x][entityPos.y + 1] = entity;
                    entityPos.y += 1;
                } else {
                    hitNo += 1;
                }
                break;
            case 'a':
            case 'A':
                if (map[entityPos.x - 1][entityPos.y] != wall) {
                    map[entityPos.x][entityPos.y] = floor;
                    map[entityPos.x - 1][entityPos.y] = entity;
                    entityPos.x -= 1;
                } else {
                    hitNo += 1;
                }
                break;
            case 'd':
            case 'D':
                if (map[entityPos.x + 1][entityPos.y] != wall) {
                    map[entityPos.x][entityPos.y] = floor;
                    map[entityPos.x + 1][entityPos.y] = entity;
                    entityPos.x += 1;
                } else {
                    hitNo += 1;
                }
                break;
            case 's':
            case 'S':
                if (map[entityPos.x][entityPos.y - 1] != wall) {
                    map[entityPos.x][entityPos.y] = floor;
                    map[entityPos.x][entityPos.y - 1] = entity;
                    entityPos.y -= 1;
                } else {
                    hitNo += 1;
                }
                break;
        }
    }

    //check the current game status
    public boolean checkStatus() {
        if (hitNo == 3) return true;
        int entityX = 0;
        int entityY = 0;
        if (hitNo != prevNo) {
            prevNo = hitNo;
            map[entityPos.x][entityPos.y] = floor;
            while (true) {
                entityX = RANDOM.nextInt(WIDTH);
                entityY = RANDOM.nextInt(HEIGHT);
                if (map[entityX][entityY] == floor) {
                    map[entityX][entityY] = entity;
                    entityPos.x = entityX;
                    entityPos.y = entityY;
                    break;
                }
            }
        }
        return false;
    }

    //check whether the player wins
    public boolean isWin() {
        if (entityPos.x == desPos.x && entityPos.y == desPos.y) return true;
        return false;
    }

    //draw the updated world
    public void updateWorld() {
        ter.renderFrame(map);
    }

    //re-draw the saved world
    public void drawWorld() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(map);
    }

    public void showHUD() {
        int mouseX = (int)Math.floor(StdDraw.mouseX());
        int mouseY = (int)Math.floor(StdDraw.mouseY());
        Font font = new Font("Monaco", Font.BOLD, 15);
        if (map[mouseX][mouseY] == wall) {
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(WIDTH/18, HEIGHT - 1, WIDTH / 18, 0.6);
            StdDraw.setPenColor(Color.yellow);
            StdDraw.text(WIDTH/18, HEIGHT - 1, "wall", 0);
            StdDraw.show();
        } else if (map[mouseX][mouseY] == floor) {
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(WIDTH/18, HEIGHT - 1, WIDTH / 18, 0.6);
            StdDraw.setPenColor(Color.yellow);
            StdDraw.text(WIDTH/18, HEIGHT - 1, "floor", 0);
            StdDraw.show();
        } else if (map[mouseX][mouseY] == entity) {
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(WIDTH/18, HEIGHT - 1, WIDTH / 18, 0.6);
            StdDraw.setPenColor(Color.red);
            StdDraw.text(WIDTH/18, HEIGHT - 1, "player", 0);
            StdDraw.show();
        } else if (map[mouseX][mouseY] == destination) {
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(WIDTH/18, HEIGHT - 1, WIDTH / 18, 0.6);
            StdDraw.setPenColor(Color.green);
            StdDraw.text(WIDTH/18, HEIGHT - 1, "destination", 0);
            StdDraw.show();
        }
        else {
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.black);
            StdDraw.filledRectangle(WIDTH/18, HEIGHT - 1, WIDTH / 18, 0.6);
            StdDraw.show();
        }
    }
}

