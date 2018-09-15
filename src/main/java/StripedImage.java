import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class StripedImage extends PApplet {
    private static final String NAME = "StripedImage";
    private static final int STRIPE_WIDTH = 10;

    private PImage img;
    private int xcount;
    private int ycount;
    private Random r = new Random();
    private boolean[][] filled;
    private int[][] imagePoints;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("monalisa.jpg");
        size(img.width, img.height);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        xcount = img.width / STRIPE_WIDTH;
        ycount = img.height / STRIPE_WIDTH;
        imagePoints = new int[xcount][ycount];
        filled = new boolean[xcount][ycount];
        // Loop through the image, collect array of points
        for (int y = 0; y < ycount; y++) {
            for (int x = 0; x < xcount; x++) {
                int color = img.get(x * STRIPE_WIDTH, y * STRIPE_WIDTH);
                imagePoints[x][y] = color;
                filled[x][y] = false;
            }
        }

        background(0);
        //noStroke();
        stroke(0);
        strokeWeight(1);

//        while (emptySquares > 0) {
//            int xinit = r.nextInt(xcount), x = xinit;
//            int yinit = r.nextInt(ycount), y = yinit;
//            boolean placedOne = false;
//            do {
//                if (!filled[x][y]) {
//                    maximalUniformStripe(x, y);
//                    placedOne = true;
//                    break;
//                }
//                x++;
//                if (x == xcount) {
//                    x = 0;
//                    y++;
//                    if (y == ycount) {
//                        y = 0;
//                    }
//                }
//            } while (x != xinit && y != yinit);
//        }

        int deltax = r.nextInt(xcount);
        int deltay = r.nextInt(ycount);
        for (int x = 0; x < xcount; x++) {
            int x1 = (x + deltax) % xcount;
            for (int y = 0; y < ycount; y++) {
                int y1 = (y + deltay) % ycount;
                if (!filled[x1][y1]) {
                    maximalUniformStripe(x1, y1);
                }
            }
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void maximalUniformStripe(int xInit, int yInit) {
        int centralColor = imagePoints[xInit][yInit];
        double maxColorDiff = 30.0;
        int xMin = xInit, xMax = xInit, yMin = yInit, yMax = yInit;

        // check left
        for (int x = xInit; x >= 0; x--) {
            if (filled[x][yInit] || colorDiff(centralColor, x, yInit) > maxColorDiff) {
                break;
            }
            xMin = x;
        }
        // check right
        for (int x = xInit; x < xcount; x++) {
            if (filled[x][yInit] || colorDiff(centralColor, x, yInit) > maxColorDiff) {
                break;
            }
            xMax = x;
        }
        // check up
        for (int y = yInit; y >= 0; y--) {
            if (filled[xInit][y] || colorDiff(centralColor, xInit, y) > maxColorDiff) {
                break;
            }
            yMin = y;
        }
        // check down
        for (int y = yInit; y < ycount; y++) {
            if (filled[xInit][y] || colorDiff(centralColor, xInit, y) > maxColorDiff) {
                break;
            }
            yMax = y;
        }
        xMin = min(xMin, xMax);
        xMax = max(xMin, xMax);
        yMin = min(yMin, yMax);
        yMax = max(yMin, yMax);
        int width = xMax - xMin + 1;
        int height = yMax - yMin + 1;
        if (width > height) {
            paintRect(centralColor, xMin, yMin, width, 1);
        } else if (height > width) {
            paintRect(centralColor, xMin, yMin, 1, height);
        } else {
            if (r.nextBoolean()) {
                paintRect(centralColor, xMin, yMin, width, 1);
            } else {
                paintRect(centralColor, xMin, yMin, 1, height);
            }
        }
    }

    private void paintRect(int color, int x, int y, int w, int h) {
        int x0 = x * STRIPE_WIDTH;
        int y0 = y * STRIPE_WIDTH;
        int width = w * STRIPE_WIDTH;
        int height = h * STRIPE_WIDTH;
        fill(color);
        rect(x0, y0, width, height);
//        textSize(10);
//        fill(0);
//        String l1 = x0 + "," + y0;
//        String l2 = width + "," + height;
//        text(l1, x0, y0 + 10);
//        text(l2, x0, y0 + 20);
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                filled[i][j] = true;
            }
        }
    }

    private double colorDiff(int baseColor, int x, int y) {
        int color = img.get(x * STRIPE_WIDTH, y * STRIPE_WIDTH);
        float rDiff = red(baseColor) - red(color);
        float gDiff = green(baseColor) - green(color);
        float bDiff = blue(baseColor) - blue(color);
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
}
