package swhite.tiling;

import java.util.List;

public class VertexRule {
    private int id;
    private List<Tile> tiles;
    private String matchString;

    public VertexRule(int id, List<Tile> tiles) {
        this.id = id;
        this.tiles = tiles;
        String tmp = tiles.stream().map(Tile::getId).collect(StringBuilder::new,StringBuilder::append,StringBuilder::append).toString();
        matchString = tmp + tmp;
    }

    public int getId() {
        return id;
    }

    public boolean vertexMatches(String signature) {
        return matchString.contains(signature);
    }
    public List<Tile> getTiles() {
        return tiles;
    }
}
