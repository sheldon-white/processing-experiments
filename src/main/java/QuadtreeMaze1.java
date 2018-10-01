import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.util.*;
import java.util.stream.Collectors;

public class QuadtreeMaze1 extends PApplet {
    private static final String NAME = "QuadtreeMaze1";
    private int cellSize = 40;
    private int cellMargin = (int)(cellSize / 8);

    private int outputWidth = 2000, outputHeight = 1000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private final boolean SPARCE = false;
    private final int initialColor = color(150, 150, 150, 128);
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    private final int completedColor = color(180, 180, 255);

    private Random random = new Random();
    private List<MazeCell> quadQueue;
    private Set<PathRunner> runners;
    private MazeCell start;
    private MazeCell finish;
    private boolean solutionVisible = false;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        smooth(8);
        pixelDensity(1);
    }

    @Override
    public void setup() {
        runners = new HashSet<>();
        StandardQuadTree<MazeCell> quadTree = new StandardQuadTree<>(new MazeCell(0, 0, xcount, ycount), 0, 1, 4);
        background(220);
        //noStroke();
        stroke(0);
        strokeWeight(1.5F);
        fillQuadtree(quadTree);
        QuadRectangle target = new QuadRectangle(0, 0, 1, 1);
        List<MazeCell> hits = quadTree.getElements(target).stream().filter(c -> c.x == 0 && c.y == 0).collect(Collectors.toList());
        start = hits.get(0);
        target = new QuadRectangle(xcount - 1, ycount - 1, 1, 1);
        hits = quadTree.getElements(target).stream().filter(c -> c.x + c.width == xcount && c.y + c.height == ycount).collect(Collectors.toList());
        finish = hits.get(0);

        placeInitialRunner(start);
        quadQueue = quadTree.getElements(quadTree.getZone());
        buildNeighbors(quadTree, quadQueue);
    }

    private void fillQuadtree(StandardQuadTree<MazeCell> quadTree) {
        int emptyCells = xcount * ycount;
        while (emptyCells > 0) {
            int x = random.nextInt(xcount);
            int y = random.nextInt(ycount);
            int maxCellDimension = 6;
            int w = 1 + random.nextInt(maxCellDimension);
            int h = 1 + random.nextInt(maxCellDimension);
            if (x + w > xcount) {
                continue;
            }
            if (y + h > ycount) {
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

    private void placeInitialRunner(MazeCell start) {
        PathRunner runner = new PathRunner(start);
        runner.place(start);
        start.setVisited(true);
        runners.add(runner);
    }

    private Set<PathRunner> advance(PathRunner pathRunner) {
        Set<PathRunner> runners = new HashSet<>();
        MazeCell current = pathRunner.getCurrent();
        // pick random next cell
        Optional<MazeCell> omc = pathRunner.randomUnvisitedNeighbor(current.getNeighbors());
        if (omc.isPresent()) {
            MazeCell unvisitedNeighbor = omc.get();
            pathRunner.place(unvisitedNeighbor);
            unvisitedNeighbor.setVisited(true);
            unvisitedNeighbor.setParent(current);
            drawQuad(unvisitedNeighbor, occupiedColor);
            drawQuad(current, visitedColor);
            float replicationFrequency = 0.2F;
            if (random.nextFloat() < replicationFrequency) {
                PathRunner newRunner = new PathRunner(pathRunner);
                newRunner.place(current);
                current.setVisited(true);
                runners.add(newRunner);
            }
            runners.add(pathRunner);
        } else {
            current = pathRunner.getCurrent();
            MazeCell next = current.getParent();
            drawQuad(current, completedColor);
            if (next != null) {
                drawQuad(next, completedColor);
                drawBridge(current, next);
                pathRunner.place(next);
                if (next.x != pathRunner.start.x || next.y != pathRunner.start.y) {
                    runners.add(pathRunner);
                }
            }
        }

        return runners;
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
            drawStartAndFinish();
        }
    }

    private void drawStartAndFinish() {
        drawQuad(start, occupiedColor);
        drawQuad(finish, occupiedColor);
        textSize(20);
        textAlign(CENTER);
        fill(0);
        text('S', (float)start.x * cellSize + cellSize / 2, (float)start.y * cellSize + cellSize / 2 + 4);
        text('F', (float)finish.x * cellSize + cellSize / 2, (float)finish.y * cellSize + cellSize / 2 + 4);
    }

    private void toggleSolution() {
        solutionVisible = ! solutionVisible;
        MazeCell cell = finish;
        while (cell != null) {
            drawQuad(cell, solutionVisible ? occupiedColor : completedColor);
            cell = cell.getParent();
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            toggleSolution();
        } else if (key == 's' || key == 'S') {
            save(NAME + ".png");
        }
    }

    private void drawQuad(MazeCell q, int color) {
        float x = (float)q.x * cellSize + cellMargin;
        float y = (float)q.y * cellSize + cellMargin;
        float w = (float)q.width * cellSize - 2 * cellMargin;
        float h = (float)q.height * cellSize - 2 * cellMargin;

        stroke(0);
        strokeWeight(2);
        fill(color);
        if (q.width == 1 && q.height == 1) {
            ellipse(x - cellMargin + cellSize / 2, y - cellMargin + cellSize / 2, w, h);
        } else {
            rect(x, y, w, h, cellSize / 2 - cellMargin);
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
        int y = cellSize * (top + bottom) / 2;
        int x0 = cellSize * (int) (left.x + left.width) - cellMargin;
        int x1 = cellSize * (int) right.x + cellMargin;
        strokeCap(SQUARE);
        stroke(0);
        strokeWeight(8);
        line(x0 - 2, y, x1 + 2, y);
    }

    private void drawVertical(MazeCell top, MazeCell bottom) {
        int left = max((int) top.x, (int) bottom.x);
        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
        int x = cellSize * (left + right) / 2;
        int y0 = cellSize * (int) (top.y + top.height) - cellMargin;
        int y1 = cellSize * (int) bottom.y + cellMargin;

        strokeCap(SQUARE);
        stroke(0);
        strokeWeight(8);
        line(x, y0 - 2, x, y1 + 2);
    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }

    private void advanceRunners() {
        runners = runners.stream().flatMap(r -> advance(r).stream()).collect(Collectors.toSet());
    }

    private void buildNeighbors(StandardQuadTree<MazeCell> quadTree, List<MazeCell> cells) {
        cells.forEach(c -> c.setNeighbors(getNeighbors(quadTree, c)));
    }

    private Set<MazeCell> getNeighbors(StandardQuadTree<MazeCell> quadTree, MazeCell q) {
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
        if (q.x + q.width < xcount) {
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
        if (q.y + q.height < ycount) {
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
}

