import java.util.List;
import java.util.Set;

public class VertexTiling {
    private int id;
    private List<Tile> tiles;

    public VertexTiling(int id, List<Tile> tiles) {
        this.id = id;
        this.tiles = tiles;
    }

    public int getId() {
        return id;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
