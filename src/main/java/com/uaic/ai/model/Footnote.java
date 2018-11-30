package com.uaic.ai.model;

import java.awt.Point;
import java.util.ArrayList;

public class Footnote {
	public Point topLeftCorner;
	public Point topRightCorner;
	public Point bottomLeftCorner;
	public Point bottomRightCorner;
	
	public Footnote(Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner) {
		this.topLeftCorner = topLeftCorner;
		this.topRightCorner = topRightCorner;
		this.bottomLeftCorner = bottomLeftCorner;
		this.bottomRightCorner = bottomRightCorner;
	}
}
