package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.lang.invoke.MethodHandles;

public class QuadtreeArrows1 extends QuadtreeDesktopGenerator {
    private int cellSize = 40;

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private static final boolean SPARCE = false;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeArrows1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeArrows1() {
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

        stroke(0);
        strokeWeight(1);
        ColorUtils.fillWithRandomColor(context);
        rect(x, y, w, h);
        noStroke();
        ColorUtils.fillWithRandomColor(context);
        float direction = r.nextInt(2);
        if (w > h) {
            ShapeUtils.drawArrow(context, x + w / 2, y + h / 2, w, h, direction * PI);
        } else {
            ShapeUtils.drawArrow(context, x + w / 2, y + h / 2, h, w, PI / 2 + direction * PI);
        }
    }
}

