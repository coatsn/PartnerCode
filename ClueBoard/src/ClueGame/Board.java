package ClueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private ArrayList<BoardCell> cells;
	private HashMap<Character, String> rooms;
	private int numRows, numColumns;
	private String boardFile, roomFile;
	private HashSet<BoardCell> targets;
	private boolean[] visited;
	private Map<Integer, LinkedList<Integer>> adjLists;

	public Board() {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		targets = new HashSet<BoardCell>();
		adjLists = new HashMap<Integer, LinkedList<Integer>>();
		boardFile = "";
		roomFile = "";
		numRows = 0;
		numColumns = 0;

	}
	
	public Board(String boardFile, String roomFile) {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		targets = new HashSet<BoardCell>();
		adjLists = new HashMap<Integer, LinkedList<Integer>>();
		this.boardFile = boardFile;
		this.roomFile = roomFile;
		numRows = 0;
		numColumns = 0;

	}

	//initializes adjLists
	public void setAdjList() {

		for(int i = 0; i < (numRows * numColumns); i++ ) {
			adjLists.put(i, new LinkedList<Integer>());
		}
	}

	//initializes visited
	public void setVisited() {
		visited = new boolean[numRows*numColumns];

		for(int i = 0; i < calcIndex(numRows-1, numColumns-1) ; i++ ) {
			visited[i] = false;
		}
	}

	public HashSet getTargets() {
		return targets;
	}

	//resets targets to an empty set every time getTargetsReset is called
	public HashSet getTargetsReset() {
		HashSet<BoardCell> temp = targets;
		targets = new HashSet<BoardCell>();
		return temp;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public HashMap getRooms() {
		return rooms;
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	//Loads both the room and board files
	public void  loadConfigFiles() {
		try {
			loadRoomConfig();
			loadBoardConfig();
		} catch(BadConfigException e) {
			System.out.println(e);
		}
	}

	//loads every room or walkway cell into cells
	public void loadBoardConfig() throws BadConfigException {
		String tempString;
		String[] tempArray;
		int count = 0;
		FileReader reader = null;
		try {
			reader = new FileReader(boardFile);
		} catch (FileNotFoundException e) {
			System.out.println("bad board file");
		}
		Scanner in = new Scanner(reader);
		numRows = 0;
		numColumns = 0;
		int numColumnsCurrent = 0;
		boolean hasLooped = false;
		while(in.hasNextLine()) {
			boolean inLegend = false;
			numRows++;

			if(numColumnsCurrent != numColumns && hasLooped){
				throw new BadConfigException("Inconsistent number of columns in board config file.");
			}
			numColumns = numColumnsCurrent;
			numColumnsCurrent = 0;
			tempString = in.nextLine();

			tempArray = tempString.split(",");

			Set<Character> keys = rooms.keySet();

			for (String s : tempArray) {
				numColumnsCurrent++;
				inLegend = false;
				for(Character key : keys) {
					if(key.equals(s.charAt(0)))
						inLegend = true;
				}
				if(!inLegend)
					throw new BadConfigException("Room not in legend.");

				if(s == "W") {
					cells.add(new WalkwayCell(count, s));
				} else {
					cells.add(new RoomCell(count, s));
				}
			}
			if(!hasLooped){
				numColumns = numColumnsCurrent;
			}
			hasLooped = true;

		}
		in.close();
	}

	//loads the legend into rooms
	public void loadRoomConfig() throws BadConfigException {
		Character index;
		String name, tempString, tempArray[];
		FileReader reader = null;

		try {
			reader = new FileReader(roomFile);
		} catch (FileNotFoundException e) {
			System.out.println("Bad room config file, file name given: " + roomFile);
		}
		Scanner in = new Scanner(reader);

		while(in.hasNextLine()) {
			tempString = in.nextLine();
			tempArray = tempString.split(",");

			if(tempArray.length != 2) {
				throw new BadConfigException("Invalid legend format.");
			}
			index = tempArray[0].charAt(0);
			name = tempArray[1];
			rooms.put(index, name);
		}
	}

	public int getNumRooms() {
		return rooms.size();
	}

	public int calcIndex(int row, int column) {
		return (row*(numColumns)) + column;
	}

	//returns the roomCell at the given row and column
	public RoomCell GetRoomCellAt(int row, int column) {
		int index = calcIndex(row, column);
		if(cells.get(index).isRoom())
			return (RoomCell)cells.get(index);
		else
			return null;
	}
	
	//returns the BoardCell at the given index
	public BoardCell getCellAt(int index) {
		return cells.get(index);
	}

	//calculates the adjacencies for every valid cell and stores
	//them in adjLists 
	public void calcAdjacencies() {
		int tempRow, tempCol;
		LinkedList<Integer> tempList;
		Set<Integer> keys = adjLists.keySet();
		System.out.println(adjLists.get(0));
		for(int key : keys) {

			tempList = new LinkedList<Integer>();
			tempRow = key/(numRows);
			tempCol = key%(numColumns);

			if(tempRow < numRows && tempCol < numColumns) {
				int index = 0;
				boolean atEdge = false;
				BoardCell current = null;

				if(tempRow > 0)
					index = calcIndex(tempRow-1,tempCol);
				else
					atEdge = true;

				current = cells.get(index);
				if(!atEdge && (current.isWalkway() || 
						(current.isDoorway() && ((RoomCell) current).getDoorDirection() == RoomCell.DoorDirection.DOWN))){
					tempList.add(index);
				}

				atEdge = false;
				if(tempRow < numRows - 1)
					index = calcIndex(tempRow+1,tempCol);
				else
					atEdge = true;

				current = cells.get(index);
				if(!atEdge && (current.isWalkway() || 
						(current.isDoorway() && ((RoomCell) current).getDoorDirection() == RoomCell.DoorDirection.UP))){
					tempList.add(index);
				}

				atEdge = false;
				if(tempCol > 0)
					index = calcIndex(tempRow,tempCol-1);
				else
					atEdge = true;
				current = cells.get(index);

				if(!atEdge && (current.isWalkway() || 
						(current.isDoorway() && ((RoomCell) current).getDoorDirection() == RoomCell.DoorDirection.RIGHT))) {
					tempList.add(index);
				}

				atEdge = false;
				if(tempCol < numColumns - 1)
					index = calcIndex(tempRow,tempCol+1);
				else
					atEdge = true;


				current = cells.get(index);

				if(!atEdge && (current.isWalkway() || 
						(current.isDoorway() && ((RoomCell) current).getDoorDirection() == RoomCell.DoorDirection.LEFT))){
					tempList.add(index);
				}
				adjLists.put(key, tempList);
			}
		}
	}

	//calculates the possible targets the given number of steps away, starting
	//from the given row and column
	public void startTargets(int row, int column, int steps) {
		int index = calcIndex(row,column);
		if(index < calcIndex(numRows,numColumns)) {
			LinkedList<Integer> temp = adjLists.get(index);
			if(steps != 0 && getCellAt(index).isWalkway()){
				visited[index] = true;
				for(Integer i : temp){
					if (visited[i] == false){
						startTargets(i/numRows, i%numColumns , steps -1);
					}
				}

			}
			else{
				targets.add(cells.get(index));
				if(steps != 0){
					for(Integer i : temp){
						if (visited[i] == false){
							startTargets(i/numRows, i%numColumns , steps -1);
						}
					}
				}

			}
		}
	}

	public LinkedList<Integer> getAdjList(int index) {
		return adjLists.get(index);
	}
}
