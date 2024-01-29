package mediSpring.dataService.controller;
import mediSpring.dataService.domain.FileRecord;
import mediSpring.dataService.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Controller
public class FileUploadController {

    @Value("${upload.path.original}")
    private String uploadPath;

    @Autowired
    private FileRecordRepository fileRecordRepository;

    private final String fastApiUrl = "http://localhost:8000/convert";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("FileUploadController.uploadFile");
            Path path = Paths.get(uploadPath + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            System.out.println("path = " + path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            saveFileRecord(file.getOriginalFilename(), path.toString());

            System.out.println("file uploaded successfully: " + file.getOriginalFilename());

            if (file.getOriginalFilename().toLowerCase().endsWith(".dcm")) {
                // Handle .dcm file - Convert to PNG using FastAPI
                Path convertedFilePath = convertDcmToPng(file, file.getOriginalFilename());
                // Save path of the converted file instead
//                saveFileRecord(file.getOriginalFilename(), convertedFilePath.toString());
                System.out.println("converted file uploaded successfully: " + file.getOriginalFilename());
            }

//            // Create a new FileRecord object and save it
//            FileRecord fileRecord = new FileRecord();
//            fileRecord.setFilename(file.getOriginalFilename());
//            fileRecord.setFilepath(path.getParent().toString() + "/" + file.getOriginalFilename());
//            fileRecordRepository.save(fileRecord);

            // Redirect to the main page with a query parameter
            return "redirect:/mainPage?show=original";  // 처리 후 리디렉션
        } catch (Exception e) {
            return "file upload failed: " + e.getMessage();
        }
    }

    private Path convertDcmToPng(MultipartFile file, String orgFileName) throws IOException {
        // Create a temporary file to store the .dcm file
        File tempFile = File.createTempFile("temp", ".dcm");
        file.transferTo(tempFile);

        System.out.println("FileUploadController.convertDcmToPng");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        FileSystemResource resource = new FileSystemResource(tempFile);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        body.add("originalName", orgFileName);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("FileUploadController.FASTAPI");
        // Assuming FastAPI endpoint returns the path of the converted PNG file
        String convertedFilePath = restTemplate.postForObject(fastApiUrl, requestEntity, String.class);

        System.out.println("convertedFilePath = " + convertedFilePath);
        // Delete the temporary file
        tempFile.delete();

        return Paths.get(convertedFilePath); // Convert string path to Path object
    }

    private void saveFileRecord(String filename, String filepath) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setFilename(filename);
        fileRecord.setFilepath(filepath);
        fileRecordRepository.save(fileRecord);
    }

}