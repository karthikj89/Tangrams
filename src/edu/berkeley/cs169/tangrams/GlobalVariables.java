package edu.berkeley.cs169.tangrams;
import android.app.Application;


public class GlobalVariables extends Application {
	private static int currentLevel = 1;
	private static int latestLevel = 1;
	private int highScore = 0;
	private boolean soundOn;
	private boolean soundEffects;
	public static boolean outlineOn=true;
	private static int numPuzzles = 12;
	private static boolean isReturningPlayer = true;
	
	/*the setter for current level*/
	public static void setCurrentLevel(int level){
		currentLevel = level;
	}
	
	public static int getLatestLevel(){
		return latestLevel;
	}
	
	public static void setLatestLevel(int level){
		if(!isReturningPlayer){//when reset 
			latestLevel = 1;
		}else if(level > latestLevel){
			latestLevel = level;
		}
	}
	
	/*the setter for high score*/
	public void setHighScore(int score){
		highScore = score;
	}
	
	/*the getter for current level*/
	public static int getCurrentLevel(){
		return currentLevel;
	}
	
	/*the getter for the highScore*/
	public int getHighScore(){
		return highScore;
	}
	
	public static void setOutlineStatus(boolean bool){
		outlineOn = bool;
	}
	
	public boolean getOutlineStatus(){
		return outlineOn;
	}
	
	public static int getNumPuzzles(){
		return numPuzzles;
	}
	
	public static void setNumPuzzles(int num){
		numPuzzles = num;
	}
	
	public static boolean getisReturningPlayer(){
		return isReturningPlayer;
	}
	
	public static void setisReturningPlayer(boolean bool){
		isReturningPlayer = bool;
	}
}
