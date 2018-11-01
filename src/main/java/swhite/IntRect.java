package swhite;

public class IntRect {
    public int x, y, w, h;

    public IntRect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public String toString() {
        return x + ", " + y + ", " + w + ", " + h;
    }
}
