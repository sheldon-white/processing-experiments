package swhite;

import javafx.geometry.Point3D;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Lorentz2 extends PApplet {
    private static final String NAME = "Lorentz2";
    private List<DisplayedPoint> displayedPoints = new ArrayList<>();

    public Lorentz2() {
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
        DisplayedPoint p = new DisplayedPoint();
        p.current = new Point3D(5, 5, 5);
        p.alpha = 255;
        displayedPoints.add(p);
    }

    @Override
    public void draw() {
        background(100, 255);
        int maxPoints = 400;
        if (displayedPoints.size() < maxPoints) {
            displayedPoints.add(advancePoint(displayedPoints.get(displayedPoints.size() - 1)));
        }
        displayedPoints = displayedPoints.stream().filter(w -> w.alpha >= 0).map(DisplayedPoint::draw).collect(Collectors.toList());
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
        //    def lorenz(x, y, z, s=10, r=28, b=2.667):
        //    x_dot = s*(y - x)
        //    y_dot = r*x - y - x*z
        //            z_dot = x*y - b*z
        //    return x_dot, y_dot, z_dot
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
        //private Random random = new Random();

        DisplayedPoint draw() {
            float x = (float)current.getX() * 10 + width / 2f;
            float y = (float)current.getY() * 10 + height / 2f;
            fill(color, alpha);
            ellipse(x, y, 10, 10);
            alpha -= dAlpha;
            return this;
        }
    }
}
