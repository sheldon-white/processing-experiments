import javafx.geometry.Point3D;
import processing.core.PApplet;
import processing.core.PFont;
import sun.security.util.DisabledAlgorithmConstraints;

import java.util.*;
import java.util.stream.Collectors;

public class Lorentz1 extends PApplet {
    private static final String NAME = "Lorentz1";
    private Random random = new Random();
    DisplayedPoint p;
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
        double x = p.current.getX() + dt * delta.getX();
        double y = p.current.getY() + dt * delta.getY();
        double z = p.current.getZ() + dt * delta.getZ();
        next.current = new Point3D(x, y, z);
        return next;
    }

    //    def lorenz(x, y, z, s=10, r=28, b=2.667):
//    x_dot = s*(y - x)
//    y_dot = r*x - y - x*z
//            z_dot = x*y - b*z
//    return x_dot, y_dot, z_dot
    private double s = 10, r = 28, b = 2.667, dt = 0.01;

    private Point3D lorenz(Point3D current) {
        double x = current.getX();
        double y = current.getY();
        double z = current.getZ();
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
            float x = (float)current.getX() * 10 + width / 2;
            float y = (float)current.getY() * 10 + height / 2;

            float r = 255 * x / width;
            float g = 255 * y / height;
            float b = (float)current.getZ() * 2 + 50;
//            pushMatrix();
//            translate(x, y);
            stroke(color(r, g, b, alpha));
            point(x, y);
 //           popMatrix();
            alpha -= dAlpha;
            return this;
        }
    }
}
