package flocks;

import processing.core.PApplet;

import java.util.Random;

public class UniformColorizer implements Colorizer {
    private int color;

    public UniformColorizer(PApplet context) {
        int floor = 100;
        Random random = new Random();
        int r = floor + random.nextInt(256 - floor);
        int g = floor + random.nextInt(256 - floor);
        int b = floor + random.nextInt(256 - floor);
        int a = floor + random.nextInt(256 - floor);
        color = context.color(r, g, b, a);
    }

    @Override
    public int nextColor() {
        return color;
    }
}
