package mediSpring.dataService.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
public class FileDownloadController {

    @Value("${upload.path.segmentation}")
    private String segmentationUploadPath;

    @PostMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFiles(@RequestBody Map<String, List<String>> body, HttpServletResponse response) {
        List<String> fileNames = body.get("fileNames");
//        System.out.println("FileDownloadController.downloadFiles");
//        System.out.println("fileNames = " + fileNames);

        if (!fileNames.isEmpty()) {
            String fileName = fileNames.get(0);
            File file = new File(segmentationUploadPath + fileName);
            System.out.println("file = " + file);
            if (file.exists()) {
                try {
                    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

                    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                    System.out.println("resource = " + resource);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName)
                            .body(resource);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Handle exception
                }
            }
            else {
                System.out.println("No file");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
