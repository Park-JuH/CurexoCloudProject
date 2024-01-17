package mediSpring.dataService.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileController {

    @Value("${upload-path}")
    private String uploadPath;

    @GetMapping("/files/original")
    public List<String> getOriginalFiles() {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(uploadPath); // 폴더 경로 설정
        System.out.println("FileController.getOriginalFiles");
        System.out.println("folder = " + folder);
        for (File file : folder.listFiles()) {
            System.out.println("file = " + file);
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }

        return fileNames;
    }
}
