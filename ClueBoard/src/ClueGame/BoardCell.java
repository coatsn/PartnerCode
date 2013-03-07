package ClueGame;

import java.util.LinkedList;
import java.util.Map;

abstract public class BoardCell {
	private int row, column, index;
	private String data;
	private LinkedList<Integer> adjList;
	
	public BoardCell(int i, String data){
		this.data = data;
		index = i;
		row = i /20;
		column = i % 20;
		adjList = new LinkedList<Integer>();
	}
	
	public int getIndex() {
		return index;
	}
	public boolean isWalkway() {
		if(data.equals("W"))
			return true;
		else
			return false;
	}
	
	public boolean isRoom() {
		if(!data.equals("W"))
			return true;
		else
			return false;
	}
	
	public LinkedList<Integer> getAdjList(){
		return new LinkedList<Integer>();
	}
	
	public boolean isDoorway() {
		Character dir = ' ';
		if(data.length() > 1) 
			dir = data.charAt(1);
		if((data.length() > 1 && !dir.equals('N')))
			return true;
		else
			return false;
	}
	
	abstract public void draw();
}
