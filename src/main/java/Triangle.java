public class Triangle {
    Triangle(Point p0, Point p1, Point p2) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    Point p0;
    Point p1;
    Point p2;

    Point centroidCenter() {
        int cx = ((p0.x + p1.x + p2.x) / 3);
        int cy = ((p0.y + p1.y + p2.y) / 3);
        return new Point(cx, cy);
    }

    Triangle transform(int xoffset, int yoffset, double scale) {
        return new Triangle(p0.transform(xoffset, yoffset, scale), p1.transform(xoffset, yoffset, scale), p2.transform(xoffset, yoffset, scale));
    }
}