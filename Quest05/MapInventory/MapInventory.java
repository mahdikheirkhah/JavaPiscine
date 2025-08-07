import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapInventory {
    public static int getProductPrice(Map<String, Integer> inventory, String productId) {
        if (inventory == null || inventory.isEmpty()){
            return -1;
        }
        Integer result = inventory.get(productId);
        if (result == null) return -1;
        return result;
    }

    public static List<String> getProductIdsByPrice(Map<String, Integer> inventory, int price) {
        List<String> result = new ArrayList<>();
        if (inventory == null || inventory.isEmpty()){
            return result;
        }
        inventory.forEach((id,p) -> {
            if(p == price) result.add(id);
        });
        return result;
    }
}