package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

public class QuadtreeTest4 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private int outputWidth = 3000, outputHeight = 2000;
    private Random r = new Random();
    private StandardQuadTree<Circle> quadTree;
    private int circleCount = 0;
    private boolean doneDrawingCircles = false;
    public static void main(String args[]) {
        PApplet.main(NAME);
    }
    private double minRadius = 100;
    private int triesPerCycle = 200;
    private int backgroundColor;

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, outputWidth, outputHeight), 0, 1, 4);
        backgroundColor = color(160, 160, 160);
        background(backgroundColor);
        stroke(0);
        strokeWeight(1.5F);
    }

    @Override
    public void draw() {
        if (!doneDrawingCircles) {
            boolean placedOne = false;
            int ctr = triesPerCycle;
            while (ctr-- > 0) {
                int radius = (int)(minRadius + r.nextDouble() * minRadius);
                int x = radius + r.nextInt(outputWidth - 2 * radius);
                int y = radius + r.nextInt(outputHeight - 2 * radius);
                int color = get(x, y);
                if (color != backgroundColor) {
                    continue;
                }

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
                    placedOne = true;
                }
            }
            if (!placedOne) {
                minRadius = minRadius * 0.95;
                triesPerCycle = (int)(triesPerCycle * 1.3);
            }
        }
    }


    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
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
}

