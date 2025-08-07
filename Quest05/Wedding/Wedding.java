import java.util.*;

public class Wedding {
    public static Map<String, String> createCouple(Set<String> first, Set<String> second) {
        List<String> firstList = new ArrayList<>(first);
        List<String> secondList = new ArrayList<>(second);

        // Shuffle both lists to ensure random pairing
        Collections.shuffle(firstList);
        Collections.shuffle(secondList);

        Map<String, String> result= new HashMap<>();

        int size = Math.min(firstList.size(), secondList.size());

        for (int i = 0; i < size; i++) {
            result.put(firstList.get(i), secondList.get(i));
        }

        return result;
    }
}