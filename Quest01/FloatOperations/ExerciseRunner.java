public class ExerciseRunner {
    public static void main(String[] args) {
        System.out.println("Add");
        System.out.println(FloatOperations.addTwoFloats(1.0f, 2.5f));
        System.out.println(FloatOperations.addTwoFloats(15.18f, 68.28347f));

        System.out.println("Divide");
        System.out.println(FloatOperations.divideTwoFloats(7.0f, 2.0f));
        System.out.println(FloatOperations.divideTwoFloats(5.6f, 6.9f));
    }
}