package edu.berkeley.cs169.tangrams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class Tangrams extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button playButton = (Button) findViewById(R.id.Button01);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				Intent i; 
				if(GlobalVariables.getisReturningPlayer()){
					i = new Intent().setClass(Tangrams.this, LevelSelectActivity.class);
				}else{
					i = new Intent().setClass(Tangrams.this, OutlineActivity.class);
				}
				startActivity(i);
			}
		});
		Button editButton = (Button) findViewById(R.id.Button02);
		editButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent().setClass(Tangrams.this, SettingsActivity.class);
				startActivity(i);
			}
		});	

		Button highScoreButton = (Button) findViewById(R.id.Button03);
		highScoreButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent().setClass(Tangrams.this, HighScoreActivity.class);
				startActivity(i);
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

			return true;

		}

		return super.onKeyDown(keyCode, event);    
	}

}