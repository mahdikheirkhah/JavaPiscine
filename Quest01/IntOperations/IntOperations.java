public class IntOperations {
    public static int addTwoIntegers(int a, int b) {
        long result = (long) a + b;
        if (result > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (result < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) result;
    }

    public static int subtractTwoIntegers(int a, int b) {
        long result = (long) a - b;
        if (result > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (result < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) result;
    }

    public static int multiplyTwoIntegers(int a, int b) {
        long result = (long) a * b;
        if (result > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (result < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) result;
    }

    public static int divideTwoIntegers(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        // Special case: overflow from Integer.MIN_VALUE / -1
        if (a == Integer.MIN_VALUE && b == -1) {
            return Integer.MAX_VALUE;
        }
        return a / b;
    }
}
