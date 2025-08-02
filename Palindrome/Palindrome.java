public class Palindrome {
    public static boolean isPalindrome(String s) {
        if (s== null ) {
            return false;
        }
        if (s.isEmpty()) {
            return true;
        }
        String reversed = new StringBuilder(s).reverse().toString();

        return reversed.equalsIgnoreCase(s);
    }
}