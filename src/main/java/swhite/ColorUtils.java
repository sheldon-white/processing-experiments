package swhite;

import processing.core.PApplet;

import java.util.Random;

public class ColorUtils {
    static void fillWithRandomColor(PApplet context) {
        context.fill(getRandomColor(context));
    }

    static int getRandomColor(PApplet context) {
        Random r = new Random();
        int baseIntensity = 100;
        int variance = 155;
        int red = baseIntensity + r.nextInt(variance);
        int green = baseIntensity + r.nextInt(variance);
        int blue = baseIntensity + r.nextInt(variance);
        int alpha = 255;
        return context.color(red, green, blue, alpha);
    }
}
