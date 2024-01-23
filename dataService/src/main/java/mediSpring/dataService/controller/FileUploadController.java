package mediSpring.dataService.controller;
import mediSpring.dataService.domain.FileRecord;
import mediSpring.dataService.repository.FileRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

@Controller
public class FileUploadController {

    @Value("${upload.path.original}")
    private String uploadPath;

    @Autowired
    private FileRecordRepository fileRecordRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("FileUploadController.uploadFile");
            Path path = Paths.get(uploadPath + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            System.out.println("path = " + path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Create a new FileRecord object and save it
            FileRecord fileRecord = new FileRecord();
            fileRecord.setFilename(file.getOriginalFilename());
            fileRecord.setFilepath(path.getParent().toString() + "/" + file.getOriginalFilename());
            fileRecordRepository.save(fileRecord);

            System.out.println("file uploaded successfully: " + file.getOriginalFilename());
            // Redirect to the main page with a query parameter
            return "redirect:/mainPage?show=original";  // 처리 후 리디렉션
        } catch (Exception e) {
            return "파일 업로드 실패: " + e.getMessage();
        }
    }
}