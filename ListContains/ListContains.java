import java.util.List;

public class ListContains {
    public static boolean containsValue(List<Integer> list, Integer value) {
       if(list == null || list.isEmpty()) {
        return false;
       }
       for (int i = 0; i < list.size(); i++) {
        if (list.get(i) == value) {
            return true;
        }
       }
       return false;
    }
}