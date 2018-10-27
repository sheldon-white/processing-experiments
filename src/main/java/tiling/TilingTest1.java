package tiling;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TilingTest1 extends PApplet {
    private static final String NAME = "tiling.TilingTest1";
    private int outputWidth = 1000, outputHeight = 800;

    private PImage img;
    private Random r = new Random();
    private static PApplet context;
    private float srcScaleX, srcScaleY;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("monalisa.jpg");
        size(outputWidth, outputHeight);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        context = this;
        background(0);
        //noStroke();
        stroke(128);
        strokeWeight(1);

//        for (int x = 0; x < outputWidth; x += cellSize) {
//            for (int y = 0; y < outputHeight; y += cellSize) {
//                tesselateSquare(x, y);
//            }
//        }
//        save(NAME + ".png");
//        print("Done!\n");
//        noLoop();
    }

//    private TileSet buildTileSet() {
//        float sqrt32 = (float)Math.sqrt(3.0) / 2;
//        Tile s = new Tile('s',
//                Arrays.asList(new flocks.FPoint(0, 0), new flocks.FPoint(0,1), new flocks.FPoint(1, 1), new flocks.FPoint(1, 0)));
//        Tile t = new Tile('t',
//                Arrays.asList(new flocks.FPoint(0, 0), new flocks.FPoint(0.5f, sqrt32), new flocks.FPoint(1, 0)));
//        Tile h = new Tile('h',
//                Arrays.asList(new flocks.FPoint(0, 0), new flocks.FPoint(-0.5f, sqrt32), new flocks.FPoint(0, 2 * sqrt32), new flocks.FPoint(1, 2 * sqrt32), new flocks.FPoint(1.5f, sqrt32), new flocks.FPoint(1, 0)));
//        int vid = 0;
//        Set<VertexRule> tilings = new HashSet<>();
//        tilings.add(new VertexRule(vid++, Arrays.asList(s, s, s, s)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(h, h, h)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(t, t, t, t, t, t)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(t, t, t, s, s)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(t, t, s, t, s)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(h, h, t, t)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(h, t, t, t, t)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(h, t, h, t)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(s, s, t, h)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(s, s, h, t)));
//        tilings.add(new VertexRule(vid++, Arrays.asList(s, h, s, t)));
//
//        return new TileSet(tilings);
//    }
}

