public class FPoint {
    FPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    FPoint(FPoint p) {
        this.x = p.x;
        this.y = p.y;
    }

    FPoint transform(float xoffset, float yoffset, float scale) {
        return new FPoint(x * scale + xoffset, y * scale + yoffset);
    }

    float x;
    float y;
}