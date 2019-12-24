package swhite;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;

public class ImageBlend2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private PImage img1, img2;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img1 = loadImage("src/main/resources/flower.jpg");
        img2 = loadImage("src/main/resources/flower-tesselated.jpg");
        size(img1.width, img1.height);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        background(0);

        float srcScaleX1 = 1;
        float srcScaleY1 = 1;
        float srcScaleX2 = 1;
        float srcScaleY2 = 1;

        for (int x = 0; x < img1.width; x++) {
            for (int y = 0; y < img1.height; y++) {
                int c1 = img1.get((int) (x * srcScaleX1), (int) (y * srcScaleY1));
                int c2 = img2.get((int) (x * srcScaleX2), (int) (y * srcScaleY2));
                int color = lerpColor(c1, c2, (float) (x + y) / (img1.width + img1.height));
                stroke(color);
                point(x, y);
            }
        }
        save(NAME + "." + System.currentTimeMillis() + ".png");
        print("Done!\n");
        noLoop();
    }
}
