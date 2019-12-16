package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

public class Arrows2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 40;

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private Random r = new Random();
    private static final boolean SPARCE = false;

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
    public void draw() {
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

        save(NAME + "." + System.currentTimeMillis() + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize;
        float y = (float) q.y * cellSize;
        float w = (float) q.width * cellSize;
        float h = (float) q.height * cellSize;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128), 128 + r.nextInt(128));

        stroke(80);
        strokeWeight(2);
        fill(color);
        rect(x, y, w, h);
        float direction = r.nextInt(2);
        if (w > h) {
            drawArrow(x + w / 2, y + h / 2, w, h, direction * PI);
        } else {
            drawArrow(x + w / 2, y + h / 2, h, w, PI / 2 + direction * PI);
        }
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

