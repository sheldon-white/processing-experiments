package swhite;

import processing.core.PApplet;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class FunctionTest1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private Random r = new Random();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        int outputWidth = 1000;
        int outputHeight = 1000;
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void setup() {
        noStroke();
        background(0, 255);
    }

    @Override
    public void draw() {
        float interval = 0.01f;
        float delta = 0.0015f;
        float scale = 0.01f;
        pushMatrix();
        translate(this.width / 2, this.height / 2);
        float hw = this.width/2;
        float hh = this.height/2;

        for (float x = -hw; x < hw; x++) {
            for (float y = -hh; y < hh; y++) {
                float z = calc(x * scale, y * scale);
             //   float zdist = abs(delta - (z % interval));
                if (z % interval < delta) {
                    stroke(255, 255, 255);
                    point(x, y);
                }
            }
        }
        popMatrix();
        noLoop();
    }

    private float calc(float x, float y) {
        float p1 = sqrt((x+3) * (x-2) + y*y);
        float p2 = sqrt(5*x*(x-3) + 7*y*(y+1)) / 10;
        return exp(-p1 - p2);
    }
}
