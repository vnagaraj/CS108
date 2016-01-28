package tetris;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
   Board b;
   Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;


   // This shows how to build things in setUp() to re-use
   // across tests.

   // In this case, setUp() makes shapes,
   // and also a 3X6 board, with pyr placed at the bottom,
   // ready to be used by tests.
   @Before
   public void setUp() throws Exception {
      b = new Board(3, 6);
      //System.out.println(b);

      pyr1 = new Piece(Piece.PYRAMID_STR);
      pyr2 = pyr1.computeNextRotation();
      pyr3 = pyr2.computeNextRotation();
      pyr4 = pyr3.computeNextRotation();

      s = new Piece(Piece.S1_STR);
      sRotated = s.computeNextRotation();

      b.place(pyr1, 0, 0);
      //System.out.println(b);

   }

   // Check the basic width/height/max after the one placement
   @Test
   public void testSample1() {
      assertEquals(1, b.getColumnHeight(0));
      assertEquals(2, b.getColumnHeight(1));
      assertEquals(2, b.getMaxHeight());
      assertEquals(3, b.getRowWidth(0));
      assertEquals(1, b.getRowWidth(1));
      assertEquals(0, b.getRowWidth(2));
   }

   // Place sRotated into the board, then check some measures
   @Test
   public void testSample2() {
      b.commit();
      //System.out.println(b);
      int result = b.place(sRotated, 1, 1);
      assertEquals(Board.PLACE_OK, result);
      assertEquals(1, b.getColumnHeight(0));
      assertEquals(4, b.getColumnHeight(1));
      assertEquals(3, b.getColumnHeight(2));
      assertEquals(4, b.getMaxHeight());
   }

   @Test
   public void testSample3() {
      Board board = new Board(3, 6);
      //System.out.println(board);
      Piece p1 = new Piece(Piece.L1_STR);
      int result = board.place(p1, 0, 0);
      assertEquals(Board.PLACE_OK, result);
      board.commit();
      //System.out.println(board);
      assertEquals(3, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(0, board.getColumnHeight(2));
      assertEquals(3, board.getMaxHeight());
      Piece p2 = new Piece(Piece.STICK_STR);
      result = board.place(p2, 2, 0);
      assertEquals(Board.PLACE_ROW_FILLED, result);
      board.commit();
      //System.out.println(board);
      assertEquals(3, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(4, board.getColumnHeight(2));
      assertEquals(4, board.getMaxHeight());
      board.clearRows();
      //System.out.println(board);
      assertEquals(2, board.getColumnHeight(0));
      assertEquals(0, board.getColumnHeight(1));
      assertEquals(3, board.getColumnHeight(2));
      assertEquals(3, board.getMaxHeight());
      board.commit();
      result = board.place(p2, 2, 3);
      assertEquals(Board.PLACE_OUT_BOUNDS, result);
      //System.out.println(board);
      board.undo();
      //System.out.println(board);

   }
   // testing clearRows when board has 2 full rows
   @Test
   public void testSample4() {
	  Board board = new Board(3,6);
	  Piece p1 = new Piece(Piece.L1_STR);
      int result = board.place(p1.computeNextRotation(), 0, 0);
      //System.out.println(board);
      assertEquals(Board.PLACE_ROW_FILLED, result);
      board.commit();
      assertEquals(1, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(2, board.getColumnHeight(2));
      assertEquals(2, board.getMaxHeight());
      assertEquals(3, board.getRowWidth(0));
      assertEquals(1, board.getRowWidth(1));
      assertEquals(0, board.getRowWidth(2));
      assertEquals(0, board.getRowWidth(3));
      assertEquals(0, board.getRowWidth(4));
      assertEquals(0, board.getRowWidth(5));
      result = board.place(new Piece(Piece.PYRAMID_STR), 0, 1);
      assertEquals(Board.PLACE_BAD, result);
      //System.out.println(board);
      board.undo();
      assertEquals(1, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(2, board.getColumnHeight(2));
      assertEquals(2, board.getMaxHeight());
      assertEquals(3, board.getRowWidth(0));
      assertEquals(1, board.getRowWidth(1));
      assertEquals(0, board.getRowWidth(2));
      assertEquals(0, board.getRowWidth(3));
      assertEquals(0, board.getRowWidth(4));
      assertEquals(0, board.getRowWidth(5));
      //System.out.println(board);
      p1 = new Piece(Piece.SQUARE_STR);
      result = board.place(p1.computeNextRotation(), 0, 1);
      assertEquals(Board.PLACE_ROW_FILLED, result);
      //System.out.println(board);
      assertEquals(3, board.getColumnHeight(0));
      assertEquals(3, board.getColumnHeight(1));
      assertEquals(2, board.getColumnHeight(2));
      assertEquals(3, board.getMaxHeight());
      assertEquals(3, board.getRowWidth(0));
      assertEquals(3, board.getRowWidth(1));
      assertEquals(2, board.getRowWidth(2));
      assertEquals(0, board.getRowWidth(3));
      assertEquals(0, board.getRowWidth(4));
      assertEquals(0, board.getRowWidth(5));
      board.clearRows();
      assertEquals(1, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(0, board.getColumnHeight(2));
      assertEquals(1, board.getMaxHeight());
      assertEquals(2, board.getRowWidth(0));
      assertEquals(0, board.getRowWidth(1));
      assertEquals(0, board.getRowWidth(2));
      assertEquals(0, board.getRowWidth(3));
      assertEquals(0, board.getRowWidth(4));
      assertEquals(0, board.getRowWidth(5));
      //System.out.println(board);
      board.undo();
      assertEquals(1, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(2, board.getColumnHeight(2));
      assertEquals(2, board.getMaxHeight());
      assertEquals(3, board.getRowWidth(0));
      assertEquals(1, board.getRowWidth(1));
      assertEquals(0, board.getRowWidth(2));
      assertEquals(0, board.getRowWidth(3));
      assertEquals(0, board.getRowWidth(4));
      assertEquals(0, board.getRowWidth(5));
      //System.out.println(board); 
      board.undo();
      assertEquals(1, board.getColumnHeight(0));
      assertEquals(1, board.getColumnHeight(1));
      assertEquals(2, board.getColumnHeight(2));
      assertEquals(2, board.getMaxHeight());
      //System.out.println(board);	   
   }
   

	@Test
	public void testSample5() {
		Board board1 = new Board(5,8);
		assertEquals((board1.getWidth()),5);
		assertEquals((board1.getHeight()),8);
		
		Board board = new Board(3,6);
		assertEquals((board.getWidth()),3);
		assertEquals((board.getHeight()),6);
		//System.out.println(board);
		int y =board.dropHeight(new Piece(Piece.STICK_STR), 0);
		assertEquals(0,y);
		board.place(new Piece(Piece.L1_STR), 0, 0);
		assertEquals((board.getGrid(0, 0)),true);
		assertEquals((board.getGrid(0, 2)),true);
		assertEquals((board.getGrid(3, 3)),true);
		assertEquals((board.getGrid(2, 6)),true);
		assertEquals((board.getGrid(2, 3)),false);
		assertEquals((board.getGrid(2, 5)),false);
		board.commit();
		y = board.dropHeight(new Piece(Piece.S1_STR).computeNextRotation(), 0);
		assertEquals(2,y);
		int result = board.place(new Piece(Piece.S1_STR).computeNextRotation(), 0,4);
		assertEquals(result, Board.PLACE_OUT_BOUNDS);
		board.undo();
		//System.out.println(board);
		result = board.place(new Piece(Piece.S1_STR).computeNextRotation(), 0,2);
		assertEquals(result, Board.PLACE_OK);
		//System.out.println(board);
		board.commit();
		y = board.dropHeight(new Piece(Piece.L1_STR).computeNextRotation().computeNextRotation(), 1);
		assertEquals(2,y);
		result = board.place(new Piece(Piece.L1_STR).computeNextRotation().computeNextRotation(), 1,y);
		assertEquals(result, Board.PLACE_ROW_FILLED);
		//System.out.println(board);
		board.clearRows();
		//System.out.println(board);
		board.undo();
		//System.out.println(board);
		y = board.dropHeight(new Piece(Piece.SQUARE_STR), 1);
		assertEquals(4,y);
		result = board.place(new Piece(Piece.SQUARE_STR), 1,y);
		assertEquals(result, Board.PLACE_ROW_FILLED);
		//System.out.println(board);
		board = new Board(3,6);
		y = board.dropHeight(new Piece(Piece.L1_STR).computeNextRotation(), 0);
		assertEquals(0,y);
		result = board.place(new Piece(Piece.L1_STR).computeNextRotation(), 0,y);
		assertEquals(result, Board.PLACE_ROW_FILLED);
		//System.out.println(board);
	}
}
   
 

   // Make  more tests, by putting together longer series of
   // place, clearRows, undo, place ... checking a few col/row/max
   // numbers that the board looks right after the operations.



