package flocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lattice {
    private List<FloatRect> cells;
    private int maxDimension;

    public Lattice() {
        init();
    }

    private void init() {
        Random random = new Random();
        int width = 4 + random.nextInt(10);
        int height = 4 + random.nextInt(10);
        float density = 0.5f * (1 + random.nextFloat());
        int cellSize = 10 + random.nextInt(20);
        int margin = 2 + random.nextInt(5);
        int rectSize = cellSize - 2 * margin;
        maxDimension = cellSize * Math.max(width, height);
        cells = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (random.nextFloat() < density) {
                    cells.add(new FloatRect(margin + x * cellSize, margin + y * cellSize, rectSize, rectSize));
                }
            }
        }
    }

    public List<FloatRect> getCells() {
        return cells;
    }

    public int getMaxDimension() {
        return maxDimension;
    }
}
