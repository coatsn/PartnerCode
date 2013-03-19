package clueGame;

public class RoomCell extends BoardCell {
	
	//enumerated type door direction
	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	
	//variables
	private DoorDirection doorDirection;
	char roomInitial;
		
	//methods
	@Override
	public boolean isRoom(){
		//isRoom = true;
		return isRoom;
	}
	
	@Override
	public boolean isDoorway(){
		//isDoor = true;
		return isDoor;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public char getRoomInitial() {
		return roomInitial;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	
	
	//setter for door direction
	
	
	//@Override
	//public void draw(){};
	
}
