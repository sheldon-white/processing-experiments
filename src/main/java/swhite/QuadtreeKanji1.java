package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PFont;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class QuadtreeKanji1 extends DesktopGenerator {
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
    public void drawDesktop() {
        background(220);
        stroke(0);
        strokeWeight(1.5F);

        QuadRectangle bounds = new QuadRectangle(0, 0, xcount, ycount);
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, xcount, ycount), 0, 1, 4);
        int emptyCells = xcount * ycount;
        while (emptyCells > 0) {
            int x = r.nextInt(xcount);
            int y = r.nextInt(ycount);
            int w = 1 + r.nextInt(8);
            int h = 1 + r.nextInt(8);
            if (w / h > 3) {
                w = h * 3;
            } else if (h / w > 3) {
                h = w * 3;
            }
            if (x + w > xcount) {
                continue;
            }
            if (y + h > ycount) {
                continue;
            }
            QuadRectangle q = new QuadRectangle(x, y, w, h);
            List<QuadRectangle> hits = quadTree.getElements(q);
            boolean fits = true;
            for (QuadRectangle c : hits) {
                if (q != c && ShapeUtils.rectanglesIntersect(q, c)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(q, q);
                emptyCells -= w * h;
            }
        }

        for (QuadRectangle q : quadTree.getElements(bounds)) {
            if (SPARCE && q.width == 1 && q.height == 1) {
                continue;
            }
            drawQuad(q);
        }
        doneDrawing = true;
    }

    private void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize + cellMargin;
        float y = (float) q.y * cellSize + cellMargin;
        float w = (float) q.width * cellSize - 2 * cellMargin;
        float h = (float) q.height * cellSize - 2 * cellMargin;
        fillWithRandomColor();
        stroke(80);
        strokeWeight(2);
        rect(x, y, w, h);
        fillWithRandomColor();
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
            PFont.Glyph glyph = font.getGlyph(c);
            if (glyph != null) {
                pushMatrix();
                translate(x + (glyph.leftExtent + w - glyph.width) / 2,
//                        y - glyph.topExtent / 2 + h - glyph.height / 2);
                        y + glyph.topExtent + (h - glyph.height) / 2);
                textFont(font);
                text(c, 0, 0);
                popMatrix();
                done = true;
            }
        }
    }

    private void fillWithRandomColor() {
        int baseIntensity = 100;
        int variance = 155;
        int red = baseIntensity + r.nextInt(variance);
        int green = baseIntensity + r.nextInt(variance);
        int blue = baseIntensity + r.nextInt(variance);
        int alpha = 255;
        fill(red, green, blue, alpha);
    }
}

