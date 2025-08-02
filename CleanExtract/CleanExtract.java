public class CleanExtract {
    public static String extract(String s) {
        if (s == null || s.isEmpty()){
            return s;
        }
        String[] splited = s.split("\\|");
        String result = "";
        for (int i = 0; i < splited.length; i++) {
            if (splited[i] != null && !splited[i].isEmpty()) {

                int firstIndex = splited[i].indexOf('.');
                int lastIndex = splited[i].lastIndexOf('.');
                String betweenDots = "";
                if (lastIndex != firstIndex) {
                    betweenDots =  splited[i].substring(firstIndex + 1, lastIndex).trim();
                    if (!betweenDots.isEmpty()){
                        result += betweenDots + " ";
                    }
                    
                } else if (firstIndex != -1) {
                    betweenDots =  splited[i].substring(firstIndex + 1).trim();
                    if (!betweenDots.isEmpty()){
                        result += betweenDots + " ";
                    }
                }
                
            }
        }

        return result.trim();
    }
}
