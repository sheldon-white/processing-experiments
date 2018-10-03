import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Maze2 extends PApplet {
    private static final String NAME = "Maze2";
    private int cellSize = 25;
    private int halfCellSize = cellSize / 2;
    private final int visitedColor = color(220, 200, 200);
    private final int occupiedColor = color(180, 255, 180);
    private final int completedColor = color(180, 180, 255);
    private int outputWidth = 2000, outputHeight = 1500;
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
        if (!runners.isEmpty()) {
            advanceRunners();
        } else {
            print("runners done!\n");
            for (int x = 0; x < xcount; x++) {
                for (int y = 0; y < ycount; y++) {
                    drawWalls(x, y);
                }
            }
            noLoop();
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
                s0.draw(randomColor(), true);
                break;
            }
            case 1: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dl), qs,"ul");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dl, dr), qs,"rd");
                qs.top = qs.left = t0;
                qs.bottom = qs.right = t1;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                break;
            }
            case 2: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dr), qs,"ur");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, dr), qs,"dl");
                qs.top = qs.right = t0;
                qs.bottom = qs.left = t1;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                break;
            }
            case 3: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,"u");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,"l");
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(ur, dl, dr), qs,"dr");
                qs.top = t0;
                qs.left = t1;
                qs.right = qs.bottom = t2;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                t2.draw(randomColor(), true);
                break;
            }
            case 4: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dl), qs,"ul");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,"r");
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,"d");
                qs.top = qs.left = t0;
                qs.right = t1;
                qs.bottom = t2;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                t2.draw(randomColor(), true);
                break;
            }
            case 5: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,"u");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,"r");
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(ul, dl, dr), qs,"dl");
                qs.top = t0;
                qs.right = t1;
                qs.bottom = qs.left = t2;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                t2.draw(randomColor(), true);
                break;
            }
            case 6: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, dr), qs,"ur");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,"l");
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,"d");
                qs.top = qs.right = t0;
                qs.left = t1;
                qs.bottom = t2;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                t2.draw(randomColor(), true);
                break;
            }
            case 7: {
                TriangularMazeCell t0 = new TriangularMazeCell(new Triangle(ul, ur, cen), qs,"u");
                TriangularMazeCell t1 = new TriangularMazeCell(new Triangle(ul, dl, cen), qs,"l");
                TriangularMazeCell t2 = new TriangularMazeCell(new Triangle(dl, dr, cen), qs,"d");
                TriangularMazeCell t3 = new TriangularMazeCell(new Triangle(ur, dr, cen), qs,"r");
                qs.top = t0;
                qs.left = t1;
                qs.bottom = t2;
                qs.right = t3;
                t0.draw(randomColor(), true);
                t1.draw(randomColor(), true);
                t2.draw(randomColor(), true);
                t3.draw(randomColor(), true);
                break;
            }
        }
    }

    private void drawWalls(int xIndex, int yIndex) {
        int x = xIndex * cellSize;
        int y = yIndex * cellSize;
    }

    private int randomColor() {
        return color(128 + r.nextInt(128), 128 + r.nextInt(128), 128 + r.nextInt(128));
    }

    private void drawTriangle(Triangle t, int color, boolean drawBorder) {
        fill(color);
        if (drawBorder) {
            stroke(0);
            strokeWeight(1);
        }
        context.triangle(t.p0.x, t.p0.y, t.p1.x, t.p1.y, t.p2.x, t.p2.y);
    }

    public abstract class SignedMazeCell extends MazeCell {
        private String signature;
        private QuadState qs;

        public SignedMazeCell(String signature, QuadState qs) {
            super();
            this.signature = signature;
            this.qs = qs;
        }
    }
    class TriangularMazeCell extends SignedMazeCell {
        private Triangle t;

        TriangularMazeCell(Triangle t, QuadState qs, String signature) {
            super(signature, qs);
            this.t = t;
        }

        public boolean equals(MazeCell other) {
            return this == other;
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
            drawTriangle(t, color, drawBorder);
        }
    }

    class SquareMazeCell extends SignedMazeCell {
        private int x, y, width, height;

        SquareMazeCell(int x, int y, int width, int height, QuadState qs) {
            super("lrud", qs);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean equals(MazeCell other) {
            SignedMazeCell signedThis = this;
            SignedMazeCell signedOther = (SignedMazeCell)other;
            return signedThis.qs == signedOther.qs && signedThis.signature.equals(signedOther.signature);
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
            fill(color);
            if (drawBorder) {
                stroke(0);
                strokeWeight(1);
            }
            rect(x, y, width, height);
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

