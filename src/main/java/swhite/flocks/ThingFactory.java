package swhite.flocks;

import processing.core.PApplet;

public interface ThingFactory {
    MovingThing build(PApplet context, int x, int y, int w, int h, int fillColor, int strokeColor);

    ThingFactory createNew();
}
