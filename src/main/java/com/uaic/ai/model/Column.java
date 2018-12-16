package com.uaic.ai.model;

import java.awt.Point;
import java.util.ArrayList;

public class Column {
	public Point topLeftCorner;
	public Point topRightCorner;
	public Point bottomLeftCorner;
	public Point bottomRightCorner;
	public ArrayList<Line> lines = new ArrayList<Line>();
	
	public Column(Point topLeftCorner, Point topRightCorner, Point bottomLeftCorner, Point bottomRightCorner) {
		this.topLeftCorner = topLeftCorner;
		this.topRightCorner = topRightCorner;
		this.bottomLeftCorner = bottomLeftCorner;
		this.bottomRightCorner = bottomRightCorner;
	}

	@Override
	public String toString() {
		
		return "Column    topLeftCorner=" + topLeftCorner + "    topRightCorner=" + topRightCorner + "    bottomLeftCorner="
				+ bottomLeftCorner + "    bottomRightCorner=" + bottomRightCorner + "\n  "+ lines.size() +"  lines=" + lines + "\n";
	}
	
}
