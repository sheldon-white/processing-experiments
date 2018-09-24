import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.*;
import java.util.stream.Collectors;

public class QuadtreeText1 extends PApplet {
    private static final String NAME = "QuadtreeText1";
    private final int cellWidth = 50;
    private final int cellHeight = 50;

    private final int xcount = 80;
    private final int ycount = 50;

    private Random random = new Random();
    private List<QuadRectangle> quadQueue;
    private Map<String, PFont> loadedFonts;
    private String[] fontNames = PFont.list();

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(cellWidth * xcount, cellHeight * ycount);
        smooth(8);
        pixelDensity(1);
    }

    @Override
    public void setup() {
        loadedFonts = new HashMap<>();
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new MazeCell(0, 0, xcount, ycount), 0, 1, 4);
        background(100);
        //noStroke();
//        stroke(0);
//        strokeWeight(1.5F);
        fillQuadtree(quadTree);
        quadQueue = quadTree.getElements(quadTree.getZone());
    }

    private PFont getRandomFont(Integer size) {
        String fontName = fontNames[random.nextInt(fontNames.length - 1)];
        String key = size + "-" + fontName;
        if (!loadedFonts.containsKey(key)) {
            loadedFonts.put(key, createFont(fontName, size));
        }
        return loadedFonts.get(key);
    }

    private void fillQuadtree(StandardQuadTree<QuadRectangle> quadTree) {
        int emptyCells = xcount * ycount;
        int maxX = 5;
        int minX = 2;
        while (emptyCells > 0) {
            int ctr = 10000;
            while (ctr-- > 0) {
                int x = random.nextInt(xcount);
                int y = random.nextInt(ycount);
                int w = minX + random.nextInt(maxX);
                int h = w;
                if (x + w > xcount) {
                    continue;
                }
                if (y + h > ycount) {
                    continue;
                }
                QuadRectangle q = new QuadRectangle(x, y, w, h);
                List<QuadRectangle> hits = quadTree.getElements(q);
                boolean fits = true;
                for (QuadRectangle c : hits) {
                    if (q != c && ShapeUtils.rectanglesIntersect(q, c)) {
                        fits = false;
                        break;
                    }
                }
                if (fits) {
                    quadTree.insert(q, q);
                    emptyCells -= w * h;
                    print("emptyCells: ", emptyCells, "\n");
                }
            }
            if (minX > 1) {
                minX--;
            }
        }
        print("filled!\n");
    }

    @Override
    public void draw() {
        for (int i = 0; i < 50; i++) {
            if (quadQueue.size() > 0) {
                QuadRectangle r = quadQueue.remove(0);
                int color = color(56 + random.nextInt(200), 56 + random.nextInt(200), 56 + random.nextInt(200));
                drawQuad(r, color);
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 's' || key == 'S') {
            save(NAME + ".png");
        }
    }

    private void drawQuad(QuadRectangle q, int color) {
        float x = (float)q.x * cellWidth;
        float y = (float)q.y * cellHeight;
        float w = (float)q.width * cellWidth;
        float h = (float)q.height * cellHeight;
        int xoffset = (int)(w * 0.2);
        int yoffset = (int)(h * 0.8);
        PFont font = getRandomFont(yoffset);
        textFont(font);
        fill(color);
        int c = 'A' + random.nextInt(25);
        text((char)c, x + xoffset, y + yoffset);
//        stroke(color);
//        strokeWeight(2);
//        noFill();
//        rect(x, y, w - 1, h - 1);
    }
}

