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
	BoundingBox bb;
	

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
		
		//house with two squares and two small trianges
		ArrayList<Position>solution1 = new ArrayList<Position>();
		solution1.add(new Position(-40,0));
		solution1.add(new Position(-40,-40));
		solution1.add(new Position(0,-80));
		solution1.add(new Position(40,-40));
		solution1.add(new Position(40,0));
		
		//christmas tree: 2 of each triangle and 1 square
		ArrayList<Position>solution1a = new ArrayList<Position>();
		solution1a.add(new Position(0,-90));
		solution1a.add(new Position(40,-50));
		solution1a.add(new Position(0,-50));
		solution1a.add(new Position(50,0));
		solution1a.add(new Position(0,0));
		solution1a.add(new Position(60,60));
		solution1a.add(new Position(20,60));
		solution1a.add(new Position(20,100));
		solution1a.add(new Position(20,100));
		solution1a.add(new Position(-20,100));
		solution1a.add(new Position(-20,60));
		solution1a.add(new Position(-60,60));
		solution1a.add(new Position(0,0));
		solution1a.add(new Position(-50,0));
		solution1a.add(new Position(0,-50));
		solution1a.add(new Position(-40,-50));
		
		//bird with four small triangles with 2 squares
		ArrayList<Position>solution2 = new ArrayList<Position>();
		solution2.add(new Position(-40,-80));
		solution2.add(new Position(-80,-40));
		solution2.add(new Position(-40,0));
		solution2.add(new Position(40,0));
		solution2.add(new Position(40,-40));
		solution2.add(new Position(80,-40));
		solution2.add(new Position(40,-80));
		solution2.add(new Position(0,-40));
		solution2.add(new Position(-40,-40));
		
		//fish: two squares and 6 small triangles
		ArrayList<Position>solution2a = new ArrayList<Position>();
		solution2a.add(new Position(0,-40));
		solution2a.add(new Position(40,-40));
		solution2a.add(new Position(80,0));
		solution2a.add(new Position(40,40));
		solution2a.add(new Position(0,40));
		solution2a.add(new Position(-40,0));
		solution2a.add(new Position(-80,40));
		solution2a.add(new Position(-80,-40));
		solution2a.add(new Position(-40,0));
		
		//animal: two squares, three small triangles and 1 large triangles
		ArrayList<Position>solution2b = new ArrayList<Position>();
		solution2b.add(new Position(0,-40));
		solution2b.add(new Position(0,-60));
		solution2b.add(new Position(60,0));
		solution2b.add(new Position(0,0));
		solution2b.add(new Position(0,40));
		solution2b.add(new Position(-40,0));
		solution2b.add(new Position(-80,0));
		solution2b.add(new Position(-80,40));
		solution2b.add(new Position(-120,0));
		solution2b.add(new Position(-80,-40));

		
		//diamond: three squares and 6 small triangles
		ArrayList<Position>solution3 = new ArrayList<Position>();
		solution3.add(new Position(0,0));
		solution3.add(new Position(-40,-40));
		solution3.add(new Position(0,-80));
		solution3.add(new Position(40,-80));
		solution3.add(new Position(80,-40));
		solution3.add(new Position(40,0));
		solution3.add(new Position(80,40));
		solution3.add(new Position(-40,40));
		
		//penguin: 7 squares and 5 small triangles
		ArrayList<Position>solution3a = new ArrayList<Position>();
		solution3a.add(new Position(40,-80));
		solution3a.add(new Position(-40,0));
		solution3a.add(new Position(-40,40));
		solution3a.add(new Position(0,80));
		solution3a.add(new Position(40,80));
		solution3a.add(new Position(80,40));
		solution3a.add(new Position(80,-40));
		solution3a.add(new Position(120,-80));
		
		//heart: 8 squares and 10 small triangles
		ArrayList<Position>solution4 = new ArrayList<Position>();
		solution4.add(new Position(0,-40));
		solution4.add(new Position(40,-80));
		solution4.add(new Position(80,-80));
		solution4.add(new Position(120,-40));
		solution4.add(new Position(0,80));
		solution4.add(new Position(-120,-40));
		solution4.add(new Position(-80,-80));
		solution4.add(new Position(-40,-80));
		
		//left broken heart: 6 small triangles and two squares
		ArrayList<Position>solution4a = new ArrayList<Position>();
		solution4a.add(new Position(40,0));
		solution4a.add(new Position(0,40));
		solution4a.add(new Position(-80,-40));
		solution4a.add(new Position(-40,-80));
		solution4a.add(new Position(0,-80));
		solution4a.add(new Position(40,-40));
		solution4a.add(new Position(0,-40));
		
		//bunny: 8 squares and 4 small triangles
		ArrayList<Position>solution4b = new ArrayList<Position>();
		solution4b.add(new Position(0,-80));
		solution4b.add(new Position(-40,-40));
		solution4b.add(new Position(-40,80));
		solution4b.add(new Position(0,120));
		solution4b.add(new Position(40,120));
		solution4b.add(new Position(80,80));
		solution4b.add(new Position(80,-40));
		solution4b.add(new Position(40,-80));
		solution4b.add(new Position(40,40));
		solution4b.add(new Position(0,40));
		
		//bow: 5 squares and 8 small triangles
		ArrayList<Position>solution4c = new ArrayList<Position>();
		solution4c.add(new Position(0,0));
		solution4c.add(new Position(-40,0));
		solution4c.add(new Position(-80,-40));
		solution4c.add(new Position(-120,0));
		solution4c.add(new Position(-120,40));
		solution4c.add(new Position(-80,80));
		solution4c.add(new Position(-40,40));
		solution4c.add(new Position(0,40));
		solution4c.add(new Position(40,80));
		solution4c.add(new Position(80,40));
		solution4c.add(new Position(80,0));
		solution4c.add(new Position(40,-40));
		
		//elephant: 16 squares and 4 small triangles 
		ArrayList<Position>solution5 = new ArrayList<Position>();
		solution5.add(new Position(-120,-40));
		solution5.add(new Position(-120,-120));
		solution5.add(new Position(-40,-200));
		solution5.add(new Position(80,-200));
		solution5.add(new Position(120,-160));
		solution5.add(new Position(120,-120));
		solution5.add(new Position(80,-80));
		solution5.add(new Position(80,0));
		solution5.add(new Position(40,0));
		solution5.add(new Position(40,-80));
		solution5.add(new Position(0,-80));
		solution5.add(new Position(0,0));
		solution5.add(new Position(-40,0));
		solution5.add(new Position(-40,-120));
		solution5.add(new Position(-80,-120));
		solution5.add(new Position(-80,-40));
		
//		ArrayList<Position>solution6 = new ArrayList<Position>();
//		solution6.add(new Position(0,0));
//		solution6.add(new Position(50,0));
//		solution6.add(new Position(90,-10));
//		solution6.add(new Position(150,-10));
//		solution6.add(new Position(90,50));
//		solution6.add(new Position(90,80));
//		solution6.add(new Position(50,80));
//		solution6.add(new Position(90,40));
//		solution6.add(new Position(50,50));
//		
//		ArrayList<Position>solution7 = new ArrayList<Position>();
//		solution7.add(new Position(0,0));
//		solution7.add(new Position(60,60));
//		solution7.add(new Position(50,60));
//		solution7.add(new Position(60,100));
//		solution7.add(new Position(60,140));
//		solution7.add(new Position(90,140));
//		solution7.add(new Position(40,190));
//		solution7.add(new Position(40,180));
//		solution7.add(new Position(0,140));
//		solution7.add(new Position(20,140));
//		solution7.add(new Position(20,100));
//		solution7.add(new Position(10,100));
//		solution7.add(new Position(0,60));
//		
//		ArrayList<Position>solution8 = new ArrayList<Position>();
//		solution8.add(new Position(0,0));
//		solution8.add(new Position(40,0));
//		solution8.add(new Position(80,-10));
//		solution8.add(new Position(120,-10));
//		solution8.add(new Position(120,-60));
//		solution8.add(new Position(170,-60));
//		solution8.add(new Position(140,-30));
//		solution8.add(new Position(140,30));
//		solution8.add(new Position(80,30));
//		solution8.add(new Position(80,40));
//		solution8.add(new Position(40,50));
//		solution8.add(new Position(40,40));
//		solution8.add(new Position(0,40));		
		
		solutionList.add(solution1);
		solutionList.add(solution1a);
		solutionList.add(solution2);
		solutionList.add(solution2a);
		solutionList.add(solution2b);
		solutionList.add(solution3);
		solutionList.add(solution3a);
		solutionList.add(solution4);
		solutionList.add(solution4a);
		solutionList.add(solution4b);
		solutionList.add(solution4c);
		solutionList.add(solution5);
//		solutionList.add(solution6);
//		solutionList.add(solution7);
//		solutionList.add(solution8);
		
		return solutionList;
	}
	
	private ArrayList<ArrayList<Piece>> getpiecesList(){
		ArrayList<ArrayList<Piece>> piecesList = new ArrayList<ArrayList<Piece>>();
		
		//house with two squares and two small trianges
		ArrayList<Piece>pieces1 = new ArrayList<Piece>();
		pieces1.add(new Piece(Piece.square,new Position(0,0)));
		pieces1.add(new Piece(Piece.square,new Position(0,0)));
		pieces1.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces1.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//christmas tree: 2 of each triangle and 1 square
		ArrayList<Piece>pieces1a = new ArrayList<Piece>();
		pieces1a.add(new Piece(Piece.square,new Position(0,0)));
		pieces1a.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces1a.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces1a.add(new Piece(Piece.mediumTriangle,new Position(0,0)));
		pieces1a.add(new Piece(Piece.mediumTriangle,new Position(0,0)));
		pieces1a.add(new Piece(Piece.largeTriangle,new Position(0,0)));
		pieces1a.add(new Piece(Piece.largeTriangle,new Position(0,0)));
		
		//bird with four small triangles with 2 squares
		ArrayList<Piece>pieces2 = new ArrayList<Piece>();
		pieces2.add(new Piece(Piece.square,new Position(0,0)));
		pieces2.add(new Piece(Piece.square,new Position(0,0)));
		pieces2.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces2.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces2.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces2.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//fish: two squares and 6 small triangles
		ArrayList<Piece>pieces3 = new ArrayList<Piece>();
		pieces3.add(new Piece(Piece.square,new Position(0,0)));
		pieces3.add(new Piece(Piece.square,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces3.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//animal: two squares, three small triangles and 1 large triangles
		ArrayList<Piece>pieces4 = new ArrayList<Piece>();
		pieces4.add(new Piece(Piece.square,new Position(0,0)));
		pieces4.add(new Piece(Piece.square,new Position(0,0)));
		pieces4.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces4.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces4.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces4.add(new Piece(Piece.largeTriangle,new Position(0,0)));

		//diamond: three squares and 6 small triangles
		ArrayList<Piece>pieces5 = new ArrayList<Piece>();
		pieces5.add(new Piece(Piece.square,new Position(0,0)));
		pieces5.add(new Piece(Piece.square,new Position(0,0)));
		pieces5.add(new Piece(Piece.square,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces5.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//penguin: 7 squares and 5 small triangles
		ArrayList<Piece>pieces6 = new ArrayList<Piece>();
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.square,new Position(0,0)));
		pieces6.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces6.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces6.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces6.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces6.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//heart: 8 squares and 10 small triangles
		ArrayList<Piece>pieces7 = new ArrayList<Piece>();
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.square,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces7.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//left broken heart: 6 small triangles and two squares
		ArrayList<Piece>pieces8 = new ArrayList<Piece>();
		pieces8.add(new Piece(Piece.square,new Position(0,0)));
		pieces8.add(new Piece(Piece.square,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces8.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//bunny: 8 squares and 4 small triangles
		ArrayList<Piece>pieces9 = new ArrayList<Piece>();
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.square,new Position(0,0)));
		pieces9.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces9.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces9.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces9.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//bow: 5 squares and 8 small triangles
		ArrayList<Piece>pieces10 = new ArrayList<Piece>();
		pieces10.add(new Piece(Piece.square,new Position(0,0)));
		pieces10.add(new Piece(Piece.square,new Position(0,0)));
		pieces10.add(new Piece(Piece.square,new Position(0,0)));
		pieces10.add(new Piece(Piece.square,new Position(0,0)));
		pieces10.add(new Piece(Piece.square,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces10.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		//elephant: 16 squares and 4 small triangles 	
		ArrayList<Piece>pieces11 = new ArrayList<Piece>();
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.square,new Position(0,0)));
		pieces11.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces11.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces11.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		pieces11.add(new Piece(Piece.smallTriangle,new Position(0,0)));
		
		piecesList.add(pieces1);
		piecesList.add(pieces1a);
		piecesList.add(pieces2);
		piecesList.add(pieces3);
		piecesList.add(pieces4);
		piecesList.add(pieces5);
		piecesList.add(pieces6);
		piecesList.add(pieces7);
		piecesList.add(pieces8);
		piecesList.add(pieces9);
		piecesList.add(pieces10);
		piecesList.add(pieces11);
		
		return piecesList;
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
		ArrayList<ArrayList<Piece>> piecesList = getpiecesList();
		int solutionIndex = (level-1)%solutionList.size();
		int pieceIndex = solutionIndex;
		this.level = solutionIndex+1;
		solution = solutionList.get(solutionIndex);
		initialize();
		
		//pieces required for puzzle
		pieces = piecesList.get(pieceIndex);
	}
	
	public void initialize(){
		if(solution != null) {
			this.xsolution  = new ArrayList<Position>();
			//copy solution to xsolution
			for(int i = 0; i < solution.size(); i++)
				xsolution.add(new Position(solution.get(i)));
			bb = new BoundingBox(solution);
		}
	}
	
	public Piece addPiece(int type, Position pos){
		Piece piece = new Piece(type, pos);
		pieces.add(piece);
		return piece;
	}
	
	public void movePiece(Piece piece, Position pos){
		if (pieces.contains(piece)){
			pieces.remove(piece);
			piece.moveTo(pos);
			pieces.add(piece);
		}
	}
	
	public void moveXSolutionTo(int x, int y){
		for(int i = 0; i < xsolution.size(); i++){
			xsolution.get(i).set(solution.get(i).getX()+x,solution.get(i).getY()+y);
		}
	}
	
	public void scaleSolution(int scale){
		for(int i = 0; i < solution.size(); i++){
			solution.get(i).scale(scale);
		}
		for(int i = 0; i < xsolution.size(); i++){
			xsolution.get(i).scale(scale);
		}
		bb.scale(scale);
		for(int i = 0; i < pieces.size(); i++){
			pieces.get(i).scale(scale);
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
	public ArrayList<Position> getXSolution(){
		return xsolution;
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
	public int getCenterX(){
		return bb.getMin().getX()+(bb.getMax().getX()-bb.getMin().getX())/2;
	}
	public int getCenterY(){
		return bb.getMin().getY()+(bb.getMax().getY()-bb.getMin().getY())/2;
	}
}
