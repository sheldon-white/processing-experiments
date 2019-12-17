package swhite;

import java.lang.invoke.MethodHandles;

public class Distribution1 extends DesktopGenerator {
    private int cellSize = 40;
    private int outputWidth = 3000, outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    public static void main(String[] args) {
        DesktopGenerator generator = new Distribution1();
        generator.cacheArgs(args);
        generator.run();
    }

    public Distribution1() {
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

        for (int y = 0; y < ycount; y++) {
            for (int x = 0; x < xcount; x++) {
                drawCell(x, y);
            }
        }
        doneDrawing = true;
    }

    private void drawCell(int x, int y) {
        int ctr = 0;

        if (randomChoice(x, 0, xcount)) {
            int x0 = x * cellSize + 10;
            int y0 = y * cellSize + 10;
            int x1 = (x + 1) * cellSize - 10;
            int y1 = (y + 1) * cellSize - 10;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (randomChoice(y, 0, ycount)) {
            int x0 = x * cellSize + 10;
            int y0 = (y + 1) * cellSize - 10;
            int x1 = (x + 1) * cellSize - 10;
            int y1 = y * cellSize + 10;
            drawLine(x0, y0, x1, y1);
            ctr++;
        }
        if (ctr == 2) {
            noStroke();
            fill(r.nextInt(255), r.nextInt(255), r.nextInt(255), 40 + r.nextInt(50));
            float radius = cellSize * (1 + 2 * r.nextFloat());
            ellipse(x * cellSize + cellSize / 2f, y * cellSize + cellSize / 2f, radius, radius);
        }
    }

    private void drawLine(int x0, int y0, int x1, int y1) {
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
        double b = 0.6;
        double c = b * Math.exp(-scaled * scaled * a);
        return c > r.nextDouble();
    }
}
