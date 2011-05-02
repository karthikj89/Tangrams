package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class Piece {
	private int type;
	private int orientation;
	private Position center;
	private ArrayList <Position> vertices, xvertices;
	private BoundingBox bb, xbb;
	private boolean _active;
	boolean dirty; //dirty flag to show if xvertices/xbb (transformed vertices/bb) changed

	/**
	 * 
	 * @param type
	 * @param pos
	 * @param orientation
	 * @param vertices
	 */
	public Piece (int type, Position pos, int orientation, ArrayList <Position> vertices) {
		this.type = type;
		this.center = new Position(pos);	
		this.orientation = orientation;
		this.vertices = vertices;
		initialize();
	}
	/**
	 * 
	 * @param type
	 * @param pos
	 */
	

	public Piece (int type, Position pos) {

		this.type = type;
		this.center = new Position(pos);

		//TODO: get orientation and vertices from database according to type
		//use a dummy 2x4 triangle defined with center at 0,0 for now
		orientation = 0;
		//NOTE: When you create pieces, please create ones centered at 0,0
		//if you want proper rotation and toolbox positioning behavior!
		//another thing to be careful about is to use only units of 10 if you want
		//snap to grid to work properly
		if(type==square){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(-20,20));
			vertices.add(new Position(20,20));
			vertices.add(new Position(20,-20));
			vertices.add(new Position(-20,-20));
		}else if(type==smallTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(20,20));
			vertices.add(new Position(20,-20));
			vertices.add(new Position(-20,-20));
		}else if(type==mediumTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(20,30));
			vertices.add(new Position(20,-20));
			vertices.add(new Position(-30,-20));
		}else if(type==largeTriangle){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(30,30));
			vertices.add(new Position(30,-30));
			vertices.add(new Position(-30,-30));
		}else if(type==parallelogram){
			vertices = new ArrayList<Position>();
			vertices.add(new Position(-20,20));
			vertices.add(new Position(30,20));
			vertices.add(new Position(20,-20));
			vertices.add(new Position(-30,-20));
		}
		initialize();
	}

	public int getWidth(){
		return bb.getMax().getX()-bb.getMin().getX();
	}
	
	public int getHeight(){
		return bb.getMax().getY()-bb.getMin().getY();
	}

	private void initialize(){
		_active = false;
		xvertices = new ArrayList<Position>();
		//copy vertices to xvertices
		for(int i = 0; i < vertices.size(); i++)
			xvertices.add(new Position(vertices.get(i)));

		bb = new BoundingBox(vertices); //create bounding box
		xbb = new BoundingBox(bb); //create transformed bounding box
		if(orientation!=0 || !(center.x==0 && center.y==0))
			update();
	}

	/**
	 * 
	 * @param newpos
	 */
	public void moveTo(Position newpos){
		dirty = true;
		center = newpos;	
	}

	public void moveTo(int x, int y){
		dirty = true;
		center.set(x,y);
	}

	public void rotate(){
		dirty = true;
		if(orientation < 3)
			orientation++;
		else
			orientation = 0;
	}
	
	//scale to help adjust for screen size
	public void scale(int scale){
		for(int i = 0; i < vertices.size(); i++){
			vertices.get(i).scale(scale);
		}
		for(int i = 0; i < xvertices.size(); i++){
			xvertices.get(i).scale(scale);
		}
		center.scale(scale);
		bb.scale(scale);
		xbb.scale(scale);
	}

	/**new class
	 * Checks if vertices inside the polygon described by vertices 
	 * 
	 * @param vertices2
	 * @return
	 */
	public boolean inside(ArrayList<Position> vertices2){
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(!xvitr.next().inside(vertices2, true))
				return false;
		}
		return true;
	}

	/**new class
	 * Checks for overlapping pieces
	 * now checks first for trivial case of piece completely contained in another
	 * then checks for edge intersection, 
	 * so basically perfect check of overlap
	 * idea from here: http://gpwiki.org/index.php/Polygon_Collision
	 */
	public boolean overlap(Piece piece2){
		//first check if BoundingBox overlaps
		//if not, return false
		
		BoundingBox xbb = getXBB();
		BoundingBox xbb2 = piece2.getXBB();
		if(!xbb.overlap(xbb2))
			return false;
		
		ArrayList<Position> vertices1 = getXVertices();
		ArrayList<Position> vertices2 = piece2.getXVertices();
		
		//check if xvertices all inside piece2
		Iterator<Position> xvitr = vertices1.iterator();
		boolean inside = true;
		while(xvitr.hasNext()){
			Position vertex1 = xvitr.next();
			if(!vertex1.inside(vertices2, true)) {
				//Log.d("overlap/inside",""+vertex1.getX()+","+vertex1.getY()+" inside:");
				//Position.logVertices(vertices2);
				inside = false;
			}
		}
		if(inside) //if inside means piece2 contains this piece so overlap is true
			return true;
		
		//check edge intersections
		int size1 = vertices1.size();
		if(size1 <= 2)
			return false; //if 2 or less vertices not a polygon so return false
		Position p1, p2, p3, p4;
		p1 = vertices1.get(0);
		for(int i = 1; i <size1; i++){
			p2 = vertices1.get(i);
			
			p3 = vertices2.get(0);
			for(int j = 1; j <vertices2.size(); j++){
				p4 = vertices2.get(j);
				if(Position.edgeIntersection(p2, p1, p4, p3))
					return true;
				p3 = p4;
			}
			p4 = vertices2.get(0); //handle ending case
			if(Position.edgeIntersection(p2, p1, p4, p3))
				return true;
			
			p1 = p2;
		}
		p2 = vertices1.get(0); //handle ending case
		p3 = vertices2.get(0);
		for(int j = 1; j <vertices2.size(); j++){
			p4 = vertices2.get(j);
			if(Position.edgeIntersection(p2, p1, p4, p3))
				return true;
			p3 = p4;
		}
		p4 = vertices2.get(0); //handle ending case
		if(Position.edgeIntersection(p2, p1, p4, p3))
			return true;
		
		return false;
	}

	/**new class
	 * returns 2 times the area of this piece 
	 */
	public int area2x(){
		return Position.area2x(vertices);
	}

	/**updates (transforms) xvertices to correct orientation and positions
	 */
	public void update(){
		if(dirty){
			//update xvertices
			for(int i = 0; i < xvertices.size(); i++){
				Position xVertex = xvertices.get(i);
				xVertex.set(vertices.get(i));
				xVertex.rotate(orientation);
				xVertex.add(center);
			}
			//update xbb
			xbb.update(xvertices);
			//update flag
			dirty = false;
		}
	}

	//new classes: getters and setters
	public int getOrientation(){
		return orientation;
	}
	public Position getPos(){
		return center;
	}
	public ArrayList<Position> getVertices(){
		return vertices;
	}
	public void setXVertices(ArrayList<Position> vert){
		xvertices = vert;
	}
	/*Returns updated transformed vertices for Piece (correct orientation and position)*/
	public ArrayList<Position> getXVertices(){
		update();
		return xvertices;
	}
	public BoundingBox getBB(){
		return bb;
	}
	/**Returns updated transformed bb for Piece (correct orientation and position)*/
	public BoundingBox getXBB(){
		update();
		return xbb;
	}

	public boolean isActive(){
		return _active;
	}

	public void setActive(boolean b){
		_active = b;
	}

	public int getType(){
		return type;
	}


	public static final int
	square = 1,
	smallTriangle = 2,
	mediumTriangle = 3,
	largeTriangle = 4,
	parallelogram = 5;
	public static final int
	toolbox = 6;
	public static final int board = 7, pickedUp = 8;
}