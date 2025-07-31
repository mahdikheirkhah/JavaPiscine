public class StringConcat {
    public static String concat(String s1, String s2) {
       if ( s2 == null  || s2.isEmpty()) {
        return s1;
       }
       if (s1 == null || s1.isEmpty()){
        return s2;
       }
       return s1 + s2;
    }
}