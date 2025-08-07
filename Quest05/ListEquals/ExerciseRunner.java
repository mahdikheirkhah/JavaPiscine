import java.util.List;

public class ExerciseRunner {
    public static void main(String[] args) {
        System.out.println(ListEquals.areListsEqual(List.of("Alice", "Bob", "Charly", "Emily"), List.of("Alice", "Bob", "Charly", "Emily")));
        System.out.println(ListEquals.areListsEqual(List.of("Alice", "Bob", "Charly", "Emily"), List.of("Alice", "Bob", "Emily", "Charly")));
    }
}