package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.util.List;
import java.util.Random;

public class QuadtreeTest4 extends PApplet {
    private static final String NAME = "QuadtreeTest4";

    private int outputWidth = 3000, outputHeight = 2000;
    private Random r = new Random();
    private StandardQuadTree<Circle> quadTree;
    private int circleCount = 0;
    private boolean doneDrawingCircles = false;
    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, outputWidth, outputHeight), 0, 1, 4);
        background(220);

        //noStroke();
        stroke(0);
        strokeWeight(1.5F);
    }

    @Override
    public void draw() {
        int minRadius = 20;
        if (!doneDrawingCircles) {
            int tries = 100;
            while (tries-- > 0) {
                int radius = 20 + r.nextInt(minRadius);
                int x = radius + r.nextInt(outputWidth - 2 * radius);
                int y = radius + r.nextInt(outputHeight - 2 * radius);

                Circle circle = new Circle(new IPoint(x, y), radius);
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
                    circleCount++;
                    print(circleCount, "\n");
                }
            }
        }
    }


    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + ".png");
        } else if (key == 'd' || key == 'D') {
            doneDrawingCircles = true;
        }
    }

    private void drawCircle(Circle c) {
        float x = (float)c.center.x;
        float y = (float)c.center.y;
        int margin = 5;
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
//        int y = cellSize * (top + bottom) / 2;
//        int x0 = cellSize * (int) (left.x + left.width) - cellMargin;
//        int x1 = cellSize * (int) right.x + cellMargin;
//        stroke(100);
//        strokeWeight(2);
//        line(x0, y, x1, y);
//    }
//
//    private void drawVertical(QuadRectangle top, QuadRectangle bottom) {
//        int left = max((int) top.x, (int) bottom.x);
//        int right = min((int) (top.x + top.width), (int) (bottom.x + bottom.width));
//        int x = cellSize * (left + right) / 2;
//        int y0 = cellSize * (int) (top.y + top.height) - cellMargin;
//        int y1 = cellSize * (int) bottom.y + cellMargin;
//        stroke(100);
//        strokeWeight(2);
//        line(x, y0, x, y1);
//    }

    private boolean between(double r, double start, double span) {
        return (r > start && r < start + span);
    }
}
