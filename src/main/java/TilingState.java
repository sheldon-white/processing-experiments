import java.util.List;

public class TilingState {
    private Vertex vertex;
    private VertexRule vertexRule;
    private List<Tile> tiles;

    public TilingState(Vertex vertex, VertexRule vertexRule, List<Tile> tiles) {
        this.vertex = vertex;
        this.vertexRule = vertexRule;
        this.tiles = tiles;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public VertexRule getVertexRule() {
        return vertexRule;
    }

    public List<Tile> getTiles() {
        return tiles;
    }
}
