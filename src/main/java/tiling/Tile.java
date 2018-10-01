package tiling;

import java.util.List;

public class Tile {
    private List<Vertex> vertices;
    private char id;

    public Tile(char id, List<Vertex> vertices) {
        this.id = id;
        this.vertices = vertices;
    }

    public char getId() {
        return id;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
