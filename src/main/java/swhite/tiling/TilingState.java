package swhite.tiling;

import java.util.List;

public class TilingState {
    private Vertex vertex;
    private VertexRule vertexRule;
    private List<Tile> tiles;
    private int zeroTileIndex;

    public TilingState(Vertex vertex, VertexRule vertexRule, List<Tile> tiles, int zeroTileIndex) {
        this.vertex = vertex;
        this.vertexRule = vertexRule;
        this.tiles = tiles;
        this.zeroTileIndex = zeroTileIndex;
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

    public int getZeroTileIndex() {
        return zeroTileIndex;
    }
}
