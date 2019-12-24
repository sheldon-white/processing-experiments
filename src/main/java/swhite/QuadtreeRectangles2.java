package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;

public class QuadtreeRectangles2 extends QuadtreeDesktopGenerator {
    private static int cellSize = 50;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;
    private int cellMargin = cellSize / 4;
    private PImage backgroundImage;
    private PImage foregroundImage;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeRectangles2();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeRectangles2() {
        super(MethodHandles.lookup().lookupClass().getName(),
                maxQuadWidth, maxQuadHeight, quadTreeWidth, quadTreeHeight);
    }

    @Override
    public void initializeSettings() {
        size(cellSize * quadTreeWidth, cellSize * quadTreeHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    protected void initFrame() {
        backgroundImage = loadImage("src/main/resources/mediumwood1.jpg");
        foregroundImage = loadImage("src/main/resources/lightwoodH1.jpg");

        for (int x = 0; x < this.width; x += backgroundImage.width) {
            for (int y = 0; y < this.height; y += backgroundImage.height) {
                image(backgroundImage, x, y);
            }
        }
    }

    @Override
    protected void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize + cellMargin;
        float y = (float) q.y * cellSize + cellMargin;
        float w = (float) q.width * cellSize - 2 * cellMargin;
        float h = (float) q.height * cellSize - 2 * cellMargin;

        stroke(0);
        strokeWeight(4);
        noFill();
        imageMode(CENTER);

        pushMatrix();
        translate(x + w/2, y + h/2);
        float longAxis = max(w, h);
        float shortAxis = min(w, h);
        float rotation = (h > w) ? PI / 2 : 0;
        int ximg = r.nextInt((int)(foregroundImage.width - longAxis));
        int yimg = r.nextInt((int)(foregroundImage.height - shortAxis));
        PImage section = foregroundImage.get(ximg, yimg, (int)longAxis, (int)shortAxis);
        rotate(rotation);
        image(section, 0, 0);
        rect(-longAxis/2, -shortAxis/2, longAxis, shortAxis);
        popMatrix();
    }
}

