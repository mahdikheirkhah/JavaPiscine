import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.Comparator;
import java.util.List;
import java.lang.Character;
import java.util.Optional;
public class StreamCollect {
    public static Map<Character, List<String>> mapByFirstLetter(Stream<String> s) {
                return s.collect(Collectors.groupingBy(
            str -> (Character) Character.toUpperCase(str.charAt(0))
        ));
    }

    public static Map<Integer, Optional<Integer>> getMaxByModulo4(Stream<Integer> s) {
        return s.collect(Collectors.groupingBy(number ->  number % 4, Collectors.maxBy(Comparator.naturalOrder())));
    }

    public static String orderAndConcatWithSharp(Stream<String> s) {
        String joined = s.sorted().collect(Collectors.joining(" # "));
        return "{" + joined +"}";
    }
}