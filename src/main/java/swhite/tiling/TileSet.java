package swhite.tiling;

import java.util.Set;

public class TileSet {
    private Set<VertexRule> rules;

    TileSet(Set<VertexRule> rules) {
        this.rules = rules;
    }

    public Set<VertexRule> getRules() {
        return rules;
    }
}
