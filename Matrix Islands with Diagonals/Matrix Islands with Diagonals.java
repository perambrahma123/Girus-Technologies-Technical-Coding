import java.util.Scanner;

public class Problem5_MatrixIslandsWithDiagonals {

    static int n, m;
    static int[][] grid;
    static boolean[][] visited;

    // Directions: up, down, left, right, and 4 diagonals
    static int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
    static int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read grid size
        n = sc.nextInt();
        m = sc.nextInt();

        // Initialize grid and visited arrays
        grid = new int[n][m];
        visited = new boolean[n][m];

        // Read the grid
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                grid[i][j] = sc.nextInt();

        // Count islands using DFS
        int islandCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // If cell is 1 and not visited, it's a new island
                if (grid[i][j] == 1 && !visited[i][j]) {
                    dfs(i, j);  // Explore the full island
                    islandCount++;
                }
            }
        }

        // Output the number of islands
        System.out.println(islandCount);
    }

    // Depth-First Search to mark all connected 1s as visited
    public static void dfs(int x, int y) {
        // Mark current cell as visited
        visited[x][y] = true;

        // Explore all 8 directions
        for (int dir = 0; dir < 8; dir++) {
            int newX = x + dx[dir];
            int newY = y + dy[dir];

            // Check bounds and if it's an unvisited 1
            if (isValid(newX, newY) && grid[newX][newY] == 1 && !visited[newX][newY]) {
                dfs(newX, newY);  // Continue DFS
            }
        }
    }

    // Helper to check if a cell is inside the grid
    public static boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < n && y < m;
    }
}
