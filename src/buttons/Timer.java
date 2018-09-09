package buttons;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class Timer{
	private long startTime;//the time the song started
	private long timeStartPause;//the time the song was paused
	private long totalTimePaused = 0;//accumulating number for the time paused
	private boolean paused = false;//is currently paused
	
	public Timer(){
		startTime = System.currentTimeMillis();//set to current time
	}
	
	public boolean isPaused(){
		return paused;
	}
	
	public void pause(){
		if(!paused){//don't pause if already paused
			timeStartPause = System.currentTimeMillis();//store pause time for later reference
			paused = true;
		}
	}
	
	public void resume(){
		if(paused){//don't resume if already playing
			totalTimePaused += System.currentTimeMillis() - timeStartPause;//increment the total time paused
			paused = false;
		}
	}
	
	/**
	 * 
	 * @return the amount of time the song has been playing in milliseconds
	 */
	public long getCurrentTime(){
		if(paused){
			return timeStartPause - startTime - totalTimePaused;//if it is paused, measure time from timeStartPause rather than the current time
		}
		return System.currentTimeMillis() - startTime - totalTimePaused;//the difference in time minus the total time paused
	}
}
