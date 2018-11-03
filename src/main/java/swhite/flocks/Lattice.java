package swhite.flocks;

import org.datasyslab.geospark.spatialPartitioning.quadtree.QuadRectangle;
import swhite.IntRect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lattice {
    private List<IntRect> cells;
    private QuadRectangle bounds;

    public Lattice(int minWidth, int minHeight) {
        init(minWidth, minHeight);
    }

    private void init(int minWidth, int minHeight) {
        Random random = new Random();
        int cellSize = 30 + random.nextInt(100);
        int margin = (int)(cellSize * 0.05) + random.nextInt((int)(cellSize * 0.1));
        int rectSize = cellSize - 2 * margin;
        int xOffset = random.nextInt(cellSize);
        int yOffset = random.nextInt(cellSize);
        int xCellCount = (int)Math.ceil((double)(minWidth + xOffset) / cellSize);
        int yCellCount = (int)Math.ceil((double)(minHeight + yOffset) / cellSize);
        bounds = new QuadRectangle(-xOffset, -yOffset, xCellCount * cellSize + xOffset, yCellCount * cellSize + yOffset);
        cells = new ArrayList<>();
        float density = 0.1f + 0.2f * random.nextFloat();

        for (int x = 0; x < xCellCount; x++) {
            for (int y = 0; y < yCellCount; y++) {
                if (random.nextFloat() < density) {
                    cells.add(new IntRect( margin - xOffset + x * cellSize, margin - yOffset + y * cellSize, rectSize, rectSize));
                }
            }
        }
    }

    public QuadRectangle getBounds() {
        return bounds;
    }

    public List<IntRect> getCells() {
        return cells;
    }
}
