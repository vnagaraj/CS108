// Board.java
package tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
 */
public class Board {
   private final int height;
   private boolean[][] grid;
   // Some ivars are stubbed out for you:
   private final int width;
   private final boolean DEBUG = true;
   boolean committed;
   private int[] widths;
   private int[] heights;
   private int maxHeight;

   //backup data structures
   private int[] backupWidths;
   private int[] backupHeights;
   private boolean[][] backupGrid;
   private int backupMaxHeight;

   // variables for sanity check
   private boolean clearRows;


   // Here a few trivial methods are provided:

   /**
    Creates an empty board of the given width and height
    measured in blocks.
    */
   public Board(int width, int height) {
      this.width = width;
      this.height = height;
      grid = new boolean[width][height];
      committed = true;

      // YOUR CODE HERE
      widths = new int[height];
      heights = new int[width];
   }


   /**
    Returns the width of the board in blocks.
    */
   public int getWidth() {
      return width;
   }


   /**
    Returns the height of the board in blocks.
    */
   public int getHeight() {
      return height;
   }


   /**
    Returns the max column height present in the board.
    For an empty board this is 0.
    */
   public int getMaxHeight() { // YOUR CODE HERE
      return this.maxHeight;
   }

   private void computeMaxHeight() {
      int val = 0;
      for (int i = 0; i < width; i++) {
         int height = getColumnHeight(i);
         if (height > val)
            val = height;
      }
      this.maxHeight = val;
   }


   /**
    Checks the board for internal consistency -- used
    for debugging.
    */
   public void sanityCheck() {
      if (DEBUG) {
         // YOUR CODE HERE
         verifyWidths();
         verifyHeights();
         verifyMaxHeight();
         if (clearRows) {
            verifyClearRows();
            clearRows = false;
         }

      }
   }

   private void verifyWidths() {
      for (int col = 0; col < height; col++) {
         int count = 0;
         int index = col;
         for (int row = 0; row < width; row++) {
            if (grid[row][col]) {
               count++;
            }
         }
         if (!(widths[index] == count))
            throw new RuntimeException("Widhths array at index " + index
                  + "not in synch with grid");
      }
   }

   private void verifyHeights() {
      for (int row = 0; row < width; row++) {
         int count = -1;
         int index = row;
         for (int col = 0; col < height; col++) {
            if (grid[row][col]) {
               count = col;
            }
         }
         if (count == -1) {
            if (!(heights[index] == 0))
               throw new RuntimeException("Heights array at index " + index
                     + "not in synch with grid");
         } else if (!(heights[index] == count + 1))
            throw new RuntimeException("Heights array at index " + index
                  + "not in synch with grid");
      }
   }

   private void verifyMaxHeight() {
      int val = 0;
      for (int i = 0; i < width; i++) {
         int height = getColumnHeight(i);
         if (height > val)
            val = height;
      }
      if (val != this.maxHeight)
         throw new RuntimeException("Max Height not in synch with grid");
   }

   private void verifyClearRows() {
      for (int col = 0; col < height; col++) {
         int count = 0;
         int index = 0;
         for (int row = 0; row < width; row++) {
            if (grid[row][col]) {
               count++;
            }
            index = row;
         }
         if (count == width)
            throw new RuntimeException("Full row still present at index "
                  + index);
      }
   }

   /**
    Given a piece and an x, returns the y
    value where the piece would come to rest
    if it were dropped straight down at that x.

    <p>
    Implementation: use the skirt and the col heights
    to compute this fast -- O(skirt length).
    */
   public int dropHeight(Piece piece, int x) {
	      int max = 0;
	      int c = x;
	      for (int i = 0; i < piece.getWidth(); i++) {
	        // if (c >= width)
	          //  throw new RuntimeException("width out of bounds");	        	 
	         int height = this.getColumnHeight(c);
	         int skirt = piece.getSkirt()[i];
	         int y = height - skirt;
	         if (y > max)
	            max = y;
	         c++;
	      }
	     // if (max + piece.getHeight() > height)
	       //  throw new RuntimeException("Height out of bounds");
	      return max;
   }


   /**
    Returns the height of the given column --
    i.e. the y value of the highest block + 1.
    The height is 0 if the column contains no blocks.
    */
   public int getColumnHeight(int x) {
      // YOUR CODE HERE
      return heights[x];
   }


   /**
    Returns the number of filled blocks in
    the given row.
    */
   public int getRowWidth(int y) {
      return this.widths[y]; // YOUR CODE HERE
   }


   /**
    Returns true if the given block is filled in the board.
    Blocks outside of the valid width/height area
    always return true.
    */
   public boolean getGrid(int x, int y) {
      if (x < 0 || x >= width)
         return true;
      if (y < 0 || y >= height)
         return true;
      return this.grid[x][y];// YOUR CODE HERE
   }


   public static final int PLACE_OK = 0;
   public static final int PLACE_ROW_FILLED = 1;
   public static final int PLACE_OUT_BOUNDS = 2;
   public static final int PLACE_BAD = 3;

   /**
    Attempts to add the body of a piece to the board.
    Copies the piece blocks into the board grid.
    Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
    for a regular placement that causes at least one row to be filled.

    <p>Error cases:
    A placement may fail in two ways. First, if part of the piece may falls out
    of bounds of the board, PLACE_OUT_BOUNDS is returned.
    Or the placement may collide with existing blocks in the grid
    in which case PLACE_BAD is returned.
    In both error cases, the board may be left in an invalid
    state. The client can use undo(), to recover the valid, pre-place state.
    */
   public int place(Piece piece, int x, int y) {
      // flag !committed problem
      if (!committed)
         throw new RuntimeException("place commit problem");
      committed = false;
      createBackups();

      int result = PLACE_OK;
      boolean rowFull = false;

      // YOUR CODE HERE
      for (TPoint tpoint : piece.getBody()) {
         int a = x + tpoint.x;
         int b = y + tpoint.y;
         if (a < 0 || a >= width)
            return (result = PLACE_OUT_BOUNDS);
         if (b < 0 || b >= height)
            return (result = PLACE_OUT_BOUNDS);
         if (grid[a][b]) {
            return (result = PLACE_BAD);
         }
         grid[a][b] = true;
         widths[b]++;
         if (widths[b] == width) {
            rowFull = true;
         }
         updateHeights();
         computeMaxHeight();

      }
      if (result == PLACE_OK && rowFull) {
         result = PLACE_ROW_FILLED;
      }
      sanityCheck();
      return result;
   }

   private void createBackups() {
      backupGrid = new boolean[width][height];
      backupWidths = new int[widths.length];
      backupHeights = new int[heights.length];
      for (int i=0; i < grid.length; i++){
          System.arraycopy(grid[i], 0, backupGrid[i], 0, grid[i].length);
      }
      System.arraycopy(widths, 0, backupWidths, 0, widths.length);
      System.arraycopy(heights, 0, backupHeights, 0, heights.length);
      backupMaxHeight = maxHeight;
   }

   private void updateOriginal() {
      this.grid = this.backupGrid;
      this.widths = this.backupWidths;
      this.heights = this.backupHeights;
      this.maxHeight = this.backupMaxHeight;
   }


   /**
    Deletes rows that are filled all the way across, moving
    things above down. Returns the number of rows cleared.
    */
   public int clearRows() {
      int rowsCleared = 0;
      // YOUR CODE HERE
      while (true) {
         int col = checkFirstFullRow();
         if (col == -1)
            break;
         updateWidths(col);
         rowsCleared++;
         for (int j = col + 1; j < height; j++) {
            for (int i = 0; i < width; i++) {
               grid[i][col] = grid[i][j];
            }
            col++;
         }
         // last col, with false values
         for (int row = 0; row < width; row++) {
            grid[row][height - 1] = false;
         }
      }
      updateHeights();
      computeMaxHeight();
      clearRows = true;
      sanityCheck();
      committed = false;
      return rowsCleared;
   }

   private void updateWidths(int col) {
      while (col < height - 1) {
         widths[col] = widths[col + 1];
         col++;
      }
      widths[height - 1] = 0;
   }

   private void updateHeights() {
      boolean noblocks = false;

      for (int j = 0; j < width; j++) {
         int val = 0;
         for (int i = 0; i < height; i++) {
            if (grid[j][i]) {
               noblocks = true;
               if (i > val) {
                  val = i;
               }
            }
         }
         if (!noblocks)
            heights[j] = 0;
         else
            heights[j] = val + 1;
         noblocks = false;
      }
   }


   private int checkFirstFullRow() {
      for (int col = 0; col < height; col++) {
         int fulllength = 0;
         for (int row = 0; row < width; row++) {
            if (grid[row][col]) {
               fulllength++;
            }
            if (fulllength == width) {
               // System.out.println("Row is Full at column " + j);
               return col;
            }
         }
      }
      return -1;
   }



   /**
    Reverts the board to its state before up to one place
    and one clearRows();
    If the conditions for undo() are not met, such as
    calling undo() twice in a row, then the second undo() does nothing.
    See the overview docs.
    */
   public void undo() {
      // YOUR CODE HERE
      if (!committed) {
         updateOriginal();
         committed = true;
      }
   }


   /**
    Puts the board in the committed state.
    */
   public void commit() {
      if (!committed) {
         committed = true;
         createBackups();
      }
   }


   /*
    Renders the board state as a big String, suitable for printing.
    This is the sort of print-obj-state utility that can help see complex
    state change over time.
    (provided debugging utility)
    */
   @Override
   public String toString() {
      StringBuilder buff = new StringBuilder();
      for (int y = height - 1; y >= 0; y--) {
         buff.append('|');
         for (int x = 0; x < width; x++) {
            if (getGrid(x, y))
               buff.append('+');
            else
               buff.append(' ');
         }
         buff.append("|\n");
      }
      for (int x = 0; x < width + 2; x++)
         buff.append('-');
      return (buff.toString());
   }
}


