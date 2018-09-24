import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Curves2 extends PApplet {
    private static final String NAME = "Curves2";
    private static final int OUTPUT_WIDTH = 1500, OUTPUT_HEIGHT = 1000;
    private static final int CELL_SIZE = 80;
    private static final int XCOUNT = OUTPUT_WIDTH / CELL_SIZE;
    private static final int YCOUNT = OUTPUT_HEIGHT / CELL_SIZE;
    private Random r = new Random();
    private static PApplet context;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(180);
        //noStroke();
        stroke(0);
        strokeWeight(3);
        List<Point> curve = randomCurve((int)(CELL_SIZE * 0.45));
        List<Float> deltas = new ArrayList<>();
        for (Point p: curve) {
            deltas.add((r.nextFloat() - 0.5F) * 4);
        }
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128),128 + r.nextInt(128));
        int dr = r.nextInt(10) - 5;
        int dg = r.nextInt(10) - 5;
        int db = r.nextInt(10) - 5;
        for (int x = 0; x < XCOUNT; x++) {
            for (int y = 0; y < YCOUNT; y++) {
                placeCurve(x, y, curve, deltas, color, dr, dg, db);
            }
        }
        print("Done!\n");
        save(NAME + ".png");
        noLoop();
    }

    private void placeCurve(int x, int y, List<Point>curve, List<Float> deltas, int color, int dr, int dg, int db) {
        int xs = x * CELL_SIZE + CELL_SIZE / 2;
        int ys = y * CELL_SIZE + CELL_SIZE / 2;
        float r = red(color) + x * dr;
        float g = red(color) + y * dg;
        float b = red(color) + (x + y) * db / 2;
        int c = color(r, g, b);
        stroke(0);
        strokeWeight(1);
        fill(c);
        beginShape();
        for (int i = 0; i < curve.size(); i++) {
            Point p = curve.get(i);
            context.curveVertex(p.x + xs + x * deltas.get(i), p.y + ys + x * deltas.get(i));
        }
        curveVertex(curve.get(0).x + xs + x * deltas.get(0), curve.get(0).y + ys + x * deltas.get(0));
        curveVertex(curve.get(1).x + xs + x * deltas.get(1), curve.get(1).y + ys + x * deltas.get(1));
        curveVertex(curve.get(2).x + xs + x * deltas.get(2), curve.get(2).y + ys + x * deltas.get(2));
        endShape();
    }

    private List<Point> randomCurve(int radius) {
        int x = 0, y = 0;
        int pointCount = 5 + r.nextInt(4);
        List<Point> points = new ArrayList<>();
        beginShape();
        for (int i = 0; i < pointCount; i++) {
            double angle = i * TWO_PI / pointCount + (0.5 * r.nextDouble());
            float px = x + (float)(Math.cos(angle) * radius * (r.nextDouble() + 0.3));
            float py = y + (float)(Math.sin(angle) * radius * (r.nextDouble() + 0.3));
            //print("angle: ", angle, " x: ", x, " y: ", y, " px: ", px, " py: ", py, "\n");
            points.add(new Point((int)px, (int)py));
        }
        return points;
    }
}
