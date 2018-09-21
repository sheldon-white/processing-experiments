public class Circle {
    Circle(Point center, int radius) {
        this.center = new Point(center);
        this.radius = radius;
    }

    boolean intersects(Circle c) {
        int dx = this.center.x - c.center.x;
        int dy = this.center.y - c.center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        boolean overlaps = distance < c.radius + this.radius;
        return overlaps;
    }

    Point center;
    int radius;
}