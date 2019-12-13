package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Maze1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 30;
    private int halfCellSize = cellSize / 2;
    private int cellMargin = cellSize / 8;

    private int outputWidth = 1800, outputHeight = 1200;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private final boolean SPARCE = false;
    private final int initialColor = color(150, 150, 150, 128);
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    private final int completedColor = color(180, 180, 255);

    private Random random = new Random();
    private List<RoundedMazeCell> quadQueue;
    private Set<PathRunner> runners;
    private RoundedMazeCell start;
    private RoundedMazeCell finish;
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
        StandardQuadTree<RoundedMazeCell> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, xcount, ycount), 0, 1, 4);
        background(220);
        //noStroke();
        stroke(0);
        strokeWeight(1.5F);
        fillQuadtree(quadTree);
        QuadRectangle target = new QuadRectangle(0, 0, 1, 1);
        List<RoundedMazeCell> hits = quadTree.getElements(target).stream().filter(c -> c.bounds.x == 0 && c.bounds.y == 0).collect(Collectors.toList());
        start = hits.get(0);
        target = new QuadRectangle(xcount - 1, ycount - 1, 1, 1);
        hits = quadTree.getElements(target).stream().filter(c -> c.bounds.x + c.bounds.width == xcount && c.bounds.y + c.bounds.height == ycount).collect(Collectors.toList());
        finish = hits.get(0);

        placeInitialRunner(start);
        quadQueue = quadTree.getElements(quadTree.getZone());
        buildNeighbors(quadTree, quadQueue);
    }

    private void fillQuadtree(StandardQuadTree<RoundedMazeCell> quadTree) {
        int emptyCells = xcount * ycount;
        while (emptyCells > 0) {
            int x = random.nextInt(xcount);
            int y = random.nextInt(ycount);
            int maxCellDimension = 4;
            int w = 1 + random.nextInt(maxCellDimension);
            int h = 1 + random.nextInt(maxCellDimension);
            if (x + w > xcount) {
                continue;
            }
            if (y + h > ycount) {
                continue;
            }
            RoundedMazeCell newCell = new RoundedMazeCell(x, y, w, h);
            List<RoundedMazeCell> hits = quadTree.getElements(newCell.bounds);
            boolean fits = true;
            for (RoundedMazeCell c : hits) {
                if (newCell != c && ShapeUtils.rectanglesIntersect(newCell.bounds, c.bounds)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(newCell.bounds, newCell);
                emptyCells -= w * h;
                print("emptyCells: ", emptyCells, "\n");
            }
        }
        print("filled!\n");
    }

    private void placeInitialRunner(MazeCell start) {
        PathRunner runner = new PathRunner(start);
        runner.place(start);
        start.setVisited();
        runners.add(runner);
    }

    @Override
    public void draw() {
        if (quadQueue.size() > 0) {
            RoundedMazeCell r = quadQueue.remove(0);
            if (SPARCE && r.bounds.width == 1 && r.bounds.height == 1) {
                return;
            }
            r.draw(initialColor, true);
        } else if (!runners.isEmpty()) {
            advanceRunners();
        } else {
            //drawConnections(quadTree);
            drawStartAndFinish();
        }
    }

    private void drawConnections(StandardQuadTree<RoundedMazeCell> quadTree) {
        for (RoundedMazeCell cell: quadTree.getElements(quadTree.getZone())) {
            if (cell.getParent() != null) {
                RoundedMazeCell parent = (RoundedMazeCell) cell.getParent();
                drawBridge(cell.bounds, parent.bounds);
            }
        }
    }

    private void drawStartAndFinish() {
        start.drawOccupied();
        finish.drawOccupied();
        textSize(20);
        textAlign(CENTER);
        fill(0);
        text('S', (float)start.bounds.x * cellSize + halfCellSize, (float)start.bounds.y * cellSize + halfCellSize + 4);
        text('F', (float)finish.bounds.x * cellSize + halfCellSize, (float)finish.bounds.y * cellSize + halfCellSize + 4);
    }

    private void toggleSolution() {
        solutionVisible = ! solutionVisible;
        MazeCell cell = finish;
        while (cell != null) {
            cell.draw(solutionVisible ? occupiedColor : completedColor, true);
            cell = cell.getParent();
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            toggleSolution();
        } else if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        }
    }

    private void drawQuad(QuadRectangle q, int color) {
        float x = (float)q.x * cellSize + cellMargin;
        float y = (float)q.y * cellSize + cellMargin;
        float w = (float)q.width * cellSize - 2 * cellMargin;
        float h = (float)q.height * cellSize - 2 * cellMargin;

        stroke(0);
        strokeWeight(2);
        fill(color);
        if (q.width == 1 && q.height == 1) {
            ellipse(x - cellMargin + halfCellSize, y - cellMargin + halfCellSize, w, h);
        } else {
            rect(x, y, w, h, halfCellSize - cellMargin);
        }
    }

    private void drawBridge(QuadRectangle m0, QuadRectangle m1) {
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

    private void drawHorizontal(QuadRectangle left, QuadRectangle right) {
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

    private void drawVertical(QuadRectangle top, QuadRectangle bottom) {
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
        runners = runners.stream().flatMap(r -> r.advance().stream()).collect(Collectors.toSet());
    }

    private void buildNeighbors(StandardQuadTree<RoundedMazeCell> quadTree, List<RoundedMazeCell> cells) {
        cells.forEach(c -> c.setNeighbors(getNeighbors(quadTree, c)));
    }

    private Set<MazeCell> getNeighbors(StandardQuadTree<RoundedMazeCell> quadTree, RoundedMazeCell q) {
        Set<MazeCell> neighbors = new HashSet<>();
        QuadRectangle target;
        List<RoundedMazeCell> hits;
        // left neighbors
        if (q.bounds.x > 0) {
            target = new QuadRectangle(q.bounds.x - 1, q.bounds.y, 1, q.bounds.height);
            hits = quadTree.getElements(target);
            for (RoundedMazeCell c : hits) {
                if (c.bounds.x + c.bounds.width == q.bounds.x) {
                    if (SPARCE && c.bounds.width == 1 && c.bounds.height == 1) {
                        continue;
                    }
                    if (c.bounds.y == q.bounds.y || c.bounds.y + c.bounds.height == q.bounds.y + q.bounds.height || between(c.bounds.y, q.bounds.y, q.bounds.height) || between(q.bounds.y, c.bounds.y, c.bounds.height)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // right neighbors
        if (q.bounds.x + q.bounds.width < xcount) {
            target = new QuadRectangle(q.bounds.x + q.bounds.width, q.bounds.y, 1, q.bounds.height);
            hits = quadTree.getElements(target);
            for (RoundedMazeCell c : hits) {
                if (q.bounds.x + q.bounds.width == c.bounds.x) {
                    if (SPARCE && c.bounds.width == 1 && c.bounds.height == 1) {
                        continue;
                    }
                    if (c.bounds.y == q.bounds.y || c.bounds.y + c.bounds.height == q.bounds.y + q.bounds.height || between(c.bounds.y, q.bounds.y, q.bounds.height) || between(q.bounds.y, c.bounds.y, c.bounds.height)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // top neighbors
        if (q.bounds.y > 0) {
            target = new QuadRectangle(q.bounds.x, q.bounds.y - 1, q.bounds.width, 1);
            hits = quadTree.getElements(target);
            for (RoundedMazeCell c : hits) {
                if (c.bounds.y + c.bounds.height == q.bounds.y) {
                    if (SPARCE && c.bounds.width == 1 && c.bounds.height == 1) {
                        continue;
                    }
                    if (c.bounds.x == q.bounds.x || c.bounds.x + c.bounds.width == q.bounds.x + q.bounds.width || between(c.bounds.x, q.bounds.x, q.bounds.width) || between(q.bounds.x, c.bounds.x, c.bounds.width)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        // bottom neighbors
        if (q.bounds.y + q.bounds.height < ycount) {
            target = new QuadRectangle(q.bounds.x, q.bounds.y + q.bounds.height, q.bounds.width, 1);
            hits = quadTree.getElements(target);
            for (RoundedMazeCell c : hits) {
                if (q.bounds.y + q.bounds.height == c.bounds.y) {
                    if (SPARCE && c.bounds.width == 1 && c.bounds.height == 1) {
                        continue;
                    }
                    if (c.bounds.x == q.bounds.x || c.bounds.x + c.bounds.width == q.bounds.x + q.bounds.width || between(c.bounds.x, q.bounds.x, q.bounds.width) || between(q.bounds.x, c.bounds.x, c.bounds.width)) {
                        neighbors.add(c);
                    }
                }
            }
        }
        return neighbors;
    }

    class RoundedMazeCell extends MazeCell {
        private QuadRectangle bounds;

        RoundedMazeCell(int x, int y, int width, int height) {
            super();
            bounds = new QuadRectangle(x, y, width, height);
        }

        public boolean equals(MazeCell other) {
            RoundedMazeCell rc = (RoundedMazeCell) other;
            return rc.bounds.x == this.bounds.x && rc.bounds.y == this.bounds.y;
        }

        public void drawOccupied() {
            this.draw(occupiedColor, true);
        }

        public void drawVisited() {
            this.draw(visitedColor, true);
        }

        public void drawCompleted() {
            this.draw(completedColor, true);
        }

        public void draw(int color, boolean drawBorder) {
            drawQuad(this.bounds, color);
        }

        public void drawConnection(MazeCell other) {
            RoundedMazeCell o = (RoundedMazeCell)other;
            QuadRectangle m0 = this.bounds;
            QuadRectangle m1 = o.bounds;
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
    }
}

