package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Maze2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 40;
    private int halfCellSize = cellSize / 2;
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    private final int completedColor = color(180, 180, 255);
    private int outputWidth = 200, outputHeight = 200;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private QuadState[][] quadStates;
    private Set<PathRunner> runners;
    private Random r = new Random();
    private static PApplet context;

    class QuadState {
        int xIndex;
        int yIndex;
        int wallState;
        MazeCell top;
        MazeCell bottom;
        MazeCell left;
        MazeCell right;
    }

    enum CellType {
        U(0),
        R(1),
        D(2),
        L(3);

        int index;

        static Map valueMap = EnumSet.allOf(CellType.class).stream().collect(Collectors.toMap(CellType::getIndex, Function.identity()));

        CellType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        boolean clockwiseFrom(CellType neighbor) {
            return this.index == (neighbor.index + 1) % 4;
        }
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        context = this;
        runners = new HashSet<>();

        quadStates = new QuadState[xcount][ycount];
        for (int x = 0; x < xcount; x++) {
            for (int y = 0; y < ycount; y++) {
                tesselateSquare(x, y);
            }
        }
        for (int x = 0; x < xcount; x++) {
            for (int y = 0; y < ycount; y++) {
                calculateNeighbors(x, y);
            }
        }
        MazeCell start = quadStates[0][0].left;
        //placeInitialRunner(start);
        testConnections();
    }

    private void placeInitialRunner(MazeCell start) {
        PathRunner runner = new PathRunner(start);
        runner.place(start);
        start.setVisited();
        runners.add(runner);
    }

    private void testConnections() {
        for (int x = 0; x < xcount; x++) {
            for (int y = 0; y < ycount; y++) {
            }
        }

    }

    @Override
    public void draw() {
        int ctr = 10;
        while (ctr-- != 0) {
            print("runners: ", runners.size(), "\n");
            if (!runners.isEmpty()) {
                advanceRunners();
            } else {
//                for (int x = 0; x < xcount; x++) {
//                    for (int y = 0; y < ycount; y++) {
//                        QuadState qs = quadStates[x][y];
//                        qs.left.drawCompleted();
//                        qs.right.drawCompleted();
//                        qs.top.drawCompleted();
//                        qs.bottom.drawCompleted();
//                    }
//                }
                noLoop();
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == ' ') {
            //toggleSolution();
        } else if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        }
    }
    private void advanceRunners() {
        runners = runners.stream().flatMap(r -> r.advance().stream()).collect(Collectors.toSet());
    }

    private void calculateNeighbors(int xIndex, int yIndex) {
        QuadState qs = quadStates[xIndex][yIndex];
        qs.left.addNeighbor(qs.top);
        qs.left.addNeighbor(qs.bottom);
        qs.right.addNeighbor(qs.top);
        qs.right.addNeighbor(qs.bottom);
        qs.top.addNeighbor(qs.left);
        qs.top.addNeighbor(qs.right);
        qs.bottom.addNeighbor(qs.left);
        qs.bottom.addNeighbor(qs.right);

        if (xIndex > 0) {
            QuadState leftState = quadStates[xIndex - 1][yIndex];
            qs.left.addNeighbor(leftState.right);
        }
        if (xIndex < xcount - 1) {
            QuadState rightState = quadStates[xIndex + 1][yIndex];
            qs.right.addNeighbor(rightState.left);
        }
        if (yIndex > 0) {
            QuadState topState = quadStates[xIndex][yIndex - 1];
            qs.top.addNeighbor(topState.bottom);
        }
        if (yIndex < ycount - 1) {
            QuadState bottomState = quadStates[xIndex][yIndex + 1];
            qs.bottom.addNeighbor(bottomState.top);
        }
    }

    private void tesselateSquare(int xIndex, int yIndex) {
        int x = xIndex * cellSize;
        int y = yIndex * cellSize;
        int color = color(220);
        IPoint ul = new IPoint(x, y);
        IPoint ur = new IPoint(x + cellSize, y);
        IPoint dl = new IPoint(x, y + cellSize);
        IPoint dr = new IPoint(x + cellSize, y + cellSize);
        IPoint cen = new IPoint(x + halfCellSize, y + halfCellSize);
        QuadState qs = new QuadState();
        quadStates[xIndex][yIndex] = qs;
        //qs.wallState = r.nextInt(8);
        qs.wallState = 7;
        qs.xIndex = xIndex;
        qs.yIndex = yIndex;

        TriangularMazeCell u = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,CellType.U);
        TriangularMazeCell r = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,CellType.R);
        TriangularMazeCell d = new TriangularMazeCell(new Triangle(dr, dl, cen), qs,CellType.D);
        TriangularMazeCell l = new TriangularMazeCell(new Triangle(dl, ul, cen), qs,CellType.L);
        qs.top = u;
        qs.left = l;
        qs.bottom = d;
        qs.right = r;
        switch (qs.wallState) {
            case 0: {
                // square
                u.removeWall(1);
                u.removeWall(2);
                r.removeWall(1);
                r.removeWall(2);
                d.removeWall(1);
                d.removeWall(2);
                l.removeWall(1);
                l.removeWall(2);
                break;
            }
            case 1: {
                // UL, DR
                u.removeWall(2);
                l.removeWall(1);
                d.removeWall(2);
                r.removeWall(1);
                break;
            }
            case 2: {
                // UR, DL
                u.removeWall(1);
                r.removeWall(2);
                d.removeWall(1);
                l.removeWall(2);
                break;
            }
            case 3: {
                // U, L, DR
                r.removeWall(1);
                d.removeWall(2);
                break;
            }
            case 4: {
                // UL, D, R
                u.removeWall(2);
                l.removeWall(1);
                break;
            }
            case 5: {
                // U, R, DL
                d.removeWall(1);
                l.removeWall(2);
                break;
            }
            case 6: {
                // UR, D, L
                u.removeWall(1);
                r.removeWall(2);
                break;
            }
            case 7: {
                // U, R, D, L
                break;
            }
        }

        u.draw(color, true);
        r.draw(color, true);
        d.draw(color, true);
        l.draw(color, true);
    }

    private void drawTriangle(Triangle t, int color, boolean drawBorder) {
        noStroke();
        fill(color);
        context.triangle(t.p0.x, t.p0.y, t.p1.x, t.p1.y, t.p2.x, t.p2.y);
        if (drawBorder) {
            stroke(0);
            strokeWeight(2);
        }
    }

    class TriangularMazeCell extends MazeCell {
        private CellType cellType;
        private QuadState qs;
        Map<Integer, Boolean> walls;
        private Triangle t;

        TriangularMazeCell(Triangle t, QuadState qs, CellType cellType) {
            this.cellType = cellType;
            this.qs = qs;
            this.walls = new HashMap<>();
            this.t = t;
            this.walls = IntStream.range(0, 3).boxed().collect(Collectors.toMap(Function.identity(), b -> Boolean.TRUE));
        }

        public boolean equals(MazeCell other) {
            return this == other;
        }

        public void drawConnection(MazeCell other) {
            TriangularMazeCell o = (TriangularMazeCell)other;
            if (this.qs.xIndex != o.qs.xIndex || this.qs.yIndex != o.qs.yIndex) {
                this.removeWall(0);
                o.removeWall(0);
            } else {
                if (this.cellType.clockwiseFrom(o.cellType)) {
                    this.removeWall(2);
                    o.removeWall(1);
                } else {
                    this.removeWall(1);
                    o.removeWall(2);
                }
            }
            this.draw(visitedColor, true);
            other.draw(visitedColor, true);
        }

        public void removeWall(int position) {
            walls.put(position, false);
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
            drawTriangle(t, color, false);
            fill(color);
            if (drawBorder) {
                stroke(0);
                strokeWeight(3);
                for (int i = 0; i < 3; i++) {
                    if (walls.get(i)) {
                        switch (i) {
                            case 0:
                                line(t.p0.x, t.p0.y, t.p1.x, t.p1.y);
                                break;
                            case 1:
                                line(t.p1.x, t.p1.y, t.p2.x, t.p2.y);
                                break;
                            case 2:
                                line(t.p0.x, t.p0.y, t.p2.x, t.p2.y);
                                break;
                        }
                    }
                }
            }
        }
    }
}

