
public class Footnote {

	private int beginningX;
	private int beginningY;
	private int endingX;
	private int endingY;
	
	
	public int getBeginningX() {
		return beginningX;
	}

	public int getBeginningY() {
		return beginningY;
	}

	public int getEndingX() {
		return endingX;
	}

	public int getEndingY() {
		return endingY;
	}

	public Footnote()
	{
		
	}
	
	public Footnote(int bX,int bY,int eX,int eY)
	{
		beginningX=bX;
		beginningY=bY;
		endingX=eX;
		endingY=eY;
	}
	
}
