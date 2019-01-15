package model;

import java.util.ArrayList;

public class Image {
	public boolean[][] pixels;
	public ArrayList<Column> columns;
	public ArrayList<Paragraph> paragraphs;
	public Footnote footnote;
	public Header header;
	public Statistics statistics;
	public String clientImage;

	@Override
	public String toString() {
		return "Image [ columns=" + columns + "\n footnote=" + footnote
				+ "\n header=" + header + "\n statistics=" + statistics + "]";
	}

    public boolean[][] getPixels() {
        return pixels;
    }

    public void setPixels(boolean[][] pixels) {
        this.pixels = pixels;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }

    public Footnote getFootnote() {
        return footnote;
    }

    public void setFootnote(Footnote footnote) {
        this.footnote = footnote;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public ArrayList<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(ArrayList<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

	public String getClientImage() {
		return clientImage;
	}

	public void setClientImage(String clientImage) {
		this.clientImage = clientImage;
	}
    
    
}
