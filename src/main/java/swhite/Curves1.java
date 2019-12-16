package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Curves1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private static final double TWO_PI = Math.PI * 2;
    private int outputWidth = 1500, outputHeight = 1000;
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

        for (int i = 0; i < 40; i++) {
            drawRandomCurve();
        }

        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }

    private void drawRandomCurve() {
        int radius = 50 + r.nextInt(100);
        int x = radius + r.nextInt(outputWidth - 2 * radius);
        int y = radius + r.nextInt(outputHeight - 2 * radius);
        int pointCount = 4 + r.nextInt(6);
        List<IPoint> points = new ArrayList<>();
        beginShape();
        for (int i = 0; i < pointCount; i++) {
            double angle = i * TWO_PI / pointCount + (0.5 * r.nextDouble());
            float px = x + (float) (Math.cos(angle) * radius * (r.nextDouble() + 0.5));
            float py = y + (float) (Math.sin(angle) * radius * (r.nextDouble() + 0.5));
            //print("angle: ", angle, " x: ", x, " y: ", y, " px: ", px, " py: ", py, "\n");
            strokeWeight(4);
            points.add(new IPoint((int) px, (int) py));
            //point(px, py);
        }
        curveVertex(points.get(0).x, points.get(0).y);
        curveVertex(points.get(1).x, points.get(1).y);
        curveVertex(points.get(2).x, points.get(2).y);
        strokeWeight(1);
        fill(r.nextInt(255), r.nextInt(255), r.nextInt(255), 40 + r.nextInt(50));
        endShape();
        //ellipse(x, y, 2 * radius, 2 * radius);
    }
}
