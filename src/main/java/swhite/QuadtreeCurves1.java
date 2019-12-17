package swhite;

import com.google.common.collect.Lists;
import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

public class QuadtreeCurves1 extends DesktopGenerator {
    private int cellSize = 60;

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;

    private static final boolean SPARCE = false;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeCurves1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeCurves1() {
        super(MethodHandles.lookup().lookupClass().getName());
    }

    @Override
    public void initializeSettings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void drawDesktop() {
        background(220);
        stroke(0);
        strokeWeight(1.5F);

        QuadRectangle bounds = new QuadRectangle(0, 0, xcount, ycount);
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, xcount, ycount), 0, 1, 4);
        int emptyCells = xcount * ycount;
        while (emptyCells > 0) {
            int x = r.nextInt(xcount);
            int y = r.nextInt(ycount);
            int w = 1 + r.nextInt(8);
            int h = 1 + r.nextInt(8);
            if (w / h > 3) {
                w = h * 3;
            } else if (h / w > 3) {
                h = w * 3;
            }
            if (x + w > xcount) {
                continue;
            }
            if (y + h > ycount) {
                continue;
            }
            QuadRectangle q = new QuadRectangle(x, y, w, h);
            List<QuadRectangle> hits = quadTree.getElements(q);
            boolean fits = true;
            for (QuadRectangle c : hits) {
                if (q != c && ShapeUtils.rectanglesIntersect(q, c)) {
                    fits = false;
                    break;
                }
            }
            if (fits) {
                quadTree.insert(q, q);
                emptyCells -= w * h;
            }
        }

        for (QuadRectangle q : quadTree.getElements(bounds)) {
            if (SPARCE && q.width == 1 && q.height == 1) {
                continue;
            }
            drawQuad(q);
        }
        doneDrawing = true;
    }

    private void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize;
        float y = (float) q.y * cellSize;
        float w = (float) q.width * cellSize;
        float h = (float) q.height * cellSize;
        fillWithRandomColor();
        stroke(80);
        strokeWeight(1);
        rect(x, y, w, h);
        fillWithRandomColor();
        randomCurve(x, y, w, h);
    }

    private void fillWithRandomColor() {
        int baseIntensity = 100;
        int variance = 155;
        int red = baseIntensity + r.nextInt(variance);
        int green = baseIntensity + r.nextInt(variance);
        int blue = baseIntensity + r.nextInt(variance);
        int alpha = 255;
        fill(red, green, blue, alpha);
    }

    private void randomCurve(float x, float y, float w, float h) {
        int pointCount = 4 + r.nextInt(3);
        float b = 0.2f * min(w, h);
        float xi = x + b;
        float yi = y + b;
        float wi = w - 2 * b;
        float hi = h - 2 * b;
        float xstep = wi / 6;
        float ystep = hi / 6;

        List<FPoint> gridPoints = Lists.newArrayList();
        for (float xctr = 0, xcur = xi; xctr < 7; xctr++) {
            for (float yctr = 0, ycur = yi; yctr < 7; yctr++) {
                FPoint p = new FPoint(xcur, ycur);
                gridPoints.add(p);
                ycur += ystep;
            }
            xcur += xstep;
        }
        Collections.shuffle(gridPoints);
        beginShape();
        gridPoints.subList(0, pointCount).forEach(p -> curveVertex(p.x, p.y));
        gridPoints.subList(0, 3).forEach(p -> curveVertex(p.x, p.y));
        endShape(CLOSE);
    }
}

