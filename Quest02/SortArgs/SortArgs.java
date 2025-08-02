import java.util.ArrayList;
import java.util.List;

public class SortArgs {
    public static void sort(String[] args) {
        if (args == null || args.length == 0){
            System.out.println("");
            return;
        }
        List<Integer> numbers = new ArrayList<>(); 
        for(int i = 0; i < args.length; i++){
            numbers.add(Integer.parseInt(args[i]));
        }
        numbers.sort(null);
        String result = "";
        for(int i = 0; i < numbers.size(); i++){
            if( i == numbers.size() - 1) {
                result += String.valueOf(numbers.get(i));
            } else {
                result += String.valueOf(numbers.get(i)) + " ";
            }
        }
        System.out.println(result);
    }
}