package edu.berkeley.cs169.tangrams.test;
import java.util.ArrayList;

import edu.berkeley.cs169.tangrams.Piece;
import edu.berkeley.cs169.tangrams.Position;
import edu.berkeley.cs169.tangrams.Puzzle;
import junit.framework.TestCase;


public class PuzzleTest extends TestCase {
	private Puzzle puzzle;

	protected void setUp() throws Exception {
		super.setUp();
		puzzle = new Puzzle(0,new ArrayList<Position>(),new ArrayList<Piece>());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		puzzle = null;
	}
	
	public void testCorrectScore(){
		ArrayList<Position> solution = puzzle.getSolution();
		//use a single triangle solution
		solution.add(new Position(0,0));
		solution.add(new Position(0,2));
		solution.add(new Position(4,0));
		puzzle.initialize();
		
		//use a single triangle piece
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		assertEquals(1,puzzle.calculateScore(pieces));
	}

	public void testMovePiece(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		
		puzzle.movePiece(piece, new Position(5,5));
		assert(piece.getPos() == new Position(5,5));
		assert(puzzle.getPieces().get(0).getPos() == new Position(5,5));
		assert(pieces.size() == 1);
	}
	public void testNonExistingPiece(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		Piece no_exist = new Piece(1, new Position(2,1), 0, vertices);
		
		puzzle.movePiece(no_exist, new Position(1,1));
		assert(no_exist.getPos() == new Position(2,1));
		assert(puzzle.getPieces().get(0).getPos() == new Position(2,1));
		assert(pieces.size() == 1);
	}
	public void testOneOutOfTwo(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		Piece piece2 = new Piece(1, new Position(3,3), 0, vertices);
		pieces.add(piece2);
		
		puzzle.movePiece(piece, new Position(5,5));
		assert(piece.getPos() == new Position(5,5));
		assert(piece2.getPos() == new Position(3,3));
		assert(pieces.size() == 2);
	}
	public void testTwOOutOfTwo(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece piece = new Piece(0, new Position(2,1), 0, vertices);
		ArrayList<Piece> pieces = puzzle.getPieces();
		pieces.add(piece);
		Piece piece2 = new Piece(1, new Position(3,3), 0, vertices);
		pieces.add(piece2);
		
		puzzle.movePiece(piece2, new Position(5,5));
		assert(piece2.getPos() == new Position(5,5));
		assert(piece.getPos() == new Position(3,3));
		assert(pieces.size() == 2);
	}
	public void testXSolution(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		ArrayList<Piece> pieces = puzzle.getPieces();
		
		puzzle = new Puzzle(0,vertices, pieces);
		puzzle.moveXSolutionTo(50, 10);
		for (int i = 0; i < puzzle.getXSolution().size(); i ++){
			assert(puzzle.getXSolution().get(i).getX() == puzzle.getSolution().get(i).getX() +50);
			assert(puzzle.getXSolution().get(i).getY() == puzzle.getSolution().get(i).getY() +10);

		}
	}
}
