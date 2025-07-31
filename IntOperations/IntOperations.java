public class IntOperations {
    public static int addTwoIntegers(int a, int b) {
        int result = a + b;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return result;
    }
    public static int subtractTwoIntegers(int a, int b) {
        int result = a - b;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return result;
    }
    public static int multiplyTwoIntegers(int a, int b) {
        int result = a * b;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return result;
    }
    public static int divideTwoIntegers(int a, int b) {
        int result = (int) a / b;
        if (result > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (result < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return result;
    }
}