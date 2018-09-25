import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;

import java.util.List;
import java.util.Random;

public class QuadtreeTest2 extends PApplet {
    private static final String NAME = "QuadtreeTest2";
    private int outputWidth = 2000, outputHeight = 1000;

    private Random r = new Random();
    private static PApplet context;

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
        context = this;
        background(0);
        noStroke();
//        stroke(128);
//        strokeWeight(1);

        int x = 0;
        int y = 0;
        int w = 5 + r.nextInt(500);
        int h = 5 + r.nextInt(500);
        Triangle triangle = ShapeUtils.randomTriangle(x, y, w, h);
        QuadRectangle bounds = new QuadRectangle(x, y, w, h);
        StandardQuadTree<Triangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, outputWidth, outputHeight), 0, 1, 2);
        int count = 0;
        while (count < 4000) {
            int xoffset = r.nextInt(outputWidth);
            int yoffset = r.nextInt(outputHeight);
            double scale = 0.05 + 10 * r.nextDouble();
            if (xoffset + w * scale > outputWidth) {
                continue;
            }
            if (yoffset + h * scale > outputHeight) {
                continue;
            }
            Triangle t = triangle.transform(xoffset, yoffset, scale);
            QuadRectangle q = ShapeUtils.transform(bounds, xoffset, yoffset, scale);
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
                Point c = t.centroidCenter();
                int r = (int)(255 * c.x / (float)outputWidth);
                int g = (int)(255 * c.y / (float)outputHeight);
                int b = 128;
                fill(r, g, b);
                triangle((float)t.p0.x, (float)t.p0.y , (float)t.p1.x, (float)t.p1.y, (float)t.p2.x, (float)t.p2.y);
                count++;
            }
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }
}

