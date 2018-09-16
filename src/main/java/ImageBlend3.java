import processing.core.PApplet;
import processing.core.PImage;

public class ImageBlend3 extends PApplet {
    private static final String NAME = "ImageBlend3";
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
        int cellSize = 20;
        int xcount = OUTPUT_WIDTH / cellSize;
        int ycount = OUTPUT_HEIGHT / cellSize;
        int[][] imagePoints1 = new int[xcount][ycount];
        int[][] imagePoints2 = new int[xcount][ycount];

        // Loop through the images, collect array of points
        for (int y = 0; y < ycount; y++) {
            int yIndex1 = (int)(y * cellSize * srcScaleY1);
            int yIndex2 = (int)(y * cellSize * srcScaleY2);
            for (int x = 0; x < xcount; x++) {
                int xIndex1 = (int)(x * cellSize * srcScaleX1);
                int xIndex2 = (int)(x * cellSize * srcScaleX2);
                int c1 = img1.get(xIndex1, yIndex1);
                int c2 = img2.get(xIndex2, yIndex2);
//                print(xIndex1, yIndex1, c1, "\n");
//                print(xIndex2, yIndex2, c2, "\n");
                imagePoints1[x][y] = c1;
                imagePoints2[x][y] = c2;
            }
        }

        noStroke();

        for (int x = 0; x < xcount; x++) {
            for (int y = 0; y < ycount; y++) {
//                stroke(0);
//                strokeWeight(1);
                int outerColor = imagePoints1[x][y];
                fill(outerColor);
                rect(x * cellSize, y * cellSize, cellSize, cellSize);
               // noStroke();
                int innerColor = imagePoints2[x][y];
                fill(innerColor);
                rect(x * cellSize + 4, y * cellSize + 4, cellSize - 8, cellSize - 8);
            }
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }
}

