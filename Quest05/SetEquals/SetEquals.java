import java.util.Set;

public class SetEquals {
    public static boolean areSetsEqual(Set<String> set1, Set<String> set2) {
        if(set1 == null && set2 == null) return true;
        if(set1 == null || set2 == null) return false;

        return set1.equals(set2);
    }
}