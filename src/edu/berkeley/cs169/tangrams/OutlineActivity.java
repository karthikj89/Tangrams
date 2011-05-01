package edu.berkeley.cs169.tangrams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OutlineActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outline);
		
		Button outlineButton = (Button) findViewById(R.id.OutlineButton);
        outlineButton.setOnClickListener(new View.OnClickListener() {
            
        	public void onClick(View v) {
                // Perform action on click
        		GlobalVariables.setisReturningPlayer(true);
        		GlobalVariables.setOutlineStatus(true);
        		Intent i = new Intent().setClass(OutlineActivity.this, PlayActivity.class);
        		startActivity(i);
            }
        });
        
        Button noOutlineButton = (Button) findViewById(R.id.NoOutlineButton);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        noOutlineButton.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				// Perform action on click
        		GlobalVariables.setisReturningPlayer(true);
				builder.setMessage("You have 20 seconds to memorize puzzle")
				.setCancelable(false)
				.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
		        		GlobalVariables.setisReturningPlayer(true);
						GlobalVariables.setOutlineStatus(false);
		        		Intent i = new Intent().setClass(OutlineActivity.this, PlayNoOutlineActivity.class);
		        		startActivity(i);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	//continue, skip level, level select, main menu
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "Continue");
		menu.add(0, 2, 2, "Level Select");
		menu.add(0, 3, 3, "Main Menu");
	    return true;
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case 1: //continue
	    	return true;
	    case 2: //level select
	    	Intent levelSelect = new Intent().setClass(OutlineActivity.this, LevelSelectActivity.class);
	    	startActivity(levelSelect);
	    	return true;
	    case 3: //main menu
	    	Intent mainMenu = new Intent().setClass(OutlineActivity.this, Tangrams.class);
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
