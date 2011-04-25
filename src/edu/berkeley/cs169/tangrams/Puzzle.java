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
		
		//2 triangle + parallelogram shape
		ArrayList<Position>solution3 = new ArrayList<Position>();
		solution3.add(new Position(0,0));
		solution3.add(new Position(10,40));
		solution3.add(new Position(0,30));
		solution3.add(new Position(0,90));
		solution3.add(new Position(60,90));
		solution3.add(new Position(60,40));
		solution3.add(new Position(50,0));
		
		//3 triangle + square shape
		ArrayList<Position>solution4 = new ArrayList<Position>();
		solution4.add(new Position(0,0));
		solution4.add(new Position(0,80));
		solution4.add(new Position(30,50));
		solution4.add(new Position(90,50));
		solution4.add(new Position(90,-10));
		solution4.add(new Position(40,-10));
		solution4.add(new Position(40,0));
		
		solutionList.add(solution1);
		solutionList.add(solution2);
		solutionList.add(solution3);
		solutionList.add(solution4);
		
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
		this.level = solutionIndex+1;
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
	
	public void initialize(){
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
		int score = 0;
		endedTime = System.nanoTime();
		if(pieces == null || pieces.size()<=0) //can't solve puzzle with no pieces
			return 0;
		Iterator <Piece> pitr;
		
		BoundingBox sbb = new BoundingBox(xsolution); //bounding box of xsolution
		BoundingBox pbb = new BoundingBox(); //bounding box of pieces
		pitr = pieces.iterator();
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
		
		//ADD 1 to score for matching bounding boxes around pieces and solution
		if(bbsMatch)
			score+=1;
		
		//if no outline, move xsolution vertices according to alignment
		if(!GlobalVariables.outlineOn) {
			Iterator<Position> slnItr = xsolution.iterator();
			while(slnItr.hasNext()){
				slnItr.next().add(displaceSBB);
			}
		}
		Position.logVertices(xsolution);
		
		//variables to support area calculation
		int slnArea = Position.area2x(xsolution);
		if(slnArea ==0) //can't solve puzzle with solution area of 0
			return 0;
		int pAreaIn = 0;
		int pAreaOut = 0;
		int pAreaOverlap = 0;
		
		//calculate if pieces inside xsolution
		boolean inside = true;
		int numPiecesInside = 0;
		pitr = pieces.iterator();
		while(pitr.hasNext()){
			Piece p = pitr.next();
			if(p.inside(xsolution)){
				pAreaIn += p.area2x();
				numPiecesInside++;
			} else {
				pAreaOut += p.area2x();
				inside = false;
			}
		}
		
		//calculate if pieces overlap
		//not needed here if done on the fly when placing pieces
		boolean overlap = false;
		pitr = pieces.iterator();
		while(pitr.hasNext() && !overlap){
			Piece p1 = pitr.next();
			Iterator<Piece> pitr2 = pieces.iterator();
			while(pitr2.hasNext()){
				Piece p2 = pitr2.next();
				if(p1!=p2 && p1.overlap(p2)){ //don't check overlap if same object
					overlap = true;
					int p1area2x = p1.area2x();
					int p2area2x = p2.area2x();
					if(p1area2x < p2area2x)
						pAreaOverlap += p1area2x;
					else
						pAreaOverlap += p2area2x;
					break;
				}
			}
		}
		
		int slnInDiff = slnArea - pAreaIn; //calculate difference in area for solution and inside pieces
		if(slnInDiff < 0)
			slnInDiff = -slnInDiff; //absolute value to account for inside area larger than solution (means overlap)
		slnInDiff = slnInDiff + pAreaOut + pAreaOverlap; //add area of outside and first overlapping piece into the "difference"
		slnInDiff = slnArea - slnInDiff; //calculate amount of correct inside area
		if(slnInDiff < 0)
			slnInDiff = 0; //set to 0 if negative
		
		//ADD 0-9 to score depending on percentage area of pieces inside xsolution
		//you also lose points for % area of pieces outside xsolution
		//you also lose points for % area of first overlapping piece
		Log.d("Puzzle.calculateScore (% area inside)",""+slnInDiff+"/"+slnArea);
		score += (9*slnInDiff)/slnArea;
		
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
	//get time elapsed in seconds since Puzzle created
	public double getElapsedTime(){
		return (double)(System.nanoTime()-startedTime)/1000000000.0;
	}
	public int getLevel(){
		return level;
	}
}
