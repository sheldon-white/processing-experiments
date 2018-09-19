import processing.core.PApplet;

import java.util.Random;

public class Distribution2 extends PApplet {
    private static final String NAME = "Distribution2";
    private static final int CELL_SIZE = 60;
    private static final int OUTPUT_WIDTH = 1500, OUTPUT_HEIGHT = 1000;
    private static final int XCOUNT = OUTPUT_WIDTH / CELL_SIZE;
    private static final int YCOUNT = OUTPUT_HEIGHT / CELL_SIZE;
    private static final int LINE_LENGTH = (int)(CELL_SIZE / 3);

    int xh0 = LINE_LENGTH;
    int yh0 = 0;
    int xh1 = (int)(LINE_LENGTH * cos(PI / 3));
    int yh1 = (int)(LINE_LENGTH * sin(PI / 3));

    private Random r = new Random();

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

        background(180);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        for (int y = 0; y < YCOUNT; y++) {
            for (int x = 0; x < XCOUNT; x++) {
                drawCell(x, y);
            }
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawCell(int x, int y) {
        int ctr = 0;
        int xs = x * CELL_SIZE + (CELL_SIZE / 2);
        int ys = y * CELL_SIZE + (CELL_SIZE / 2);
        int x0, y0, x1, y1;

        if (randomChoice(x, 0, XCOUNT)) {
            x0 = xs + xh0;
            y0 = ys + yh0;
            x1 = xs - xh0;
            y1 = y0;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (randomChoice(y, 0, YCOUNT)) {
            x0 = xs + xh1;
            y0 = ys + yh1;
            x1 = xs - xh1;
            y1 = ys - yh1;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (randomChoice(y, 0, YCOUNT)) {
            x0 = xs - xh1;
            y0 = ys + yh1;
            x1 = xs + xh1;
            y1 = ys - yh1;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }

        if (ctr == 3) {
            noStroke();
            fill(r.nextInt(255),r.nextInt(255),r.nextInt(255), 40 + r.nextInt(50));
            float radius = CELL_SIZE * (1 + 2 * r.nextFloat());
            ellipse(xs, ys, radius, radius);
        }
    }

    private void drawLine(int x0, int y0, int x1, int y1) {
        print(x0, y0, x1, y1, "\n");
        stroke(0);
        strokeWeight(2);
        line(x0, y0, x1, y1);
        strokeWeight(8);
        point(x0, y0);
        point(x1, y1);
    }

    private boolean randomChoice(int i, int min, int max) {
        double scaled = (double)(i - max / 2) / (max - min);
        double a = 10;
        double b = 0.7;
        double c = b * Math.exp(-scaled * scaled * a);
        return c > r.nextDouble();
    }
}
