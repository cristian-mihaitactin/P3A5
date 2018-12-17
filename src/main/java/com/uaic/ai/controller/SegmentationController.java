package com.uaic.ai.controller;

import com.uaic.ai.dto.ImageDto;
import com.uaic.ai.mapper.ImageMapper;
import com.uaic.ai.model.Footnote;
import com.uaic.ai.model.Image;
import com.uaic.ai.service.*;
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
    private ColumnsRecognition columnsRecognitionService;
    private ImageMapper imageMapper;
    private ImageServiceImpl imageServiceImpl;
    private StatisticsServiceImpl statisticsService;
    private FootnotesServiceImpl footnotesPositionService;

    @Autowired
    public SegmentationController(ColumnsRecognition columnsRecognitionService, ImageMapper imageMapper, ImageServiceImpl imageServiceImpl,
                                  StatisticsServiceImpl statisticsService, FootnotesServiceImpl footnotesPositionService) {
        this.columnsRecognitionService = columnsRecognitionService;
        this.imageMapper = imageMapper;
        this.imageServiceImpl = imageServiceImpl;
        this.statisticsService = statisticsService;
        this.footnotesPositionService = footnotesPositionService;

    }


    @PostMapping("/get-cols")
    public ImageDto getColumns(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String imagePath = STORAGE_LOCATION + "\\" + fileName;

            /*
            binaryMatrixService.createMatrix(imagePath);
            imageResult.setPixels(binaryMatrixService.getMatrix());
            */

            //create binary array in Image Object
            imageResult = imageServiceImpl.processImage(imagePath);

            //create statistics for image, using the binary array
            imageResult.setStatistics(statisticsService.computeStatistics(imageResult));

            //create and set Column Object, using statistics
            columnsRecognitionService.computeColumns(imageResult);


            return imageMapper.map(imageResult);
        }

        return null;
    }


    @PostMapping("/get-footnotes")
    public ImageDto getColumns2(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String imagePath = STORAGE_LOCATION + "\\" + fileName;

            //create binary array in Image Object
            imageResult = imageServiceImpl.processImage(imagePath);

            //create statistics for image, using the binary array
            imageResult.setStatistics(statisticsService.computeStatistics(imageResult));

            //create Footnote Object, using statistics
            Footnote footnotes = footnotesPositionService.getFootnotesCoordinates(imageResult);
            imageResult.setFootnote(footnotes);


            return imageMapper.map(imageResult);
        }

        return imageMapper.map(null);
    }


//    @PostMapping
//    public ImageDto getFootnotes(@RequestPart("image") MultipartFile image) {
//
//        Image imageResult = new Image();
//
//        if(uploadImage(image)) {
//            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
//            String imagePath = STORAGE_LOCATION + "\\" + fileName;
//
//            binaryMatrixService.createMatrix(imagePath);
//            imageResult.setPixels(binaryMatrixService.getMatrix());
//
//
//        }
//
//
//    }


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
