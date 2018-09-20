import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import org.omg.CORBA.MARSHAL;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class QuadtreeTest3 extends PApplet {
    private static final String NAME = "QuadtreeTest3";
    private static final int CELL_SIZE = 80;
    private static final int CELL_MARGIN = (int)(CELL_SIZE / 8);

    private static final int OUTPUT_WIDTH = 2000, OUTPUT_HEIGHT = 1600;
    private static final int XCOUNT = OUTPUT_WIDTH / CELL_SIZE;
    private static final int YCOUNT = OUTPUT_HEIGHT / CELL_SIZE;

    private Random r = new Random();
    private static PApplet context;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(220);

        //noStroke();
        stroke(0);
        strokeWeight(1.5F);

        QuadRectangle bounds = new QuadRectangle(0, 0, XCOUNT, YCOUNT);
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, XCOUNT, YCOUNT), 0, 1, 4);
        int emptyCells = XCOUNT * YCOUNT;
        while (emptyCells > 0) {
            int x = r.nextInt(XCOUNT);
            int y = r.nextInt(YCOUNT);
            int w = 1 + r.nextInt(8);
            int h = 1 + r.nextInt(8);
            if (x + w > XCOUNT) {
                continue;
            }
            if (y + h > YCOUNT) {
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
            drawQuad(q);
            if (q.x > 0) {
                drawHorizontals(quadTree, q);
            }
            if (q.y > 0) {
                drawVerticals(quadTree, q);
            }
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawQuad(QuadRectangle q) {
        float x = (float)q.x * CELL_SIZE + CELL_MARGIN;
        float y = (float)q.y * CELL_SIZE + CELL_MARGIN;
        float w = (float)q.width * CELL_SIZE - 2 * CELL_MARGIN;
        float h = (float)q.height * CELL_SIZE - 2 * CELL_MARGIN;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128),128 + r.nextInt(128));

        stroke(120);
        strokeWeight(2);
        fill(color);
        if (q.width == 1 && q.height == 1) {
            ellipse(x - CELL_MARGIN + CELL_SIZE / 2, y - CELL_MARGIN + CELL_SIZE / 2, w, h);
        } else {
            rect(x, y, w, h, CELL_SIZE / 2 - CELL_MARGIN);
        }
    }

    private void drawHorizontals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
        QuadRectangle target = new QuadRectangle(q.x - 1, q.y, 1, q.height);
        List<QuadRectangle> hits = quadTree.getElements(target);
        for (QuadRectangle c: hits) {
            if (c.x + c.width == q.x) {
                if (c.y == q.y || c.y + c.height == q.y + q.height || between(c.y, q.y, q.height) || between(q.y, c.y, c.height)) {
                    drawHorizontal(c, q);
                }
            }
        }
    }

    private void drawVerticals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
        QuadRectangle target = new QuadRectangle(q.x, q.y - 1, q.width, 1);
        List<QuadRectangle> hits = quadTree.getElements(target);
        for (QuadRectangle c: hits) {
            if (c.y + c.height == q.y) {
                if (c.x == q.x || c.x + c.width == q.x + q.width || between(c.x, q.x, q.width) || between(q.x, c.x, c.width)) {
                    drawVertical(c, q);
                }
            }
        }
    }

    private void drawHorizontal(QuadRectangle left, QuadRectangle right) {
        int top = max((int) left.y, (int) right.y);
        int bottom = min((int) (left.y + left.height), (int) (right.y + right.height));
        int y = CELL_SIZE * (top + bottom) / 2;
        int x0 = CELL_SIZE * (int) (left.x + left.width) - CELL_MARGIN;
        int x1 = CELL_SIZE * (int) right.x + CELL_MARGIN;
        stroke(100);
        strokeWeight(2);
        line(x0, y, x1, y);
//        strokeWeight(5);
//        stroke(0);
//        point(x0, y);
//        point(x1, y);
    }

    private void drawVertical(QuadRectangle top, QuadRectangle bottom) {
        int left = max((int) top.x, (int) bottom.x);
        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
        int x = CELL_SIZE * (left + right) / 2;
        int y0 = CELL_SIZE * (int) (top.y + top.height) - CELL_MARGIN;
        int y1 = CELL_SIZE * (int) bottom.y + CELL_MARGIN;
        stroke(100);
        strokeWeight(2);
        line(x, y0, x, y1);
//        strokeWeight(5);
//        stroke(0);
//        point(x, y0);
//        point(x, y1);
    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }
}

