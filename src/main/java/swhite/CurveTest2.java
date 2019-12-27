package swhite;

import com.google.common.collect.Lists;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

public class CurveTest2 extends PApplet {
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
        int pointCount = 6;
        int ctr = 0;
        int steps = 20;
        float rwidth = 80;
        float rheight = 10;

        int pointDeltaX = this.width / (pointCount + 1);
        List<FPoint> points = Lists.newArrayList();
        List<BezierPoints> beziers = Lists.newArrayList();

        for (int i = 0; i < pointCount + 2; i++) {
            points.add(new FPoint(pointDeltaX * i,
                    this.height / 2 + 150 * r.nextFloat() * (r.nextBoolean() ? 1 : -1)));
        }

        noFill();
        noStroke();
        beginShape();
        vertex(points.get(0).x, points.get(0).y);
        FPoint controlPoint = getRandomControlPoint();
        FPoint previousPoint = null;

        for (FPoint p: points) {
            float cp1x = 0, cp1y = 0, cp2x = 0, cp2y = 0;
            if (ctr == 0) {
                vertex(p.x, p.y);
                ellipse(p.x, p.y, 5, 5);
            } else {
                cp1x = previousPoint.x + controlPoint.x;
                cp1y = previousPoint.y + controlPoint.y;
                controlPoint = getRandomControlPoint();
                cp2x = p.x - controlPoint.x;
                cp2y = p.y - controlPoint.y;

//                stroke(0, 100, 0);
//                line(previousPoint.x, previousPoint.y, cp1x, cp1y);
//                stroke(100, 0, 0);
//                line(p.x, p.y, cp2x, cp2y);
//                stroke(0);
                bezierVertex(cp1x, cp1y, cp2x, cp2y, p.x, p.y);
//                ellipse(p.x, p.y, 5, 5);
            }
            if (ctr != 0) {
                BezierPoints bezier = new BezierPoints();
                bezier.leftBounds = previousPoint;
                bezier.rightBounds = p;
                bezier.leftControl = new FPoint(cp1x, cp1y);
                bezier.rightControl = new FPoint(cp2x, cp2y);
                beziers.add(bezier);
            }
            previousPoint = p;
            ctr++;
        }
        endShape();
        stroke(0);

        int leftColor = ColorUtils.getRandomColor(this);
        int rightColor = ColorUtils.getRandomColor(this);
        for (BezierPoints b: beziers) {
            for (float i = 0; i < steps; i++) {
                float t = i / steps;

                float x1 = b.leftBounds.x;
                float x2 = b.leftControl.x;
                float x3 = b.rightControl.x;
                float x4 = b.rightBounds.x;
                float y1 = b.leftBounds.y;
                float y2 = b.leftControl.y;
                float y3 = b.rightControl.y;
                float y4 = b.rightBounds.y;
                float x = bezierPoint(x1, x2, x3, x4, t);
                float y = bezierPoint(y1, y2, y3, y4, t);
                // Get the tangent points
                float tx = bezierTangent(x1, x2, x3, x4, t);
                float ty = bezierTangent(y1, y2, y3, y4, t);
                float a = atan2(ty, tx) + HALF_PI;

                pushMatrix();
                translate(x, y);
                rotate(a);
               // stroke(0, 0, 255);
                fill(lerpColor(leftColor, rightColor, t));
                rect(-rwidth / 2, -rheight / 2, rwidth, rheight);
//                stroke(0);
//                ellipse(0, 0, 5, 5);
                popMatrix();
            }
            leftColor = rightColor;
            rightColor = ColorUtils.getRandomColor(this);
        }
        noLoop();
    }

    FPoint getRandomControlPoint() {
        return new FPoint(60 + r.nextFloat() * 60, 60 + r.nextFloat() * 60 * (r.nextBoolean() ? 1 : -1));
    }

    class BezierPoints {
        FPoint leftBounds;
        FPoint rightBounds;
        FPoint leftControl;
        FPoint rightControl;
    }
}
