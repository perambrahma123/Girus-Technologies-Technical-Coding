import java.util.*;

public class Problem1_SudokuValidator {

    // Main validation function — checks rows, columns, boxes, and custom zones
    public static boolean isValidSudoku(int[][] board, List<int[][]> customZones) {
        return validateRows(board) &&
               validateColumns(board) &&
               validateBoxes(board) &&
               validateCustomZones(board, customZones);
    }

    // Check if every row contains unique digits 1–9
    private static boolean validateRows(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] seen = new boolean[10]; // Index 1–9 represent digits
            for (int j = 0; j < 9; j++) {
                int val = board[i][j];
                if (val < 1 || val > 9 || seen[val]) return false; // Invalid or duplicate
                seen[val] = true;
            }
        }
        return true;
    }

    // Check if every column contains unique digits 1–9
    private static boolean validateColumns(int[][] board) {
        for (int j = 0; j < 9; j++) {
            boolean[] seen = new boolean[10];
            for (int i = 0; i < 9; i++) {
                int val = board[i][j];
                if (val < 1 || val > 9 || seen[val]) return false;
                seen[val] = true;
            }
        }
        return true;
    }

    // Check each 3x3 box for uniqueness
    private static boolean validateBoxes(int[][] board) {
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] seen = new boolean[10];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int val = board[boxRow + i][boxCol + j];
                        if (val < 1 || val > 9 || seen[val]) return false;
                        seen[val] = true;
                    }
                }
            }
        }
        return true;
    }

    // Check each custom zone (user-defined 9-cell group) for uniqueness
    private static boolean validateCustomZones(int[][] board, List<int[][]> zones) {
        for (int[][] zone : zones) {
            boolean[] seen = new boolean[10];
            for (int[] coord : zone) {
                int row = coord[0];
                int col = coord[1];
                int val = board[row][col];
                if (val < 1 || val > 9 || seen[val]) return false;
                seen[val] = true;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Read the 9x9 board from user input
        int[][] board = new int[9][9];
        System.out.println("Enter the 9x9 Sudoku board (values 1–9, one row per line):");

        for (int i = 0; i < 9; i++) {
            String[] row = scanner.nextLine().trim().split("\\s+");
            if (row.length != 9) {
                System.out.println("Each row must have 9 numbers. Try again.");
                return;
            }
            for (int j = 0; j < 9; j++) {
                board[i][j] = Integer.parseInt(row[j]);
            }
        }

        // Step 2: Ask for the number of custom zones
        System.out.print("Enter number of custom zones: ");
        int zoneCount = Integer.parseInt(scanner.nextLine());

        List<int[][]> customZones = new ArrayList<>();

        // Step 3: For each custom zone, read 9 coordinates
        for (int z = 0; z < zoneCount; z++) {
            System.out.println("Enter 9 coordinates (row,col) for custom zone " + (z + 1) + ":");
            int[][] zone = new int[9][2];

            for (int i = 0; i < 9; i++) {
                String[] parts = scanner.next().split(",");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);

                // Validate coordinates are within board limits
                if (r < 0 || r >= 9 || c < 0 || c >= 9) {
                    System.out.println("Invalid coordinate: (" + r + "," + c + "). Must be between 0 and 8.");
                    return;
                }
                zone[i][0] = r;
                zone[i][1] = c;
            }

            customZones.add(zone);
        }

        // Step 4: Check if the board is valid
        boolean result = isValidSudoku(board, customZones);
        System.out.println("Result: " + (result ? "✅ Valid Sudoku" : "❌ Invalid Sudoku"));

        scanner.close();
    }
}
