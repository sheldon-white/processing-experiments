package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.lang.invoke.MethodHandles;

public class QuadtreeRectangles1 extends QuadtreeDesktopGenerator {
    private static int cellSize = 50;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;
    private int cellMargin = cellSize / 8;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeRectangles1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeRectangles1() {
        super(MethodHandles.lookup().lookupClass().getName(),
                maxQuadWidth, maxQuadHeight, quadTreeWidth, quadTreeHeight);
    }

    @Override
    public void initializeSettings() {
        size(cellSize * quadTreeWidth, cellSize * quadTreeHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    protected void initFrame() {
        background(220);
    }

    @Override
    protected void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize + cellMargin;
        float y = (float) q.y * cellSize + cellMargin;
        float w = (float) q.width * cellSize - 2 * cellMargin;
        float h = (float) q.height * cellSize - 2 * cellMargin;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128), 128 + r.nextInt(128));

        stroke(80);
        strokeWeight(2);
        fill(color);
        rect(x, y, w, h);
    }
}

