package edu.berkeley.cs169.tangrams.test;
import java.util.ArrayList;

import edu.berkeley.cs169.tangrams.Piece;
import edu.berkeley.cs169.tangrams.Position;

import junit.framework.TestCase;


public class PieceTest extends TestCase {
	private Piece piece;

	protected void setUp() throws Exception {
		super.setUp();
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		piece = new Piece(0, new Position(0,0), 0, vertices);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		piece = null;
	}
	public void testMovePiece1(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece = new Piece(0, new Position(1,1), 0, vertices);
		
		temp_piece.moveTo(50, 100);
		assert(temp_piece.getPos().getX() == 50);
		assert(temp_piece.getPos().getY() == 100);
	}
	public void testMovePiece2(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece= new Piece(0, new Position(1,1), 0, vertices);
		
		temp_piece.moveTo(new Position(100,50));
		assert(temp_piece.getPos().getX() == 100);
		assert(temp_piece.getPos().getY() == 50);
	}
	public void testRotation1(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece = new Piece(0, new Position(1,1), 0, vertices);
		
		if (temp_piece.getOrientation() < 3){
			int temp = temp_piece.getOrientation();
			temp_piece.rotate();
			assert(temp == temp_piece.getOrientation()-1);
		}
	}
	public void testRotation2(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece = new Piece(0, new Position(1,1), 1, vertices);
		
		if (temp_piece.getOrientation() < 3){
			int temp = temp_piece.getOrientation();
			temp_piece.rotate();
			assert(temp == temp_piece.getOrientation()-1);
		}
	}
	public void testRotation3(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece = new Piece(0, new Position(1,1), 2, vertices);
		
		if (temp_piece.getOrientation() < 3){
			int temp = temp_piece.getOrientation();
			temp_piece.rotate();
			assert(temp == temp_piece.getOrientation()-1);
		}
	}
	public void testRotation4(){
		ArrayList<Position> vertices = new ArrayList<Position>();
		vertices.add(new Position(-2,-1));
		vertices.add(new Position(-2,1));
		vertices.add(new Position(2,-1));
		Piece temp_piece = new Piece(0, new Position(1,1), 3, vertices);
		
		if (temp_piece.getOrientation() == 3){
			temp_piece.rotate();
			assert(temp_piece.getOrientation() == 0);
		}
	}

	

}
