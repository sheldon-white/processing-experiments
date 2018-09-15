import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

public class ImageBlend1 extends PApplet {
    private static final String NAME = "ImageBlend1";
    private static final int CELL_SIZE = 2;
    private static final int OUTPUT_WIDTH = 1500, OUTPUT_HEIGHT = 1000;
    private static final int XCOUNT = OUTPUT_WIDTH / CELL_SIZE;
    private static final int YCOUNT = OUTPUT_HEIGHT / CELL_SIZE;

    private PImage img1, img2;
    private Random r = new Random();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img1 = loadImage("image1.jpg");
        img2 = loadImage("image2.jpg");
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        background(0);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        int srcCellWidth1 = img1.width / XCOUNT;
        int srcCellHeight1 = img1.height / YCOUNT;
        int srcCellWidth2 = img2.width / XCOUNT;
        int srcCellHeight2 = img2.height / YCOUNT;
        int dstCellWidth = OUTPUT_WIDTH / XCOUNT;
        int dstCellHeight = OUTPUT_HEIGHT / YCOUNT;

        for (int x = 0; x < XCOUNT; x++) {
            for (int y = 0; y < YCOUNT; y++) {
                if (overThreshold(x, y)) {
                    image(img1.get(x * srcCellWidth1, y * srcCellHeight1, srcCellWidth1, srcCellHeight1),
                            x * dstCellWidth, y * dstCellHeight, dstCellWidth, dstCellHeight);
                } else {
                    image(img2.get(x * srcCellWidth2, y * srcCellHeight2, srcCellWidth2, srcCellHeight2),
                            x * dstCellWidth, y * dstCellHeight, dstCellWidth, dstCellHeight);
                }
            }
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private boolean overThreshold(int x, int y) {
        double s1 = 10.0;
        double s2 = 4.0;
        double x0 = s1 * (-0.5 + (double)x / XCOUNT);
        //double y0 = s1 * (-0.5 + (double)y / ycount);
        print(x, x0, "\n");
        return x0 > (-0.5 + r.nextDouble()) * s2;
    }
}
