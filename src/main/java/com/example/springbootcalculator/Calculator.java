package com.example.springbootcalculator;

import java.util.Stack;

public class Calculator {

    private String expression;
    private String postfixExpression;
    private double result;

    public Calculator() {
        expression = "";
        postfixExpression = "";
        result = 0;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String getPostfixExpression() {
        return postfixExpression;
    }

    public double getResult() {
        return result;
    }

    public void calculate() {

        validateExpression(expression);

        if (!bracketsBalanced(expression)) {
            throw new IllegalArgumentException(
                    "Unbalanced brackets.");
        }

        postfixExpression = infixToPostfix(expression);

        result = evaluatePostfix(postfixExpression);
    }

    // Overloaded Method #1
    public double calculate(double a,
                            double b,
                            char operator) {

        switch (operator) {

            case '+':
                return a + b;

            case '-':
                return a - b;

            case '*':
                return a * b;

            case '/':

                if (b == 0)
                    throw new ArithmeticException(
                            "Division by zero.");

                return a / b;

            case '^':
                return Math.pow(a, b);

            default:
                throw new IllegalArgumentException(
                        "Invalid operator.");
        }
    }

    // Overloaded Method #2
    public double calculate(int a,
                            int b) {

        return a + b;
    }

    // EVERYTHING BELOW CAN BE PRIVATE

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;

            case '^':
                return 3;

            default:
                return -1;
        }
    }

    private static boolean isValidCharacter(char c) {
        return Character.isDigit(c)
                || c == '.'
                || c == '+'
                || c == '-'
                || c == '*'
                || c == '/'
                || c == '^'
                || c == '('
                || c == ')'
                || Character.isWhitespace(c);
    }

    private static void validateExpression(
            String expression) {
        for (char c : expression.toCharArray()) {

            if (!isValidCharacter(c)) {
                throw new IllegalArgumentException(
                        "Invalid character detected: '" + c + "'");
            }
        }
    }

    private static boolean bracketsBalanced(
            String expression) {
        Stack<Character> stack = new Stack<>();

        for (char c : expression.toCharArray()) {

            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {

                if (stack.isEmpty()) {
                    return false;
                }

                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    private static String infixToPostfix(
            String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {

            char c = expression.charAt(i);

            if (Character.isWhitespace(c))
                continue;

            // Number (supports decimals)
            if (Character.isDigit(c)
                    || c == '.'
                    || (c == '-'
                    && (i == 0
                    || expression.charAt(i - 1) == '('
                    || expression.charAt(i - 1) == '+'
                    || expression.charAt(i - 1) == '-'
                    || expression.charAt(i - 1) == '*'
                    || expression.charAt(i - 1) == '/'
                    || expression.charAt(i - 1) == '^'))) {

                if (c == '-') {
                    result.append('-');
                    i++;
                }

                while (i < expression.length()
                        && (Character.isDigit(expression.charAt(i))
                        || expression.charAt(i) == '.')) {

                    result.append(expression.charAt(i));
                    i++;
                }

                result.append(" ");
                i--;
            }

            else if (c == '(') {
                stack.push(c);
            }

            else if (c == ')') {

                while (!stack.isEmpty()
                        && stack.peek() != '(') {

                    result.append(stack.pop()).append(" ");
                }

                if (stack.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Mismatched brackets.");
                }

                stack.pop();
            }

            else {

                while (!stack.isEmpty()
                        && stack.peek() != '('
                        && (precedence(c) < precedence(stack.peek())
                        || (precedence(c) == precedence(stack.peek())
                        && c != '^'))) {

                    result.append(stack.pop()).append(" ");
                }

                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {

            if (stack.peek() == '(') {
                throw new IllegalArgumentException(
                        "Mismatched brackets.");
            }

            result.append(stack.pop()).append(" ");
        }

        return result.toString();
    }

    private static double evaluatePostfix(
            String postfix) {
        Stack<Double> stack = new Stack<>();

        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {

            if (token.isEmpty())
                continue;

            try {
                stack.push(Double.parseDouble(token));
                continue;
            } catch (NumberFormatException e) {
            }

            if (stack.size() < 2) {
                throw new IllegalArgumentException(
                        "Malformed expression.");
            }

            double b = stack.pop();
            double a = stack.pop();

            switch (token.charAt(0)) {

                case '+':
                    stack.push(a + b);
                    break;

                case '-':
                    stack.push(a - b);
                    break;

                case '*':
                    stack.push(a * b);
                    break;

                case '/':

                    if (b == 0) {
                        throw new ArithmeticException(
                                "Division by zero.");
                    }

                    stack.push(a / b);
                    break;

                case '^':
                    stack.push(Math.pow(a, b));
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unknown operator: "
                                    + token.charAt(0));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException(
                    "Malformed expression.");
        }

        return stack.pop();
    }
}
