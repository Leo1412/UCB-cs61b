package lab11.graphs;
import java.util.*;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private int t;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> queue = new LinkedList<>();
        int currentNode = s;
        marked[s] = true;
        announce();
        queue.add(s);

        while (true) {
            //remove the currentNode
            currentNode = queue.remove();
            for (int w : maze.adj(currentNode)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = currentNode;
                    announce();
                    distTo[w] = distTo[currentNode] + 1;
                    queue.add(w);
                    if (w == t) {
                        return; //target found.
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

