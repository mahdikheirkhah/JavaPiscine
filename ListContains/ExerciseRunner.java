import java.util.List;

public class ExerciseRunner {

    public static void main(String[] args) {
        System.out.println(ListContains.containsValue(List.of(9, 13, 8, 23, 1, 0, 89), 8));
        System.out.println(ListContains.containsValue(List.of(9, 13, 8, 23, 1, 0, 89), 10));
    }
}