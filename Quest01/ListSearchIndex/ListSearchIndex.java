import java.util.ArrayList;
import java.util.List;

public class ListSearchIndex {

    public static Integer findLastIndex(List<Integer> list, Integer value) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int result = list.lastIndexOf(value);
        return result == -1 ? null : result;
    }

    public static Integer findFirstIndex(List<Integer> list, Integer value) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int result = list.indexOf(value);
        return result == -1 ? null : result;
    }

    public static List<Integer> findAllIndexes(List<Integer> list, Integer value) {
        List<Integer> result = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return result; 
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) { 
                result.add(i);
            }
        }
        return result;
    }
}
