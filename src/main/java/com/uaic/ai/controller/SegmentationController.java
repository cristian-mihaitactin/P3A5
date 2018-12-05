package com.uaic.ai.controller;

import com.uaic.ai.service.BinaryMatrix;
import com.uaic.ai.service.ColumnsRecognition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/segmentation")
public class SegmentationController {

    private BinaryMatrix binaryMatrixService;
    private ColumnsRecognition columnsRecognitionService;

    @Autowired
    public SegmentationController(BinaryMatrix binaryMatrixService, ColumnsRecognition columnsRecognitionService) {
        this.binaryMatrixService = binaryMatrixService;
        this.columnsRecognitionService = columnsRecognitionService;
    }

    @PostMapping("/columns")
    public byte[] getColumns(@RequestPart("image") MultipartFile image) {

        try {
            return image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
