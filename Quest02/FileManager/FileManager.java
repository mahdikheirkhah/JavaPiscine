import java.io.*;

public class FileManager {

    public static void createFile(String fileName, String content) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

    public static String getContentFile(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }

        StringBuilder content = new StringBuilder();
        try (InputStream input = new FileInputStream(fileName)) {
            byte[] buffer = new byte[4096]; 
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                content.append(new String(buffer, 0, bytesRead));
            }
        }
        return content.toString();
    }

    public static void deleteFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return;
        }

        File file = new File(fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
