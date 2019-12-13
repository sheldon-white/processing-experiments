package swhite;

//
// Based on a posting by Manohar Vanga
// https://sighack.com/post/poisson-disk-sampling-bridsons-algorithm
//

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PoissonDiskSampling {
    private int radius;
    private int maxTries;
    private int minX, minY, maxX, maxY;

    public PoissonDiskSampling(int minX, int minY, int maxX, int maxY, int radius, int maxTries) {
        this.radius = radius;
        this.maxTries = maxTries;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public List<PVector> calculatePoints() {
        int N = 2;
        Random random = new Random();

        /* The final set of points to return */
        ArrayList<PVector> points = new ArrayList<>();
        /* The currently "active" set of points */
        ArrayList<PVector> active = new ArrayList<>();
        /* Initial point p0 */
        PVector p0 = new PVector(minX + random.nextInt(maxX - minX), minY + random.nextInt(maxY - minY));
        PVector[][] grid;
        float cellsize = (int)Math.floor(radius / Math.sqrt(N));

        /* Figure out no. of cells in the grid for our canvas */
        int ncells_width = (int)Math.ceil(maxX - minX / cellsize) + 1;
        int ncells_height = (int)Math.ceil(maxY - minY / cellsize) + 1;

        /* Allocate the grid an initialize all elements to null */
        grid = new PVector[ncells_width][ncells_height];
        for (int i = 0; i < ncells_width; i++)
            for (int j = 0; j < ncells_height; j++)
                grid[i][j] = null;

        insertPoint(grid, cellsize, p0);
        points.add(p0);
        active.add(p0);

        while (active.size() > 0) {
            int random_index = random.nextInt(active.size());
            PVector p = active.get(random_index);

            boolean found = false;
            for (int tries = 0; tries < maxTries; tries++) {
                float theta = random.nextFloat() * 360;
                float new_radius = radius * (1 + random.nextFloat());
                float pnewx = p.x + new_radius * (float)Math.cos(Math.toRadians(theta));
                float pnewy = p.y + new_radius * (float)Math.sin(Math.toRadians(theta));
                PVector pnew = new PVector(pnewx, pnewy);

                if (!isValidPoint(grid, cellsize,
                        ncells_width, ncells_height,
                        pnew, radius))
                    continue;

                points.add(pnew);
                insertPoint(grid, cellsize, pnew);
                active.add(pnew);
                found = true;
                break;
            }

            /* If no point was found after k tries, remove p */
            if (!found)
                active.remove(random_index);
        }

        return points;
    }

    private void insertPoint(PVector[][] grid, float cellsize, PVector point) {
        int xindex = (int)Math.floor(point.x / cellsize);
        int yindex = (int)Math.floor(point.y / cellsize);
        grid[xindex][yindex] = point;
    }

    private boolean isValidPoint(PVector[][] grid, float cellsize,
                                 int gwidth, int gheight,
                                 PVector p, float radius) {
        /* Make sure the point is on the screen */
        if (p.x < minX || p.x >= maxX || p.y < minY || p.y >= maxY)
            return false;

        /* Check neighboring eight cells */
        int xindex = (int)Math.floor(p.x / cellsize);
        int yindex = (int)Math.floor(p.y / cellsize);
        int i0 = Math.max(xindex - 1, 0);
        int i1 = Math.min(xindex + 1, gwidth - 1);
        int j0 = Math.max(yindex - 1, 0);
        int j1 = Math.min(yindex + 1, gheight - 1);

        for (int i = i0; i <= i1; i++) {
            for (int j = j0; j <= j1; j++) {
                if (grid[i][j] != null) {
                    double distance = Math.hypot(grid[i][j].x - p.x, grid[i][j].y - p.y);
                    if (distance < radius) {
                        return false;
                    }
                }
            }
        }
        /* If we get here, return true */
        return true;
    }
}