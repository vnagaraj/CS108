package assign3;

import java.util.ArrayList;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class CopyOfSudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = CopyOfSudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2", "2 0 0 4 0 3 9 1 0", "0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0", "5 0 0 1 0 2 0 0 8", "0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0", "0 7 3 5 0 9 0 0 1", "4 0 0 0 0 0 6 7 9");

	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = CopyOfSudoku.stringsToGrid("530070000",
			"600195000", "098000060", "800060003", "400803001", "700020006",
			"060000280", "000419005", "000080079");


	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = CopyOfSudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0", "0 0 1 0 9 3 0 0 0", "0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2", "0 0 0 0 4 0 0 0 0", "5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0", "0 0 0 5 3 0 9 0 0", "0 3 0 0 0 0 0 5 1");
	

	public static final int SIZE = 9; // size of the whole 9x9 puzzle
	public static final int PART = 3; // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	/**
	 * Returns a 2-d grid parsed from strings, one string per row. The "..." is
	 * a Java 5 feature that essentially makes "rows" a String[] array.
	 * (provided utility)
	 * 
	 * @param rows
	 *            array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row < rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}

	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid. Skips
	 * all the non-numbers in the text. (provided utility)
	 * 
	 * @param text
	 *            string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE * SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:"
					+ nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}

	/**
	 * Given a string containing digits, like "1 23 4", returns an int[] of
	 * those digits {1 2 3 4}. (provided utility)
	 * 
	 * @param string
	 *            string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i + 1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}

	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		CopyOfSudoku sudoku;
		sudoku = new CopyOfSudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	private void initSpotArr(Spot[][] spotArr, int[][] ints) {
		int pos = 0;
		for (int row = 0; row < spotArr.length; row++) {
			for (int col = 0; col < spotArr[0].length; col++) {
				int num = ints[row][col];
				Spot spot = new Spot(num);
				spotArr[row][col] = spot;
				spot.setRow(row);
				spot.setCol(col);
				spot.setRegionFromPos(pos);
				pos++;
			}
		}
	}

	private final Spot[][] spotArr;
	ArrayList<Spot[][]> s = new ArrayList<Spot[][]>();

	public CopyOfSudoku(String text) {
		int[][] ints = CopyOfSudoku.stringsToGrid(text);
		spotArr = new Spot[ints.length][ints[0].length];
		this.initSpotArr(spotArr, ints);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.spotArr.length; i++) {
			for (int j = 0; j < this.spotArr[0].length; j++) {
				sb.append(spotArr[i][j].value);
			}
			sb.append(System.getProperty("line.separator"));
		}
		return new String(sb);
	}

	/**
	 * Sets up based on the given ints.
	 */
	public CopyOfSudoku(int[][] ints) {
		// YOUR CODE HERE
		spotArr = new Spot[ints.length][ints[0].length];
		this.initSpotArr(spotArr, ints);
	}

	private ArrayList<Spot> getUnAssignedSpots(Spot[][] spotArr) {
		ArrayList<Spot> spotList = new ArrayList<Spot>();
		for (int row = 0; row < spotArr.length; row++) {
			for (int col = 0; col < spotArr[0].length; col++) {
				Spot spot = spotArr[row][col];
				if (spot.getValue() == 0)
					spotList.add(spot);
			}
		}
		return spotList;
	}

	private ArrayList<Integer> addNosToList(ArrayList<Integer> list) {
		for (int i = 1; i < 10; i++) {
			list.add(i);
		}
		return list;
	}

	private ArrayList<Integer> getAssignableLocations(Spot spot,
			Spot[][] spotArr) {
		if (spot.getValue() != 0) {
			return new ArrayList<Integer>();
		}
		int row = spot.getRow();
		int col = spot.getCol();
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		rowList = this.addNosToList(rowList);
		for (int num = 1; num < 10; num++) {
			for (int i = 0; i < spotArr[0].length; i++) {
				Spot sp = spotArr[row][i];
				int val = sp.getValue();
				if (val == num) {
					Integer value = val;
					rowList.remove(value);
					break;
				}
			}
		}
		ArrayList<Integer> colList = new ArrayList<Integer>();
		colList = this.addNosToList(colList);
		for (int num = 1; num < 10; num++) {
			for (int i = 0; i < spotArr.length; i++) {
				Spot sp = spotArr[i][col];
				int val = sp.getValue();
				if (val == num) {
					Integer value = val;
					colList.remove(value);
					break;
				}
			}
		}
		ArrayList<Integer> regionList = new ArrayList<Integer>();
		regionList = this.addNosToList(regionList);
		for (int i = 0; i < spotArr.length; i++) {
			for (int j = 0; j < spotArr[0].length; j++) {
				int region = spotArr[i][j].getRegion();
				if (region == spot.getRegion()) {
					Integer val = spotArr[i][j].getValue();
					if (regionList.contains(val)) {
						regionList.remove(val);
					}
				}
			}
		}
		ArrayList<Integer> list1 = findCommonElements(rowList, colList);
		return this.findCommonElements(list1, regionList);

	}

	private ArrayList<Integer> findCommonElements(ArrayList<Integer> l1,
			ArrayList<Integer> l2) {
		ArrayList<Integer> commonList = new ArrayList<Integer>();
		for (Integer val : l1) {
			if (l2.contains(val)) {
				commonList.add(val);
			}
		}
		return commonList;
	}


	private long startTime;
	private long endTime;

	private long timeElapsedOnSolve;


	private boolean solved;

	private boolean set = true;

	private static class Spot {
		private int value;
		// variables to store location of spot on grid
		private int row;
		private int col;
		// to get region info
		private int region;

		public int getValue() {
			return value;
		}

		public void setRegionFromPos(int pos) {
			region = this.computeRegion(pos);
		}

		public void setRegion(int region) {
			this.region = region;
		}

		public int getRegion() {
			return region;
		}

		private int computeRegion(int pos) {
			if (pos % 9 < 3 && pos <= 20)
				return 0;
			else if (pos % 9 < 3 && pos <= 47)
				return 1;
			else if (pos % 9 < 3 && pos <= 74)
				return 2;
			else if (pos % 9 < 6 && pos <= 23)
				return 3;
			else if (pos % 9 < 6 && pos <= 50)
				return 4;
			else if (pos % 9 < 6 && pos <= 77)
				return 5;
			else if (pos % 9 < 9 && pos <= 26)
				return 6;
			else if (pos % 9 < 9 && pos <= 53)
				return 7;
			else
				return 8;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getRow() {
			return row;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public int getCol() {
			return this.col;
		}

		Spot(int value) {
			this.value = value;
		}

		public void setValue(int val) {
			this.value = val;
		}

	}

	/*
	 * Returns true if the two given arrays have the same value for all the
	 * entries.
	 */

	private boolean compareSpotArrays(Spot[][] spotArr1, Spot[][] spotArr2) {
		if (spotArr1.length != spotArr2.length)
			return false;
		if (spotArr1[0].length != spotArr2[0].length)
			return false;
		for (int i = 0; i < spotArr1.length; i++) {
			for (int j = 0; j < spotArr1[0].length; j++) {
				Spot spot1 = spotArr1[i][j];
				Spot spot2 = spotArr2[i][j];
				if (!(spot1.getValue() == spot2.getValue()))
					return false;
			}
		}
		return true;
	}

	/*
	 * Adds unique spotArr to the set by checking if there is no match with the
	 * existing entries in the set
	 */

	private void addSpotArrToSet(Spot[][] spotArr) {
		boolean match = false;
		for (Spot[][] spotArrInSet : s) {
			if (this.compareSpotArrays(spotArrInSet, spotArr)) {
				match = true;
				break;
			}
		}
		if (!match) {
			this.endTime = System.currentTimeMillis();
			if (set ){
			this.timeElapsedOnSolve = this.endTime - this.startTime;
			set = false;
			}
			// print(spotArr);
			Spot[][] newSpotArr = this.createBackUpSpotArr(spotArr);
			s.add(newSpotArr);
		}
	}

	private Spot[][] createBackUpSpotArr(Spot[][] spotArr) {
		Spot[][] backupSpotArr = new Spot[spotArr.length][spotArr[0].length];
		for (int i = 0; i < spotArr.length; i++) {
			for (int j = 0; j < spotArr[0].length; j++) {
				Spot spot = spotArr[i][j];
				Spot backup = new Spot(spot.getValue());
				backup.setCol(spot.getCol());
				backup.setRow(spot.getRow());
				backup.setRegion(spot.getRegion());
				backupSpotArr[i][j] = backup;
			}
		}
		return backupSpotArr;
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		// return 0; // YOUR CODE HERE
		Spot[][] spotArray = this.createBackUpSpotArr(this.spotArr);
		this.solveSoduku(spotArray);
		return s.size();
	}

	private void checkStateOFArr(Spot spot, Spot[][] spotArr) {
		int row = spot.getRow();
		int col = spot.getCol();
		for (int i = row + 1; i < this.spotArr.length; i++) {
			for (int j = 0; j < this.spotArr[0].length; j++) {
				if (spotArr[i][j].value != this.spotArr[i][j].value)
					spotArr[i][j].value = this.spotArr[i][j].value;
			}
		}
		for (int i = col + 1; i < this.spotArr[0].length; i++) {
			if (spotArr[row][i].value != this.spotArr[row][i].value)
				spotArr[row][i].value = this.spotArr[row][i].value;
		}
	}

	private void solveSoduku(Spot[][] spotArr) {
		if (s.size() == this.MAX_SOLUTIONS)
			return ;
		ArrayList<Spot> unAssignedSpots = getUnAssignedSpots(spotArr);
		if (unAssignedSpots.size() == 0) {
			this.solved = true;
			addSpotArrToSet(spotArr);
			spotArr = this.createBackUpSpotArr(this.spotArr);
			return;
		}
		Spot spot = unAssignedSpots.get(0);

		ArrayList<Integer> assignableLocationsList = this
				.getAssignableLocations(spot, spotArr);

		for (Integer val : assignableLocationsList) {
			spotArr[spot.getRow()][spot.getCol()].setValue(val);
			checkStateOFArr(spot, spotArr);
			this.solveSoduku(spotArr);
			if (!this.solved) {
				spotArr[spot.getRow()][spot.getCol()].setValue(0); // unassigned
				checkStateOFArr(spot, spotArr);
			} else {
				solved = false;

			}
		}
		if (assignableLocationsList.size() == 0) {
			this.solved = false;
			return;
		}
	}

	private int[][] convSpotArrToIntArr(Spot[][] spotArr) {
		int[][] grid = new int[spotArr.length][spotArr[0].length];
		for (int i = 0; i < spotArr.length; i++) {
			for (int j = 0; j < spotArr[0].length; j++) {
				grid[i][j] = spotArr[i][j].getValue();
			}
		}
		return grid;
	}

	public String getSolutionText() {
		if (s.size() != 0) {
			Spot[][] spotSol = s.get(0);
			int[][] ints = this.convSpotArrToIntArr(spotSol);
			CopyOfSudoku sudoku = new CopyOfSudoku(ints);
			return sudoku.toString();
		}
		return ""; // YOUR CODE HERE
	}

	public long getElapsed() {
		return timeElapsedOnSolve; // YOUR CODE HERE
	}

}