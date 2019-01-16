package com.uaic.ai.service;

import java.awt.Point;

import com.uaic.ai.model.Header;
import com.uaic.ai.model.Image;
import org.springframework.stereotype.Service;

@Service
public class HeaderServiceImpl implements HeaderService{

	@Override
	public void computeHeader(Image img) {
		int emptyLines = 0;
		int titleBeginning=-1;
		int titleEnd=-1;
		
		int []blackPixelsInLine=img.statistics.blackPixelsInLine;
		double potentialTitlePixelCount=img.statistics.potentialTitlePixelCount;
		

		for (int i = 0;i<=blackPixelsInLine.length/3; i++) {

			if (blackPixelsInLine[i]<potentialTitlePixelCount)
			{
				if((titleBeginning!=-1)&&(titleEnd==-1))
					titleEnd=i-1;
				emptyLines++;
			} 
			else
			{
				if(titleBeginning==-1)
					titleBeginning=i;
				if (emptyLines > img.statistics.avgEmptyLinesBetweenTextsLines)
				{
					if(titleBeginning!=i)
					{
						titleEnd=i-emptyLines;
						break;
					}

				}
				emptyLines = 0;
			}
		}
		int x1=0,x2=0;
		for(int i=0;i<img.pixels[titleBeginning].length;i++)
			for(int j=titleBeginning;j<titleEnd;j++)
				if(img.pixels[j][i])
				{
					x1=i;
					i=img.pixels[titleBeginning].length;
					break;
				}
		for(int i=img.pixels[titleBeginning].length-1;i>=0;i--)
			for(int j=titleBeginning;j<titleEnd;j++)
			if(img.pixels[j][i])
			{
				x2=i;
				i=-1;
				break;
			}
		
		img.header=new Header(new Point(x1,titleBeginning),new Point(x2,titleBeginning),new Point(x1,titleEnd),new Point(x2,titleEnd));
		
	}
	


	
	
	


   
}
