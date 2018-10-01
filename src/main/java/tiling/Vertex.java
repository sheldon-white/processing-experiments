package tiling;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Vertex {
    private List<Tile> tiles;
    private FPoint location;

    public Vertex() {
    }

    public void addTile(Tile t) {
        tiles.add(t);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Set<VertexRule> getMatchingRules(TileSet tileSet) {
        String signature = getSignature();
        return tileSet.getRules().stream().filter(vr -> vr.vertexMatches(signature)).collect(Collectors.toSet());
    }

    public FPoint getLocation() {
        return location;
    }

    private String getSignature() {
        return tiles.stream().map(Tile::getId).collect(StringBuilder::new,StringBuilder::append,StringBuilder::append).toString();
    }
}
