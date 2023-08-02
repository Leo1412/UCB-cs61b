package byog.Core;
import byog.TileEngine.TETile;

import java.util.ArrayList;


//keep track of all the rooms
public class RoomManager {
    private ArrayList<Room> RoomList = new ArrayList<>();
    //the width and height of the world.
    private int x;
    private int y;
    private int size;

    public RoomManager(int width, int height) {
        x = width;
        y = height;
        size = 0;
    }

    //add a new room to the list
    public void AddRoom(Room NewRoom) {
        RoomList.add(NewRoom);
        size += 1;
    }

    //determine whether the newly added room is overlapping
    public boolean isOverlap(Room NewRoom) {
        for (int i = 0; i < size; i++) {
            Room RoomToCheck = RoomList.get(i);
            //check whether RoomToCheck overlaps with NewRoom
            int xExtreme1 = NewRoom.p.x + NewRoom.w + 1;
            int yExtreme1 = NewRoom.p.y + NewRoom.h + 1;
            //NewRoom x range: [NewRoom.p.x, xExtreme1]
            //NewRoom y range: [NewRoom.p.y, yExtreme1]
            int xExtreme2 = RoomToCheck.p.x + RoomToCheck.w + 1;
            int yExtreme2 = RoomToCheck.p.y + RoomToCheck.h + 1;
            //RoomToCheck x range: [RoomToCheck.p.x, xExtreme2]
            //RoomToCheck y range: [RoomToCheck.p.y, yExtreme2]
            if ((xExtreme2 < NewRoom.p.x) || (xExtreme1 < RoomToCheck.p.x)) {
                continue;
            } else if((yExtreme2 < NewRoom.p.y) || (yExtreme1 < RoomToCheck.p.y)) {
                continue;
            }
            //if overlaps in both x and y, then two overlaps
            return true;
        }
        return false;
    }

    //determine whether the newly added room is out of bound of the world
    public boolean isOutOfBound(Room NewRoom) {
        int xExtreme = NewRoom.p.x + NewRoom.w + 1;
        int yExtreme = NewRoom.p.y + NewRoom.h + 1;
        if ((xExtreme >= x) || (yExtreme >= y)) {
            return true;
        }
        return false;
    }
}
