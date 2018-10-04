import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.util.HashSet;
import java.util.Set;

public abstract class MazeCell {
    private Set<MazeCell> neighbors;
    private boolean visited;
    private MazeCell parent;

    MazeCell() {
        neighbors = new HashSet<>();
    }

    public abstract boolean equals(MazeCell other);

    public abstract void drawOccupied();
    public abstract void drawVisited();
    public abstract void drawCompleted();
    public abstract void draw(int color, boolean drawBorder);
    public abstract void drawConnection(MazeCell other);

    Set<MazeCell> getNeighbors() {
        return neighbors;
    }

    void setNeighbors(Set<MazeCell> neighbors) {
        this.neighbors = neighbors;
    }

    void addNeighbor(MazeCell neighbor) {
        if (this != neighbor) {
            this.neighbors.add(neighbor);
        }
    }

    boolean isVisited() {
        return visited;
    }

    void setVisited() {
        this.visited = true;
    }

    MazeCell getParent() {
        return parent;
    }

    void setParent(MazeCell parent) {
        this.parent = parent;
    }
}
