package ClueGame;

public class RoomCell extends BoardCell {
	enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private DoorDirection doorDirection;
	private char initial;
	private String data;
	private int count;
	
	public RoomCell(int count, String s) throws BadConfigException {
		super(count, s);
		initial = s.charAt(0);
		if(s.length() > 1) {
			char dir = s.charAt(1);
			switch(dir) {
				case('U'):
					doorDirection = DoorDirection.UP;
					break;
				case('D'):
					doorDirection = DoorDirection.DOWN;
					break;
				case('L'):
					doorDirection = DoorDirection.LEFT;
					break;
				case('R'):
					doorDirection = DoorDirection.RIGHT;
					break;
				case('N'):
					doorDirection = DoorDirection.NONE;
					break;
				default:
		//			System.out.println(s);
					throw new BadConfigException("Invalid room cell.");
			}
		}
		else {
			doorDirection = DoorDirection.NONE;
		}
	}

	@Override
	public boolean isRoom() {
		return true;
	}
	
	@Override
	public void draw() {
		
	}
	
	public char getInitial() {
		return initial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
}
