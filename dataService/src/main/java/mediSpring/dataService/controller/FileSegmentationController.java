package mediSpring.dataService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import mediSpring.dataService.service.SegmentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/segmentation")
public class FileSegmentationController {

    private final SegmentationService segmentationService;

    public FileSegmentationController(SegmentationService segmentationService) {
        this.segmentationService = segmentationService;
    }

    //Send selected fileName to segmentationService
    @PostMapping("/predict")
    public String predict(@RequestBody Map<String, List<String>> body) throws UnsupportedEncodingException, JsonProcessingException {
        List<String> fileNames = body.get("fileNames");
        String fileName = fileNames.get(0);
        return segmentationService.makePrediction(fileName);
    }
}
