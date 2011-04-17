package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class Puzzle {
	int level;
	long startedTime; //changed to a long
	long endedTime; //changed to a long
	ArrayList<Position> solution, xsolution; //xsolution transformed solution
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
		initialize();
		this.pieces = pieces;
	}
	
	private ArrayList<ArrayList<Position>> getsolutionList(){
		ArrayList<ArrayList<Position>> solutionList = new ArrayList<ArrayList<Position>>();
		
		//simple square and triangle shape
		ArrayList<Position>solution1 = new ArrayList<Position>();
		solution1.add(new Position(0,0));
		solution1.add(new Position(0,40));
		solution1.add(new Position(40,40));
		solution1.add(new Position(80,0));
		
		//shape using all 3 triangles
		ArrayList<Position>solution2 = new ArrayList<Position>();
		solution2.add(new Position(40,40));
		solution2.add(new Position(80,0));
		solution2.add(new Position(80,-50));
		solution2.add(new Position(90,-60));
		solution2.add(new Position(30,-60));
		solution2.add(new Position(30,0));
		solution2.add(new Position(40,0));
		
		//special shape where my inside algorithm seems to fail. Don't delete this one!
		/*
		solution2.add(new Position(0,0));
		solution2.add(new Position(50,-50));
		solution2.add(new Position(100,0));
		solution2.add(new Position(100,80));
		*/
		
		//triangle + parallelogram shape
		ArrayList<Position>solution3 = new ArrayList<Position>();
		solution3.add(new Position(0,0));
		solution3.add(new Position(10,40));
		solution3.add(new Position(60,90));
		solution3.add(new Position(60,40));
		solution3.add(new Position(50,0));
		
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
		
		ArrayList<ArrayList<Position>> solutionList = getsolutionList();
		int solutionIndex = (level-1)%solutionList.size();
		solution = solutionList.get(solutionIndex);
		initialize();
		
		//pieces required for puzzle
		pieces = new ArrayList<Piece>();
		pieces.add(new Piece(1, new Position(0,0)));
		pieces.add(new Piece(2, new Position(0,0)));
		pieces.add(new Piece(3, new Position(0,0)));
		pieces.add(new Piece(4, new Position(0,0)));
		pieces.add(new Piece(5, new Position(0,0)));
	}
	
	private void initialize(){
		if(solution != null) {
			this.xsolution  = new ArrayList<Position>();
			//copy solution to xsolution
			for(int i = 0; i < solution.size(); i++)
				xsolution.add(new Position(solution.get(i)));
		}
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
	
	public void moveXSolutionTo(int x, int y){
		for(int i = 0; i < xsolution.size(); i++){
			xsolution.get(i).set(solution.get(i).getX()+x,solution.get(i).getY()+y);
		}
	}
	
	public int calculateScore(ArrayList<Piece> pieces){
		endedTime = System.nanoTime();
		if(pieces == null || pieces.size()<=0) //can't solve puzzle with no pieces
			return 0;
		Iterator <Piece> pitr = pieces.iterator();
		
		//calculate if pieces and solution same area
		//comment out area checking for now.
		/*
		int slnArea = Position.area2x(xsolution);
		int piecesArea = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			piecesArea += pitr.next().area2x();
		}
		boolean sameArea = false;
		if(slnArea == piecesArea)
			sameArea = true;
		
		//** return score=0 if pieces not the same area as xsolution
		if(!sameArea)
			return 0;
		*/
		
		BoundingBox sbb = new BoundingBox(xsolution); //bounding box of xsolution
		BoundingBox pbb = new BoundingBox(); //bounding box of pieces
		while(pitr.hasNext()){
			Piece piece = pitr.next();
			Position.logVertices(piece.getXVertices());
			pbb.expand(piece);
		}
		
		//align xsolution BB to pieces BB according to min
		Position displaceSBB = new Position(pbb.getMin().x - sbb.getMin().x, pbb.getMin().y - sbb.getMin().y);
		Position alignedSBBMax = new Position(sbb.getMax().x+displaceSBB.x,
				sbb.getMax().y+displaceSBB.y);
		
		//check if bounding boxes match
		boolean bbsMatch = false;
		if(pbb.getMax().equals(alignedSBBMax))
			bbsMatch = true;
		
		/* comment out bounding box checking for now too
		//** return score=1 if at least area is the same, but bounding box doesn't match
		if(!bbsMatch)
			return 1;
		*/
		
		//if no outline, move xsolution vertices according to alignment
		if(!GlobalVariables.outlineOn) {
			Iterator<Position> slnItr = xsolution.iterator();
			while(slnItr.hasNext()){
				slnItr.next().add(displaceSBB);
			}
		}
		Position.logVertices(xsolution);
		
		//calculate if pieces inside xsolution
		boolean inside = true;
		int numPiecesInside = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			if(pitr.next().inside(xsolution)){
				numPiecesInside++;
			} else {
				inside = false;
			}
		}
		
		//** return score=0 to 8 depending on percentage of pieces inside xsolution
		Log.d("Puzzle.calculateScore",""+numPiecesInside+"/"+pieces.size());
		if(!inside)
			return (8*numPiecesInside)/pieces.size();
		
		//calculate if pieces overlap
		//not needed here if done on the fly when placing pieces
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
		
		//** return score=10 if satisfies all criterion
		//else return score=9 if overlap detected
		if(!overlap)
			return 10;
		else
			return 9;
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
	//get time elapsed in seconds since Puzzle created
	public double getElapsedTime(){
		return (double)(System.nanoTime()-startedTime)/1000000000.0;
	}
}
