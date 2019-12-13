package swhite;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.alsclo.voronoi.Voronoi;
import de.alsclo.voronoi.graph.Edge;
import de.alsclo.voronoi.graph.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;


public class PoissonDiskSampling2 extends PApplet {
    private static final String NAME = MethodHandles.lookup().lookupClass().getName();
    private int regionCtr = 0;
    private int radius = 10;
    float cellSize = (int)Math.floor(radius / Math.sqrt(2));
    private List<Edge> edges;
    private List<PolygonalCell> allCells;

    public class PolygonalCell extends MazeCell {
        private List<Point> corners;
        private Point node;
        private final int visitedColor = color(220, 200, 200);
        private final int occupiedColor = color(180, 255, 180);
        private final int completedColor = color(180, 180, 255);

        PolygonalCell(Point node, List<Point> corners) {
            this.corners = corners;
            this.node = node;
        }

        public boolean equals(MazeCell other) {
            return this == other;
        }

        public void drawOccupied() {
            this.draw(occupiedColor, true);
        }

        public void drawVisited() {
            this.draw(visitedColor, true);
        }

        public void drawCompleted() {
            this.draw(completedColor, true);
        }

        public void draw(int color, boolean drawBorder) {
            // TODO draw walls individually
            fill(color);
            stroke(0);
            beginShape();
            corners.forEach(c -> vertex((float)c.x, (float)c.y));
            endShape();
        }

        public void drawConnection(MazeCell other) {
        }
    }

    public PoissonDiskSampling2() {
    }

    public static void main(String args[]) {
        PApplet.main(NAME);
    }

    @Override
    public void settings() {
        size(1600, 1000);
        pixelDensity(1);
        smooth(8);
    }

    public void setup() {
        background(255);
        int margin = 0;
        PoissonDiskSampling diskSampling = new PoissonDiskSampling(margin, margin, width - 2 * margin, height - 2 * margin, radius, 30);
        List<PVector> plist = diskSampling.calculatePoints();
        // create voronoi graph
        List<Point> vPoints = plist.stream().map(p -> new Point(p.x, p.y)).collect(Collectors.toList());
        Voronoi voronoi = new Voronoi(vPoints);
        edges = voronoi.getGraph().edgeStream().collect(Collectors.toList());
        Map<Point, Optional<PolygonalCell>> regionMap = calculateRegions();
        allCells = regionMap.values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    public void draw() {
        for (int c = 0; c < 100; c++) {
            if (regionCtr < allCells.size()) {
                allCells.get(regionCtr).drawCompleted();
                regionCtr++;
            } else {
                println("Done!");
                noLoop();
                break;
            }
        }
    }

    private Map<Point, Optional<PolygonalCell>> calculateRegions() {
        Map<Point, List<Point>> regionMap = new HashMap<>();
        Map<Point, List<Edge>> siteEdges = new HashMap<>();

        // first: map all sites to the edges that border them
        edges.forEach(e -> {
            Point site1 = e.getSite1();
            Point site2 = e.getSite2();
            if (site1 != null) {
                if (!regionMap.containsKey(site1)) {
                    regionMap.put(site1, new ArrayList<>());
                }
                if (!siteEdges.containsKey(site1)) {
                    siteEdges.put(site1, new ArrayList<>());
                }
                siteEdges.get(site1).add(e);
            } else {
                println("edge " + e + " has null site1");
            }
            if (site2 != null) {
                if (!regionMap.containsKey(site2)) {
                    regionMap.put(site2, new ArrayList<>());
                }
                if (!siteEdges.containsKey(site2)) {
                    siteEdges.put(site2, new ArrayList<>());
                }
                siteEdges.get(site2).add(e);
            } else {
                println("edge " + e + " has null site2");
            }
        });

        Map<Point, Optional<PolygonalCell>> cells = siteEdges.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> buildCellFromEdges(e.getKey(), e.getValue())));
        // TODO calculate cell neighbors
        return cells;
    }

    private Optional<PolygonalCell> buildCellFromEdges(Point node, List<Edge> edges) {
        Multimap<Point, Edge> pointEdgeMap = HashMultimap.create();
        Optional<PolygonalCell> retval = Optional.empty();

        for (Edge e: edges) {
            if (e.getA() != null && e.getB() != null) {
                Point a = e.getA().getLocation();
                Point b = e.getB().getLocation();
                if (pointOutsideRectangle(a, 0, 0, width, height) || pointOutsideRectangle(b, 0, 0, width, height)) {
                    return retval;
                }
                pointEdgeMap.put(a, e);
                pointEdgeMap.put(b, e);
            } else {
                return retval;
            }
        }
        Edge edge = edges.get(0);
        List<Point> shapePoints = new ArrayList<>();
        shapePoints.add(edge.getA().getLocation());
        shapePoints.add(edge.getB().getLocation());

        while (true) {
            Point last = shapePoints.get(shapePoints.size() - 1);
            Point next;
            Collection<Edge> adjacentEdges = pointEdgeMap.get(last);
            if (adjacentEdges.size() != 2) {
                break;
            }
            for (Edge e: adjacentEdges) {
                if (e != edge) {
                    edge = e;
                    break;
                }
            }
            if (last == edge.getA().getLocation()) {
                next = edge.getB().getLocation();
            } else {
                next = edge.getA().getLocation();
            }
            if (next == shapePoints.get(0)) {
                PolygonalCell cell = new PolygonalCell(node, shapePoints);
                retval = Optional.of(cell);
                break;
            }
            shapePoints.add(next);
        }
        return retval;
    }

    private boolean pointOutsideRectangle(Point p, int x, int y, int width, int height) {
        return !(p.x >= x) || !(p.y >= y) || !(p.x <= x + width) || !(p.y <= y + height);
    }
}