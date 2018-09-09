package buttons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;

/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class Button{
	private static final double SIZE_SCALE = .07;//proportional size to screen
	//The maximum size is the longer component of the height or width of the sreen multiplied by the size scale
	private static final int MAX_SIZE = (int)(Math.max(Toolkit.getDefaultToolkit().getScreenSize().getHeight(),Toolkit.getDefaultToolkit().getScreenSize().getWidth()) * SIZE_SCALE);
	private static final long MAX_TIME = 1400;//The total time the button appears on the screen, must be divisible by 14
	private static long currentTime = 0;//the current time updated by the Timer
	private static final double startHue = 250 / 360.0, endHue = 0 / 360.0;//from red to purple
	private static final long DISAPPEAR_TIME = 200;//the amount of time it takes for the clicked animation to play
	private static final double ringThicknessScale = .11;//the relative thickness of the white ring around the green button
	private static final int ringThickness = 7;//the thickness of the rings in the disappear animation
	
	private boolean wasClicked = false;//the button was clicked
	private float lastHue = (float)startHue;//the last hue that the button was, starts with start hue
	private long timeClicked;//the time when the button was clicked
	private boolean done = false;//represents a button that was already clicked and finished its animation
	private int x, y;//middle of the button in pixels from top left of the screen
	private long optTime;//the optimal time to click the button
	
	/**
	 * range from -3 to 4 with -3 matching with purple, 3 is red and 4 is miss
	 * 
	 * @return returns the score of button
	 */
	public int getScore(){
		if(!wasClicked){
			return 4;
		}
		return (int)Math.floor(7.0 * (timeClicked - optTime) / MAX_TIME + .5);
	}
	
	/**
	 * used for checking if white ring is displayed
	 * @return true if the current time is optimal for clicking
	 */
	private boolean isOptimal(){
		return Math.abs(currentTime - optTime) < MAX_TIME / 14.0;
	}
	
	public static long getMaxTime(){
		return MAX_TIME;
	}
	
	/**
	 * size changes linearly from half size to full size then back to half size
	 * @return
	 */
	public int getSize(){
		long time = Math.abs(optTime - currentTime);
		return (int)(MAX_SIZE - time * MAX_SIZE / MAX_TIME);//start at half size
	}
	
	/**
	 * updates the current time
	 * @param currentTime the current time of the song
	 */
	public static void updateTime(long currentTime){
		Button.currentTime = currentTime;
	}
	
	/**
	 * draws the button on a graphics2d
	 * @param g the graphic to draw on
	 * @return returns true if the button was visible and painted and false otherwise
	 */
	public boolean draw(Graphics2D g){
		if(done){
			return false;//not visible
		}
		Color buttonColor = calculateColor();
		if(buttonColor == null){
			if(currentTime - optTime > MAX_TIME / 2){//ran out of time
				done = true;//button wasn't clicked
			}
			return false;//not visible on the screen
		}
		int size = getSize();
		if(isOptimal() && !wasClicked){//draw with white ring around it
			g.setColor(Color.WHITE);//white circle
			g.fillOval(x - size / 2,y - size / 2,size,size);
			g.setColor(buttonColor);//smaller green circle
			int ringSize = (int)Math.round(size - size * ringThicknessScale);
			g.fillOval(x - ringSize / 2,y - ringSize / 2,ringSize,ringSize);
		}else{
			if(wasClicked){//display the clicked animation
				int ringSize = (int)(MAX_SIZE + MAX_SIZE * .5 * (currentTime - timeClicked) / DISAPPEAR_TIME);//ring expands outward
				g.setColor(Color.getHSBColor(lastHue,1,1));
				for(int i = 0; i < ringThickness; i++){//pixels thick
					g.drawOval(x - (ringSize - i) / 2,y - (ringSize - i) / 2,ringSize - i,ringSize - i);
				}
			}
			g.setColor(buttonColor);//color the button at the appropriate size
			g.fillOval(x - size / 2,y - size / 2,size,size);
		}
		return true;
	}
	
	/**
	 * 
	 * @return true if the button is within the active time frame, false otherwise
	 */
	private boolean isVisible(){
		long appearanceDifference = currentTime - optTime;
		return Math.abs(appearanceDifference) <= MAX_TIME / 2;
	}
	
	/**
	 * The hue of the color changes linearly from startHue to endHue
	 * Once clicked, the saturation of the last clicked color changes linearly to white
	 * @return the color of the button
	 */
	private Color calculateColor(){
		if(wasClicked){
			long timeSinceClicked = currentTime - timeClicked;
			if(timeSinceClicked > DISAPPEAR_TIME){
				return null;
			}
			float saturation = (float)(1.0 - timeSinceClicked * 1.0 / DISAPPEAR_TIME);
			
			return Color.getHSBColor(lastHue,saturation,saturation);
		}
		long appearanceDifference = currentTime - optTime;//goes from negative to positive
		if(Math.abs(appearanceDifference) > MAX_TIME / 2){//if not within the time bounds
			return null;//not visible on the screen
		}
		//linear transformation from time to hue
		float hue = (float)((1.0 * (endHue - startHue) / MAX_TIME) * appearanceDifference + (startHue + endHue) / 2.0);
		//System.out.println(hue*360);
		lastHue = hue;
		Color c = Color.getHSBColor(hue,1,1);
		return c;
	}
	
	/**
	 * method for receiving mouse click
	 * @param mouseX the x coordinate of the mouse
	 * @param mouseY the y coordinate of the mouse
	 * @return true if the button was in the state of being able to click
	 * 			and was clicked within the boundaries of the size
	 */
	public boolean clicked(int mouseX, int mouseY){
		if(wasClicked || !isVisible()){
			return false;
		}
		//distance formula
		double distanceFromCenter = Math.sqrt((mouseX - x) * (mouseX - x) + (mouseY - y) * (mouseY - y));
		if(distanceFromCenter <= MAX_SIZE / 2){
			timeClicked = currentTime;
			wasClicked = true;
			return true;
		}
		return false;
	}
	
	public Button(int x, int y, long optTime){
		this.x = x;
		this.y = y;
		this.optTime = optTime;
	}
	
	/**
	 * resets the button to erase previous game data
	 */
	public void reset(){
		done = false;
		wasClicked = false;
		lastHue = (float)startHue;
	}
	
	public static int getMaxSize(){
		return MAX_SIZE;
	}
}
