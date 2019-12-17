package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class Arrows1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 75;
    private int halfCellSize = cellSize / 2;
    private int outputWidth = 3000, outputHeight = 2000;

    private Random r = new Random();

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
        background(0);
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
        IPoint cen = new IPoint(x + halfCellSize, y + halfCellSize);

        setRandomColor();
        rect(x, y, cellSize, cellSize);

        switch (r.nextInt(3)) {
            case 0:
                drawArrow(new FRect(x, y, cellSize, cellSize), 2 * PI * r.nextFloat());
                break;
            case 1:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
                drawArrow(new FRect(x, y, cellSize, cellSize), 2 * PI * r.nextFloat());
                break;
            case 2:
                setRandomColor();
                drawCircle(cen, cellSize * 0.8f);
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

    private void drawArrow(FRect bounds, float theta) {
        float xc = bounds.x + bounds.w / 2;
        float yc = bounds.y + bounds.h / 2;
        pushMatrix();
        translate(xc, yc);
        rotate(theta);
        fillWithRandomColor();
        float b = bounds.w / 6;
        float wi = bounds.w - 2 * b;
        float hi = bounds.h - 2 * b;
        float y0 = -hi / 2;
        float y2 = 0;
        float y3 = hi / 2;
        float x0 = -wi / 2;
        float x1 = wi / 2 - (hi / 2);
        float x2 = wi / 2;
        rect(x0, -hi / 6, x1 - x0 + 3, hi / 3);
        triangle(x1, y0, x2, y2, x1, y3);
        popMatrix();
    }

    private void fillWithRandomColor() {
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

