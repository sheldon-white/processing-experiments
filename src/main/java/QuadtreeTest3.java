import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class QuadtreeTest3 extends PApplet {
    private static final String NAME = "QuadtreeTest3";
    private static final int CELL_SIZE = 80;
    private static final int OUTPUT_WIDTH = 2000, OUTPUT_HEIGHT = 1500;
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
        background(0);

        //noStroke();
        stroke(0);
        strokeWeight(1);

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
            for (QuadRectangle c: hits) {
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
        for (QuadRectangle q: quadTree.getElements(bounds)) {
            int color = color(128 + r.nextInt(128), 128 + r.nextInt(128),128 + r.nextInt(128));
            fill(color);
            rect((int)q.x * CELL_SIZE, (int)q.y * CELL_SIZE, (int)q.width * CELL_SIZE, (int)q.height * CELL_SIZE);
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }
}

