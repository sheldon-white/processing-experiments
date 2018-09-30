import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;

import java.util.Random;

public class ShapeUtils {
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
