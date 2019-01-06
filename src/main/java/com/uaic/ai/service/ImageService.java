package com.uaic.ai.service;

import com.uaic.ai.model.Image;

import javax.validation.constraints.NotNull;

public interface ImageService {

    Image processImage(byte[] bytes);

    Image processImage(String path);
    
	void correctImage(String path,String allignedImagePath, String outPath);

}
