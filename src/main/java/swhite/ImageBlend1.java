package swhite;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class ImageBlend1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 1;
    private int outputWidth = 1500, outputHeight = 1000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private PImage img1, img2;
    private Random r = new Random();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img1 = loadImage("src/main/resources/flower.jpg");
        img2 = loadImage("src/main/resources/flower-tesselated.jpg");
        size(img1.width, img1.height);
        outputWidth = img1.width;
        outputHeight = img1.height;
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        background(0);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        int srcCellWidth1 = img1.width / xcount;
        int srcCellHeight1 = img1.height / ycount;
        int srcCellWidth2 = img2.width / xcount;
        int srcCellHeight2 = img2.height / ycount;
        int dstCellWidth = outputWidth / xcount;
        int dstCellHeight = outputHeight / ycount;

        for (int x = 0; x < xcount; x++) {
            for (int y = 0; y < ycount; y++) {
                if (overThreshold(x)) {
                    image(img1.get(x * srcCellWidth1, y * srcCellHeight1, srcCellWidth1, srcCellHeight1),
                            x * dstCellWidth, y * dstCellHeight, dstCellWidth, dstCellHeight);
                } else {
                    image(img2.get(x * srcCellWidth2, y * srcCellHeight2, srcCellWidth2, srcCellHeight2),
                            x * dstCellWidth, y * dstCellHeight, dstCellWidth, dstCellHeight);
                }
            }
        }
        save(NAME + "." + System.currentTimeMillis() + ".png");
        print("Done!\n");
        noLoop();
    }

    private boolean overThreshold(int x) {
        double s1 = 10.0;
        double s2 = 4.0;
        double x0 = s1 * (-0.5 + (double) x / xcount);
        // print(x, x0, "\n");
        return x0 > (-0.5 + r.nextDouble()) * s2;
    }
}
