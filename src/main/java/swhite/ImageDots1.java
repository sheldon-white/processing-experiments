package swhite;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class ImageDots1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private Random r = new Random();
    private static String imageName = "image4.jpg";
    private PImage image;

    public static void main(String args[]) {
        if (args.length > 0 && args[0] != null) {
            imageName = args[0];
        }
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        pixelDensity(1);
        smooth(8);
        image = loadImage(imageName);
        size(image.width, image.height);
    }

    @Override
    public void setup() {
        int backgroundColor = 0;
        background(backgroundColor);
    }

    @Override
    public void draw() {
        int minRadius = 10;
        int tries = 100;
        while (tries-- > 0) {
            int radius = 10 + r.nextInt(minRadius);
            int x = radius + r.nextInt(width - 2 * radius);
            int y = radius + r.nextInt(height - 2 * radius);
            int color = image.get(x, y);
            Circle circle = new Circle(new IPoint(x, y), radius);
            drawCircle(circle, color);
        }
    }


    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        } else if (key == 'd' || key == 'D') {
            noLoop();
        }
    }

    private void drawCircle(Circle c, int color) {
        noStroke();
        int alpha = 128;
        float x = (float)c.center.x;
        float y = (float)c.center.y;
        float w = 2 * (float)c.radius;
        int colorWithAlpha = (color & 0xffffff) | (alpha << 24);
        fill(colorWithAlpha);
        ellipse(x, y, w, w);
    }
}

