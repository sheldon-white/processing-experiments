package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PFont;

import java.lang.invoke.MethodHandles;

public class QuadtreePunctuation1 extends QuadtreeDesktopGenerator {
    private static int cellSize = 50;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;
    private int cellMargin = cellSize / 8;

    private FontLoader fontLoader = new FontLoader();

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreePunctuation1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreePunctuation1() {
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
        ColorUtils.fillWithRandomColor(context);
        stroke(80);
        strokeWeight(2);
        rect(x, y, w, h);
        ColorUtils.fillWithRandomColor(context);
        drawRandomCharacter(x, y, w, h);
    }

    private void drawRandomCharacter(float x, float y, float w, float h) {
        int min = floor(min(w, h) * 0.75f);
        char c;
        if (h / w > 2) {
            c = '!';
        } else if (h / w > 1.5) {
            c = '?';
        } else {
            c = '*';
        }
        boolean done = false;
        while (!done) {
            PFont font = fontLoader.getRandomFont(this, min);
            done = ShapeUtils.centerCharacterInRect(context, font, c, x, y, w, h);
        }
    }
}

