import java.text.DecimalFormat;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class BinaryMatrixImpl implements BinaryMatrix {

	public boolean [][]matrix;
	public BinaryMatrixImpl()
	{
	}
	
	public void compute(String path)
	{
		Mat m=Imgcodecs.imread(path);
		byte[]data=new byte[3];
		matrix=new boolean[m.rows()][m.cols()];
		int darkPixel=0,brightPixel=0,brightness=0,pixel,darkPixelCount=0;
		//GET BRIGHTEST AND DARKEST PIXEL
		for(int i=0;i<m.rows();i++)
		{
			for(int j=0;j<m.cols();j++)
			{
				m.get(i,j,data);
				brightness=brightness+(data[0]&0xFF+data[1]&0xFF+data[2]&0xFF)/3;
			}
		}
		//System.out.println(brightness+" "+m.rows()*m.cols());
		brightness=(brightness/(m.rows()*m.cols()));
		//BUILD THE MATRIX
		for(int i=0;i<m.rows();i++)
			for(int j=0;j<m.cols();j++)
			{
				m.get(i,j,data);
				pixel=(data[0]&0xFF+data[1]&0xFF+data[2]&0xFF)/3;
				
				if(pixel<brightness)
				{
					darkPixel+=pixel;
					darkPixelCount++;
				}
				else
				{
					brightPixel+=pixel;
				}
			}
		
		if(darkPixelCount>0)
			darkPixel=darkPixel/darkPixelCount;
		brightPixel=brightPixel/(m.rows()*m.cols()-darkPixelCount);
		
		for(int i=0;i<m.rows();i++)
			for(int j=0;j<m.cols();j++)
				{
				m.get(i,j,data);
				pixel=(data[0]&0xFF+data[1]&0xFF+data[2]&0xFF)/3;
				matrix[i][j] = ((pixel- darkPixel)*(pixel- darkPixel) < (brightPixel - pixel)*(brightPixel - pixel));
				}
		
	}
	/**
	 * @return the matrix
	 */
	public boolean [][] getMatrix() {
		return matrix;
	}
	/**
	 * @param matrix the matrix to set
	 */
	public void setMatrix(boolean [][] matrix) {
		this.matrix = matrix;
	}
	
	public boolean containsFootnotes()
	{
		return getFootnotesBeginningLine()==-1?false:true;
	}
	
	public int getFootnotesBeginningLine()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		BinaryMatrixImpl m = new BinaryMatrixImpl();
		m.compute("C:\\Users\\MasterCode\\Desktop\\AI proiect\\2.jpg");
		double[] linesPixelPercentage = new double[m.matrix.length];
		
		  
		double blackPixelsPerLine=0,blackPixelsPerTextLine=0,blackPixelsPerEmptyLine=0;
		int blackPixelCount=0,textLines=0,emptyLines=0,blackPixelsInLine[]=new int[m.matrix.length];
		for(int i=0;i<m.matrix.length;i++)
		{
			blackPixelCount=0;
			for(int j=0;j<m.matrix[i].length;j++)
			{
				blackPixelCount+=m.matrix[i][j]?1:0;
			}
			blackPixelsInLine[i]=blackPixelCount;
		}
		blackPixelCount=0;
		for(int i=0;i<blackPixelsInLine.length;i++)
			blackPixelCount+=blackPixelsInLine[i];
		
		blackPixelsPerLine=(double)blackPixelCount/(m.matrix.length*m.matrix[0].length);
		for(int i=0;i<m.matrix.length;i++)
		{
			
			if(((double)blackPixelsInLine[i]/m.matrix[i].length)>blackPixelsPerLine)
			{
				blackPixelsPerTextLine+=(double)blackPixelsInLine[i]/m.matrix[i].length;
				textLines++;
			}
			else
			{
				blackPixelsPerEmptyLine+=(double)blackPixelsInLine[i]/m.matrix[i].length;
				emptyLines++;
			}
		}
		blackPixelsPerTextLine=blackPixelsPerTextLine/textLines;
		blackPixelsPerEmptyLine=blackPixelsPerEmptyLine/emptyLines;
		
		double actualPixelsPerLine=0;
		emptyLines=0;
		double averageEmptyLinesBetweenTextsLines=0;
		int emptySpacesCount=0;
		boolean lineIsText[]=new boolean[blackPixelsInLine.length];
		for(int i=0;i<blackPixelsInLine.length;i++)
		{
			actualPixelsPerLine=(double)blackPixelsInLine[i]/m.matrix[i].length;
			if(((actualPixelsPerLine-blackPixelsPerEmptyLine)*(actualPixelsPerLine-blackPixelsPerEmptyLine))<((actualPixelsPerLine-blackPixelsPerLine)*(actualPixelsPerLine-blackPixelsPerLine)))
			{
				lineIsText[i]=false;
				emptyLines++;
			}
			else
			{
				lineIsText[i]=true;
				if(emptyLines>0)
				{
					averageEmptyLinesBetweenTextsLines+=emptyLines;
					emptyLines=0;
					emptySpacesCount++;
				}
				
			}
		}
		averageEmptyLinesBetweenTextsLines=averageEmptyLinesBetweenTextsLines/emptySpacesCount;
		double inferiorLimit=0;
		emptyLines=0;
		emptySpacesCount=0;
		for(int i=0;i<lineIsText.length;i++)
		{
			if(!lineIsText[i])
			{
				emptyLines++;
				
			}
			else
			{
				if((emptyLines>averageEmptyLinesBetweenTextsLines)&&(emptyLines>0))
				{
					inferiorLimit+=emptyLines;
					emptySpacesCount++;
					emptyLines=0;
				}
			}
		}
		averageEmptyLinesBetweenTextsLines=inferiorLimit/emptySpacesCount;
		emptyLines=0;
		emptySpacesCount=0;
		//UNCOMMENT FOR DEBUG PURPOSE
		for(int i=lineIsText.length-(lineIsText.length/3);i<lineIsText.length;i++)
		{
			
			if(!lineIsText[i])
			{
				emptyLines++;
				//System.out.print("EMPTY: ");
			}
			else
			{
				if(emptyLines>averageEmptyLinesBetweenTextsLines)
				{
					emptySpacesCount++;
					return i;
					//System.out.println("UNUSUAL:");
					
				}
				emptyLines=0;
				//System.out.print("TEXT:  ");
			}
			/*for(int j=0;j<m.matrix[i].length;j++)
				System.out.print(m.matrix[i][j]?1:" ");
			System.out.println(" ");*/
		}
		
		
		//PRINT STATISTICS
		/*System.out.println("Empty lines: "+emptyLines);
		System.out.println("Black Pixels Per Line: "+blackPixelsPerLine);
		System.out.println("Black Pixels Per Text Line: "+blackPixelsPerTextLine);
		System.out.println("Black Pixels Per Empty Line: "+blackPixelsPerEmptyLine);
		System.out.println("Average Empty Lines Between Texts Lines: "+averageEmptyLinesBetweenTextsLines);
		System.out.println("Large Empty Spaces between texts in the footnote zone: "+emptySpacesCount);*/
		
		return -1;
		

	}
	
}
