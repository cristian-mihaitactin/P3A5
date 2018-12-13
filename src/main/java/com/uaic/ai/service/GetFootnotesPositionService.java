
public class GetFootnotesPositionService {

	
	public static int getFootnotes(Image img)
	{
		 int emptyLines = 0;
	     int emptySpacesCount = 0;
	     boolean []lineIsText=img.statistics.lineIsText;
	     

	        for (int i = lineIsText.length - (lineIsText.length / 3); i < lineIsText.length; i++) {

	            if (!lineIsText[i]) {
	                emptyLines++;
	                //System.out.print("EMPTY: ");
	            } else {
	                if (emptyLines > img.statistics.avgEmptyLinesBetweenTextsLines) {
	                    emptySpacesCount++;
	                    return i;
	                    //System.out.println("UNUSUAL:");

	                }
	                emptyLines = 0;
	                //System.out.print("TEXT:  ");
	            }

				/*
				for(int j=0;j<matrix[i].length;j++)
					System.out.print(matrix[i][j]?1:" ");
				System.out.println(" ");
				*/
	        }
		return -1;
	}
	
}
