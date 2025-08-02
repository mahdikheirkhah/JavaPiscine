public class StringReplace {
    public static String replace(String s, String target, String replacement) {
        if (target == null || target.isEmpty()) {
            return s;
        }
        for (int i = 0; i <= s.length() - target.length(); ) {
            if (s.substring(i, i + target.length()).equals(target)) {
                s = s.substring(0, i) + replacement + s.substring(i + target.length());
                i += replacement.length();
            } else {
                i++;
            }
        }
        return s;
    }

    public static String replace(String s, char target, char replacement) {
        return replace(s, String.valueOf(target), String.valueOf(replacement));
    }
}