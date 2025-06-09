import java.util.*;
import java.util.regex.*;

public class MiniInterpreter {

    static Map<String, Integer> globalScope = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read multiline input
        StringBuilder program = new StringBuilder();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) break; // End input on empty line
            program.append(line).append("\n");
        }

        // Evaluate program
        int result = evaluateProgram(program.toString());
        System.out.println(result);
    }

    // Evaluate the whole program
    public static int evaluateProgram(String code) {
        return evaluateBlock(code, new HashMap<>());
    }

    // Evaluate a block of code with local variable scope
    private static int evaluateBlock(String code, Map<String, Integer> localScope) {
        String[] lines = code.split(";");
        int lastValue = 0;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("let ")) {
                // let x = 5
                line = line.substring(4).trim();
                String[] parts = line.split("=");
                String var = parts[0].trim();
                int value = evaluateExpression(parts[1].trim(), localScope);
                localScope.put(var, value);
            } else if (line.startsWith("if")) {
                // if (x > y) { let z = 1; } else { let z = 2; }
                Matcher m = Pattern.compile("if\\s*\\(([^)]+)\\)\\s*\\{([^}]+)\\}\\s*else\\s*\\{([^}]+)\\}").matcher(line);
                if (m.find()) {
                    String condition = m.group(1);
                    String ifBlock = m.group(2);
                    String elseBlock = m.group(3);

                    boolean condValue = evaluateCondition(condition.trim(), localScope);
                    if (condValue) {
                        lastValue = evaluateBlock(ifBlock, new HashMap<>(localScope));
                    } else {
                        lastValue = evaluateBlock(elseBlock, new HashMap<>(localScope));
                    }
                }
            } else {
                // Expression evaluation
                lastValue = evaluateExpression(line, localScope);
            }
        }

        return lastValue;
    }

    // Evaluate simple expressions (variables, arithmetic)
    private static int evaluateExpression(String expr, Map<String, Integer> scope) {
        expr = expr.replaceAll("\\s+", "");

        // Handle simple integers
        if (expr.matches("-?\\d+")) return Integer.parseInt(expr);

        // Replace variables with values
        for (String var : scope.keySet()) {
            expr = expr.replaceAll("\\b" + var + "\\b", scope.get(var).toString());
        }

        // Evaluate the arithmetic expression (basic only)
        return evalSimpleMath(expr);
    }

    // Evaluate simple arithmetic expression (no parentheses)
    private static int evalSimpleMath(String expr) {
        // Only + and - support for now (extendable)
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("\\d+|[+\\-*/]").matcher(expr);
        while (m.find()) tokens.add(m.group());

        Stack<Integer> stack = new Stack<>();
        int num = Integer.parseInt(tokens.get(0));
        stack.push(num);

        for (int i = 1; i < tokens.size(); i += 2) {
            String op = tokens.get(i);
            int next = Integer.parseInt(tokens.get(i + 1));
            switch (op) {
                case "+": stack.push(stack.pop() + next); break;
                case "-": stack.push(stack.pop() - next); break;
                case "*": stack.push(stack.pop() * next); break;
                case "/": stack.push(stack.pop() / next); break;
            }
        }

        return stack.pop();
    }

    // Evaluate condition inside if ( ... )
    private static boolean evaluateCondition(String cond, Map<String, Integer> scope) {
        // Replace variables
        for (String var : scope.keySet()) {
            cond = cond.replaceAll("\\b" + var + "\\b", scope.get(var).toString());
        }

        // Simple comparisons only
        if (cond.contains("==")) {
            String[] parts = cond.split("==");
            return Integer.parseInt(parts[0]) == Integer.parseInt(parts[1]);
        } else if (cond.contains("!=")) {
            String[] parts = cond.split("!=");
            return Integer.parseInt(parts[0]) != Integer.parseInt(parts[1]);
        } else if (cond.contains(">=")) {
            String[] parts = cond.split(">=");
            return Integer.parseInt(parts[0]) >= Integer.parseInt(parts[1]);
        } else if (cond.contains("<=")) {
            String[] parts = cond.split("<=");
            return Integer.parseInt(parts[0]) <= Integer.parseInt(parts[1]);
        } else if (cond.contains(">")) {
            String[] parts = cond.split(">");
            return Integer.parseInt(parts[0]) > Integer.parseInt(parts[1]);
        } else if (cond.contains("<")) {
            String[] parts = cond.split("<");
            return Integer.parseInt(parts[0]) < Integer.parseInt(parts[1]);
        }

        return false;
    }
}
