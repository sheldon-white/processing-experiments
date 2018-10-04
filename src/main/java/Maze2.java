import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;

import java.util.*;
import java.util.stream.Collectors;

public class Maze2 extends PApplet {
    private static final String NAME = "Maze2";
    private int cellSize = 40;
    private int halfCellSize = cellSize / 2;
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    //private final int completedColor = color(180, 180, 255);
    private int outputWidth = 800, outputHeight = 600;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private QuadState[][] quadStates;
    private Set<PathRunner> runners;
    private Random r = new Random();
    private static PApplet context;

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
        placeInitialRunner(start);
    }

    private void placeInitialRunner(MazeCell start) {
        PathRunner runner = new PathRunner(start);
        runner.place(start);
        start.setVisited();
        runners.add(runner);
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
//                        drawWalls(x, y);
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
            save(NAME + ".png");
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
        qs.wallState = r.nextInt(8);
        qs.xIndex = xIndex;
        qs.yIndex = yIndex;

        switch (qs.wallState) {
            case 0: {
                SquareMazeCell s0 = new SquareMazeCell(x, y, cellSize, cellSize, qs);
                qs.top = qs.left = qs.bottom = qs.right = s0;
                s0.draw(color, true);
                break;
            }
            case 1: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dl), qs,CellType.UL);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dl, dr), qs,CellType.DR);
                qs.top = qs.left = t0;
                qs.bottom = qs.right = t1;
                t0.draw(color, true);
                t1.draw(color, true);
                break;
            }
            case 2: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dr), qs,CellType.UR);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, dr), qs,CellType.DL);
                qs.top = qs.right = t0;
                qs.bottom = qs.left = t1;
                t0.draw(color, true);
                t1.draw(color, true);
                break;
            }
            case 3: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,CellType.U);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,CellType.L);
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(ur, dl, dr), qs,CellType.DR);
                qs.top = t0;
                qs.left = t1;
                qs.right = qs.bottom = t2;
                t0.draw(color, true);
                t1.draw(color, true);
                t2.draw(color, true);
                break;
            }
            case 4: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dl), qs,CellType.UL);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,CellType.R);
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,CellType.D);
                qs.top = qs.left = t0;
                qs.right = t1;
                qs.bottom = t2;
                t0.draw(color, true);
                t1.draw(color, true);
                t2.draw(color, true);
                break;
            }
            case 5: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,CellType.U);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,CellType.R);
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(ul, dl, dr), qs,CellType.DL);
                qs.top = t0;
                qs.right = t1;
                qs.bottom = qs.left = t2;
                t0.draw(color, true);
                t1.draw(color, true);
                t2.draw(color, true);
                break;
            }
            case 6: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dr), qs,CellType.UR);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,CellType.L);
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,CellType.D);
                qs.top = qs.right = t0;
                qs.left = t1;
                qs.bottom = t2;
                t0.draw(color, true);
                t1.draw(color, true);
                t2.draw(color, true);
                break;
            }
            case 7: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,CellType.U);
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,CellType.L);
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,CellType.D);
                TriangularMazeCell t3 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,CellType.R);
                qs.top = t0;
                qs.left = t1;
                qs.bottom = t2;
                qs.right = t3;
                t0.draw(color, true);
                t1.draw(color, true);
                t2.draw(color, true);
                t3.draw(color, true);
                break;
            }
        }
    }

    private void drawTriangle(Triangle t, int color, boolean drawBorder) {
        fill(color);
        if (drawBorder) {
            stroke(0);
            strokeWeight(2);
        }
        context.triangle(t.p0.x, t.p0.y, t.p1.x, t.p1.y, t.p2.x, t.p2.y);
    }

    enum CellType {
        LRUD,
        U,
        D,
        L,
        R,
        UL,
        UR,
        DL,
        DR
    }

    public abstract class SignedMazeCell extends MazeCell {
        private CellType cellType;
        private QuadState qs;
        private Map<SignedMazeCell, Integer> neighbors;
        public SignedMazeCell(CellType cellType, QuadState qs) {
            super();
            this.cellType = cellType;
            this.qs = qs;
            this.neighbors = new HashMap<>();
        }

        public void addNeighbor(SignedMazeCell neighbor, int position) {
            neighbors.put(neighbor, position);
        }

        public void drawConnection(MazeCell other) {
            SignedMazeCell o = (SignedMazeCell)other;
            strokeWeight(3);
            stroke(255, 0, 0);
            IPoint p0 = center();
            IPoint p1 = o.center();
            line(p0.x, p0.y, p1.x, p1.y);
        }

        public void drawOccupied() {
        }
        public void drawVisited() {
        }
        public void drawCompleted() {
        }

        public abstract IPoint center();
    }

    class TriangularMazeCell extends SignedMazeCell {
        private Triangle t;

        TriangularMazeCell(Triangle t, QuadState qs, CellType cellType) {
            super(cellType, qs);
            this.t = t;
        }

        public boolean equals(MazeCell other) {
            return this == other;
        }

//        public void drawOccupied() {
//            this.draw(occupiedColor, true);
//        }
//
//        public void drawVisited() {
//            this.draw(visitedColor, true);
//        }
//
//        public void drawCompleted() {}

        public void draw(int color, boolean drawBorder) {
            drawTriangle(t, color, drawBorder);
        }

        public IPoint center() {
            return t.centroidCenter();
        }
    }

    class SquareMazeCell extends SignedMazeCell {
        private int x, y, width, height;

        SquareMazeCell(int x, int y, int width, int height, QuadState qs) {
            super(CellType.LRUD, qs);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean equals(MazeCell other) {
            SignedMazeCell signedThis = this;
            SignedMazeCell signedOther = (SignedMazeCell)other;
            return signedThis.qs == signedOther.qs && signedThis.cellType == signedOther.cellType;
        }

//        public void drawOccupied() {
//            this.draw(occupiedColor, true);
//        }
//
//        public void drawVisited() {
//            this.draw(visitedColor, true);
//        }
//
//        public void drawCompleted() {}

        public void draw(int color, boolean drawBorder) {
            fill(color);
            if (drawBorder) {
                stroke(0);
                strokeWeight(2);
            }
            rect(x, y, width, height);
        }

        public IPoint center() {
            return new IPoint(x + width / 2, y + height/2);
        }
    }

    class QuadState {
        int xIndex;
        int yIndex;
        int wallState;
        MazeCell top;
        MazeCell bottom;
        MazeCell left;
        MazeCell right;
    }
}

