import java.util.HashSet;
import java.util.Set;

//public class MazeCell extends QuadRectangle {
public abstract class MazeCellBase {
    private Set<MazeCellBase> neighbors;
    private boolean visited;
    private MazeCellBase parent;

  //  MazeCell(int x, int y, int width, int height) {
    MazeCellBase() {
       // super(x, y, width, height);
        neighbors = new HashSet<>();
    }

    public Set<MazeCellBase> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<MazeCellBase> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public MazeCellBase getParent() {
        return parent;
    }

    public void setParent(MazeCellBase parent) {
        this.parent = parent;
    }

    public abstract void draw(int color);

    public abstract void drawPath(MazeCellBase next, int color);

    public abstract boolean equals(MazeCellBase other);
}
