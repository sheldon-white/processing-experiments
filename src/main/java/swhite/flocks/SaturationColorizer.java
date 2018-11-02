package swhite.flocks;

import processing.core.PApplet;

import java.util.Random;

import static processing.core.PConstants.HSB;

public class SaturationColorizer implements Colorizer {
    private int saturation;

    SaturationColorizer() {
        saturation = new Random().nextInt(256);
    }

    @Override
    public int nextColor(PApplet context) {
        int floor = 100;
        Random random = new Random();
        int h = floor + random.nextInt(256 - floor);
        int b = floor + random.nextInt(256 - floor);
        int a = floor + random.nextInt(256 - floor);
        context.colorMode(HSB, 256);
        return context.color(h, b, saturation, a);
    }
}
