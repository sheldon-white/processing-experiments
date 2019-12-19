package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.lang.invoke.MethodHandles;

public class QuadtreeArrows2 extends QuadtreeDesktopGenerator {
    private static int cellSize = 20;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeArrows2();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeArrows2() {
        super(MethodHandles.lookup().lookupClass().getName(),
                maxQuadWidth, maxQuadHeight, 80, 60);
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
        float x = (float) q.x * cellSize;
        float y = (float) q.y * cellSize;
        float w = (float) q.width * cellSize;
        float h = (float) q.height * cellSize;
        float xcen = x + w / 2;
        float ycen = y + h / 2;

//        fillWithRandomColor();
//        rect(x, y, w, h);
        ColorUtils.fillWithRandomColor(context);
        ellipse(xcen, ycen, w, h);
        noStroke();
        ColorUtils.fillWithRandomColor(context);
        ShapeUtils.drawArrow(context, xcen, ycen, w, h, 2 * PI * r.nextFloat());
    }

    @Override
    protected IPoint getRandomBounds() {
        int w = 1 + r.nextInt(maxQuadWidth);
        return new IPoint(w, w);
    }
}

