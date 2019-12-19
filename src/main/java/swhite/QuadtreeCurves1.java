package swhite;

import com.google.common.collect.Lists;
import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

public class QuadtreeCurves1 extends QuadtreeDesktopGenerator {
    private static int cellSize = 60;
    private static int quadTreeWidth = 80;
    private static int quadTreeHeight = 60;
    private static int maxQuadWidth = 8;
    private static int maxQuadHeight = 8;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeCurves1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeCurves1() {
        super(MethodHandles.lookup().lookupClass().getName(),
                maxQuadWidth, maxQuadHeight, quadTreeWidth, quadTreeHeight);
    }

    @Override
    public void initializeSettings() {
        size(cellSize * quadTreeWidth, cellSize * quadTreeHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    protected void initFrame() {
        background(220);
    }

    @Override
    protected void drawQuad(QuadRectangle q) {
        float x = (float) q.x * cellSize;
        float y = (float) q.y * cellSize;
        float w = (float) q.width * cellSize;
        float h = (float) q.height * cellSize;
        ColorUtils.fillWithRandomColor(context);
        stroke(80);
        strokeWeight(1);
        rect(x, y, w, h);
        ColorUtils.fillWithRandomColor(context);
        randomCurve(x, y, w, h);
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

    @Override
    protected IPoint getRandomBounds() {
        int w = 1 + r.nextInt(maxQuadWidth);
        int h = 1 + r.nextInt(maxQuadHeight);
        if (w / h > 3) {
            w = h * 3;
        } else if (h / w > 3) {
            h = w * 3;
        }
        return new IPoint(w, h);
    }
}

