import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListFiles {
    // Method to recursively collect all files in a folder and its subfolders
    public static List<File> allFilesIn(File folder) {
        List<File> files = new ArrayList<>();
        // Check if folder is a directory and accessible
        if (folder.isDirectory()) {
            File[] fileArray = folder.listFiles();
            // Check if listFiles() returns null (e.g., due to permissions)
            if (fileArray != null) {
                for (File f : fileArray) {
                    if (f.isDirectory()) {
                        // Recursively add files from subdirectories
                        files.addAll(allFilesIn(f));
                    } else if (f.isFile()) {
                        // Add individual files
                        files.add(f);
                    }
                }
            }
        }
        return files;
    }

    public static void main(String[] args) {
        // Specify the directory path (replace with your Linux directory path)
        String directoryPath = "/home/poppie/Documents/25_kot"; // Example: "/home/user/documents"
        File folder = new File(directoryPath);

        // Check if the directory exists and is valid
        if (!folder.exists()) {
            System.err.println("Error: Directory '" + directoryPath + "' does not exist.");
            return;
        }
        if (!folder.isDirectory()) {
            System.err.println("Error: '" + directoryPath + "' is not a directory.");
            return;
        }

        try {
            // Get the list of all files
            List<File> files = allFilesIn(folder);
            
            // Check if no files were found
            if (files.isEmpty()) {
                System.out.println("No files found in directory: " + directoryPath);
                return;
            }

            // Print the paths of all files
            System.out.println("Files in directory '" + directoryPath + "':");
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("An error occurred while listing files: " + e.getMessage());
        }
    }
}
