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

    @Value("${upload.path.original}")
    private String originalUploadPath;

    @Value("${upload.path.segmentation}")
    private String segmentationUploadPath;

    //Show original files to list
    @GetMapping("/files/original")
    public List<String> getOriginalFiles() {
        List<String> orgfileNames = new ArrayList<>();
        File folder = new File(originalUploadPath); // 폴더 경로 설정

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                orgfileNames.add(file.getName());
            }
        }

        return orgfileNames;
    }

    //Show segmentation files to list
    @GetMapping("/files/segmentation")
    public List<String> getSegmentationFiles() {
        List<String> segfileNames = new ArrayList<>();
        File folder = new File(segmentationUploadPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                segfileNames.add(file.getName());
            }
        }

        return segfileNames;
    }
}
