package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class ColorTest1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private Random r = new Random();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 1000;
        int outputHeight = 1000;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {

        background(180);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        int ycount = 100;
        for (int y = 0; y < ycount; y++) {
            int xcount = 100;
            for (int x = 0; x < xcount; x++) {
                drawCell(x, y);
            }
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawCell(int x, int y) {
        int h = 50;
        colorMode(HSB, 100);
        int c = color(h, x, y);
        println(c);
        fill(c);
        int cellSize = 10;
        rect(x * cellSize, y * cellSize, cellSize, cellSize);
    }
}
