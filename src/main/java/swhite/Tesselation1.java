package swhite;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class Tesselation1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int cellSize = 100;
    private int halfCellSize = cellSize / 2;
    private int outputWidth, outputHeight;

    private PImage img;
    private Random r = new Random();
    private static PApplet context;
    private float srcScaleX, srcScaleY;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("src/main/resources/flower.jpg");
        size(img.width, img.height);
        outputWidth = img.width;
        outputHeight = img.height;
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(0);
        srcScaleX = 1;
        srcScaleY = 1;

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

        switch (r.nextInt(8)) {
            case 0:
                fill(img.get((int) srcScaleX * (x + halfCellSize), (int) srcScaleY * (y + halfCellSize)));
                rect(x, y, cellSize, cellSize);
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

    private void drawTriangle(IPoint p0, IPoint p1, IPoint p2) {
        Triangle t = new Triangle(p0, p1, p2);
        IPoint tc = t.centroidCenter();
        int c = img.get((int) (srcScaleX * tc.x), (int) (srcScaleY * tc.y));
        fill(c);
        drawTriangle(t);
    }

    private void drawTriangle(Triangle t) {
        context.triangle(t.p0.x, t.p0.y, t.p1.x, t.p1.y, t.p2.x, t.p2.y);
    }
}

