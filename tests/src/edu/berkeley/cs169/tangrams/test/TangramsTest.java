package edu.berkeley.cs169.tangrams.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import edu.berkeley.cs169.tangrams.Tangrams;
import edu.berkeley.cs169.tangrams.R;

public class TangramsTest extends ActivityInstrumentationTestCase2<Tangrams> {
	Activity mActivity;
	
	public TangramsTest() {
		super("edu.berkeley.cs169.tangrams", Tangrams.class);
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
	
	public void testButtonsExist() {
		assertNotNull("Play Button",mActivity.findViewById(R.id.Button01));
		assertNotNull("Settings Button",mActivity.findViewById(R.id.Button01));
		assertNotNull("High Scores Button",mActivity.findViewById(R.id.Button01));
	}
}
