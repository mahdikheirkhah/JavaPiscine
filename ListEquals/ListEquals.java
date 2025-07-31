import java.util.List;

public class ListEquals {
    public static boolean areListsEqual(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list2.size(); i++ ) {
            if (!list1.get(i).equals(list2.get(i))){
                return false;
            }
        }
        return true;
    }
}