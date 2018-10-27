package flocks;

import processing.core.PApplet;

public interface Renderer {
    void render(PApplet context, MovingThing thing);
}
