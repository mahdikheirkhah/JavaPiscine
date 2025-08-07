import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatch {
    public static boolean containsOnlyAlpha(String s) {
        Pattern p = Pattern.compile("^[a-zA-Z]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    
    public static boolean startWithLetterAndEndWithNumber(String s) {
        Pattern p = Pattern.compile("^[a-zA-Z].*\\d$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    
    public static boolean containsAtLeast3SuccessiveA(String s) {
        Pattern p = Pattern.compile("A{3,}");
        Matcher m = p.matcher(s);
        return m.find();
    }
}