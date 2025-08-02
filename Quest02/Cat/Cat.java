import java.io.*;

public class Cat {
    public static void cat(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            return; // No file provided
        }

        try (InputStream input = new FileInputStream(args[0])) {
            byte[] buffer = new byte[4096]; 
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                System.out.write(buffer, 0, bytesRead);
            }

            System.out.flush(); 
        }
    }
}
