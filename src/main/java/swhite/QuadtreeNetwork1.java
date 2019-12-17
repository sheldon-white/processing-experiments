package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class QuadtreeNetwork1 extends DesktopGenerator {
    private int cellSize = 40;
    private int cellMargin = (int) (cellSize / 8);

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private static final boolean SPARCE = false;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeNetwork1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeNetwork1() {
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

        //noStroke();
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
            if (q.x > 0) {
                drawHorizontals(quadTree, q);
            }
            if (q.y > 0) {
                drawVerticals(quadTree, q);
            }
        }
        doneDrawing = true;
    }

    private void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize + cellMargin;
        float y = (float) q.y * cellSize + cellMargin;
        float w = (float) q.width * cellSize - 2 * cellMargin;
        float h = (float) q.height * cellSize - 2 * cellMargin;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128), 128 + r.nextInt(128));

        stroke(80);
        strokeWeight(2);
        fill(color);
        if (q.width == 1 && q.height == 1) {
            ellipse(x - cellMargin + cellSize / 2f, y - cellMargin + cellSize / 2f, w, h);
        } else {
            rect(x, y, w, h, cellSize / 2f - cellMargin);
        }
    }

    private void drawHorizontals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
        QuadRectangle target = new QuadRectangle(q.x - 1, q.y, 1, q.height);
        List<QuadRectangle> hits = quadTree.getElements(target);
        for (QuadRectangle c : hits) {
            if (c.x + c.width == q.x) {
                if (SPARCE && c.width == 1 && c.height == 1) {
                    continue;
                }
                if (c.y == q.y || c.y + c.height == q.y + q.height || between(c.y, q.y, q.height) || between(q.y, c.y, c.height)) {
                    drawHorizontal(c, q);
                }
            }
        }
    }

    private void drawVerticals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
        QuadRectangle target = new QuadRectangle(q.x, q.y - 1, q.width, 1);
        List<QuadRectangle> hits = quadTree.getElements(target);
        for (QuadRectangle c : hits) {
            if (c.y + c.height == q.y) {
                if (SPARCE && c.width == 1 && c.height == 1) {
                    continue;
                }
                if (c.x == q.x || c.x + c.width == q.x + q.width || between(c.x, q.x, q.width) || between(q.x, c.x, c.width)) {
                    drawVertical(c, q);
                }
            }
        }
    }

    private void drawHorizontal(QuadRectangle left, QuadRectangle right) {
        int top = max((int) left.y, (int) right.y);
        int bottom = min((int) (left.y + left.height), (int) (right.y + right.height));
        int y = cellSize * (top + bottom) / 2;
        int x0 = cellSize * (int) (left.x + left.width) - cellMargin;
        int x1 = cellSize * (int) right.x + cellMargin;
        stroke(100);
        strokeWeight(2);
        line(x0, y, x1, y);
    }

    private void drawVertical(QuadRectangle top, QuadRectangle bottom) {
        int left = max((int) top.x, (int) bottom.x);
        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
        int x = cellSize * (left + right) / 2;
        int y0 = cellSize * (int) (top.y + top.height) - cellMargin;
        int y1 = cellSize * (int) bottom.y + cellMargin;
        stroke(100);
        strokeWeight(2);
        line(x, y0, x, y1);
    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }
}

