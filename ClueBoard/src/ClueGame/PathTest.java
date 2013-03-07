package ClueGame;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PathTest {
	
	private Board board;
	private String roomFile, boardFile;
	
	@Before
	public void setup() {
		roomFile = "roomFile.txt";
		boardFile = "boardFile.txt";
		board = new Board(boardFile, roomFile);
		board.loadConfigFiles();
		board.setAdjList();
		board.setVisited();
		board.calcAdjacencies();
		
	}
	
	@Test
	public void testWalkwayAdjacency(){
		int row = 14;
		int column = 6;
		int index = board.calcIndex(row, column);
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = board.getAdjList(index);
		assertTrue(list.size() != 0);
		assertTrue(list.contains(287));
		assertTrue(list.contains(285));
		assertTrue(list.contains(266));
		assertTrue(list.contains(306));
		assertEquals(4, list.size());
	}
	
	@Test
	public void testEdgeAdjacency(){
		int row = 5;
		int column = 0;
		int index = board.calcIndex(row, column);
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = board.getAdjList(index);
		assertTrue(list.contains(101));
		assertEquals(1, list.size());
		
		row = 0;
		column = 5;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(4));
		assertTrue(list.contains(25));
		assertEquals(2, list.size());
		
		row = 5;
		column = 19;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(118));
		assertTrue(list.contains(99));
		assertEquals(2, list.size());
		
		row = 19;
		column = 6;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(366));
		assertTrue(list.contains(385));
		assertEquals(2, list.size());
	}
	
	@Test
	public void testRoomAdjacency(){
		int row = 14;
		int column = 1;
		int index = board.calcIndex(row, column);
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = board.getAdjList(index);
		assertTrue(list.contains(280));
		assertTrue(list.contains(282));
		assertEquals(2, list.size());
		
		row = 5;
		column = 9;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(129));
		assertTrue(list.contains(108));
		assertTrue(list.contains(110));
		assertEquals(3, list.size());
	}
	
	@Test
	public void testNearDoorAdjacency(){
		int row = 4;
		int column = 6;
		int index = board.calcIndex(row, column);
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = board.getAdjList(index);
		assertTrue(list.contains(106));
		assertTrue(list.contains(85));
		assertTrue(list.contains(87));
		assertEquals(3, list.size());
		
		row = 14;
		column = 9;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(288));
		assertTrue(list.contains(290));
		assertTrue(list.contains(309));
		assertTrue(list.contains(269));
		assertEquals(4, list.size());
		
	}
	
	@Test
	public void testDoorAdjacency(){
		int row = 4;
		int column = 3;
		int index = board.calcIndex(row, column);
		LinkedList<Integer> list = new LinkedList<Integer>();
		list = board.getAdjList(index);
		assertTrue(list.contains(84));
		assertTrue(list.contains(103));
		assertEquals(2, list.size());
		
		row = 2;
		column = 11;
		index = board.calcIndex(row, column);
		list = board.getAdjList(index);
		assertTrue(list.contains(52));
		assertEquals(1, list.size());
		
	}
	
	@Test
	public void testWalkwayTargets(){
		board.startTargets(0, 4, 1);
		HashSet<BoardCell> targets = board.getTargetsReset();
		
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 5))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 4))));
		
		board.startTargets(3, 5, 2);
		targets= board.getTargetsReset();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(5, 5))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 5))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 4))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 4))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 6))));
		
		board.startTargets(14, 0, 3);
		targets= board.getTargetsReset();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 3))));
		
		board.startTargets(13, 17, 4);
		targets= board.getTargetsReset();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(12, 14))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 15))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(13, 13))));
	}
	
	@Test
	public void testEnterTargets(){
		board.startTargets(9, 4, 2);
		Set targets= board.getTargetsReset();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(7, 4))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(11, 4))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 3))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(10, 3))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(8, 5))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(10, 5))));


		board.startTargets(2, 12, 2);
		targets= board.getTargetsReset();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 11))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 13))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(0, 12))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 12))));
		
	}
	
	@Test
	public void testRoomTargets(){
		board.startTargets(16, 14, 2);
		Set targets= board.getTargetsReset();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 13))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 12))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 13))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 14))));
		
		board.startTargets(4, 15, 3);
		targets= board.getTargetsReset();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(6, 14))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(5, 13))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(5, 17))));
		assertTrue(targets.contains(board.getCellAt(board.calcIndex(4, 15))));
	}

}
