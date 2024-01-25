package mediSpring.dataService.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageViewController {

    @Value("${upload.path.segmentation}")
    private String segmentationUploadPath;

    @PostMapping("/view")
    public ResponseEntity<Resource> viewImage(@RequestBody Map<String, String> requestData) throws MalformedURLException {
        String fileName = requestData.get("fileName");
        Path filePath = Paths.get(segmentationUploadPath, fileName);
        Resource fileResource = new UrlResource(filePath.toUri());

        if (!fileResource.exists() || !fileResource.isReadable()) {
            throw new RuntimeException("Unable to read the file: " + fileName);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // or the appropriate media type
                .body(fileResource);
    }
}
