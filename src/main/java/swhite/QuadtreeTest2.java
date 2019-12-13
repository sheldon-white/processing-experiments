package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

public class QuadtreeTest2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int outputWidth = 2000, outputHeight = 1000;

    private Random r = new Random();
    private Triangle template;
    private StandardQuadTree<Triangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, outputWidth, outputHeight), 0, 1, 2);

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
        background(0);
        noStroke();
        int x = 0;
        int y = 0;
        int w = 5 + r.nextInt(500);
        int h = 5 + r.nextInt(500);
        QuadRectangle bounds = new QuadRectangle(x, y, w, h);
        template = ShapeUtils.randomTriangle(x, y, w, h);
        quadTree.insert(bounds, template);
    }

    @Override
    public void draw() {
        for (int i = 0; i < 20; i++) {
            int x = 0;
            int y = 0;
            int w = 5 + r.nextInt(500);
            int h = 5 + r.nextInt(500);
            QuadRectangle bounds = new QuadRectangle(x, y, w, h);
            int xoffset = r.nextInt(outputWidth);
            int yoffset = r.nextInt(outputHeight);
            double scale = 0.05 + 10 * r.nextDouble();
            if (xoffset + w * scale > outputWidth) {
                return;
            }
            if (yoffset + h * scale > outputHeight) {
                return;
            }
            Triangle t = template.transform(xoffset, yoffset, scale);
            QuadRectangle q = ShapeUtils.transform(bounds, xoffset, yoffset, scale);
            List<Triangle> hits = quadTree.getElements(q);
            boolean fits = true;
            for (Triangle c : hits) {
                if (t != c && ShapeUtils.trianglesIntersect(t, c)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(q, t);
                IPoint c = t.centroidCenter();
                int r = (int) (255 * c.x / (float) outputWidth);
                int g = (int) (255 * c.y / (float) outputHeight);
                int b = 128;
                fill(r, g, b);
                triangle((float) t.p0.x, (float) t.p0.y, (float) t.p1.x, (float) t.p1.y, (float) t.p2.x, (float) t.p2.y);
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        }
    }
}

