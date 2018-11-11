package swhite;

import javafx.geometry.Point3D;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class Lissajous1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int time;
    private int displayWidth = 1000;
    private int displayHeight = 1000;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(displayWidth, displayHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        noStroke();
        background(0);
        init();
    }

    @Override
    public void draw() {
        int ctr = 300;
        while (ctr-- > 0) {
            FPoint p = pendulum(time);
            drawPoint(p);
            time++;
        }
    }

    private void drawPoint(FPoint p) {
        int x = (int)(p.x + displayWidth / 2);
        int y = (int)(p.y + displayHeight / 2);
       // println(x, y);
        stroke(255);
        point(x, y);
    }

    private double a1, a2, a3, a4;
    private double f1, f2, f3, f4;
    private double p1, p2, p3, p4;
    private double d1, d2, d3, d4;

    private void init() {
        Random r = new Random();
        a1 = 10 + r.nextInt(300);
        a2 = 10 + r.nextInt(300);
        a3 = 10 + r.nextInt(300);
        a4 = 10 + r.nextInt(300);
        f1 = 0.0001 + r.nextFloat() * 0.003;
        f2 = 0.0001 + r.nextFloat() * 0.003;
        f3 = 0.0001 + r.nextFloat() * 0.003;
        f4 = 0.0001 + r.nextFloat() * 0.003;
        p1 = r.nextFloat();
        p2 = r.nextFloat();
        p3 = r.nextFloat();
        p4 = r.nextFloat();
        d1 = 0.000001 + r.nextFloat() * .000001;
        d2 = 0.000001 + r.nextFloat() * .000001;
        d3 = 0.000001 + r.nextFloat() * .000001;
        d4 = 0.000001 + r.nextFloat() * .000001;
    }


    private FPoint pendulum(int t) {
        double x = a1 * Math.sin(f1 * t + p1) * Math.exp(-t * d1) + a2 * Math.sin(f2 * t + p2) * Math.exp(-t * d2);
        double y = a3 * Math.sin(f3 * t + p3) * Math.exp(-t * d3) + a4 * Math.sin(f4 * t + p4) * Math.exp(-t * d4);
        return new FPoint((float)x, (float)y);
    }


    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + ".png");
        } else if (key == 'r' || key == 'R') {
            setup();
        }
    }
}
