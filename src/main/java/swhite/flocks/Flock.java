package swhite.flocks;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;
import swhite.IntRect;
import swhite.ShapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flock {
    private PApplet context;
    private List<MovingThing> things = new ArrayList<>();
    private float deltaX, deltaY;
    private boolean wasDisplayed = false;
    private boolean done = false;
    private IntRect contextBounds;

    private Flock(PApplet context) {
        this.context = context;
        this.contextBounds = new IntRect(0, 0, context.width, context.height);
    }

    static Flock build(PApplet context, Colorizer colorizer, ThingFactory thingFactory) {
        Flock flock = new Flock(context);
        Random random = new Random();
        float delta = (2 + random.nextFloat() * 3) * (random.nextBoolean() ? 1 : -1);
        float multiplier = 1.5f + random.nextFloat();
        boolean horizontal = random.nextBoolean();
        int xOffset = 0, yOffset = 0;
        int minWidth = context.width;
        int minHeight = context.height;
        if (horizontal) {
            flock.deltaX = delta;
            minWidth *= multiplier;
        } else {
            flock.deltaY = delta;
            minHeight *= multiplier;
        }

        Lattice lattice = new Lattice(minWidth, minHeight);
        QuadRectangle bounds = lattice.getBounds();
        if (horizontal) {
            xOffset = (int)bounds.width * (delta > 0 ? -1 : 1);
        } else {
            yOffset = (int)bounds.height * (delta > 0 ? -1 : 1);
        }
        for (IntRect cell: lattice.getCells()) {
            flock.things.add(thingFactory.build(context, xOffset + cell.x, yOffset + cell.y, cell.w, cell.h, colorizer.nextColor(context), colorizer.nextColor(context)));
        }

        return flock;
    }

    void advance() {
        boolean thingWasDisplayed = false;
        for (MovingThing thing: things) {
            thing.advance(deltaX, deltaY);
            //context.println(thing.getBounds());
            if (ShapeUtils.rectanglesIntersect(contextBounds, thing.getBounds())) {
                thing.render(context);
                wasDisplayed = true;
                thingWasDisplayed = true;
            }
        }
        if (wasDisplayed && !thingWasDisplayed) {
            done = true;
        }
    }

    public boolean done() {
        return done;
    }
}
