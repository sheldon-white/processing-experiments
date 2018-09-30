import java.util.List;
import java.util.stream.Collectors;

public class Tile {
    private List<FPoint> points;
    private char id;

    public Tile(char id, List<FPoint> points) {
        this.id = id;
        this.points = points.stream().map(FPoint::new).collect(Collectors.toList());
    }

    public char getId() {
        return id;
    }

    public List<FPoint> getPoints() {
        return points;
    }
}
