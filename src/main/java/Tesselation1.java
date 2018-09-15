import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;

public class Tesselation1 extends PApplet {
    private static final String NAME = "Tesselation1";
    private static final int CELL_SIZE = 15;
    private static final int HALF_CELL_SIZE = CELL_SIZE / 2;
    private static final int OUTPUT_WIDTH = 2200, OUTPUT_HEIGHT = 2900;

    private PImage img;
    private Random r = new Random();
    private static PApplet context;
    private float srcScaleX, srcScaleY;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("monalisa.jpg");
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(0);
        srcScaleX = (float) img.width / OUTPUT_WIDTH;
        srcScaleY = (float) img.height / OUTPUT_HEIGHT;

        background(0);
        //noStroke();
        stroke(128);
        strokeWeight(1);

        for (int x = 0; x < OUTPUT_WIDTH; x += CELL_SIZE) {
            for (int y = 0; y < OUTPUT_HEIGHT; y += CELL_SIZE) {
                tesselateSquare(x, y);
            }
        }
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void tesselateSquare(int x, int y) {
        Point ul = new Point(x, y);
        Point ur = new Point(x + CELL_SIZE, y);
        Point ll = new Point(x, y + CELL_SIZE);
        Point lr = new Point(x + CELL_SIZE, y + CELL_SIZE);
        Point cen = new Point(x + HALF_CELL_SIZE, y + HALF_CELL_SIZE);

        switch (r.nextInt(8)) {
            case 0:
                fill(img.get((int)srcScaleX * (x + HALF_CELL_SIZE), (int)srcScaleY * (y + HALF_CELL_SIZE)));
                rect(x, y, CELL_SIZE, CELL_SIZE);
                break;
            case 1:
                drawTriangle(ul, ur, ll);
                drawTriangle(ur, ll, lr);
                break;
            case 2:
                drawTriangle(ul, ur, lr);
                drawTriangle(ul, ll, lr);
                break;
            case 3:
                drawTriangle(ul, ur, cen);
                drawTriangle(ul, ll, cen);
                drawTriangle(ur, ll, lr);
                break;
            case 4:
                drawTriangle(ul, ur, ll);
                drawTriangle(ur, lr, cen);
                drawTriangle(ll, lr, cen);
                break;
            case 5:
                drawTriangle(ul, ur, cen);
                drawTriangle(ur, lr, cen);
                drawTriangle(ul, ll, lr);
                break;
            case 6:
                drawTriangle(ul, ur, lr);
                drawTriangle(ul, ll, cen);
                drawTriangle(ll, lr, cen);
                break;
            case 7:
                drawTriangle(ul, ur, cen);
                drawTriangle(ul, ll, cen);
                drawTriangle(ur, lr, cen);
                drawTriangle(ll, lr, cen);
                break;
        }
    }

    private void drawTriangle(Point p0, Point p1, Point p2) {
        Triangle t = new Triangle(p0, p1, p2);
        Point tc = t.centroidCenter();
        int c = img.get((int)(srcScaleX * tc.x), (int)(srcScaleY * tc.y));
        fill(c);
        t.draw();
    }

    static class Point {
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        int x;
        int y;
    }

    static class Triangle {
        Triangle(Point p0, Point p1, Point p2) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
        }

        Point p0;
        Point p1;
        Point p2;

        Point centroidCenter() {
            int cx = ((p0.x + p1.x + p2.x) / 3);
            int cy = ((p0.y + p1.y + p2.y) / 3);
            return new Point(cx, cy);
        }

        void draw() {
            context.triangle(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y);
        }
    }
}

