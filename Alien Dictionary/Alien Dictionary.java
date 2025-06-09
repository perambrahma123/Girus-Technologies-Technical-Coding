import java.util.*;

public class Problem2_AlienDictionary {

    // Main function to find alien order from sorted words
    public static String alienOrder(String[] words) {
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // Initialize graph nodes for all unique characters
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }

        // Build edges from adjacent words
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int len = Math.min(w1.length(), w2.length());

            boolean foundOrder = false;
            for (int j = 0; j < len; j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);
                if (c1 != c2) {
                    if (!graph.get(c1).contains(c2)) {
                        graph.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    foundOrder = true;
                    break;
                }
            }

            // Edge case: word1 is longer but starts with word2, like ["abc", "ab"]
            if (!foundOrder && w1.length() > w2.length()) {
                return "Invalid order";
            }
        }

        // Topological sort using BFS (Kahn's Algorithm)
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) queue.add(c);
        }

        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            char curr = queue.poll();
            result.append(curr);
            for (char neighbor : graph.get(curr)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }

        // If result length is not equal to number of unique characters, there's a cycle
        return result.length() == inDegree.size() ? result.toString() : "Invalid order";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of words: ");
        int n = Integer.parseInt(scanner.nextLine());

        String[] words = new String[n];
        System.out.println("Enter the words sorted by alien dictionary:");
        for (int i = 0; i < n; i++) {
            words[i] = scanner.nextLine().trim();
        }

        String result = alienOrder(words);
        System.out.println("Character order: " + result);
    }
}
