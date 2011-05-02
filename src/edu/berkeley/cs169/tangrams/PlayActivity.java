package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayActivity extends Activity {
	TextView tv;
	Puzzle puzzle;
	ArrayList<Piece> _board;
	LinearLayout layout;
	LinearLayout buttonsLayout;
	private int toolboxHeight, displayWidth, displayHeight, scale;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_board = new ArrayList<Piece>();
		
		//set variables for drawing
		Display display = getWindowManager().getDefaultDisplay(); 
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		if(displayWidth >= 480)
			scale = 2;
		else
			scale = 1;
		toolboxHeight = 80*scale;
		
		final Panel myPanel = new Panel(this);

		layout = new LinearLayout(this);
		buttonsLayout = new LinearLayout(this);

		layout.setOrientation(LinearLayout.VERTICAL);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

		layout.addView(myPanel, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1.0f));

		// Recall button
		Button recallBtn = new Button(this);
		recallBtn.setText("Recall");
		recallBtn.setClickable(true);
		recallBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Perform action on click
				myPanel.recall();
			}
		});

		// Submit button
		Button submitBtn = new Button(this);
		submitBtn.setText("Submit");
		submitBtn.setClickable(true);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int score = puzzle.calculateScore(_board);
				builder.setMessage("Your score: "+score)
				.setCancelable(false)
				.setPositiveButton("Replay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
						startActivity(i);
					}
				})
				.setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						int nextLevel = GlobalVariables.getCurrentLevel()+1;
						GlobalVariables.setCurrentLevel(nextLevel);
						GlobalVariables.setLatestLevel(nextLevel);
						Intent i = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
						startActivity(i);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);

		buttonsLayout.addView(recallBtn, btnLP); // add recall button to view
		buttonsLayout.addView(submitBtn, btnLP); // add submit button to view
		buttonsLayout.setBackgroundColor(Color.TRANSPARENT);
		layout.addView(buttonsLayout);

		tv = new TextView(this);
		layout.addView(tv);
		tv.setText("Level "+puzzle.getLevel());
		Drawable bg = this.getResources().getDrawable(R.drawable.brick);
		layout.setBackgroundDrawable(bg);
		//		layout.setBackgroundColor(Color.BLUE);

		setContentView(layout);
	}
	
	//continue, skip level, level select, main menu
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Continue");
		menu.add(0, 2, 2, "Skip Level");
		menu.add(0, 3, 3, "Level Select");
		menu.add(0, 4, 4, "Main Menu");
	    return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case 1: //continue
	    	return true;
	    case 2: //skip level		
	    	int nextLevel = GlobalVariables.getCurrentLevel()+1;
	    	GlobalVariables.setCurrentLevel(nextLevel);
	    	GlobalVariables.setLatestLevel(nextLevel);
	    	Intent outlineView = new Intent().setClass(PlayActivity.this, OutlineActivity.class);
	    	startActivity(outlineView);
	    	return true;
	    case 3: //level select
	    	Intent levelSelect = new Intent().setClass(PlayActivity.this, LevelSelectActivity.class);
	    	startActivity(levelSelect);
	    	return true;
	    case 4: //main menu
	    	Intent mainMenu = new Intent().setClass(PlayActivity.this, Tangrams.class);
	    	startActivity(mainMenu);
	    	return true;
	    default:
	        return true;
	    }
	}
	
	//Disable Back button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {

	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

	     return true;

	     }

	     return super.onKeyDown(keyCode, event);    
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		//private ArrayList<Piece> _board = new ArrayList<Piece>();
		private ArrayList<Piece> _toolbox = new ArrayList<Piece>();
		private Piece _currentGraphic = null;
		private Piece _rotatedGraphic = null;

		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			//_thread = new ActionThread(getHolder(), this);

			puzzle = new Puzzle(GlobalVariables.getCurrentLevel()); // Retrieve puzzle for Level 1
			puzzle.scaleSolution(scale);
			ArrayList<Piece> pieces = puzzle.pieces; // Retrieve the pieces for
			// that puzzle
			
			for (int i = 0; i < pieces.size(); i++) {
				Piece p = pieces.get(i);
				p.setActive(false);
				_toolbox.add(p); // add each piece to the toolbox
			}
			updateToolbox(); //update the positions of the pieces

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// synchronized (_thread.getSurfaceHolder()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				float x = event.getX();
				float y = event.getY();
				
				for (int i = 0; i < _toolbox.size(); i++) {
					Piece piece = _toolbox.get(i);
					//if pointer inside BoundingBox of piece, select it
					if (piece.getXBB().inside((int)x,(int)y) && (new Position((int)x,(int)y)).inside(piece.getXVertices(), false)) {
						_currentGraphic = piece;
						break;
					}
				}

				for (int i = 0; i < _board.size(); i++) {
					Piece piece = _board.get(i);
					//if pointer inside BoundingBox of piece, select it
					if (piece.getXBB().inside((int)x,(int)y) && (new Position((int)x,(int)y)).inside(piece.getXVertices(), false)) {
						_currentGraphic = piece;
						break;
					}
				}

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (_currentGraphic != null) {
					_currentGraphic.setActive(false);
					int posX = (int) event.getX();
					int posY = (int) event.getY();
					_currentGraphic.moveTo(posX, posY);
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
		
				if (_currentGraphic != null) {
					if (event.getY() > toolboxHeight) {// within the board area and outside the toolbox
						int posX = Math.round(event.getX() / 10) * 10;
						int posY = Math.round(event.getY() / 10) * 10;
						
						int maxY = (layout.getHeight()-buttonsLayout.getHeight())-_currentGraphic.getHeight();
						if(posY>maxY){//make sure not below the buttons layout
							posY = maxY;
						}
						
						int minX = _currentGraphic.getWidth()/2;
						int maxX = layout.getWidth()-minX;
						if(posX>maxX){//make sure it is not too far to the right
							posX = maxX;
						}else if(posX<minX){//make sure it is not too far to the left
							posX = minX;
						}
						
						//snap to grid
						posX = Math.round(posX / 10) * 10;
						posY = Math.round(posY / 10) * 10;
						
						_currentGraphic.moveTo(posX, posY);
						if(!_board.contains(_currentGraphic)) {
							_board.add(_currentGraphic);
							_toolbox.remove(_currentGraphic);
							updateToolbox();
						}
					} else if (event.getY() <= toolboxHeight) {// within the toolbox area
						if(!_toolbox.contains(_currentGraphic)) {
							_toolbox.add(_currentGraphic);
							_board.remove(_currentGraphic);
						}
						updateToolbox(); //always keep toolbox updated if in toolbox
					}

					if (_currentGraphic.isActive() && event.getY() > toolboxHeight) {
						//check in board area too before rotating
						_currentGraphic.rotate();
						_rotatedGraphic = _currentGraphic;
					}

					setActive(_currentGraphic);
					_currentGraphic = null;
				}
			}
			return true;
		}

		// }

		/**
		 * setActive sets all the pieces on the board and in the toolbox
		 * inactive except for the active piece
		 * 
		 * @param p
		 */
		public void setActive(Piece p) {
			for (Piece piece : _toolbox) {
				piece.setActive(false);
			}
			for (Piece piece : _board) {
				piece.setActive(false);
			}
			p.setActive(true);
		}

		/**
		 * updateToolBox rearranges the pieces in the right position
		 */
		public void updateToolbox() {
			int margin = toolboxHeight/8;
			int centerX = 0;
			int centerY = 0;
			for (int i = 0; i < _toolbox.size(); i++) {
				Piece p = _toolbox.get(i);
				centerX += (p.getWidth() + margin); // add width of Piece + margin
				centerY = toolboxHeight/2;
				p.moveTo(centerX, centerY);
			}
		}

		/**
		 * Recall takes all of the pieces on the board and puts them back into
		 * the toolbox
		 */
		public void recall() {
			//move all the pieces back to toolbox
			for (int i = 0; i < _board.size(); i++) {
				Piece p = _board.get(i);
				p.setActive(false);
				_toolbox.add(p);

			}
			_board.clear();
			updateToolbox();
		}

		@Override
		public void onDraw(Canvas canvas){
			//draw the background
			BitmapDrawable background;
			background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.brick));
			background.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
			background.draw(canvas);

			//paint for the pieces
			Paint piecePaint = new Paint();
			piecePaint.setColor(Color.RED);
			//paint for the active piece
			Paint activePaint = new Paint();
			activePaint.setColor(Color.CYAN);

			Paint paint = new Paint();
			paint.setColor(Color.BLACK); 
			canvas.drawLine(0, toolboxHeight, displayWidth, toolboxHeight, paint); //Draw the line between the toolbox and play area
			
			if(GlobalVariables.outlineOn){
				//Draw puzzle in the background 
				Path puzzlePath = new Path();
				ArrayList<Position> puzzlePositions = puzzle.getSolution();
				for(int i = 0; i < puzzlePositions.size();i++){
					if(i==0){
						puzzlePath.moveTo(puzzlePositions.get(i).getX(), 
								puzzlePositions.get(i).getY());							
					}else{
					puzzlePath.lineTo(puzzlePositions.get(i).getX(), 
							puzzlePositions.get(i).getY());
					}
				}
				puzzlePath.close();
				//snap to grid
				int posX = Math.round((displayWidth/2-puzzle.getCenterX()) / 10) * 10;
				int posY = Math.round((displayHeight/2-puzzle.getCenterY()) / 10) * 10;
				puzzlePath.offset(posX, posY);
				//Offset the "model" too! move xsolution
				puzzle.moveXSolutionTo(posX, posY);
				canvas.drawPath(puzzlePath, paint);
			}else {
				//do not display outline
			}
			
			if (! _toolbox.isEmpty()){
				Path toolboxPiecePath;

				for (int j = 0; j < _toolbox.size(); j++){ //Draw all objects in the toolbox
					Piece toolboxPiece = _toolbox.get(j);

					toolboxPiecePath = new Path();
					ArrayList<Position> toolboxPieceVertices = toolboxPiece.getXVertices();

					for(int i = 0; i < toolboxPieceVertices.size();i++){
						if(i==0){
							toolboxPiecePath.moveTo(toolboxPieceVertices.get(i).getX(), 
									toolboxPieceVertices.get(i).getY());							
						}else{
							toolboxPiecePath.lineTo(toolboxPieceVertices.get(i).getX(), 
									toolboxPieceVertices.get(i).getY());
						}
					}

					toolboxPiecePath.close();

					//toolboxPiecePath.offset(10+70*j, 0);
					//toolboxPiecePath.offset(_currentGraphic., _currentGraphic.());
					canvas.drawPath(toolboxPiecePath, piecePaint);
				}
			}
			if (! _board.isEmpty()){
				Path boardPiecePath;

				for (int j = 0; j < _board.size();j++) { //Draw all objects on the board		
					Piece boardPiece = _board.get(j);

					boardPiecePath = new Path();
					ArrayList<Position> boardPieceVertices = boardPiece.getXVertices();

					for(int i = 0; i < boardPieceVertices.size();i++){
						if(i==0){
							boardPiecePath.moveTo(boardPieceVertices.get(i).getX(), 
									boardPieceVertices.get(i).getY());							
						}else{
							boardPiecePath.lineTo(boardPieceVertices.get(i).getX(), 
									boardPieceVertices.get(i).getY());
						}
					}
					//this offset is messing the model don't do this
					//rather design the Piece itself so it is centered!
					//boardPiecePath.offset(boardPiece.getXOffset(), boardPiece.getYOffset());
					boardPiecePath.close();
					
					//flash the rotated piece in teal
					if(_rotatedGraphic!= null && _rotatedGraphic == boardPiece) {
						canvas.drawPath(boardPiecePath, activePaint);
						_rotatedGraphic = null;
					} else {
						canvas.drawPath(boardPiecePath, piecePaint);
					}
				}
			}

			// Draw the object that is being dragged (if there is one)
			if (_currentGraphic != null) {
				Path path = new Path();

				ArrayList<Position> pieceVertices = _currentGraphic.getXVertices();

				for(int i = 0; i < pieceVertices.size();i++){
					if(i==0){
						path.moveTo(pieceVertices.get(i).getX(), 
								pieceVertices.get(i).getY());
					}else{
						path.lineTo(pieceVertices.get(i).getX(), 
								pieceVertices.get(i).getY());
					}
				}
				//path.offset(_currentGraphic.getXOffset(), _currentGraphic.getYOffset());
				path.close();
				canvas.drawPath(path, activePaint);
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		public void surfaceCreated(SurfaceHolder holder) {
			_thread = new ActionThread(getHolder(), this);
			_thread.setRunning(true);
			_thread.start();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// simply copied from sample application LunarLander:
			// we have to tell thread to shut down & wait for it to finish, or
			// else
			// it might touch the Surface after we return and explode
			boolean retry = true;
			_thread.setRunning(false);
			while (retry) {
				try {
					_thread.join();
					retry = false;
				} catch (InterruptedException e) {
					// we will try it again and again...
				}
			}
		}
	}

	class ActionThread extends Thread {
		private SurfaceHolder _surfaceHolder;
		private Panel _panel;
		private boolean _run = false;

		public ActionThread(SurfaceHolder surfaceHolder, Panel panel) {
			_surfaceHolder = surfaceHolder;
			_panel = panel;
		}

		public void setRunning(boolean run) {
			_run = run;
		}

		public SurfaceHolder getSurfaceHolder() {
			return _surfaceHolder;
		}

		@Override
		public void run() {
			Canvas c;
			while (_run) {
				c = null;
				try {
					c = _surfaceHolder.lockCanvas(null);
					_panel.onDraw(c);
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						_surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
}