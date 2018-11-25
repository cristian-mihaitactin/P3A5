import java.text.DecimalFormat;

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
		int min=255,max=0,brightness;
		//GET BRIGHTEST AND DARKEST PIXEL
		for(int i=0;i<m.rows();i++)
		{
			for(int j=0;j<m.cols();j++)
			{
				m.get(i,j,data);
				brightness=(data[0]&0xFF+data[1]&0xFF+data[2]&0xFF)/3;
				if(min>brightness)
					min=brightness;
				if(max<brightness)
					max=brightness;
			}
		}
		//BUILD THE MATRIX
		for(int i=0;i<m.rows();i++)
			for(int j=0;j<m.cols();j++)
			{
				m.get(i,j,data);
				brightness=(data[0]&0xFF+data[1]&0xFF+data[2]&0xFF)/3;
				matrix[i][j]=((brightness-min)<(max-brightness));
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
		
		//REFACTOR THIS
		BinaryMatrixImpl m = new BinaryMatrixImpl();
		m.compute("C:\\Users\\MasterCode\\Desktop\\AI proiect\\2.jpg");
		double[] linesPixelPercentage = new double[m.matrix.length];
		/*
		 * for(int i=0;i<110;i++) { for(int j=0;j<110;j++)
		 * System.out.print((m.matrix[i][j]?1:" ")+" "); System.out.println(""); }
		 */
		for (int i = 0; i < m.matrix.length; i++) {
			int zeroP = 0, oneP = 0;
			for (int j = 0; j < m.matrix[i].length; j++) {
				if (m.matrix[i][j]) {
					oneP++;
				} else
					zeroP++;
				// System.out.print((m.matrix[i][j]?1:" ")+" ");
			}
			linesPixelPercentage[i] = (((double) oneP / (oneP + zeroP)) * 100);
			// System.out.println("\nLine: "+i+" Zeros: "+zeroP+" Ones: "+oneP);
		}
		DecimalFormat df = new DecimalFormat("#.##");
		int sum = 0;

		for (int i = 0; i < linesPixelPercentage.length; i++) {
			sum += (linesPixelPercentage[i] * 100);
			System.out.println("Line: " + i + " Percentage: " + df.format(linesPixelPercentage[i]) + "%");
		}
		sum = (sum / 100) / linesPixelPercentage.length;
		System.out.println("MEDIA: " + sum);
		double linieLibera = sum / 10;
		int countLiniiLibere = 0;
		double medieCount = 0;
		int spatii = 0;
		boolean ok=false;
		for (int i = 0; i < linesPixelPercentage.length; i++) {
			if (linesPixelPercentage[i] < linieLibera)
			{
				countLiniiLibere++;
				ok=true;
			}
			else {
				if(ok) {
				medieCount += countLiniiLibere;
				spatii++;
				countLiniiLibere = 0;
				ok=false;
				}
			}
		}
		
		
		
		System.out.println("Media Count: " + medieCount / spatii);
		ok=false;
		countLiniiLibere=0;
		for (int i = 0; i < linesPixelPercentage.length; i++) 
		{
			if (linesPixelPercentage[i] < linieLibera)
			{
				countLiniiLibere++;
				
				if(countLiniiLibere>countLiniiLibere+(countLiniiLibere/2))
					ok=true;
			}
			if(ok)
			{
				
			}
			
		}
		return true;
	}
	
}
