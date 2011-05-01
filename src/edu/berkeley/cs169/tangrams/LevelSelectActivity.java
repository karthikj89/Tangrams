package edu.berkeley.cs169.tangrams;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LevelSelectActivity extends Activity {
	int level=1;
	Button levelSelectBtn;
	ArrayList<Button> buttons = new ArrayList<Button>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout buttonLayout = new LinearLayout(this);
		LinearLayout levelselectlayout = new LinearLayout(this);
		int numPuzzles = GlobalVariables.getNumPuzzles();

		levelselectlayout.setOrientation(LinearLayout.VERTICAL);

		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams buttonLP = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f);
		buttonLP.gravity = 1; //1 = CENTER_HORIZONTAL

		Drawable bg = this.getResources().getDrawable(R.drawable.brick);
		levelselectlayout.setBackgroundDrawable(bg);

		// Create the buttons
		for(int i = 1; i <= numPuzzles; i++){
			levelSelectBtn = new Button(this);
			levelSelectBtn.setText(""+i);
			levelSelectBtn.setClickable(true);
			levelSelectBtn.setId(i);
			buttons.add(levelSelectBtn);
		}

		for(int i = 0; i<buttons.size();i++){
			final Button b = buttons.get(i);
			BitmapDrawable unlockBg = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.unlock));
			b.setBackgroundDrawable(unlockBg);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Perform action on click
					level = b.getId();
					if(level<=GlobalVariables.getLatestLevel()){//only allow unlocked levels
						GlobalVariables.setCurrentLevel(level);
						Intent outlineClass = new Intent().setClass(LevelSelectActivity.this, OutlineActivity.class);
						startActivity(outlineClass);
					}
				}
			});
			if(b.getId()>GlobalVariables.getLatestLevel()){//locked levels
				BitmapDrawable lockBg = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.lock));
				b.setBackgroundDrawable(lockBg);
			}
			if(i%5==0){
				levelselectlayout.addView(buttonLayout);
				buttonLayout = new LinearLayout(this);
			}
			buttonLayout.addView(b, buttonLP); 
		}

		levelselectlayout.addView(buttonLayout);
		setContentView(levelselectlayout); 
	}

	//continue, skip level, level select, main menu
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Continue");
		menu.add(0, 2, 2, "Main Menu");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case 1: //continue
			return true;
		case 2: //main menu
			Intent mainMenu = new Intent().setClass(LevelSelectActivity.this, Tangrams.class);
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
}