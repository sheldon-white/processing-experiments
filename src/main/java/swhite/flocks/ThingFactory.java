package swhite.flocks;

public interface ThingFactory {
    public abstract MovingThing build(int x, int y, int w, int h, int fillColor, int strokeColor);
}
