import javafx.scene.control.Cell;
import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;
import sun.nio.cs.ext.MacArabic;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class QuadtreeMaze1 extends PApplet {
    private static final String NAME = "QuadtreeMaze1";
    private static final int CELL_SIZE = 40;
    private static final int CELL_MARGIN = (int)(CELL_SIZE / 8);

    private static final int OUTPUT_WIDTH = 600, OUTPUT_HEIGHT = 600;
    private static final int XCOUNT = OUTPUT_WIDTH / CELL_SIZE;
    private static final int YCOUNT = OUTPUT_HEIGHT / CELL_SIZE;

    private static final boolean SPARCE = false;
    private final int initialColor = color(150, 150, 150, 128);
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    private final int completedColor = color(180, 180, 255);
    private final int maxCellDimension = 6;
    private final float replicationFrequency = 0.2F;

    private Random random = new Random();
    private List<MazeCell> quadQueue;
    private Set<PathRunner> runners;
    private Stack<MazeCell> solution;

    private class PathRunner {
        PathRunner() {
            path = new Stack<>();
        }
        PathRunner(PathRunner parent) {
            this();
            parentPath = (Stack<MazeCell>)parent.path.clone();
        }

        private void place(MazeCell cell) {
            path.push(cell);
            cell.setVisited(true);
        }

        Set<PathRunner> advance() {
            Set<PathRunner> runners = new HashSet<>();
            try {
                MazeCell current = path.peek();
                // pick random next cell
                Optional<MazeCell> unvisitedNeighbor = randomUnvisitedNeighbor(current.getNeighbors());
                if (unvisitedNeighbor.isPresent()) {
                    place(unvisitedNeighbor.get());
                    drawQuad(unvisitedNeighbor.get(), occupiedColor);
                    drawQuad(current, visitedColor);
                    // TODO if at the end, set the solution
                    if (random.nextFloat() < replicationFrequency) {
                        PathRunner newRunner = new PathRunner(this);
                        newRunner.place(current);
                        runners.add(newRunner);
                    }
                    runners.add(this);
                } else {
                    current = path.pop();
                    MazeCell next = path.peek();
                    drawQuad(current, completedColor);
                    drawQuad(next, completedColor);
                    drawBridge(current, next);
                    runners.add(this);
                }
            } catch (EmptyStackException e) {
            }
            return runners;
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

        Stack<MazeCell> path;
        Stack<MazeCell> parentPath;
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        smooth(8);
        pixelDensity(1);
    }

    @Override
    public void setup() {
        runners = new HashSet<>();
        StandardQuadTree<MazeCell> quadTree = new StandardQuadTree<>(new MazeCell(0, 0, XCOUNT, YCOUNT), 0, 1, 4);
        background(220);
        //noStroke();
        stroke(0);
        strokeWeight(1.5F);
        fillQuadtree(quadTree);
        placeInitialRunner(quadTree);
        quadQueue = quadTree.getElements(quadTree.getZone());
        buildNeighbors(quadTree, quadQueue);
    }

    private void fillQuadtree(StandardQuadTree<MazeCell> quadTree) {
        int emptyCells = XCOUNT * YCOUNT;
        while (emptyCells > 0) {
            int x = random.nextInt(XCOUNT);
            int y = random.nextInt(YCOUNT);
            int w = 1 + random.nextInt(maxCellDimension);
            int h = 1 + random.nextInt(maxCellDimension);
            if (x + w > XCOUNT) {
                continue;
            }
            if (y + h > YCOUNT) {
                continue;
            }
            MazeCell q = new MazeCell(x, y, w, h);
            List<MazeCell> hits = quadTree.getElements(q);
            boolean fits = true;
            for (QuadRectangle c : hits) {
                if (q != c && ShapeUtils.rectanglesIntersect(q, c)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(q, q);
                emptyCells -= w * h;
                print("emptyCells: ", emptyCells, "\n");
            }
        }
        print("filled!\n");
    }

    private void placeInitialRunner(StandardQuadTree<MazeCell> quadTree) {
        QuadRectangle target = new QuadRectangle(0, 0, 1, 1);
        List<MazeCell> hits = quadTree.getElements(target).stream().filter(c -> c.x == 0 && c.y == 0).collect(Collectors.toList());
        PathRunner runner = new PathRunner();
        runner.place(hits.get(0));
        runners.add(runner);
    }

    @Override
    public void draw() {
        if (quadQueue.size() > 0) {
            MazeCell r = quadQueue.remove(0);
            if (SPARCE && r.width == 1 && r.height == 1) {
                return;
            }
            drawQuad(r, initialColor);
        } else if (!runners.isEmpty()) {
            advanceRunners();
        } else {
            save(NAME + ".png");
            print("Done!\n");
            noLoop();
        }

    }

    private void advanceRunners() {
        runners = runners.stream().flatMap(r -> r.advance().stream()).collect(Collectors.toSet());
    }

    private void buildNeighbors(StandardQuadTree<MazeCell> quadTree, List<MazeCell> cells) {
        cells.forEach(c -> c.setNeighbors(getNeighbors(quadTree, c)));
    }

    Set<MazeCell> getNeighbors(StandardQuadTree<MazeCell> quadTree, MazeCell q) {
        Set<MazeCell> neighbors = new HashSet<>();
        QuadRectangle target;
        List<MazeCell> hits;
        // left neighbors
        if (q.x > 0) {
            target = new QuadRectangle(q.x - 1, q.y, 1, q.height);
            hits = quadTree.getElements(target);
            for (MazeCell c : hits) {
                if (c.x + c.width == q.x) {
                    if (SPARCE && c.width == 1 && c.height == 1) {
                        continue;
                    }
                    if (c.y == q.y || c.y + c.height == q.y + q.height || between(c.y, q.y, q.height) || between(q.y, c.y, c.height)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // right neighbors
        if (q.x + q.width < XCOUNT) {
            target = new QuadRectangle(q.x + q.width, q.y, 1, q.height);
            hits = quadTree.getElements(target);
            for (MazeCell c : hits) {
                if (q.x + q.width == c.x) {
                    if (SPARCE && c.width == 1 && c.height == 1) {
                        continue;
                    }
                    if (c.y == q.y || c.y + c.height == q.y + q.height || between(c.y, q.y, q.height) || between(q.y, c.y, c.height)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // top neighbors
        if (q.y > 0) {
            target = new QuadRectangle(q.x, q.y - 1, q.width, 1);
            hits = quadTree.getElements(target);
            for (MazeCell c : hits) {
                if (c.y + c.height == q.y) {
                    if (SPARCE && c.width == 1 && c.height == 1) {
                        continue;
                    }
                    if (c.x == q.x || c.x + c.width == q.x + q.width || between(c.x, q.x, q.width) || between(q.x, c.x, c.width)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // bottom neighbors
        if (q.y + q.height < YCOUNT) {
            target = new QuadRectangle(q.x, q.y + q.height, q.width, 1);
            hits = quadTree.getElements(target);
            for (MazeCell c : hits) {
                if (q.y + q.height == c.y) {
                    if (SPARCE && c.width == 1 && c.height == 1) {
                        continue;
                    }
                    if (c.x == q.x || c.x + c.width == q.x + q.width || between(c.x, q.x, q.width) || between(q.x, c.x, c.width)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        return neighbors;
    }

    private void drawQuad(MazeCell q, int color) {
        float x = (float)q.x * CELL_SIZE + CELL_MARGIN;
        float y = (float)q.y * CELL_SIZE + CELL_MARGIN;
        float w = (float)q.width * CELL_SIZE - 2 * CELL_MARGIN;
        float h = (float)q.height * CELL_SIZE - 2 * CELL_MARGIN;

        stroke(0);
        strokeWeight(2);
        fill(color);
        if (q.width == 1 && q.height == 1) {
            ellipse(x - CELL_MARGIN + CELL_SIZE / 2, y - CELL_MARGIN + CELL_SIZE / 2, w, h);
        } else {
            rect(x, y, w, h, CELL_SIZE / 2 - CELL_MARGIN);
        }
    }

    private void drawBridge(MazeCell m0, MazeCell m1) {
        if (m0.x + m0.width == m1.x) {
            drawHorizontal(m0, m1);
        } else if (m1.x + m1.width == m0.x) {
            drawHorizontal(m1, m0);
        } else if (m0.y + m0.height == m1.y) {
            drawVertical(m0, m1);
        } else if (m1.y + m1.height == m0.y) {
            drawVertical(m1, m0);
        }
    }

    private void drawHorizontal(MazeCell left, MazeCell right) {
        int top = max((int) left.y, (int) right.y);
        int bottom = min((int) (left.y + left.height), (int) (right.y + right.height));
        int y = CELL_SIZE * (top + bottom) / 2;
        int x0 = CELL_SIZE * (int) (left.x + left.width) - CELL_MARGIN;
        int x1 = CELL_SIZE * (int) right.x + CELL_MARGIN;
        strokeCap(SQUARE);
        stroke(0);
        strokeWeight(8);
        line(x0 - 2, y, x1 + 2, y);
//        stroke(completedColor);
//        strokeWeight(4);
//        line(x0 - 2, y, x1 + 2, y);
    }

    private void drawVertical(MazeCell top, MazeCell bottom) {
        int left = max((int) top.x, (int) bottom.x);
        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
        int x = CELL_SIZE * (left + right) / 2;
        int y0 = CELL_SIZE * (int) (top.y + top.height) - CELL_MARGIN;
        int y1 = CELL_SIZE * (int) bottom.y + CELL_MARGIN;
//        noStroke();
//        fill(0);
//        rect(x - 4, y0 - 2, 8, (float)(bottom.y - y0) + (2 * CELL_MARGIN));
//        fill(completedColor);
//        rect(x - 2, y0 - 2, 4, (float)(bottom.y - y0) + (2 * CELL_MARGIN));

        strokeCap(SQUARE);
        stroke(0);
        strokeWeight(8);
        line(x, y0 - 2, x, y1 + 2);
//        stroke(completedColor);
//        strokeWeight(4);
//        line(x, y0 - 2, x, y1 + 2);
    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }
}

