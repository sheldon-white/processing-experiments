package flocks;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flock1 extends PApplet {
    private static final String NAME = "flocks.Flock1";
    private int outputWidth = 1500, outputHeight = 1000;
    private List<Flock> flocks = new ArrayList<>();

    private Renderer circleRenderer = (context, thing) -> {
        context.stroke(thing.getStrokeColor());
        context.strokeWeight(1);
        context.fill(thing.getFillColor());
        FloatRect bounds = thing.getBounds();
        context.ellipse(bounds.x, bounds.y, bounds.w, bounds.h);
    };

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        for (int i = 0; i < 10; i++) {
            Flock flock = Flock.build(this, new HueColorizer(this), circleRenderer);
            flocks.add(flock);
        }
    }

    @Override
    public void draw() {
        background(180);
        for (Flock flock: flocks) {
            flock.advance();
        }
    }
}
