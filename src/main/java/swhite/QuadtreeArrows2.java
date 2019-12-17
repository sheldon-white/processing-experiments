package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class QuadtreeArrows2 extends DesktopGenerator {
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
    public void drawDesktop() {
        background(220);

        QuadRectangle bounds = new QuadRectangle(0, 0, xcount, ycount);
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, xcount, ycount), 0, 1, 4);
        int emptyCells = xcount * ycount;
        while (emptyCells > 0) {
            int x = r.nextInt(xcount);
            int y = r.nextInt(ycount);
            int w = 1 + r.nextInt(20);
            int h = w;
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
        float x = (float) q.x * cellSize;
        float y = (float) q.y * cellSize;
        float w = (float) q.width * cellSize;
        float h = (float) q.height * cellSize;
        float xcen = x + w / 2;
        float ycen = y + h / 2;

        fillWithRandomColor();
        rect(x, y, w, h);
        fillWithRandomColor();
        ellipse(xcen, ycen, w, h);
        fillWithRandomColor();
        drawArrow(xcen, ycen, w, h, 2 * PI * r.nextFloat());
    }

    private void drawArrow(float xc, float yc, float w, float h, float theta) {
        pushMatrix();
        translate(xc, yc);
        rotate(theta);
        noStroke();
        fillWithRandomColor();
        float b = h / 8;
        float wi = w - 2 * b;
        float hi = h - 2 * b;
        float y0 = -hi / 2;
        float y2 = 0;
        float y3 = hi / 2;
        float x0 = -wi / 2;
        float x1 = wi / 2 - (hi / 2);
        float x2 = wi / 2;
        x1 = min(x1, x2);
        x2 = max(x1, x2);
        triangle(x1, y0, x2, y2, x1, y3);
        rect(x0, -hi / 4, x1 - x0 + 1, hi / 2);
        popMatrix();
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

