import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.util.HashSet;
import java.util.Set;

public class MazeCell extends QuadRectangle {
    private Set<MazeCell> neighbors;
    private boolean visited;

    MazeCell(int x, int y, int width, int height) {
        super(x, y, width, height);
        neighbors = new HashSet<>();
    }

    public Set<MazeCell> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<MazeCell> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
