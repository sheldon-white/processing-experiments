package swhite;

import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Random;

public class PenrosedImage extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private static final float GOLDEN_RATIO = (1 + sqrt(5)) / 2;
    private int imgOffsetX, imgOffsetY, penroseSize;

    private PImage img;

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        img = loadImage("image7.jpg");
        penroseSize = 2 * Math.max(img.width, img.height);
        size(img.width, img.height);
        Random r = new Random();
        imgOffsetX = r.nextInt(400);
        imgOffsetY = r.nextInt(400);
        pixelDensity(1);
        smooth(8);
    }

    @Override
    public void draw() {
        background(0);
        ArrayList<Tile> tiles = generateWheelTiles(imgOffsetX, imgOffsetY);

        // how many times to divide tiles into sub-tiles
        int num_subdivisions = 11;
        for (int deflationNumber = 1; deflationNumber < num_subdivisions; deflationNumber++) {
            ArrayList<Tile> newTiles = new ArrayList<>();
            for (Tile tile : tiles) {
                newTiles.addAll(deflateTile(tile));
            }
            tiles = newTiles;
        }
        tiles.forEach(t -> {
            if (t.middle.x > 0 &&
                    t.middle.x < img.width &&
                    t.middle.y > 0 &&
                    t.middle.y < img.height) {
                t.draw();
            }
        });
        save(NAME + ".png");
        print("Done!\n");
        noLoop();
    }


    private ArrayList<Tile> deflateTile(Tile initialTile) {
        ArrayList<Tile> newTiles = new ArrayList<>();
        Point a = initialTile.a;
        Point b = initialTile.b;
        Point c = initialTile.c;

        if (initialTile.type == 0){
            Point p = a.plus((b.minus(a)).divide(GOLDEN_RATIO));

            newTiles.add(new Tile(0, c, p, b));
            newTiles.add(new Tile(1, p, c, a));
        } else {
            Point q = b.plus((a.minus(b)).divide(GOLDEN_RATIO));
            Point r = b.plus((c.minus(b)).divide(GOLDEN_RATIO));

            newTiles.add(new Tile(1, r, c, a));
            newTiles.add(new Tile(1, q, r, b));
            newTiles.add(new Tile(0, r, q, a));
        }

        return newTiles;
    }


    private ArrayList<Tile> generateWheelTiles(int offsetX, int offsetY) {
        ArrayList<Tile> tiles = new ArrayList<>();

        Point a = new Point(width/2f + offsetX, height/2f + offsetY);
        float r = penroseSize / 2f;
        float theta;

        for(int i=0; i<10; i++){
            theta = (2*i - 1) * PI / 10;
            Point b = new Point(r * cos(theta) + a.x, r * sin(theta) + a.y);
            theta = (2*i + 1) * PI / 10;
            Point c = new Point(r * cos(theta) + a.x, r * sin(theta) + a.y);

            // Reverse every other triangle so that it's mirrored, this matters later for decomposition
            if (i % 2 == 0) {
                tiles.add(new Tile(0, a, b, c));
            } else {
                tiles.add(new Tile(0, a, c, b));
            }
        }

        return tiles;
    }


    class Point {
        float x, y;

        Point(float x0, float y0) {
            x = x0;
            y = y0;
        }

        Point plus(Point p) {
            return new Point(x + p.x, y + p.y);
        }

        Point minus(Point p) {
            return new Point(x - p.x, y - p.y);
        }

        Point divide(float a) {
            return new Point(x / a, y / a);
        }

        Point multiply(float a) {
            return new Point(x * a, y * a);
        }
    }


    class Tile {
        int type;
        Point a, b, c, middle;

        Tile(int type0, Point a0, Point b0, Point c0) {
            type = type0;
            a = a0;
            b = b0;
            c = c0;
            middle = new Point((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3);
        }

        void draw() {
            int color = img.get((int)middle.x, (int)middle.y);
            fill(color);

            boolean drawBorders = true;
            if (drawBorders) {
                stroke(0, 0, 0, 100);
            } else {
                noStroke();
            }
            triangle(a.x, a.y, b.x, b.y, c.x, c.y);

            boolean debug = false;
            if (debug) {
                print(a.x, ",", a.y, " ", b.x, ",", b.y, " ", c.x, ",", c.y, "\n");
            }
        }
    }
}
