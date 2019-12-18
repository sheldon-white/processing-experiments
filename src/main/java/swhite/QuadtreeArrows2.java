package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.lang.invoke.MethodHandles;

public class QuadtreeArrows2 extends QuadtreeDesktopGenerator {
    private int cellSize = 20;

    private int outputWidth = 3500;
    private int outputHeight = 2500;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private static final boolean SPARCE = false;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeArrows2();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeArrows2() {
        super(MethodHandles.lookup().lookupClass().getName());
    }

    @Override
    public void initializeSettings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
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
    protected IPoint getRandomBounds(int maxWidth, int maxHeight) {
        int w = 1 + r.nextInt(20);
        return new IPoint(w, w);
    }
}

