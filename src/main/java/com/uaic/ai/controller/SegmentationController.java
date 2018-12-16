package com.uaic.ai.controller;

import com.uaic.ai.dto.ImageDto;
import com.uaic.ai.mapper.ImageMapper;
import com.uaic.ai.model.Image;
import com.uaic.ai.service.BinaryMatrix;
import com.uaic.ai.service.ColumnsRecognition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/segmentation")
public class SegmentationController {

    private static final Path STORAGE_LOCATION = Paths.get("D:\\Andy\\an3\\InteligentaArtificiala_IA\\P3A5\\src\\main\\resources\\downloads");
    private BinaryMatrix binaryMatrixService;
    private ColumnsRecognition columnsRecognitionService;
    private ImageMapper imageMapper;

    @Autowired
    public SegmentationController(BinaryMatrix binaryMatrixService, ColumnsRecognition columnsRecognitionService, ImageMapper imageMapper) {
        this.binaryMatrixService = binaryMatrixService;
        this.columnsRecognitionService = columnsRecognitionService;
        this.imageMapper = imageMapper;

    }

    @PostMapping("/get-cols")
    public ImageDto getColumns(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String imagePath = STORAGE_LOCATION + "\\" + fileName;

            // we need to do these 2 lines of code in order to use  columnsRecognitionService.computeColumns(imageResult);
            // TODO: modify createMatrix to return value of createMatrix(imagePath) in Image imageResult
            binaryMatrixService.createMatrix(imagePath);
            imageResult.setPixels(binaryMatrixService.getMatrix());

            columnsRecognitionService.computeColumns(imageResult);
            return imageMapper.map(imageResult);
        }

        return null;
    }


    /**
     * Saves a given MultipartFile file and saves it to {@link #STORAGE_LOCATION} path.
     *
     * @param file - the given image to be locally saved
     */
    private boolean uploadImage(MultipartFile file) {
        // Get image name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());    // blabla.png

        // Copy image to target location (replace existing file with same name)
        try {
            Path targetLocation = STORAGE_LOCATION.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (InvalidPathException e) {
            // fileName could not be converted to a Path
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }


}
