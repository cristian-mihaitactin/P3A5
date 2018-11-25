import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class BinaryMatrixImpl implements BinaryMatrix {

	public boolean [][]matrix;
	public BinaryMatrixImpl(String path)
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
	
}
