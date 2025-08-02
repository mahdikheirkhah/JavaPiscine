import java.io.File;

public class FileSearch {
    public static String searchFile(String fileName) {
        if (fileName == null) {
            return null;
        }
        
        // Start searching from the documents directory
        File documentsDir = new File("documents");
        if (!documentsDir.exists() || !documentsDir.isDirectory()) {
            return null;
        }
        
        // Search recursively for the file
        String result = searchFileRecursively(documentsDir, fileName, "documents");
        return result;
    }
    
    private static String searchFileRecursively(File directory, String fileName, String currentPath) {
        // Get all files and directories in the current directory
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        
        // First, check files in the current directory
        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) {
                return currentPath + "/" + fileName;
            }
        }
        
        // Then, recursively search subdirectories
        for (File file : files) {
            if (file.isDirectory()) {
                String result = searchFileRecursively(file, fileName, currentPath + "/" + file.getName());
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }
}

