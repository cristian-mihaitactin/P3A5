package com.uaic.ai.dto;

import com.uaic.ai.model.Column;
import com.uaic.ai.model.Footnote;
import com.uaic.ai.model.Header;
import com.uaic.ai.model.Paragraph;
import com.uaic.ai.model.Statistics;

import java.util.ArrayList;

public class ImageDto {

    private ArrayList<Column> columns;
    private Footnote footnote;
    private Header header;
    private String clientImage;
    private ArrayList<Paragraph> paragraphs;

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

    public String getClientImage() {
        return clientImage;
    }

    public void setClientImage(String clientImage) {
        this.clientImage = clientImage;
    }

	public ArrayList<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(ArrayList<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}
    
    

}
