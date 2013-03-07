package ClueGame;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {
	
	private Board board;
	private String roomFile, boardFile;
	
	@Before
	public void setup() {
		roomFile = "roomFile.txt";
		boardFile = "boardFile.txt";
		board = new Board(boardFile, roomFile);
		board.loadConfigFiles();
	}

	@Test
	public void testRooms() {
		int numRooms = 11;
		assertEquals(numRooms, board.getNumRooms());
	}

	@Test
	public void testMapping() {
		HashMap<Character, String> mapping = board.getRooms();
		Set<Character> keys = mapping.keySet();

		assertTrue(keys.size() != 0);
		for(Character key : keys) {
			if(key.equals('C')) {
				assertEquals(mapping.get(key), "Conservatory");
			} else if(key.equals('K')) {
				assertEquals(mapping.get(key), "Kitchen");
			} else if(key.equals('B')) {
				assertEquals(mapping.get(key), "Ballroom");
			} else if(key.equals('R')) {
				assertEquals(mapping.get(key), "Billiard Room");
			} else if(key.equals('L')) {
				assertEquals(mapping.get(key), "Library");
			} else if(key.equals('S')) {
				assertEquals(mapping.get(key), "Study");
			} else if(key.equals('D')) {
				assertEquals(mapping.get(key), "Dining Room");
			} else if(key.equals('O')) {
				assertEquals(mapping.get(key), "Lounge");
			} else if(key.equals('H')) {
				assertEquals(mapping.get(key), "Hall");
			} else if(key.equals('X')) {
				assertEquals(mapping.get(key), "Closet");
			} else if(key.equals('W')) {
				assertEquals(mapping.get(key), "Walkway");
			}
		}
	}

	@Test
	public void testNumRowCol() {
		int numRows = 20;
		int numCols = 20;

		assertEquals(numRows, board.getNumRows());
		assertEquals(numCols, board.getNumColumns());
	}
	
	@Test
	public void testDoorDirection() {
		int numDoors = 18;
		int countDoors = 0;
		ArrayList<BoardCell> cells = board.getCells();
	    int dRow = 4;
	    int dCol = 3;
	    
		for(BoardCell cell : cells) {
			if(cell.isDoorway()) {
				countDoors++;
			}
		}
		assertEquals(numDoors, countDoors);
		RoomCell cell = board.GetRoomCellAt(dRow,dCol);
		assertEquals(RoomCell.DoorDirection.DOWN, cell.getDoorDirection());	
	}
	
	@Test
	public void testInitials() {
		char initial = 'C';
		int row = 0;
		int col = 0;
		RoomCell cell = board.GetRoomCellAt(row,col);
		assertEquals(initial, cell.getInitial());
	}
	
	@Test
	public void testIndex() {
		int realIndex = 48;
		int index = board.calcIndex(2,8);
		assertEquals(realIndex, index);
		realIndex = 399;
		index = board.calcIndex(19,19);
		assertEquals(realIndex, index);
	}
	
	@Test (expected = BadConfigException.class)
	public void testBadColumns() throws BadConfigException {
		Board newBoard = new Board("badColumns.txt", roomFile);
		newBoard.loadRoomConfig();
		newBoard.loadBoardConfig();
	}
	
	@Test (expected = BadConfigException.class)
	public void testBadRoom() throws BadConfigException {
		Board newBoard = new Board("badRoom.txt", roomFile);
		newBoard.loadRoomConfig();
		newBoard.loadBoardConfig();
	}
	
	@Test (expected = BadConfigException.class)
	public void testBadRoomFormat() throws BadConfigException {
		Board newBoard = new Board("badLayout.txt", roomFile);
		newBoard.loadRoomConfig();
		newBoard.loadBoardConfig();
	}
	
	
}
