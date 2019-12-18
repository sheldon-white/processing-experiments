package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.Random;

public class ShapeUtils {

    static boolean centerCharacterInRect(PApplet context,
                                         PFont font,
                                         char c,
                                         float x,
                                         float y,
                                         float w,
                                         float h) {
        PFont.Glyph glyph = font.getGlyph(c);
        if (glyph != null) {
            context.pushMatrix();
            context.translate(x + (glyph.leftExtent + w - glyph.width) / 2,
                    y + glyph.topExtent + (h - glyph.height) / 2);
            context.textFont(font);
            context.text(c, 0, 0);
            context.popMatrix();
            return true;
        }
        return false;
    }

    static void drawArrow(PApplet context, float xc, float yc, float w, float h, float theta) {
        context.pushMatrix();
        context.translate(xc, yc);
        context.rotate(theta);
        float b = h / 8;
        float wi = w - 2 * b;
        float hi = h - 2 * b;
        float y0 = -hi / 2;
        float y2 = 0;
        float y3 = hi / 2;
        float x0 = -wi / 2;
        float x1 = wi / 2 - (hi / 2);
        float x2 = wi / 2;
        x1 = context.min(x1, x2);
        x2 = context.max(x1, x2);
        context.triangle(x1, y0, x2, y2, x1, y3);
        context.rect(x0, -hi / 4, x1 - x0 + 1, hi / 2);
        context.popMatrix();
    }

    public static Triangle randomTriangle(int x, int y, int width, int height) {
        Random r = new Random();
        IPoint p0 = new IPoint(x + r.nextInt(width), y + r.nextInt(height));
        IPoint p1 = new IPoint(x + r.nextInt(width), y + r.nextInt(height));
        IPoint p2 = new IPoint(x + r.nextInt(width), y + r.nextInt(height));
        return new Triangle(p0, p1, p2);
    }

    public static boolean rectanglesIntersect(QuadRectangle r1, QuadRectangle r2) {
        return !(r1.x + r1.width <= r2.x ||
                r2.x + r2.width <= r1.x ||
                r1.y + r1.height <= r2.y ||
                r2.y + r2.height <= r1.y);
    }

    public static boolean rectanglesIntersect(IntRect r1, IntRect r2) {
        return !(r1.x + r1.w <= r2.x ||
                r2.x + r2.w <= r1.x ||
                r1.y + r1.h <= r2.y ||
                r2.y + r2.h <= r1.y);
    }

    public static boolean trianglesIntersect(Triangle t0, Triangle t1) {
        return segmentsIntersect(t0.p0, t0.p1, t1.p0, t1.p1) ||
                segmentsIntersect(t0.p1, t0.p2, t1.p0, t1.p1) ||
                segmentsIntersect(t0.p2, t0.p0, t1.p0, t1.p1) ||
                segmentsIntersect(t0.p0, t0.p1, t1.p1, t1.p2) ||
                segmentsIntersect(t0.p1, t0.p2, t1.p1, t1.p2) ||
                segmentsIntersect(t0.p2, t0.p0, t1.p1, t1.p2) ||
                segmentsIntersect(t0.p0, t0.p1, t1.p2, t1.p0) ||
                segmentsIntersect(t0.p1, t0.p2, t1.p2, t1.p0) ||
                segmentsIntersect(t0.p2, t0.p0, t1.p2, t1.p0) ||
                pointInsideTriangle(t0.p0, t1) ||
                pointInsideTriangle(t1.p0, t0);
    }

    public static QuadRectangle transform(QuadRectangle q, int xoffset, int yoffset, double scale) {
        return new QuadRectangle(q.x * scale + xoffset, q.y * scale + yoffset, q.width * scale, q.height * scale);
    }

    public static boolean pointInsideTriangle(IPoint s, Triangle t) {
        int as_x = s.x - t.p0.x;
        int as_y = s.y - t.p0.y;

        boolean s_ab = (t.p1.x - t.p0.x) * as_y - (t.p1.y - t.p0.y) * as_x > 0;

        if ((t.p2.x - t.p0.x) * as_y - (t.p2.y - t.p0.y) * as_x > 0 == s_ab) return false;

        return (t.p2.x - t.p1.x) * (s.y - t.p1.y) - (t.p2.y - t.p1.y) * (s.x - t.p1.x) > 0 == s_ab;
    }

    public static boolean segmentsIntersect(IPoint s11, IPoint s12, IPoint s21, IPoint s22) {
        return ccw(s11, s21, s22) != ccw(s12, s21, s22) && ccw(s11, s12, s21) != ccw(s11, s12, s22);
    }


    private static boolean ccw(IPoint a, IPoint b, IPoint c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }
}
