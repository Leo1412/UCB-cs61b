package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.Random;
import java.util.ArrayList;


public class MapGenerator {
    static private long seed;
    final static private int WIDTH = 90;
    final static private int HEIGHT = 40;
    private static Random RANDOM = null;
    private TETile[][] map = new TETile[WIDTH][HEIGHT];
    Position CPos;
    ArrayList<Position> PointList = new ArrayList<>();
    RoomManager manager = new RoomManager(WIDTH, HEIGHT);

    public MapGenerator(int s) {
        seed = s;
        RANDOM = new Random(seed);

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        //background
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }

        GenerateMap();
        printPoints();

        //render the generated map.
        ter.renderFrame(map);

        /*
        //---------------------------------------------------
        //testing
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }
        Position testP = new Position(20, 10);
        Room testRoom = new Room(map, 15, 5, testP);
        testRoom.GenerateRoom();
        ter.renderFrame(map);
        //---------------------------------------------------
         */
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
        do{
        no1 = (int)RandomUtils.gaussian(RANDOM, 10, 1);
        no2 = (int)RandomUtils.gaussian(RANDOM, 10, 1);
        no3 = (int)RandomUtils.gaussian(RANDOM, 10, 1);
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
                finalW = RandomUtils.uniform(RANDOM, 1, 10);
                finalH = RandomUtils.uniform(RANDOM, 1, 10);
                NewRoom = new Room(map, finalW, finalH, PointList.get(z));
                no += 1;
            } while((manager.isOverlap(NewRoom) || manager.isOutOfBound(NewRoom)));

            if(isKeep) {
                //store the new room in room manager and generate the new room
                manager.AddRoom(NewRoom);
                NewRoom.GenerateRoom();
            }

        }

    }

    //method to randomly generate the whole map.
    public void GenerateMap() {
        GenerateRooms();
    }

    public TETile[][] frame() {
        return map;
    }

    //for checking the points generation
    public void printPoints() {
        System.out.println("the total number of points: " + Integer.toString(PointList.size()));
        for (int i = 0; i < PointList.size(); i++) {
            System.out.print(PointList.get(i).x);
            System.out.print(' ');
            System.out.print(PointList.get(i).y);
            System.out.println(' ');
        }
    }
}

