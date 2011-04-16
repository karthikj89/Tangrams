package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;
import java.util.Iterator;

public class Puzzle {
	int level;
	long startedTime; //changed to a long
	long endedTime; //changed to a long
	ArrayList<Position> solution;
	ArrayList<Piece> pieces;
	

	/**
	 * 
	 * @param level
	 * @param solution
	 * @param pieces
	 */

	public Puzzle(int level, ArrayList<Position> solution, ArrayList<Piece> pieces){

		this.level = level;
		//TODO: set startedTime and endedTime to currentTime
		startedTime = System.nanoTime();
		this.solution = solution;
		this.pieces = pieces;
	}
	
	private ArrayList<ArrayList<Position>> getsolutionList(){
		ArrayList<ArrayList<Position>> solutionList = new ArrayList<ArrayList<Position>>();
		
		ArrayList<Position>solution1 = new ArrayList<Position>();
		solution1.add(new Position(0,0));
		solution1.add(new Position(50,-50));
		solution1.add(new Position(100,0));
		solution1.add(new Position(100,80));
		solution1.add(new Position(0,80));
		
		ArrayList<Position>solution2 = new ArrayList<Position>();
		solution2.add(new Position(0,0));
		solution2.add(new Position(50,-50));
		solution2.add(new Position(100,0));
		solution2.add(new Position(100,80));
		
		ArrayList<Position>solution3 = new ArrayList<Position>();
		solution3.add(new Position(0,0));
		solution3.add(new Position(50,-50));
		solution3.add(new Position(100,0));
		
		solutionList.add(solution1);
		solutionList.add(solution2);
		solutionList.add(solution3);
		
		return solutionList;
	}
	

	/**
	 * 
	 * @param level
	 */

	public Puzzle(int level){
		this(level,null,null);		
		//TODO: load solution and pieces from database according to level
		//use a dummy triangle solution for now
		
		int solutionIndex = level-1;
		ArrayList<ArrayList<Position>> solutionList = getsolutionList();
		solution = solutionList.get(solutionIndex);
		
		//pieces required for puzzle
		pieces = new ArrayList<Piece>();
		pieces.add(new Piece(1, new Position(0,0)));
		pieces.add(new Piece(2, new Position(0,0)));
		pieces.add(new Piece(3, new Position(0,0)));
		pieces.add(new Piece(4, new Position(0,0)));
		pieces.add(new Piece(5, new Position(0,0)));
	}
	
	private Piece addPiece(int type, Position pos){
		Piece piece = new Piece(type, pos);
		pieces.add(piece);
		return piece;
	}
	
	public void movePiece(Piece piece, Position pos){
		pieces.remove(piece);
		piece.moveTo(pos);
		pieces.add(piece);
	}
	
	public int calculateScore(){
		int score = 0;
		BoundingBox sbb = new BoundingBox(solution); //bounding box of solution
		BoundingBox pbb = new BoundingBox(); //bounding box of pieces
		Iterator <Piece> pitr = pieces.iterator();
		while(pitr.hasNext()){
			Piece piece = pitr.next();
			pbb.expand(piece);
		}
		
		//align solution BB to pieces BB according to min
		Position displaceSBB = new Position(pbb.getMin().x - sbb.getMin().x, pbb.getMin().y - sbb.getMin().y);
		Position alignedSBBMax = new Position(sbb.getMax().x+displaceSBB.x,
				sbb.getMax().y+displaceSBB.y);
		
		//check if bounding boxes match
		if(pbb.getMax().equals(alignedSBBMax))
			score+= 1;
		
		//move solution vertices according to alignment
		Iterator<Position> slnItr = solution.iterator();
		while(slnItr.hasNext()){
			slnItr.next().add(displaceSBB);
		}
		
		//calculate if pieces and solution same area (eliminate possibility of holes)
		int slnArea = Position.area2x(solution);
		int piecesArea = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			piecesArea += pitr.next().area2x();
		}
		if(slnArea == piecesArea)
			score+=10;
		
		//calculate if pieces inside solution
		boolean inside = true;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			if(!pitr.next().inside(solution)){
				inside = false;
				break;
			}
		}
		if(inside)
			score+=100;
		
		//TODO: calculate if pieces overlap
		boolean overlap = false;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			Iterator<Piece> pitr2 = pieces.iterator();
			while(pitr2.hasNext()){
				if(pitr.next().overlap(pitr2.next())){
					overlap = true;
					break;
				}
			}
		}
		if(!overlap)
			score+=1000;
		
		return score;
	}
	
	public void reset(){
		//TODO: set startedTime to currentTime
		//TODO: set solution and pieces back to initial state (pull from database);
	}
	
	//new classes: getters and setters
	public ArrayList<Position> getSolution(){
		return solution;
	}
	public ArrayList<Piece> getPieces(){
		return pieces;
	}
}
