package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PFont;

import java.lang.invoke.MethodHandles;

public class QuadtreeKanji1 extends QuadtreeDesktopGenerator {
    private static int cellSize = 50;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;
    private int cellMargin = cellSize / 8;

    private FontLoader fontLoader = new FontLoader();

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeKanji1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeKanji1() {
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
    protected IPoint getRandomBounds() {
        int w = 1 + r.nextInt(maxQuadWidth);
        int h = 1 + r.nextInt(maxQuadHeight);
        if (w / h > 3) {
            w = h * 3;
        } else if (h / w > 3) {
            h = w * 3;
        }
        return new IPoint(w, h);
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
        // Common and uncommon kanji ( 4e00 - 9faf)
        int minChar = 0x4E00;
        int maxChar = 0x9faf;
        int min = floor(min(w, h) * 0.75f);
        boolean done = false;
        while (!done) {
            PFont font = fontLoader.getRandomFont(this, min);
            char c = (char) (minChar + r.nextInt(maxChar - minChar));
            done = ShapeUtils.centerCharacterInRect(context, font, c, x, y, w, h);
        }
    }
}

