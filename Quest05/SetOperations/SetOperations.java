import java.util.HashSet;
import java.util.Set;

public class SetOperations {
    public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> result = new HashSet<>();
        if(set1 == null || set1.isEmpty()) return set2;
        if(set2 == null || set2.isEmpty()) return set1;
        result.addAll(set1);
        result.addAll(set2);
        return result;
    }

    public static Set<Integer> intersection(Set<Integer> set1, Set<Integer> set2) {
        if (set1 == null || set2 == null) {
            return new HashSet<>();
        }
        
        Set<Integer> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }
}