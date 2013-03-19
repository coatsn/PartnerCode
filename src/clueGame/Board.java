package clueGame;

import java.io.*;
import java.util.*;


public class Board {

	//variables
	private ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	private Map<Character, String> rooms = new HashMap<Character, String>();
	private int numRows, numColumns;

	//Board Info
	//	private final int numRows = 25;
	//	private final int numColumns = 25;
	private LinkedList<BoardCell> adjacencies;
	private ArrayList<LinkedList<BoardCell>> listOfAdjacencies;

	//Scanner and File IO
	private Scanner scan;
	private FileReader fileIn;


	boolean [] visited;
	Set targets = new HashSet<Integer>();


	//Constructors	
	public Board(){
		//will use a known file that works for the legend and the layout
	}

	public Board(String layoutFile, String legendFile) throws FileNotFoundException, BadConfigFormatException {
		loadConfigFiles(layoutFile, legendFile);
	}


	//methods
	//refactoring: changed xminus1, yminus1, xplus1, and yplus1 to camelCase
	public void calcAdjacencies(){
		listOfAdjacencies = new ArrayList<LinkedList<BoardCell>>();
		for(int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				int currentIndex = calcIndex(i,j);
				adjacencies = new LinkedList<BoardCell>(); 
				if (i-1 >= 0) {
					int xMinus1 = calcIndex(i-1, j);
					if(adjacencyIsValid(currentIndex, xMinus1)){
						adjacencies.add(cells.get(xMinus1));
					}
				}
				if (j-1 >= 0) {
					int yMinus1 = calcIndex(i, j-1);
					if(adjacencyIsValid(currentIndex, yMinus1)){
						adjacencies.add(cells.get(yMinus1));
					}
				}
				if (i+1 < numRows) {
					int xPlus1 = calcIndex(i+1, j);
					if(adjacencyIsValid(currentIndex, xPlus1)){
						adjacencies.add(cells.get(xPlus1));
					}
				}
				if (j+1 < numColumns) {
					int yPlus1 = calcIndex(i, j+1);
					if(adjacencyIsValid(currentIndex, yPlus1)){
						adjacencies.add(cells.get(yPlus1));
					}
				}
				listOfAdjacencies.add(adjacencies);
			}
		}

	}

	//helper method for calc adjacency, only checks for rooms
	public boolean adjacencyIsValid(int currentIndex, int index){
		//added two variables, currentTemp and temp, to simplify the logic inside the if statements
		//(instead of using cells.get(index).isDoorway(), now is temp.isDoorway()
		//this makes the code significantly easier to read
		BoardCell currentTemp = cells.get(currentIndex);
		BoardCell temp = cells.get(index);
		if(currentTemp.isRoom()){
			//refactoring this if statement to have the logic on a single line
			if(currentTemp.isDoorway() && temp.isWalkway()) {
				return true;
			}
			else{
				return false;
			}
		}


		else if(currentTemp.isWalkway()){
			//refactoring this if statement to have the logic on a single line
			if(temp.isRoom() && !temp.isDoorway()){
					return false;
			} else{
				return true;
			}
		}

		return true;
	} 

	public LinkedList<BoardCell> getAdjList(int index){
		listOfAdjacencies = new ArrayList<LinkedList<BoardCell>>();
		calcAdjacencies();
		return listOfAdjacencies.get(index);
	}

	public void setVisitedTrue(int index) {
		visited[index] = true;
	}

	public void setVisitedFalse(int index) {
		visited[index] = false;
	}

	public void startTargets(int start, int steps) {
		targets.clear();
		calcTargets(start, steps, new ArrayList<Integer>());
	}

	public void calcTargets(int start, int steps, ArrayList<Integer> visited) {
		visited.add(start);
		if(steps > 1) {
			for(BoardCell i : listOfAdjacencies.get(start)) {
				if(!visited.contains(calcIndex(i.row,i.col))) {
					if(getCells(calcIndex(i.row,i.col)).isDoorway()) {
						targets.add(getCells(calcIndex(i.row,i.col)));
					}
					ArrayList<Integer> newVisited = (ArrayList<Integer>) visited.clone();
					calcTargets(calcIndex(i.row, i.col), steps - 1, newVisited);
				}
			}
		}
		else if(steps == 1) {
			for(BoardCell i : listOfAdjacencies.get(start)) {
				BoardCell boardCell = getCells(calcIndex(i.row,i.col));
				if(!visited.contains(calcIndex(i.row,i.col)))
					targets.add(boardCell);
			}
		}
	}


	public Set getTargets(){
		return targets;
	}


	public int calcIndex(int row, int col){
		if(row > numRows || row < 0 || col > numColumns || col < 0){
			System.out.println("The row or column is out of bounds.");
		}

		return row*numColumns + col;
	}

	public void loadConfigFiles(String layoutFile, String legendFile) throws FileNotFoundException, BadConfigFormatException {
		loadRoomConfigFiles(legendFile);
		loadBoardConfigFiles(layoutFile);
	}

	public void loadRoomConfigFiles(String legendFile) throws BadConfigFormatException, FileNotFoundException{
		fileIn = new FileReader(legendFile);
		scan = new Scanner(fileIn);
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			String[] toSplit = line.split(", ");
			if (toSplit.length > 2 || toSplit.length < 0) throw new BadConfigFormatException(legendFile);
			else {
				char c = toSplit[0].charAt(0);
				rooms.put(c, toSplit[1]);
			}
		}
	}

	public void loadBoardConfigFiles(String layoutFile) throws BadConfigFormatException {
		try {
			fileIn = new FileReader(layoutFile);
			scan = new Scanner(fileIn);
			int rowCount = 0;
			int columnCount = 0;
			int badColumnCheck = 0;
			boolean firstrun = true;
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] toSplit = line.split(",");
				if (firstrun == true) {
					badColumnCheck = toSplit.length;
				}
				firstrun = false;
				if (toSplit.length != badColumnCheck) {
					throw new BadConfigFormatException(layoutFile);
				} else badColumnCheck = toSplit.length;
				for (int i = 0; i < toSplit.length; ++i) {
					if (toSplit[i].equals("W")) {
						WalkwayCell w = new WalkwayCell();
						w.isWalkway = true;
						w.row = rowCount;
						w.col = i;
						cells.add(w);
					} else {
						RoomCell r = new RoomCell();
						r.roomInitial = toSplit[i].charAt(0);
						if (!rooms.containsKey(r.roomInitial)) {
							throw new BadConfigFormatException(layoutFile);
						}
						r.isRoom = true;
						r.row = rowCount;
						r.col = i;
						if(toSplit[i].length() >  1){
							determineAndSetDoorwayDirection(toSplit[i].charAt(1), r);
							r.isDoor = true;
						} else{
							r.setDoorDirection(RoomCell.DoorDirection.NONE);
						}
						cells.add(r);
					}
				} 
				rowCount++;
				columnCount++;
				//				line = scan.nextLine();
				//				toSplit = line.split(",");

			}
			this.numColumns = columnCount;
			this.numRows = columnCount;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	//helper function for loadBoardConfigFiles function
	public void determineAndSetDoorwayDirection(char aLetter, RoomCell aCell){
		switch(aLetter){
		case 'R': aCell.setDoorDirection(RoomCell.DoorDirection.RIGHT);
		break;
		case 'L': aCell.setDoorDirection(RoomCell.DoorDirection.LEFT);
		break;
		case 'D': aCell.setDoorDirection(RoomCell.DoorDirection.DOWN);
		break;
		case 'U': aCell.setDoorDirection(RoomCell.DoorDirection.UP);
		break;
		}	

	}


	//getters 
	public RoomCell getRoomCellAt(int row, int col) {
		int index = calcIndex(row,col);
		RoomCell returnRoom = new RoomCell();
		if (cells.get(index).isRoom() == true) {
			returnRoom = (RoomCell)cells.get(index);
		}
		return returnRoom;
	}

	public BoardCell getCells(int index) {
		return cells.get(index);
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}



}
