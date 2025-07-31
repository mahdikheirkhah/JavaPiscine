import java.util.List;

public class SortList {

    public static List<Integer> sort(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        List<Integer> copy = new java.util.ArrayList<>(list); // use fully qualified name
        copy.sort(null); // null = natural order
        return copy;
    }

    public static List<Integer> sortReverse(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        List<Integer> copy = new java.util.ArrayList<>(list);
        copy.sort((a, b) -> b.compareTo(a)); // custom reverse comparator
        return copy;
    }
}

