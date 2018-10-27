import processing.core.PApplet;

import java.util.Random;

public class ColorTest1 extends PApplet {
    private static final String NAME = "ColorTest1";
    private int cellSize = 10;
    private int outputWidth = 1000, outputHeight = 1000;
    private int xcount = 100;
    private int ycount = 100;

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

        background(180);
        //noStroke();
        stroke(0);
        strokeWeight(1);

        for (int y = 0; y < ycount; y++) {
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
        int s = x;
        int b = y;
        colorMode(HSB, 100);
        int c = color(h, s, b);
        println(c);
        fill(c);
        rect(x * cellSize, y * cellSize, cellSize, cellSize);
    }
}
