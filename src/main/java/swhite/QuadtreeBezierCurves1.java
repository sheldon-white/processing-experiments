package swhite;

import com.google.common.collect.Lists;
import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;

public class QuadtreeBezierCurves1 extends DesktopGenerator {
    private int cellSize = 40;
    private int cellMargin = (int) (cellSize / 8);

    private int outputWidth = 3000;
    private int outputHeight = 2000;
    private int xcount = outputWidth / cellSize;
    private int ycount = outputHeight / cellSize;
    private static final boolean SPARCE = false;

    public static void main(String[] args) {
        DesktopGenerator generator = new QuadtreeBezierCurves1();
        generator.cacheArgs(args);
        generator.run();
    }

    public QuadtreeBezierCurves1() {
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
        float x = (float) q.x * cellSize + cellMargin;
        float y = (float) q.y * cellSize + cellMargin;
        float w = (float) q.width * cellSize - 2 * cellMargin;
        float h = (float) q.height * cellSize - 2 * cellMargin;
        int color = color(128 + r.nextInt(128), 128 + r.nextInt(128), 128 + r.nextInt(128));

        stroke(80);
        strokeWeight(2);
        fill(color);
        rect(x, y, w, h);
        randomCurve(x, y, w, h);
    }

    private void randomCurve(float x, float y, float w, float h) {
        int pointCount = 4 + r.nextInt(4);
        float b = 0.1f * min(w, h);
        List<FPoint> gridPoints = Lists.newArrayList();
        float xstep = w - 2 * b / 6;
        float ystep = h - 2 * b / 6;
        for (int xctr = 0; xctr < 7; xctr++) {
            for (int yctr = 0; yctr < 7; yctr++) {
                FPoint p = new FPoint(x + (xctr * xstep), y + (yctr * ystep));
                System.out.println(p.x + "," + p.y);
                gridPoints.add(p);
            }
        }
        Collections.shuffle(gridPoints);
        beginShape();
        gridPoints.subList(0, pointCount).forEach(p -> curveVertex(p.x, p.y));
        gridPoints.subList(0, 2).forEach(p -> curveVertex(p.x, p.y));
        endShape();
    }
}

