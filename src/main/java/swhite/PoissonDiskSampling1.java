package swhite;

import processing.core.PApplet;
import processing.core.PVector;

import java.lang.invoke.MethodHandles;
import java.util.List;


public class PoissonDiskSampling1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private int margin = 20;
    private int a;
    private int radius = 10;
    float cellsize = (int)Math.floor(radius / Math.sqrt(2));

    private List<PVector> plist;

    public PoissonDiskSampling1() {
        a = 0;
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(1600, 1000);
        pixelDensity(1);
        smooth(8);
    }

    public void setup() {
        background(255);
        PoissonDiskSampling diskSampling = new PoissonDiskSampling(margin, margin, width - 2 * margin, height - 2 * margin, radius, 30);
        plist = diskSampling.calculatePoints();
    }

    public void draw() {
        for (int c = 0; c < 100; c++) {
            if (a < plist.size()) {
                stroke(0);
                noFill();
                float px = plist.get(a).x;
                float py = plist.get(a).y;
                float left = px - (px % cellsize);
                float top = py - (py % cellsize);
                stroke(128);
                rect(left, top, cellsize, cellsize);
                stroke(0);
                ellipse(px, py, 1, 1);
                a++;
            } else {
                println("Done!");
                noLoop();
            }
        }
    }
}