package swhite;

import javafx.geometry.Point3D;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;

public class Lorentz1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private DisplayedPoint p;

    public Lorentz1() {
        r = 28;
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 900;
        int outputHeight = 900;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        noStroke();
        background(0, 255);
        p = new DisplayedPoint();
        p.current = new Point3D(5, 5, 5);
        p.alpha = 255;
    }

    @Override
    public void draw() {
        int ctr = 100;
        while (ctr-- > 0) {
            p.draw();
            p = advancePoint(p);
        }
    }

    private DisplayedPoint advancePoint(DisplayedPoint p) {
        Point3D delta = lorenz(p.current);
        DisplayedPoint next = new DisplayedPoint();
        next.alpha = 255;
        double dt = 0.01;
        double x = p.current.getX() + dt * delta.getX();
        double y = p.current.getY() + dt * delta.getY();
        double z = p.current.getZ() + dt * delta.getZ();
        next.current = new Point3D(x, y, z);
        return next;
    }

    private double r;

    private Point3D lorenz(Point3D current) {
        double x = current.getX();
        double y = current.getY();
        double z = current.getZ();
        double s = 10;
        double b = 2.667;
        return new Point3D(s * (y - x),
                r * x - y - x * z,
                x * y - b * z);
    }

    private class DisplayedPoint {
        Point3D current;
        int color = 255;
        int alpha;
        int dAlpha = 1;

        DisplayedPoint draw() {
            float x = (float)current.getX() * 10 + width / 2f;
            float y = (float)current.getY() * 10 + height / 2f;

            float r = 255 * x / width;
            float g = 255 * y / height;
            float b = (float)current.getZ() * 2 + 50;
            stroke(color(r, g, b, alpha));
            point(x, y);
            alpha -= dAlpha;
            return this;
        }
    }
}
