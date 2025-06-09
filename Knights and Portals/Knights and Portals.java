import java.util.*;

public class Problem3_KnightsAndPortals {

    // Define directions for up, down, left, right movement
    static int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};

    // Helper class to represent a cell in the grid
    static class Point {
        int row, col, dist;

        Point(int r, int c, int d) {
            this.row = r;
            this.col = c;
            this.dist = d;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read grid dimensions
        int n = sc.nextInt();
        int m = sc.nextInt();
        sc.nextLine();

        int[][] grid = new int[n][m];

        // Read grid values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = sc.nextInt();
            }
        }

        // Run the algorithm and print the shortest distance
        int result = shortestPathWithTeleport(grid, n, m);
        System.out.println(result);
    }

    // Main function that returns the shortest path using optional teleport
    private static int shortestPathWithTeleport(int[][] grid, int n, int m) {
        // If start or end is blocked, path is impossible
        if (grid[0][0] == 1 || grid[n-1][m-1] == 1) return -1;

        // Distance from start to all reachable cells
        int[][] distFromStart = bfs(grid, n, m, 0, 0);
        // Distance from end to all reachable cells
        int[][] distFromEnd = bfs(grid, n, m, n - 1, m - 1);

        int minDistance = Integer.MAX_VALUE;

        // Case 1: Direct path without teleport
        if (distFromStart[n - 1][m - 1] != -1) {
            minDistance = distFromStart[n - 1][m - 1];
        }

        // Case 2: Use teleport between two empty cells
        for (int i1 = 0; i1 < n; i1++) {
            for (int j1 = 0; j1 < m; j1++) {
                if (grid[i1][j1] == 0 && distFromStart[i1][j1] != -1) {
                    for (int i2 = 0; i2 < n; i2++) {
                        for (int j2 = 0; j2 < m; j2++) {
                            if ((i1 != i2 || j1 != j2) &&
                                grid[i2][j2] == 0 &&
                                distFromEnd[i2][j2] != -1) {
                                // Total cost = start to cell1 + 1 (teleport) + cell2 to end
                                int total = distFromStart[i1][j1] + 1 + distFromEnd[i2][j2];
                                minDistance = Math.min(minDistance, total);
                            }
                        }
                    }
                }
            }
        }

        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    // Standard BFS to compute distance from a start point to all reachable cells
    private static int[][] bfs(int[][] grid, int n, int m, int startRow, int startCol) {
        int[][] dist = new int[n][m];
        for (int[] row : dist) Arrays.fill(row, -1); // -1 means unreachable

        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(startRow, startCol, 0));
        dist[startRow][startCol] = 0;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                // Check boundaries and if cell is not visited and not blocked
                if (newRow >= 0 && newRow < n &&
                    newCol >= 0 && newCol < m &&
                    grid[newRow][newCol] == 0 &&
                    dist[newRow][newCol] == -1) {

                    dist[newRow][newCol] = current.dist + 1;
                    queue.offer(new Point(newRow, newCol, current.dist + 1));
                }
            }
        }

        return dist;
    }
}
