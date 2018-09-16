public class Point {
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point transform(int xoffset, int yoffset, double scale) {
        return new Point((int)(x * scale) + xoffset, (int)(y * scale) + yoffset);
    }

    int x;
    int y;
}