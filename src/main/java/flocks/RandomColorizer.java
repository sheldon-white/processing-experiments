package flocks;

import processing.core.PApplet;

import java.util.Random;

public class RandomColorizer implements Colorizer {
    private PApplet context;

    public RandomColorizer(PApplet context) {
        this.context = context;
    }

    @Override
    public int nextColor() {
        int floor = 100;
        Random random = new Random();
        int r = floor + random.nextInt(256 - floor);
        int g = floor + random.nextInt(256 - floor);
        int b = floor + random.nextInt(256 - floor);
        int a = floor + random.nextInt(256 - floor);
        return context.color(r, g, b, a);
    }
}
