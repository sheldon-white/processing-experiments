import java.util.*;
import java.util.stream.Collectors;

class PathRunner {
    static Random random = new Random();
    MazeCell start;
    MazeCell current;

    PathRunner(MazeCell start) {
        this.start = start;
    }

    PathRunner(PathRunner parent) {
        start = current = parent.current;
    }

    void place(MazeCell cell) {
        current = cell;
    }

    Optional<MazeCell> randomUnvisitedNeighbor(Set<MazeCell> neighbors) {
        Set<MazeCell> unvisitedNeighbors = neighbors.stream().filter(c -> !c.isVisited()).collect(Collectors.toSet());
        if (unvisitedNeighbors.size() > 0) {
            int index = random.nextInt(unvisitedNeighbors.size());
            Iterator<MazeCell> iter = unvisitedNeighbors.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }
            return Optional.of(iter.next());
        }
        return Optional.empty();
    }

    public MazeCell getStart() {
        return start;
    }

    public void setStart(MazeCell start) {
        this.start = start;
    }

    public MazeCell getCurrent() {
        return current;
    }

    public void setCurrent(MazeCell current) {
        this.current = current;
    }
}
