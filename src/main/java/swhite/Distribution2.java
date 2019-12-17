package swhite;

import java.lang.invoke.MethodHandles;

public class Distribution2 extends DesktopGenerator {
    private int cellSize = 50;
    private int outputWidth = 3000, outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;
    private int LINE_LENGTH = (int) (cellSize / 3);

    private int xh0 = LINE_LENGTH;
    private int xh1 = (int) (LINE_LENGTH * cos(PI / 3));
    private int yh1 = (int) (LINE_LENGTH * sin(PI / 3));

    public static void main(String[] args) {
        DesktopGenerator generator = new Distribution2();
        generator.cacheArgs(args);
        generator.run();
    }

    public Distribution2() {
        super(MethodHandles.lookup().lookupClass().getName());
    }

    @Override
    public void initializeSettings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void drawDesktop() {
        background(180);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        for (int y = 0; y < ycount; y++) {
            for (int x = 0; x < xcount; x++) {
                drawCell(x, y);
            }
        }
        doneDrawing = true;
    }

    private void drawCell(int x, int y) {
        int ctr = 0;
        int xs = x * cellSize + (cellSize / 2);
        int ys = y * cellSize + (cellSize / 2);
        int x0, y0, x1, y1;

        if (randomChoice(x, 0, xcount)) {
            x0 = xs + xh0;
            int yh0 = 0;
            y0 = ys + yh0;
            x1 = xs - xh0;
            y1 = y0;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (randomChoice(y, 0, ycount)) {
            x0 = xs + xh1;
            y0 = ys + yh1;
            x1 = xs - xh1;
            y1 = ys - yh1;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (randomChoice(y, 0, ycount)) {
            x0 = xs - xh1;
            y0 = ys + yh1;
            x1 = xs + xh1;
            y1 = ys - yh1;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }

        if (ctr == 3) {
            noStroke();
            fill(r.nextInt(255), r.nextInt(255), r.nextInt(255), 40 + r.nextInt(50));
            float radius = cellSize * (1 + 2 * r.nextFloat());
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
        double scaled = (double) (i - max / 2) / (max - min);
        double a = 10;
        double b = 0.7;
        double c = b * Math.exp(-scaled * scaled * a);
        return c > r.nextDouble();
    }
}
