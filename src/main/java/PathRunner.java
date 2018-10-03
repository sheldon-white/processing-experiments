import java.util.*;
import java.util.stream.Collectors;

class PathRunner {
    static Random random = new Random();
    private MazeCell start;
    private MazeCell current;

    PathRunner(MazeCell start) {
        this.start = start;
    }

    private PathRunner(PathRunner parent) {
        start = current = parent.current;
    }

    void place(MazeCell cell) {
        current = cell;
    }

    private MazeCell getCurrent() {
        return current;
    }

    Set<PathRunner> advance() {
        Set<PathRunner> runners = new HashSet<>();
        MazeCell current = getCurrent();
        // pick random next cell
        Optional<MazeCell> omc = randomUnvisitedNeighbor(current.getNeighbors());
        if (omc.isPresent()) {
            MazeCell unvisitedNeighbor = omc.get();
            this.place(unvisitedNeighbor);
            unvisitedNeighbor.setVisited();
            unvisitedNeighbor.setParent(current);
            unvisitedNeighbor.drawOccupied();
            current.drawVisited();
            float replicationFrequency = 0.2F;
            if (random.nextFloat() < replicationFrequency) {
                PathRunner newRunner = new PathRunner(this);
                newRunner.place(current);
                current.setVisited();
                runners.add(newRunner);
            }
            runners.add(this);
        } else {
            MazeCell next = current.getParent();
            current.drawCompleted();
            if (next != null) {
                next.drawCompleted();
                place(next);
                if (!next.equals(start)) {
                    runners.add(this);
                }
            }
        }

        return runners;
    }

    private Optional<MazeCell> randomUnvisitedNeighbor(Set<MazeCell> neighbors) {
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
}
