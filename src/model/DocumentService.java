package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentService {

    public String uploadDocument(String name, String type, InputStream fileContent, String uploadDirectory) {
        // Ensure the upload directory is structured correctly (separate by "/")
        uploadDirectory = uploadDirectory.replace("\\", "/");  // Ensure all slashes are forward slashes

        // Split the uploadDirectory into subdirectories and ensure each level exists
        Path path = Paths.get(uploadDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path); // Ensure that all subdirectories are created
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to create directories.";
            }
        }

        // Construct the full file path by adding the file name
        String filePath = uploadDirectory + File.separator + name;  // Use File.separator for system compatibility

        // Get the current date
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        try {
            // Save the file to the specified path
            try (OutputStream fileOutputStream = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileContent.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            }

            // Insert document metadata into database
            String query = "INSERT INTO document_management (name, upload_date, type, file_path, compliance_dashboard_id) VALUES ('"
                    + name + "', '" + formattedDate + "','" + type + "', '" + filePath + "','1')";
            MySQL2.executeIUD(query);

            return "Document uploaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload document.";
        }
    }
}
