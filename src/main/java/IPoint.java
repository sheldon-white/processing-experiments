public class IPoint {
    IPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    IPoint(IPoint p) {
        this.x = p.x;
        this.y = p.y;
    }
    IPoint transform(int xoffset, int yoffset, double scale) {
        return new IPoint((int)(x * scale) + xoffset, (int)(y * scale) + yoffset);
    }

    int x;
    int y;
}