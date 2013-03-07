package ClueGame;

public class WalkwayCell extends BoardCell {
	private String data;
	private int index;
	
	WalkwayCell(int index, String data) {
		super(index, data);
	}

	@Override
	public boolean isWalkway() {
		return true;

	}
	
	@Override
	public void draw() {
		
	}
}
