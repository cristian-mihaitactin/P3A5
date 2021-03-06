package com.uaic.ai.model;

import java.awt.Point;

public class Paragraph {
	public Point topLeftCorner;
	public Point topRightCorner;
	public Point bottomLeftCorner;
	public Point bottomRightCorner;
	
	public Paragraph(Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner) {
		this.topLeftCorner = topLeftCorner;
		this.topRightCorner = topRightCorner;
		this.bottomLeftCorner = bottomLeftCorner;
		this.bottomRightCorner = bottomRightCorner;
	}
	
	@Override
	public String toString() {
		
		return "Paragraph    topLeftCorner=" + topLeftCorner + "    topRightCorner=" + topRightCorner + "    bottomLeftCorner="
				+ bottomLeftCorner + "    bottomRightCorner=" + bottomRightCorner;
	}
}
