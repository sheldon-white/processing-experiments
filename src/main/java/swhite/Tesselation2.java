package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class Tesselation2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 100;
    private int halfCellSize = cellSize / 2;
    private int outputWidth = 2200, outputHeight = 2900;

    private Random r = new Random();
    private static PApplet context;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(0);

        background(0);
        //noStroke();
        stroke(128);
        strokeWeight(1);

        for (int x = 0; x < outputWidth; x += cellSize) {
            for (int y = 0; y < outputHeight; y += cellSize) {
                tesselateSquare(x, y);
            }
        }
        save(NAME + "." + System.currentTimeMillis() + ".png");
        print("Done!\n");
        noLoop();
    }

    private void tesselateSquare(int x, int y) {
        IPoint ul = new IPoint(x, y);
        IPoint ur = new IPoint(x + cellSize, y);
        IPoint ll = new IPoint(x, y + cellSize);
        IPoint lr = new IPoint(x + cellSize, y + cellSize);
        IPoint cen = new IPoint(x + halfCellSize, y + halfCellSize);

        setRandomColor();
        rect(x, y, cellSize, cellSize);

        switch (r.nextInt(8)) {
            case 0:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                setRandomColor();
                arc(cen.x, cen.y, cellSize * 0.8f, cellSize * 0.8f, HALF_PI / 2, 3 * HALF_PI / 2);
                arc(cen.x, cen.y, cellSize * 0.8f, cellSize * 0.8f, 5 * HALF_PI / 2, 7 * HALF_PI / 2);
                break;
            case 1:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                break;
            case 2:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                setRandomColor();
                drawCircle(cen, cellSize * 0.5f);
                break;
            case 3:
                setRandomColor();
                drawCircle(cen, cellSize * 0.5f);
                break;
            case 4:
                setRandomColor();
                drawCircle(cen, cellSize * 0.5f);
                break;
            case 5:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                setRandomColor();
                arc(cen.x, cen.y, cellSize * 0.8f, cellSize * 0.8f, -HALF_PI, HALF_PI);
                break;
            case 6:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                setRandomColor();
                arc(cen.x, cen.y, cellSize * 0.8f, cellSize * 0.8f, 0, PI);
                break;
            case 7:
                setRandomColor();
                int xc = x + cellSize / 4;
                int yc = y + cellSize / 4;
                drawCircle(new IPoint(xc, yc), cellSize / 4);
                xc = x + 3 * cellSize / 4;
                yc = y + cellSize / 4;
                drawCircle(new IPoint(xc, yc), cellSize / 4);
                xc = x + 3 * cellSize / 4;
                yc = y + 3 * cellSize / 4;
                drawCircle(new IPoint(xc, yc), cellSize / 4);
                xc = x + cellSize / 4;
                yc = y + 3 * cellSize / 4;
                drawCircle(new IPoint(xc, yc), cellSize / 4);
                break;
        }
    }

    private void drawCircle(IPoint center, float radius) {
        ellipse(center.x, center.y, radius, radius);
    }

    private void setRandomColor() {
        noStroke();
        int baseIntensity = 100;
        int variance = 155;
        int red = baseIntensity + r.nextInt(variance);
        int green = baseIntensity + r.nextInt(variance);
        int blue = baseIntensity + r.nextInt(variance);
        int alpha = 255;
        fill(red, green, blue, alpha);
    }
}

