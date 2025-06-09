import java.util.Scanner;

public class Problem4_BitwiseMatchingPattern {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read integer input from user
        int n = sc.nextInt();

        // Call method to get the next number with same number of 1s
        int result = getNextNumberWithSameBitCount(n);

        // Print result
        System.out.println(result);
    }

    /**
     * This method returns the next greater integer that has
     * the same number of 1s in binary representation as the input integer.
     */
    public static int getNextNumberWithSameBitCount(int n) {
        if (n == 0) return -1;

        int c = n;
        int c0 = 0;  // count of trailing 0s
        int c1 = 0;  // count of 1s right before trailing 0s

        // Count trailing 0s (c0)
        while (((c & 1) == 0) && (c != 0)) {
            c0++;
            c >>= 1;
        }

        // Count 1s until we hit a 0 again
        while ((c & 1) == 1) {
            c1++;
            c >>= 1;
        }

        // If all 1s or no bigger number exists
        if (c0 + c1 == 31 || c0 + c1 == 0)
            return -1;

        // Position of rightmost non-trailing zero
        int p = c0 + c1;

        // Flip rightmost non-trailing zero
        n |= (1 << p);

        // Clear all bits to the right of p
        n &= ~((1 << p) - 1);

        // Insert (c1-1) ones on the right
        n |= (1 << (c1 - 1)) - 1;

        return n;
    }
}
