/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentService {

    public String uploadDocument(String name, String type, InputStream fileContent, String uploadDirectory) {
        // Construct the full file path dynamically based on the provided directory
        String filePath = uploadDirectory + "/" + name;  // Upload directory + file name

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get(uploadDirectory)); // Creates the directory if it doesn't exist

            // Save file to the specified path
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
