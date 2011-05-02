package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;

import edu.berkeley.cs169.tangrams.PlayActivity.ActionThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayNoOutlineActivity extends Activity{
	TextView timer; //textview to display the countdown
	MyCount counter;
	private int displayWidth, displayHeight, scale;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Display display = getWindowManager().getDefaultDisplay(); 
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		scale = 1;
		if(displayWidth >= 480)
			scale = 2;

		LinearLayout nooutlinelayout = new LinearLayout(this); 

		timer = new TextView(this);
		LinearLayout.LayoutParams timerLP = new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				0.0f);
		nooutlinelayout.addView(timer, timerLP); //add timer to layout

		//20000 is the starting number (in milliseconds)
		//1000 is the number to count down each time (in milliseconds)
		counter = new MyCount(20000,1000);
		counter.start();
		Panel myPanel = new Panel(this);
		nooutlinelayout.setOrientation(LinearLayout.VERTICAL); 
		nooutlinelayout.addView(myPanel, new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.FILL_PARENT, 
				1.0f));  

		//Play Now button
		Button playNowBtn = new Button(this); 
		playNowBtn.setText("Play Now!"); 
        playNowBtn.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		counter.cancel();
        		Intent startPlay = new Intent().setClass(PlayNoOutlineActivity.this, PlayActivity.class);
        		startActivity(startPlay);
            }
        });
        
		LinearLayout.LayoutParams btnLP = new LinearLayout.LayoutParams ( 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				0.0f);
		nooutlinelayout.addView(playNowBtn, btnLP); //add button to view
		Drawable bg = this.getResources().getDrawable(R.drawable.brick);
		nooutlinelayout.setBackgroundDrawable(bg);

		setContentView(nooutlinelayout); 
	}
	
	//Disable Back button
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {

	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

	     return true;

	     }

	     return super.onKeyDown(keyCode, event);    
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
	    	counter.cancel();
	    	Intent outlineView = new Intent().setClass(PlayNoOutlineActivity.this, OutlineActivity.class);
	    	startActivity(outlineView);
	    	return true;
	    case 3: //level select
	    	counter.cancel();
	    	Intent levelSelect = new Intent().setClass(PlayNoOutlineActivity.this, LevelSelectActivity.class);
	    	startActivity(levelSelect);
	    	return true;
	    case 4: //main menu
	    	counter.cancel();
	    	Intent mainMenu = new Intent().setClass(PlayNoOutlineActivity.this, Tangrams.class);
	    	startActivity(mainMenu);
	    	return true;
	    default:
	        return true;
	    }
	}

	class Panel extends SurfaceView implements SurfaceHolder.Callback {
		private ActionThread _thread;
		Puzzle puzzle;
		public Panel(Context context) {
			super(context);
			getHolder().addCallback(this);
			//_thread = new ActionThread(getHolder(), this);
			
			puzzle = new Puzzle(GlobalVariables.getCurrentLevel());
			puzzle.scaleSolution(scale);
		}

		public void onDraw(Canvas canvas){
			//draw the background
			BitmapDrawable background;
			background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.brick));
			background.setBounds(0, 0, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
			background.draw(canvas);
			
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);

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
			int posX = Math.round((displayWidth/2-puzzle.getCenterX()) / 10) * 10;
			int posY = Math.round((displayHeight/2-puzzle.getCenterY()) / 10) * 10;
			puzzlePath.offset(posX, posY);
			//Offset the "model" too! move xsolution
			puzzle.moveXSolutionTo(posX, posY);
			canvas.drawPath(puzzlePath, paint);
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

	//Timer class
	class MyCount extends CountDownTimer{
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
    		Intent i = new Intent().setClass(PlayNoOutlineActivity.this, PlayActivity.class);
    		startActivity(i);
		}
		@Override
		public void onTick(long millisUntilFinished) {
			timer.setText("Time Left: "+millisUntilFinished/1000);
		}
	}
}

