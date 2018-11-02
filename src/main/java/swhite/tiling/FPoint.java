package swhite.tiling;

public class FPoint {
    public FPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FPoint(FPoint p) {
        this.x = p.x;
        this.y = p.y;
    }

    public FPoint transform(float xoffset, float yoffset, float scale) {
        return new FPoint(x * scale + xoffset, y * scale + yoffset);
    }

    private float x;
    private float y;
}