package com.uaic.ai.service;

import com.uaic.ai.model.Footnote;
import com.uaic.ai.model.Image;

public interface FootnotesService {

    Footnote getFootnotesCoordinates(Image img);

}
