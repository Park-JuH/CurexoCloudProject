package mediSpring.dataService.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

@Controller
public class FileUploadController {

    @Value("${upload-path}")
    private String uploadPath;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("FileUploadController.uploadFile");
            Path path = Paths.get(uploadPath + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            System.out.println("path = " + path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("file uploaded successfully: " + file.getOriginalFilename());
            return "redirect:/mainPage"; // 처리 후 리디렉션
        } catch (Exception e) {
            return "파일 업로드 실패: " + e.getMessage();
        }
    }
}