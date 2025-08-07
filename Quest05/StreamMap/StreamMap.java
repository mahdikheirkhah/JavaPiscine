import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.Set;
public class StreamMap {
    public static Integer sumOfStringLength(Stream<String> s) {
       return s.map(String::length).reduce(0,Integer::sum);
    }

    public static List<String> upperCaseAllString(Stream<String> s) {
        return s.map(String::toUpperCase).collect(Collectors.toList());
    }
    
    public static Set<Integer> uniqIntValuesGreaterThan42(Stream<Double> s) {
        return s.filter(numb -> numb > 42).map(Double::intValue).collect(Collectors.toSet());
    }
}