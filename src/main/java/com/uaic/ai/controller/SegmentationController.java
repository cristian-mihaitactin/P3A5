package com.uaic.ai.controller;

import com.uaic.ai.service.BinaryMatrix;
import com.uaic.ai.service.ColumnsRecognition;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/segmentation")
public class SegmentationController {

    private BinaryMatrix binaryMatrix;
    private ColumnsRecognition columnsRecognition;


}
