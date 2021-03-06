package com.uaic.ai.model;

public class Pixel {
	private double red, green, blue;

	public Pixel(double red, double green, double blue) {
		this.red = red;
		this.blue = blue;
		this.green = green;
	}

	public double getBlackness() {
		return (red + green + blue) / 3 / 255;
	}
}
