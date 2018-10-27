package flocks;

public class FPoint {
    FPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    FPoint(FPoint p) {
        this.x = p.x;
        this.y = p.y;
    }

    float x;
    float y;
}