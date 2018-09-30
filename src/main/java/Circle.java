public class Circle {
    Circle(IPoint center, int radius) {
        this.center = new IPoint(center);
        this.radius = radius;
    }

    boolean intersects(Circle c) {
        int dx = this.center.x - c.center.x;
        int dy = this.center.y - c.center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        boolean overlaps = distance < c.radius + this.radius;
        return overlaps;
    }

    IPoint center;
    int radius;
}