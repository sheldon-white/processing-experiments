package swhite.flocks;

import processing.core.PApplet;
import swhite.IntRect;

public class MovingThing {
    private IntRect bounds;
    private int fillColor;
    private int strokeColor;
    private boolean stopped;
    private Renderer renderer;

    public MovingThing(int x, int y, int w, int h, int fillColor, int strokeColor, Renderer renderer) {
        this.bounds = new IntRect(x, y, w, h);
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.stopped = false;
        this.renderer = renderer;
    }

    public void advance(float deltaX, float deltaY) {
        this.bounds.x += deltaX;
        this.bounds.y += deltaY;
    }

    public void render(PApplet context) {
        renderer.render(context, this);
    }

    public IntRect getBounds() {
        return bounds;
    }

    public int getFillColor() {
        return fillColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public boolean isStopped() {
        return stopped;
    }
}
