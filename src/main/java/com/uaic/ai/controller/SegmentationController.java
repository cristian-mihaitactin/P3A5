package com.uaic.ai.controller;

import com.uaic.ai.dto.ImageDto;
import com.uaic.ai.mapper.ImageMapper;
import com.uaic.ai.model.Footnote;
import com.uaic.ai.model.Image;
import com.uaic.ai.service.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/segmentation")
public class SegmentationController {

    private static final Path STORAGE_LOCATION = Paths.get("C:\\Users\\gabri\\Desktop\\Git\\P3A5\\src\\main\\resources");
    private ColumnsRecognition columnsRecognitionService;
    private ImageMapper imageMapper;
    private ImageService imageService;
    private StatisticsService statisticsService;
    private FootnotesService footnotesService;

    @Autowired
    public SegmentationController(ColumnsRecognition columnsRecognitionService, ImageMapper imageMapper, ImageService imageService,
                                  StatisticsService statisticsService, FootnotesServiceImpl footnotesService) {
        this.columnsRecognitionService = columnsRecognitionService;
        this.imageMapper = imageMapper;
        this.imageService = imageService;
        this.statisticsService = statisticsService;
        this.footnotesService = footnotesService;
    }

    @PostMapping(value = "", produces = "application/json")
    public ImageDto getAll(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String inputPath = STORAGE_LOCATION + "\\" + fileName;
            String clientPath = STORAGE_LOCATION + "\\client_" + fileName;
            String outputPath = STORAGE_LOCATION + "\\output_" + fileName;

            imageService.correctImage(inputPath, clientPath, outputPath);
            
            imageResult = imageService.processImage(outputPath);
            
            setClientImage(imageResult,clientPath);

            statisticsService.computeStatistics(imageResult);
            
            footnotesService.computeFootnotes(imageResult);

            columnsRecognitionService.computeColumns(imageResult);
            
            columnsRecognitionService.computeLinesOfColumns(imageResult);


            return imageMapper.map(imageResult);
        }

        return imageMapper.map(null);
    }
    
    @PostMapping(value = "/get-cols", produces = "application/json")
    public ImageDto getColumns(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String inputPath = STORAGE_LOCATION + "\\" + fileName;
            String clientPath = STORAGE_LOCATION + "\\client_" + fileName;
            String outputPath = STORAGE_LOCATION + "\\output_" + fileName;

            imageService.correctImage(inputPath, clientPath, outputPath);
            
            imageResult = imageService.processImage(outputPath);

            //create statistics for image, using the binary array
            statisticsService.computeStatistics(imageResult);
            
            footnotesService.computeFootnotes(imageResult);

            //create and set Column Object, using statistics
            columnsRecognitionService.computeColumns(imageResult);


            return imageMapper.map(imageResult);
        }

        return imageMapper.map(null);
    }


    @PostMapping(value = "/get-footnotes", produces = "application/json")
    public ImageDto getFootnotes(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String imagePath = STORAGE_LOCATION + "\\" + fileName;

            //create binary array in Image Object
            imageResult = imageService.processImage(imagePath);

            //create statistics for image, using the binary array
            statisticsService.computeStatistics(imageResult);

            //create Footnote Object, using statistics
            footnotesService.computeFootnotes(imageResult);


            return imageMapper.map(imageResult);
        }

        return imageMapper.map(null);
    }


    @PostMapping(value = "/get-stats", produces = "application/json")
    public ImageDto getStatistics(@RequestPart("image") MultipartFile image) {

        Image imageResult = new Image();

        if (uploadImage(image)) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String imagePath = STORAGE_LOCATION + "\\" + fileName;

            //create binary array in Image Object
            imageResult = imageService.processImage(imagePath);

            //create statistics for image, using the binary array
            statisticsService.computeStatistics(imageResult);

            return imageMapper.map(imageResult);
        }

        return imageMapper.map(null);
    }

    @GetMapping(value = "/get-image-base64")
    public String sendBase64Image() {

        String encodedfile = null;
        try {

            File file = new File("D:\\Andy\\an3\\InteligentaArtificiala_IA\\P3A5\\src\\main\\resources\\downloads\\page-with-footnote-3.jpg");
            FileInputStream fileInputStreamReader = new FileInputStream(file);

            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);

            //encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            encodedfile = Base64.encodeBase64String(bytes);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return encodedfile;

    }

    @GetMapping(value = "/get-image-bytes")
    public byte[] sendBytesFromBase64Image() {

        String encodedfile = null;
        try {

            File file = new File("D:\\Andy\\an3\\InteligentaArtificiala_IA\\P3A5\\src\\main\\resources\\downloads\\page-with-footnote-3.jpg");
            FileInputStream fileInputStreamReader = new FileInputStream(file);

            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);

            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            encodedfile = Base64.encodeBase64String(bytes);
            return Base64.decodeBase64(bytes);

        } catch (IOException e) {
            e.printStackTrace();
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

    private void setClientImage(Image image, String path) {
        try {

            File file = new File(path);
            FileInputStream fileInputStreamReader = new FileInputStream(file);

            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            
            fileInputStreamReader.close();

            image.clientImage = Base64.encodeBase64String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
