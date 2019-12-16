package swhite;

import com.google.common.collect.Lists;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomCurves1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 150;
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
        noStroke();
        stroke(128);
        strokeWeight(1);

        for (int x = 0; x < outputWidth; x += cellSize) {
            for (int y = 0; y < outputHeight; y += cellSize) {
                drawCell(x, y);
            }
        }
        save(NAME + "." + System.currentTimeMillis() + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawCell(int x, int y) {
        stroke(128);
        strokeWeight(1);
        setRandomColor();
        rect(x, y, cellSize, cellSize);
        setRandomColor();
        //noStroke();
        randomCurve(x, y, cellSize, cellSize);
    }

    private void randomCurve(float x, float y, float w, float h) {
        int pointCount = 4 + r.nextInt(3);
        float b = 0.15f * min(w, h);
        float xi = x + b;
        float yi = y + b;
        float wi = w - 2 * b;
        float hi = h - 2 * b;
        float xstep = wi / 6;
        float ystep = hi / 6;

        List<FPoint> gridPoints = Lists.newArrayList();
        for (float xctr = 0, xcur = xi; xctr < 7; xctr++) {
            for (float yctr = 0, ycur = yi; yctr < 7; yctr++) {
                FPoint p = new FPoint(xcur, ycur);
                gridPoints.add(p);
                ycur += ystep;
            }
            xcur += xstep;
        }
        Collections.shuffle(gridPoints);
        beginShape();
        gridPoints.subList(0, pointCount).forEach(p -> curveVertex(p.x, p.y));
        gridPoints.subList(0, 3).forEach(p -> curveVertex(p.x, p.y));
        endShape(CLOSE);
    }

    private void setRandomColor() {
        int baseIntensity = 100;
        int variance = 155;
        int red = baseIntensity + r.nextInt(variance);
        int green = baseIntensity + r.nextInt(variance);
        int blue = baseIntensity + r.nextInt(variance);
        int alpha = 255;
        fill(red, green, blue, alpha);
    }
}

