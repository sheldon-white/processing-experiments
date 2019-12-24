package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import processing.core.PImage;

public class ImageTest1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private Random r = new Random();
    private int cellSize = 50;
    private int cellMargin = 10;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 400;
        int outputHeight = 400;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        PImage foregroundHImage = loadImage("src/main/resources/lightwoodH1.jpg");
        PImage foregroundVImage = loadImage("src/main/resources/lightwoodV1.jpg");
        background(180);

        float x = (float) 99;
        float y = (float) 200;
        float w = (float) 40;
        float h = (float) 70;

        stroke(0);
        strokeWeight(4);
        noFill();
        PImage section;
        PImage source = foregroundHImage;
        int yimg = r.nextInt((int)(source.width - w));
        int ximg = r.nextInt((int)(source.height - h));
        section = source.get(ximg, yimg, (int)h, (int)w);

        pushMatrix();
        translate(x + w/2, y + h/2);
        rotate(PI / 2);
        imageMode(CENTER);
        image(section, 0, 0);
        rect(-h/2, -w/2, h, w);
        ellipse(0, 0, 2, 2);
        popMatrix();
        noLoop();
    }
}
