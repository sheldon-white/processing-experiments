import java.util.Set;

public class TileSet {
    private Set<VertexTiling> legalVertexTilings;

    TileSet(Set<VertexTiling> legalVertexTilings) {
        this.legalVertexTilings = legalVertexTilings;
    }

    public Set<VertexTiling> getLegalVertexTilings() {
        return legalVertexTilings;
    }
}
