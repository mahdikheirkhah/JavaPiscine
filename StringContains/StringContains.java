public class StringContains {
    public static boolean isStringContainedIn(String subString, String s) {
        if (subString == null || s == null || subString.isEmpty()   || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i <= s.length() - subString.length(); i++) {
            if (s.substring(i, i + subString.length()).equals(subString)) {
                return true;
            }
        }
        return false;
    }
}