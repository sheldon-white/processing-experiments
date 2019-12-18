package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PFont;

import java.lang.invoke.MethodHandles;

public class QuadtreeKanji1 extends QuadtreeDesktopGenerator {
    private int cellSize = 50;
    private int cellMargin = cellSize / 8;

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private static final boolean SPARCE = false;
    private FontLoader fontLoader = new FontLoader();

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeKanji1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeKanji1() {
        super(MethodHandles.lookup().lookupClass().getName());
    }

    @Override
    public void initializeSettings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    protected IPoint getRandomBounds(int maxWidth, int maxHeight) {
        int w = 1 + r.nextInt(maxWidth);
        int h = 1 + r.nextInt(maxHeight);
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

