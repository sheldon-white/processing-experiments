package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class CurveTest1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private Random r = new Random();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 1000;
        int outputHeight = 800;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        background(180);
    }

    @Override
    public void draw() {
        FPoint ap1 = new FPoint(50, this.height / 2);
        FPoint ap2 = new FPoint(this.width - 50, this.height / 2);

        float x1 = ap1.x;
        float y1 = ap1.y;
        float x2 = 100;
        float y2 = 100;
        float x3 = 905;
        float y3 = 715;
        float x4 = ap2.x;
        float y4 = ap2.y;
        float rwidth = 200;
        float rheight = 20;
        noFill();
        stroke(0);
        bezier(x1, y1, x2, y2, x3, y3, x4, y4);
        float steps = 40;
        fill(255);
        stroke(200, 0, 200);
        ellipse(x1, y1, 5, 5);
        ellipse(x2, y2, 5, 5);
        ellipse(x3, y3, 5, 5);
        ellipse(x4, y4, 5, 5);

        for (int i = 0; i <= steps; i++) {
            float t = i / steps;
            // Get the location of the point
            float x = bezierPoint(x1, y2, x3, x4, t);
            float y = bezierPoint(y1, y2, y3, y4, t);
            // Get the tangent points
            float tx = bezierTangent(x1, x2, x3, x4, t);
            float ty = bezierTangent(y1, y2, y3, y4, t);
            float a = atan2(ty, tx) + HALF_PI;

            pushMatrix();
            translate(x, y);
            rotate(a);
            stroke(0, 0, 255);
            ColorUtils.fillWithRandomColor(this);
            rect(-rwidth / 2,-rheight / 2, rwidth, rheight);
            stroke(0);
            ellipse(0, 0, 5, 5);
            popMatrix();
        }
        noLoop();
    }

    private float calc(float x, float y) {
        float p1 = sqrt((x+3) * (x-2) + y*y);
        float p2 = sqrt(5*x*(x-3) + 7*y*(y+1)) / 10;
        return exp(-p1 - p2);
    }
}
