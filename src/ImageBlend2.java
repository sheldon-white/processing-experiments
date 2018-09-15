import processing.core.PApplet;
import processing.core.PImage;

public class ImageBlend2 extends PApplet {
    private static final String NAME = "ImageBlend2";
    private static final int OUTPUT_WIDTH = 1500, OUTPUT_HEIGHT = 1000;

    private PImage img1, img2;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img1 = loadImage("image1.jpg");
        img2 = loadImage("image3.jpg");
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        background(0);

        float srcScaleX1 = (float)img1.width / OUTPUT_WIDTH;
        float srcScaleY1 = (float)img1.height / OUTPUT_HEIGHT;
        float srcScaleX2 = (float)img2.width / OUTPUT_WIDTH;
        float srcScaleY2 = (float)img2.height / OUTPUT_HEIGHT;

        for (int x = 0; x < OUTPUT_WIDTH; x++) {
            for (int y = 0; y < OUTPUT_HEIGHT; y++) {
                int c1 = img1.get((int)(x * srcScaleX1), (int)(y * srcScaleY1));
                int c2 = img2.get((int)(x * srcScaleX2), (int)(y * srcScaleY2));
                int color = lerpColor(c1, c2, (float)(x + y) / (OUTPUT_WIDTH + OUTPUT_HEIGHT));
                stroke(color);
                point(x, y);
            }
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }
}
