package edu.berkeley.cs169.tangrams.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;

import edu.berkeley.cs169.tangrams.PlayActivity;

public class PlayActivityTest extends ActivityInstrumentationTestCase2<PlayActivity> {
	Activity mActivity;
	
	public PlayActivityTest() {
		super("edu.berkeley.cs169.tangrams", PlayActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
		//default junit method to set up every test
        super.setUp();
        mActivity = this.getActivity();
    }
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mActivity = null;
	}
	
	public void testDragDrop() {
		
		//pause to let app load
		try {
			Thread.sleep(500);
		} catch(InterruptedException e){
		}
		
		//get height and width
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		TouchUtils.drag(this, 40, width/2, 105, height/2, 50);
		
		//pause to show
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e){
		}
	}

}
