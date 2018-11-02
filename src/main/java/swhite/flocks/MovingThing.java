package swhite.flocks;

import processing.core.PApplet;
import swhite.IntRect;

public class MovingThing {
    private IntRect bounds;
    private int fillColor;
    private int strokeColor;
    private boolean stopped;
    private Renderer renderer;

    MovingThing(int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
        this.bounds = new IntRect(x, y, w, h);
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.stopped = false;
        this.renderer = renderer;
    }

    void advance(float deltaX, float deltaY) {
        this.bounds.x += deltaX;
        this.bounds.y += deltaY;
    }

    void render(PApplet context) {
        renderer.render(context, this);
    }

    IntRect getBounds() {
        return bounds;
    }

    int getFillColor() {
        return fillColor;
    }

    int getStrokeColor() {
        return strokeColor;
    }

    public boolean isStopped() {
        return stopped;
    }
}
