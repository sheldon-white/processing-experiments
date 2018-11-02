package swhite;

class Circle {
    Circle(IPoint center, int radius) {
        this.center = new IPoint(center);
        this.radius = radius;
    }

    boolean intersects(Circle c) {
        int dx = this.center.x - c.center.x;
        int dy = this.center.y - c.center.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < c.radius + this.radius;
    }

    IPoint center;
    int radius;
}