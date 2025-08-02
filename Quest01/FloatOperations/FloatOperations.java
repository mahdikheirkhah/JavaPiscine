public class FloatOperations {
    public static float addTwoFloats(float a, float b) {
        float result = a + b;
        if (Float.isInfinite(result)) {
            return a > 0 && b > 0 ? Float.MAX_VALUE : -Float.MAX_VALUE;
        }
        return result;
    }
    public static float divideTwoFloats(float a, float b) {
        if (b == 0.0f) {
            return a > 0 ? Float.POSITIVE_INFINITY :
                   a < 0 ? Float.NEGATIVE_INFINITY : Float.NaN;
        }
        float result = a / b;
        if (Float.isInfinite(result)) {
            return result > 0 ? Float.MAX_VALUE : -Float.MAX_VALUE;
        }
        return result;
    }
}