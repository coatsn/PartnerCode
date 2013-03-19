package clueGame;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.*;

import org.junit.*;



public class BoardTests {
	//Setup Board
	private final int NUM_ROOMS = 11;
	private final int NUM_ROWS =25,  NUM_COLUMNS = 25;
	private Board board;
	private Map<Character, String> testMapRooms;

	@Before
	public void setUpBoard() throws BadConfigFormatException, FileNotFoundException {
		board = new Board("ConfigLayout.csv", "ConfigRooms.txt");	
	}

	//TESTS FOR ROOMS AND BOARD 
	@Test
	public void testCorrectNumOfRooms(){
		//Testing size of Map rooms
		testMapRooms = board.getRooms();
		assertEquals(NUM_ROOMS, testMapRooms.size());
	}

	@Test
	public void testRoomCharacters(){
		testMapRooms = board.getRooms();
		//test that all the room characters map to their respective room (C to 'Conservatory')
		assertEquals("Walkway", testMapRooms.get('W'));
		assertEquals("Conservatory", testMapRooms.get('C'));
		assertEquals("Billiard room", testMapRooms.get('R'));
		assertEquals("Library", testMapRooms.get('L'));
		assertEquals("Dining room", testMapRooms.get('D'));
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}



	// Test a doorway in each direction, plus two cells that are not
	// a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		// Test one each RIGHT/LEFT/UP/DOWN

		RoomCell room = board.getRoomCellAt(3, 3);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getRoomCellAt(2, 10);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getRoomCellAt(9, 18);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		room = board.getRoomCellAt(19, 12);
		assertTrue(room.isDoorway());
		assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getRoomCellAt(14, 14);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCells(board.calcIndex(12, 16));
		assertFalse(cell.isDoorway());		

	}

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(625, totalCells);
		for (int i=0; i < totalCells; i++)
		{
			BoardCell cell = board.getCells(i);
			if (cell.isDoorway())
				numDoors++;
		}
		Assert.assertEquals(16, numDoors);
	}


	// Test a few room cells to ensure the room initial is
	// correct.
	@Test
	public void testRoomInitials() {
		//setup Board and rooms		

		assertEquals('L', board.getRoomCellAt(0, 0).getRoomInitial());
		assertEquals('S', board.getRoomCellAt(4, 8).getRoomInitial());
		assertEquals('H', board.getRoomCellAt(11, 0).getRoomInitial());
		assertEquals('D', board.getRoomCellAt(21, 22).getRoomInitial());
		assertEquals('O', board.getRoomCellAt(21, 0).getRoomInitial());
	}


	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		// overloaded Board ctor takes config file names
		Board b = new Board("ClueLayoutBadColumns.csv", "ClueLegend.txt");
		b.calcAdjacencies();
		// You may change these calls if needed to match your function names
		// My loadConfigFiles has a try/catch, so I can't call it directly to
		// see test throwing the BadConfigFormatException
	}


	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws FileNotFoundException, BadConfigFormatException {
		// overloaded Board ctor takes config file name
		Board b = new Board("ClueLayoutBadRoom.csv", "ClueLegend.txt");
	}


	// Test that an exception is thrown for a bad config file
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		// overloaded Board ctor takes config file name
		Board b = new Board("ClueLayout.csv", "ClueLegendBadFormat.txt");
	}


	@Test
	public void testCalcIndex() {

		// Test each corner of the board
		assertEquals(0, board.calcIndex(0, 0));
		assertEquals(NUM_COLUMNS-1, board.calcIndex(0, NUM_COLUMNS-1));
		assertEquals(600, board.calcIndex(NUM_ROWS-1, 0));
		assertEquals(624, board.calcIndex(NUM_ROWS-1, NUM_COLUMNS-1));
		// Test a couple others
		assertEquals(26, board.calcIndex(1, 1));
		assertEquals(70, board.calcIndex(2, 20));		
	}

	//testing adjacency lists

	// Ensure that player does not move around within room
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner-A1
		LinkedList<BoardCell> testList = board.getAdjList(board.calcIndex(0, 0));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway underneath- J7 :)
		testList = board.getAdjList(board.calcIndex(6, 9));
		Assert.assertEquals(0, testList.size());
		// Test one that has walkway above- J20
		testList = board.getAdjList(board.calcIndex(19, 9));
		Assert.assertEquals(0, testList.size());
		// Test one that is in middle of room- V20
		testList = board.getAdjList(board.calcIndex(19, 21));
		Assert.assertEquals(0, testList.size());
		// Test one beside a door-C13
		testList = board.getAdjList(board.calcIndex(12, 2));
		Assert.assertEquals(0, testList.size());
		// Test one in a corner of room-U4
		testList = board.getAdjList(board.calcIndex(3, 20));
		Assert.assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. 
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT-D4
		LinkedList<BoardCell> testList = board.getAdjList(board.calcIndex(3, 3));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(3, 4))));
		// TEST DOORWAY LEFT-S18 
		testList = board.getAdjList(board.calcIndex(16, 18));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(16, 17))));
		//TEST DOORWAY DOWN-P4
		testList = board.getAdjList(board.calcIndex(3, 15));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(4, 15))));
		//TEST DOORWAY UP-M20
		testList = board.getAdjList(board.calcIndex(19, 12));
		Assert.assertEquals(1, testList.size());
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(18, 12))));

	}

	// Test adjacency at entrance to rooms
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT-E4
		LinkedList<BoardCell> testList = board.getAdjList(board.calcIndex(3, 4));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(2, 4))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(3, 5))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(4, 4))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(3, 3))));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction DOWN- P5
		testList = board.getAdjList(board.calcIndex(4, 15));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(4, 16))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(4, 14))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(5, 15))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(3, 15))));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction LEFT- R18
		testList = board.getAdjList(board.calcIndex(17, 17));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(16, 17))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(18, 17))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(17, 16))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(17, 18))));
		Assert.assertEquals(4, testList.size());
		// Test beside a door direction UP- M19
		testList = board.getAdjList(board.calcIndex(18, 12));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(17, 12))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(18, 11))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(18, 13))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(19, 12))));
		Assert.assertEquals(4, testList.size());
	}

	// Test a variety of walkway scenarios
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, four walkway pieces F1
		LinkedList<BoardCell> testList = board.getAdjList(board.calcIndex(0, 5));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(0, 4))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(0, 6))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(1,5))));
		Assert.assertEquals(3, testList.size());

		// Test on left edge of board, two walkway pieces A20
		testList = board.getAdjList(board.calcIndex(19, 0));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(19, 1))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(20, 0))));
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(18,0))));
		Assert.assertEquals(2, testList.size());

		// Test between two rooms, walkways right and left
//		testList = board.getAdjList(board.calcIndex(6, 21));
//		Assert.assertTrue(testList.contains(board.calcIndex(6, 20)));
//		Assert.assertTrue(testList.contains(board.calcIndex(6, 22)));
//		Assert.assertEquals(2, testList.size());

		// Test surrounded by 4 walkways J18
		testList = board.getAdjList(board.calcIndex(17, 9));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(16, 9))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(18, 9))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(17, 8))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(17, 10))));
		Assert.assertEquals(4, testList.size());
		
		//Q13 
		testList = board.getAdjList(board.calcIndex(12, 16));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(11, 16))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(13, 16))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(12, 15))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(12, 17))));
		Assert.assertEquals(4, testList.size());
		

		// Test on bottom edge of board, next to 1 room and 2 walkway Q24
		testList = board.getAdjList(board.calcIndex(24, 16));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(23, 16))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(24, 17))));
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(24, 15))));
		Assert.assertEquals(2, testList.size());

		// Test on right edge of board, next to 1 room piece and 2 walkway Y15
		testList = board.getAdjList(board.calcIndex(14, 24));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(13, 24))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(14, 23))));
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(15, 24))));
		Assert.assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter, contains 3 walkway, NOT 1 door B13 or 2 rooms
		testList = board.getAdjList(board.calcIndex(13, 2));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(14, 2))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(13, 3))));
		Assert.assertTrue(testList.contains(board.getCells(board.calcIndex(13, 1))));
		//checking door is not included
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(12,1))));
		//checking rooms
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(12, 2))));
		Assert.assertFalse(testList.contains(board.getCells(board.calcIndex(12, 3))));
		Assert.assertEquals(3, testList.size());
	}

	//testing path creation
	@Test
	public void testTargetsOneStep() {
		//K9 and L9
		board.calcAdjacencies();
		board.startTargets(board.calcIndex(8, 10), 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(8, 11)))); 
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(8, 9))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(7,10))));

		board.startTargets(board.calcIndex(8, 11), 1);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(8, 12))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(8, 10))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(7, 11))));			
	}
	
	// Tests of just walkways, 2 steps
	@Test
	public void testTargetsTwoSteps() {
		board.calcAdjacencies();
		board.startTargets(board.calcIndex(13, 19), 2);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		System.out.println(targets);
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 17)))); 
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 21))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 18))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 20))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 18))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 20))));
		
		

		board.startTargets(board.calcIndex(13, 21), 2);
		targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 19)))); 
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 23))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 20))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 22))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 20))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 22))));			
	}
	// Tests of just walkways, 4 steps
	@Test
	public void testTargetsFourSteps() {
		board.calcAdjacencies();
		Set<BoardCell> targets= board.getTargets();
		
		board.startTargets(board.calcIndex(13, 6), 4);
		targets= board.getTargets();
		Assert.assertEquals(10, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 2))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 4))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 8))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 3))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 5))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 7))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(15, 8))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(10, 7))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(11, 8))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 7))));
		
		
		board.startTargets(board.calcIndex(13, 2), 4);
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 0))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 4))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 6))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 1))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 3))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 5))));
		//these are rooms
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 1))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(15, 0))));
		
		
		
	}	
	// Tests of just walkways plus one door, 6 steps

	@Test
	public void testTargetsSixSteps() {
		board.calcAdjacencies();
		board.startTargets(board.calcIndex(24, 22), 6);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(10, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(24, 24))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(24, 20))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(24, 18))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(24, 16))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(23, 23))));	
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(23, 21))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(23, 19))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(23, 17))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(22, 18))));
		
		//This is to enter a room
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(22, 24))));
	}	

	// Test getting into a room

	@Test 
	public void testTargetsIntoRoom()
	{
		board.calcAdjacencies();
		board.startTargets(board.calcIndex(13, 2), 4);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 0))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 4))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(13, 6))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 1))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 3))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(14, 5))));
		//these are rooms
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(12, 1))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(15, 0))));
	}

	// Test getting out of a room
	@Test
	public void testRoomExit()
	{
		board.calcAdjacencies();
		// Take one step, essentially just the adj list
		board.startTargets(board.calcIndex(19, 12), 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(18, 12))));
		// Take two steps
		board.startTargets(board.calcIndex(3, 15), 2);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(5, 15))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(4, 16))));
		Assert.assertTrue(targets.contains(board.getCells(board.calcIndex(4, 14))));
	}
	
	// Test getting into room, doesn't require all steps
//		@Test
//		public void testTargetsIntoRoomShortcut() 
//		{
//			board.calcTargets(board.getCells(board.calcIndex(12, 7)), 3);
//			Set<BoardCell> targets= board.getTargets();
//			Assert.assertEquals(12, targets.size());
//			// directly up and down
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(15, 7)));
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(9, 7)));
//			// directly right (can't go left)
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(12, 10)));
//			// right then down
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(13, 9)));
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(13, 7)));
//			// down then left/right
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(14, 6)));
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(14, 8)));
//			// right then up
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(10, 8)));
//			// into the rooms
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(11, 6)));
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(10, 6)));		
//			// 
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(11, 7)));		
//			Assert.assertTrue(targets.contains(board.getRoomCellAt(12, 8)));		
//	
//		}
//

}
