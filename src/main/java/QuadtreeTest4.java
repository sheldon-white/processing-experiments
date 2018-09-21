import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.util.List;
import java.util.Random;

public class QuadtreeTest4 extends PApplet {
    private static final String NAME = "QuadtreeTest4";

    private static final int OUTPUT_WIDTH = 2400, OUTPUT_HEIGHT = 1600;
    private static final int margin = 5;
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

        StandardQuadTree<Circle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, OUTPUT_WIDTH, OUTPUT_HEIGHT), 0, 1, 4);
        int minRadius = 100;
        while (minRadius > 10) {
            int tries = 1000;
            while (tries > 0) {
                int radius = minRadius + r.nextInt(minRadius);
                int x = radius + r.nextInt(OUTPUT_WIDTH - 2 * radius);
                int y = radius + r.nextInt(OUTPUT_HEIGHT - 2 * radius);

                Circle circle = new Circle(new Point(x, y), radius);
                QuadRectangle q = new QuadRectangle(x - radius, y - radius, 2 * radius, 2 * radius);
                List<Circle> hits = quadTree.getElements(q);
                boolean fits = true;
                for (Circle c : hits) {
                    if (circle != c && circle.intersects(c)) {
                        fits = false;
                        break;
                    }
                }
                if (fits) {
                    quadTree.insert(q, circle);
                    drawCircle(circle);
                }
                tries--;
            }
            minRadius -= 10;
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawCircle(Circle c) {
        float x = (float)c.center.x;
        float y = (float)c.center.y;
        float w = 2 * (float)c.radius - 2 * margin;
        float h = w;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128),128 + r.nextInt(128));

        stroke(80);
        strokeWeight(2);
        fill(color);
        ellipse(x, y, w, h);
    }

//    private void drawHorizontals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
//        QuadRectangle target = new QuadRectangle(q.x - 1, q.y, 1, q.height);
//        List<QuadRectangle> hits = quadTree.getElements(target);
//        for (QuadRectangle c: hits) {
//            if (c.x + c.width == q.x) {
//                if (c.y == q.y || c.y + c.height == q.y + q.height || between(c.y, q.y, q.height) || between(q.y, c.y, c.height)) {
//                    drawHorizontal(c, q);
//                }
//            }
//        }
//    }

//    private void drawVerticals(StandardQuadTree<QuadRectangle> quadTree, QuadRectangle q) {
//        QuadRectangle target = new QuadRectangle(q.x, q.y - 1, q.width, 1);
//        List<QuadRectangle> hits = quadTree.getElements(target);
//        for (QuadRectangle c: hits) {
//            if (c.y + c.height == q.y) {
//                if (c.x == q.x || c.x + c.width == q.x + q.width || between(c.x, q.x, q.width) || between(q.x, c.x, c.width)) {
//                    drawVertical(c, q);
//                }
//            }
//        }
//    }
//
//    private void drawHorizontal(QuadRectangle left, QuadRectangle right) {
//        int top = max((int) left.y, (int) right.y);
//        int bottom = min((int) (left.y + left.height), (int) (right.y + right.height));
//        int y = CELL_SIZE * (top + bottom) / 2;
//        int x0 = CELL_SIZE * (int) (left.x + left.width) - CELL_MARGIN;
//        int x1 = CELL_SIZE * (int) right.x + CELL_MARGIN;
//        stroke(100);
//        strokeWeight(2);
//        line(x0, y, x1, y);
//    }
//
//    private void drawVertical(QuadRectangle top, QuadRectangle bottom) {
//        int left = max((int) top.x, (int) bottom.x);
//        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
//        int x = CELL_SIZE * (left + right) / 2;
//        int y0 = CELL_SIZE * (int) (top.y + top.height) - CELL_MARGIN;
//        int y1 = CELL_SIZE * (int) bottom.y + CELL_MARGIN;
//        stroke(100);
//        strokeWeight(2);
//        line(x, y0, x, y1);
//    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }
}

