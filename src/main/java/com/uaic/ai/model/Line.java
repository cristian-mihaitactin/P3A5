package com.uaic.ai.model;

import java.awt.Point;
import java.util.ArrayList;

public class Line {
	public Point topLeftCorner;
	public Point topRightCorner;
	public Point bottomLeftCorner;
	public Point bottomRightCorner;
	public ArrayList<Word> words;
	
	public Line(Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner) {
		this.topLeftCorner = topLeftCorner;
		this.topRightCorner = topRightCorner;
		this.bottomLeftCorner = bottomLeftCorner;
		this.bottomRightCorner = bottomRightCorner;
	}
	
	@Override
	public String toString() {
		
		return "Line    topLeftCorner=" + topLeftCorner + "    topRightCorner=" + topRightCorner + "    bottomLeftCorner="
				+ bottomLeftCorner + "    bottomRightCorner=" + bottomRightCorner;
	}
}
