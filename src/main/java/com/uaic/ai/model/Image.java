package com.uaic.ai.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Image {
	public boolean[][] pixels;
	public ArrayList<Column> columns;
	public Footnote footnote;
	public Header header;
	public Statistics statistics;
	@Override
	public String toString() {
		return "Image [ columns=" + columns + "\n footnote=" + footnote
				+ "\n header=" + header + "\n statistics=" + statistics + "]";
	}

}
