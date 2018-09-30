import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class QuadtreeTest1 extends PApplet {
    private static final String NAME = "QuadtreeTest1";

    private Random r = new Random();
    private static PApplet context;
    private PImage img;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("image3.jpg");
        size(img.width, img.height);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(0);

        noStroke();
//        stroke(128);
//        strokeWeight(1);

        StandardQuadTree<Triangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, img.width, img.height), 0, 1, 2);
        int count = 0;
        while (count < 5000) {
            int x = r.nextInt(img.width);
            int y = r.nextInt(img.height);
            int w = 5 + r.nextInt(500);
            int h = 5 + r.nextInt(500);
            if (x + w > img.width) {
                continue;
            }
            if (y + h > img.height) {
                continue;
            }
            Triangle t = ShapeUtils.randomTriangle(x, y, w, h);
            QuadRectangle q = new QuadRectangle(x, y, w, h);
            List<Triangle> hits = quadTree.getElements(q);
            boolean fits = true;
            for (Triangle c: hits) {
                if (t != c && ShapeUtils.trianglesIntersect(t, c)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(q, t);
                IPoint c = t.centroidCenter();
                fill(img.get(c.x, c.y));
                triangle((float)t.p0.x, (float)t.p0.y , (float)t.p1.x, (float)t.p1.y, (float)t.p2.x, (float)t.p2.y);
                count++;
            }
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }
}

