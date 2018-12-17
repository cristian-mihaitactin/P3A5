package com.uaic.ai.service;

import com.uaic.ai.model.Image;
import com.uaic.ai.model.Statistics;

public interface StatisticsService {

    Statistics computeStatistics(Image img);

}
