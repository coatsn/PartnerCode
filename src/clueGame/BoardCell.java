package clueGame;

public abstract class BoardCell {
	//variables
	protected int row, col;
	protected boolean isWalkway = false;
	protected boolean isDoor = false, isRoom = false;
	
	//methods
	public boolean isWalkway(){
		return false;
	}
	
	public boolean isRoom(){
		return false;
	}
	
	public boolean isDoorway(){
		return false;
	}
	
	//public abstract void draw();
	

}
