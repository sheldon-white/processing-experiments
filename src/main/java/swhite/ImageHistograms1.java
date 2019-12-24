package swhite;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;
import processing.core.PImage;

import java.lang.invoke.MethodHandles;
import java.util.PriorityQueue;

public class ImageHistograms1 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();

    private PriorityQueue<Patch> patchQueue = new PriorityQueue<>(5,(a, b) -> (int)(b.score - a.score));
    private PImage image;
    private int xOffset, yOffset;
    private static String imageName = "src/main/resources/image6.jpg";
    private int backgroundColor = 80;

    public static void main(String args[]) {
        if (args.length > 0 && args[0] != null) {
            imageName = args[0];
        }
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        image = loadImage(imageName);
        int size = min(image.width, image.height);
        int diff = Math.abs(image.width - image.height);
        xOffset = image.width > image.height ? diff / 2 : 0;
        yOffset = image.width > image.height ? 0 : diff / 2;
        size(size, size);
        smooth(8);
        pixelDensity(1);
    }

    @Override
    public void setup() {
        colorMode(RGB, 255);
//        strokeWeight(1);
//        stroke(128);
        noStroke();
        background(backgroundColor);

        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, width, height), 0, 1, 4);
        Patch patch = new Patch();
        patch.quad = quadTree.getZone();
        patch.colorFromHistogram(image);
        patchQueue.add(patch);
    }

    @Override
    public void draw() {
        if (patchQueue.size() > 0) {
            print("queue: ", patchQueue.size(), "\n");
        }
        for (int ctr = 0; ctr < 200; ctr++) {
            Patch nextPatch = patchQueue.poll();
            if (nextPatch != null) {
                int scoreThreshold = 50;
                if (nextPatch.score > scoreThreshold) {
                    QuadRectangle q = nextPatch.quad;
                    double w2 = q.width / 2;
                    double h2 = q.height / 2;
                    if (w2 >= 4) {
                        nextPatch.erase();

                        Patch p0 = new Patch();
                        p0.quad = new QuadRectangle(q.x, q.y, w2, h2);
                        p0.colorFromHistogram(image);
                        patchQueue.add(p0);
                        p0.draw();

                        Patch p1 = new Patch();
                        p1.quad = new QuadRectangle(q.x + w2, q.y, w2, h2);
                        p1.colorFromHistogram(image);
                        patchQueue.add(p1);
                        p1.draw();

                        Patch p2 = new Patch();
                        p2.quad = new QuadRectangle(q.x, q.y + h2, w2, h2);
                        p2.colorFromHistogram(image);
                        patchQueue.add(p2);
                        p2.draw();

                        Patch p3 = new Patch();
                        p3.quad = new QuadRectangle(q.x + w2, q.y + h2, w2, h2);
                        p3.colorFromHistogram(image);
                        patchQueue.add(p3);
                        p3.draw();
                    } else {
                        nextPatch.draw();
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + "." + System.currentTimeMillis() + ".png");
        }
    }

    class Patch {
        boolean circleFill = true;
        QuadRectangle quad;
        int[] rHist = new int[256];
        int[] gHist = new int[256];
        int[] bHist = new int[256];

        int color = 0;
        double error = 0, score = 0;

        public void draw() {
            fill(color);

            if (circleFill) {
                noStroke();
                ellipse((float)(quad.x + quad.width / 2), (float)(quad.y + quad.height / 2), (float)quad.width, (float)quad.height);
            } else {
                strokeWeight(1);
                stroke(128);
                rect((float)quad.x, (float)quad.y, (float)quad.width, (float)quad.height);
            }
        }

        void erase() {
            fill(backgroundColor);
            rect((float)quad.x, (float)quad.y, (float)quad.width, (float)quad.height);
        }

        private void hist(PImage image) {
            int xi = (int)quad.x + xOffset;
            int yi = (int)quad.y + yOffset;
            for (int _x = xi; _x < xi + quad.width; _x++) {
                for (int _y = yi; _y < yi + (int)quad.height; _y++) {
                    int c = image.get(_x + xOffset, _y + yOffset);
                    int r = c >> 16 & 0xFF;
                    rHist[r]++;
                    int g = c >> 8 & 0xFF;
                    gHist[g]++;
                    int b = c & 0xFF;
                    bHist[b]++;
                }
            }
        }

        private void colorFromHistogram(PImage image) {
            hist(image);
            int rTotal = 0, bTotal = 0, gTotal = 0;
            double rError = 0, gError = 0, bError = 0;
            long rValue = 0, gValue = 0, bValue = 0;

            for (int i = 0; i < 256; i++) {
                rTotal += rHist[i];
                rValue += rHist[i] * i;
                gTotal += gHist[i];
                gValue += gHist[i] * i;
                bTotal += bHist[i];
                bValue += bHist[i] * i;
            }
            rValue /= rTotal;
            gValue /= gTotal;
            bValue /= bTotal;

            for (int i = 0; i < 256; i++) {
                rError += Math.pow(rValue - i, 2) * rHist[i];
                gError += Math.pow(gValue - i, 2) * gHist[i];
                bError += Math.pow(bValue - i, 2) * bHist[i];
            }
            rError = Math.sqrt(rError / rTotal);
            gError = Math.sqrt(gError / gTotal);
            bError = Math.sqrt(bError / bTotal);

            error = rError * 0.2989 + gError * 0.5870 + bError * 0.1140;
            color = color(rValue, gValue, bValue);
            score = error * Math.pow(quad.width * quad.height, 0.25);
        }
    }
}

