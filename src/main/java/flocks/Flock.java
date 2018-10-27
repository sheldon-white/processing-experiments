package flocks;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flock {
    private PApplet context;
    private List<MovingThing> things;
    private float deltaX;
    private float deltaY;
    private float theta;

    public Flock(PApplet context) {
        this.context = context;
        this.things = new ArrayList<>();
    }

    public static Flock build(PApplet context, Colorizer colorizer, Renderer renderer) {
        Flock flock = new Flock(context);
        Lattice lattice = new Lattice();
        Random random = new Random();
        flock.deltaX = random.nextFloat() * 3 * (random.nextBoolean() ? 1 : -1);
        flock.deltaY = random.nextFloat() * 3 * (random.nextBoolean() ? 1 : -1);
        float initX = -flock.deltaX * lattice.getMaxDimension() * 2;
        float initY = -flock.deltaY * lattice.getMaxDimension() * 2;
        flock.theta = random.nextFloat() * 2 * (float)Math.PI;
        for (FloatRect cell: lattice.getCells()) {
            flock.things.add(new MovingThing(initX + cell.x, initY + cell.y, cell.w, cell.h, colorizer.nextColor(), colorizer.nextColor(), renderer));
        }

        return flock;
    }

    public void advance() {
        context.pushMatrix();
        context.rotate(theta);
        for (MovingThing thing: things) {
            thing.advance(deltaX, deltaY);
            thing.render(context);
            PApplet.println(thing.getBounds().x, thing.getBounds().y);
        }
        context.popMatrix();
    }

    private static int main(String[] args) {
        PApplet context = null;
        Flock flock = new Flock(context);
        return 0;
    }
}
