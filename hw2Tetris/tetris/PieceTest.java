package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	


	@Before
	public void setUp() throws Exception {
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	
	@Test
	public void testSTICK_STR_Rotation(){
		Piece p1 = new Piece(Piece.STICK_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(0,0), (new TPoint(1,0)), (new TPoint(2,0)), (new TPoint(3,0))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, p1);	
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[0].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), p1);		
	}
	
	@Test
	public void testL1_STR_Rotation(){
		Piece p1 = new Piece(Piece.L1_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(0,0), (new TPoint(1,0)), (new TPoint(2,0)), (new TPoint(2,1))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		TPoint[] tpointList2 = {new TPoint(1,0), (new TPoint(1,1)), (new TPoint(1,2)), (new TPoint(0,2))};
		Piece expectedRotatedPiece2 = new Piece(tpointList2);
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, expectedRotatedPiece2);
		TPoint[] tpointList3 = {new TPoint(0,0), (new TPoint(0,1)), (new TPoint(1,1)), (new TPoint(2,1))};
		Piece expectedRotatedPiece3 = new Piece(tpointList3);
		Piece actualRotatedPiece3 = actualRotatedPiece2.computeNextRotation();
		assertEquals(actualRotatedPiece3, expectedRotatedPiece3);
		assertEquals(actualRotatedPiece3.computeNextRotation(), p1);
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[1].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), expectedRotatedPiece2);	
		assertEquals(fast.fastRotation().fastRotation(), expectedRotatedPiece3);
		assertEquals(fast.fastRotation().fastRotation().fastRotation(), p1);
	}	
	
	@Test
	public void testL2_STR_Rotation(){
		Piece p1 = new Piece(Piece.L2_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(0,1), (new TPoint(1,1)), (new TPoint(2,0)), (new TPoint(2,1))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		TPoint[] tpointList2 = {new TPoint(0,0), (new TPoint(0,1)), (new TPoint(0,2)), (new TPoint(1,2))};
		Piece expectedRotatedPiece2 = new Piece(tpointList2);
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, expectedRotatedPiece2);
		TPoint[] tpointList3 = {new TPoint(0,0), (new TPoint(0,1)), (new TPoint(1,0)), (new TPoint(2,0))};
		Piece expectedRotatedPiece3 = new Piece(tpointList3);
		Piece actualRotatedPiece3 = actualRotatedPiece2.computeNextRotation();
		assertEquals(actualRotatedPiece3, expectedRotatedPiece3);
		assertEquals(actualRotatedPiece3.computeNextRotation(), p1);
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[2].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), expectedRotatedPiece2);	
		assertEquals(fast.fastRotation().fastRotation(), expectedRotatedPiece3);
		assertEquals(fast.fastRotation().fastRotation().fastRotation(), p1);
	}
	
	@Test
	public void testS1_STR_Rotation(){
		Piece p1 = new Piece(Piece.S1_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(0,1), (new TPoint(0,2)), (new TPoint(1,0)), (new TPoint(1,1))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, p1);
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[3].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), p1);	
	}
	
	@Test
	public void testS2_STR_Rotation(){
		Piece p1 = new Piece(Piece.S2_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(0,0), (new TPoint(0,1)), (new TPoint(1,1)), (new TPoint(1,2))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, p1);
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[4].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), p1);	
	}
	
	@Test
	public void testSQUARE_STR(){
		Piece p1 = new Piece(Piece.SQUARE_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		assertEquals(actualRotatedPiece, p1);	
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[5].fastRotation();
		assertEquals(fast, p1);
	}
	
	@Test
	public void testPYRAMID_STR(){
		Piece p1 = new Piece(Piece.PYRAMID_STR);
		Piece actualRotatedPiece = p1.computeNextRotation();
		TPoint[] tpointList = {new TPoint(1,0), (new TPoint(0,1)), (new TPoint(1,1)), (new TPoint(1,2))};
		Piece expectedRotatedPiece = new Piece(tpointList);
		assertEquals(actualRotatedPiece, expectedRotatedPiece);	
		TPoint[] tpointList2 = {new TPoint(1,0), (new TPoint(0,1)), (new TPoint(1,1)), (new TPoint(2,1))};
		Piece expectedRotatedPiece2 = new Piece(tpointList2);
		Piece actualRotatedPiece2 = actualRotatedPiece.computeNextRotation();
		assertEquals(actualRotatedPiece2, expectedRotatedPiece2);
		TPoint[] tpointList3 = {new TPoint(0,0), (new TPoint(0,1)), (new TPoint(0,2)), (new TPoint(1,1))};
		Piece expectedRotatedPiece3 = new Piece(tpointList3);
		Piece actualRotatedPiece3 = actualRotatedPiece2.computeNextRotation();
		assertEquals(actualRotatedPiece3, expectedRotatedPiece3);
		assertEquals(actualRotatedPiece3.computeNextRotation(), p1);
		Piece[] pieceList = Piece.getPieces();
		Piece fast = pieceList[6].fastRotation();
		assertEquals(fast, expectedRotatedPiece);
		assertEquals(fast.fastRotation(), expectedRotatedPiece2);	
		assertEquals(fast.fastRotation().fastRotation(), expectedRotatedPiece3);
		assertEquals(fast.fastRotation().fastRotation().fastRotation(), p1);
	}
	
	@Test
	public void test_STICK_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.STICK_STR);
		assertTrue(Arrays.equals(new int[] {0}, p1.getSkirt()));
		assertEquals(1, p1.getWidth());	
		assertEquals(4, p1.getHeight());	
		Piece p2 = p1.computeNextRotation();
		assertTrue(Arrays.equals(new int[] {0,0,0,0}, p2.getSkirt()));
		assertEquals(1, p2.getHeight());	
		assertEquals(4, p2.getWidth());		
	}
	
	@Test
	public void testL1_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.L1_STR);
		assertEquals(2, p1.getWidth());	
		assertEquals(3, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(3, p2.getWidth());	
		assertEquals(2, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0,0}, p2.getSkirt()));
		Piece p3 = p2.computeNextRotation();
		assertEquals(2, p3.getWidth());	
		assertEquals(3, p3.getHeight());
		assertTrue(Arrays.equals(new int[] {2,0}, p3.getSkirt()));
		Piece p4 = p3.computeNextRotation();
		assertEquals(3, p4.getWidth());	
		assertEquals(2, p4.getHeight());
		assertTrue(Arrays.equals(new int[] {0,1,1}, p4.getSkirt()));
		Piece p5 = p4.computeNextRotation();
		assertEquals(2, p5.getWidth());	
		assertEquals(3, p5.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p5.getSkirt()));
	}	
	
	@Test
	public void testL2_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.L2_STR);
		assertEquals(2, p1.getWidth());	
		assertEquals(3, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(3, p2.getWidth());	
		assertEquals(2, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {1,1,0}, p2.getSkirt()));
		Piece p3 = p2.computeNextRotation();
		assertEquals(2, p3.getWidth());	
		assertEquals(3, p3.getHeight());
		assertTrue(Arrays.equals(new int[] {0,2}, p3.getSkirt()));
		Piece p4 = p3.computeNextRotation();
		assertEquals(3, p4.getWidth());	
		assertEquals(2, p4.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0,0}, p4.getSkirt()));
		Piece p5 = p4.computeNextRotation();
		assertEquals(2, p5.getWidth());	
		assertEquals(3, p5.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p5.getSkirt()));
	}	
	
	@Test
	public void testS1_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.S1_STR);
		assertEquals(3, p1.getWidth());	
		assertEquals(2, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0,1}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(2, p2.getWidth());	
		assertEquals(3, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {1,0}, p2.getSkirt()));
		Piece p3 = p2.computeNextRotation();
		assertEquals(3, p3.getWidth());	
		assertEquals(2, p3.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0,1}, p3.getSkirt()));
	}
	
	@Test
	public void testS2_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.S2_STR);
		assertEquals(3, p1.getWidth());	
		assertEquals(2, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {1,0,0}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(2, p2.getWidth());	
		assertEquals(3, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {0,1}, p2.getSkirt()));
		Piece p3 = p2.computeNextRotation();
		assertEquals(3, p3.getWidth());	
		assertEquals(2, p3.getHeight());
		assertTrue(Arrays.equals(new int[] {1,0,0}, p3.getSkirt()));
	}
	
	@Test
	public void testSQUARE_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.SQUARE_STR);
		assertEquals(2, p1.getWidth());	
		assertEquals(2, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(2, p2.getWidth());	
		assertEquals(2, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0}, p2.getSkirt()));
	}
	
	@Test
	public void testPYRAMID_STR_Width_Height_Skirt(){
		Piece p1 = new Piece(Piece.PYRAMID_STR);
		assertEquals(3, p1.getWidth());	
		assertEquals(2, p1.getHeight());
		assertTrue(Arrays.equals(new int[] {0,0,0}, p1.getSkirt()));
		Piece p2 = p1.computeNextRotation();
		assertEquals(2, p2.getWidth());	
		assertEquals(3, p2.getHeight());
		assertTrue(Arrays.equals(new int[] {1,0}, p2.getSkirt()));
		Piece p3 = p2.computeNextRotation();
		assertEquals(3, p3.getWidth());	
		assertEquals(2, p3.getHeight());
		assertTrue(Arrays.equals(new int[] {1,0,1}, p3.getSkirt()));
	}
}
