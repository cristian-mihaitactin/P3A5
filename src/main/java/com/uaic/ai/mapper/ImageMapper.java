package com.uaic.ai.mapper;

import com.uaic.ai.dto.ImageDto;
import com.uaic.ai.model.Image;
import org.springframework.stereotype.Service;

/**
 * Mapper that converts an "Image" object do an "ImageDto"
 */
@Service
public class ImageMapper {

    public ImageDto map(Image image) {
        ImageDto imageDto = new ImageDto();

        imageDto.setColumns(image.getColumns());
        imageDto.setFootnote(image.getFootnote());
        imageDto.setHeader(image.getHeader());
        imageDto.setClientImage(image.getClientImage());
        imageDto.setParagraphs(image.getParagraphs());
        imageDto.setSidenotes(image.getSidenotes());
        return imageDto;
    }

}
