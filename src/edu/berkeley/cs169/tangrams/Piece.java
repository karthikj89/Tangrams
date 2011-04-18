package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;
import java.util.Iterator;

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
	 * currently only checks if vertices are inside Piece p2 ,
	 * not a perfect check of overlap (perfect check would check edges),
	 * more useful if used while placing pieces
	 * idea from here: http://gpwiki.org/index.php/Polygon_Collision
	 */
	public boolean overlap(Piece p2){
		//first check if BoundingBox overlaps
		//if not, return false
		BoundingBox xbb = getXBB();
		BoundingBox xbb2 = p2.getXBB();
		if(!xbb.overlap(xbb2))
			return false;

		//check if xvertices inside Piece p2 (not checking by edge intersections)
		ArrayList<Position> vertices2 = p2.getXVertices();
		Iterator<Position> xvitr = getXVertices().iterator();
		while(xvitr.hasNext()){
			if(xvitr.next().inside(vertices2, false))
				return true;
		}
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