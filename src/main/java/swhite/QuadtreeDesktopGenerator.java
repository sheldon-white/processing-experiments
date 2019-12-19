package swhite;

import com.google.common.collect.Sets;
import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import org.datasyslab.geospark.spatialPartitioning.quadtree.StandardQuadTree;

import java.util.List;
import java.util.Set;

public abstract class QuadtreeDesktopGenerator extends DesktopGenerator {
    private int maxQuadWidth;
    private int maxQuadHeight;
    private int quadTreeWidth;
    private int quadTreeHeight;

    private static final boolean SPARCE = false;

    public QuadtreeDesktopGenerator(String name,
                                    int maxQuadWidth,
                                    int maxQuadHeight,
                                    int quadTreeWidth,
                                    int quadTreeHeight) {
        super(name);
        this.quadTreeWidth = quadTreeWidth;
        this.quadTreeHeight = quadTreeHeight;
        this.maxQuadWidth = maxQuadWidth;
        this.maxQuadHeight = maxQuadHeight;
    }

    @Override
    public final void drawDesktop() {
        initFrame();
        Set<String> legalDimensions = getLegalDimensions(maxQuadWidth, maxQuadHeight);

        QuadRectangle bounds = new QuadRectangle(0, 0, quadTreeWidth, quadTreeHeight);
        StandardQuadTree<QuadRectangle> quadTree = new StandardQuadTree<>(new QuadRectangle(0, 0, quadTreeWidth, quadTreeHeight), 0, 1, 4);
        int emptyCells = quadTreeWidth * quadTreeHeight;
        while (emptyCells > 0) {
            IPoint dim = getRandomBounds();
            int w = dim.x;
            int h = dim.y;

            String key = w + "-" + h;
            if (!legalDimensions.contains(key)) {
                // already marked impossible
                continue;
            }
            if (w * h > emptyCells) {
                // too big for remaining cells
                legalDimensions.remove(key);
                continue;
            }
            // start sweeping over the tree
            QuadRectangle q = tryToPlaceRect(quadTree, w, h);
            if (q != null) {
                emptyCells -= w * h;
                //System.out.println("placed " + w + "," + h + " at " + q);
            } else {
                // It can't be placed
                //System.out.println(key + " is illegal");
                legalDimensions.remove(key);
            }
        }

        for (QuadRectangle q : quadTree.getElements(bounds)) {
            if (SPARCE && q.width == 1 && q.height == 1) {
                continue;
            }
            drawQuad(q);
        }
        doneDrawing = true;
    }

    protected QuadRectangle tryToPlaceRect(StandardQuadTree<QuadRectangle> quadTree,
                                           int rectWidth,
                                           int rectHeight) {
        int widthRange = quadTreeWidth - rectWidth + 1;
        int heightRange = quadTreeHeight - rectHeight + 1;
        int x = r.nextInt(widthRange);
        int y = r.nextInt(heightRange);

        int yctr = heightRange;
        while (yctr > 0) {
            int xctr = widthRange;
            while (xctr > 0) {
                QuadRectangle q = new QuadRectangle(x, y, rectWidth, rectHeight);
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
                    return q;
                }
                x = (x + 1) % widthRange;
                xctr--;
            }
            y = (y + 1) % heightRange;
            yctr--;
        }
        return null;
    }

    Set<String> getLegalDimensions(int maxWidth,
                                   int maxHeight) {
        Set<String> legalDimensions = Sets.newHashSet();
        for (int w = 1; w <= maxWidth; w++) {
            for (int h = 1; h <= maxHeight; h++) {
                legalDimensions.add(w + "-" + h);
            }
        }
        return legalDimensions;
    }

    protected IPoint getRandomBounds() {
        return new IPoint(1 + r.nextInt(maxQuadWidth), 1 + r.nextInt(maxQuadHeight));
    }

    protected abstract void drawQuad(QuadRectangle q);

    protected abstract void initFrame();
}

