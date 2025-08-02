import java.io.*;
import java.nio.charset.StandardCharsets;

public class Capitalize {
    public static void capitalize(String[] args) throws IOException {
        if (args == null || args.length != 2) {
            return;  // or throw IllegalArgumentException if you want
        }

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))
        ) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    writer.newLine();
                }
                firstLine = false;
                
                // First trim the line and replace multiple spaces with single space
                String trimmedLine = line.trim().replaceAll("\\s+", " ");
                StringBuilder sb = new StringBuilder(trimmedLine.length());
                boolean capitalizeNext = true;

                for (char c : trimmedLine.toCharArray()) {
                    if (Character.isLetter(c)) {
                        if (capitalizeNext) {
                            sb.append(Character.toUpperCase(c));
                            capitalizeNext = false;
                        } else {
                            sb.append(Character.toLowerCase(c));
                        }
                    } else {
                        sb.append(c);
                        // Only capitalize after whitespace, not after punctuation or special characters
                        if (Character.isWhitespace(c)) {
                            capitalizeNext = true;
                        } else {
                            capitalizeNext = false;
                        }
                    }
                }

                writer.write(sb.toString());
            }
        }
    }
}
