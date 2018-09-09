package buttons;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class ButtonController{
	private Button[] buttons;
	private Random rand = new Random();//random number generator for location
	private int score = 0;
	
	/**
	 * 
	 * @return the current percentage of buttons clicked for displaying in game
	 */
	public String runningScore(){
		return String.format("%d / %d = %.2f%s",score,buttons.length * 4,Math.round(score * 1.0 / (buttons.length * 4) * 10000) / 100.00,"%");
	}
	
	public ButtonController(long[] buttonTimes){
		Arrays.sort(buttonTimes);
		buttons = new Button[buttonTimes.length];
		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new Button(randWidth(),randHeight(),buttonTimes[i]);
		}
	}
	
	private int randHeight(){
		//between size/2+topBarThickness to GameplayGUI.getHeight()-size/2-GameplayGUI.getBottomBarHeight()
		return rand.nextInt(GameplayGUI.getHeight() - Button.getMaxSize() - GameplayGUI.getTopBarThickness() - GameplayGUI.getBottombarheight()) + Button.getMaxSize() / 2 + GameplayGUI.getTopBarThickness();
	}
	
	/**
	 * iteratively draw buttons
	 * @param g the graphic to draw the buttons on
	 */
	public void drawButtons(Graphics2D g){
		for(int i = 0; i < buttons.length; i++){
			buttons[i].draw(g);
		}
	}
	
	private int randWidth(){
		//between size/2 to GameplayGUI.getWidth()-size/2
		return rand.nextInt(GameplayGUI.getWidth() - Button.getMaxSize()) + Button.getMaxSize() / 2;
	}
	
	/**
	 * method for detecting which button was clicked if any
	 * @param mouseX the x coordinate of the mouse
	 * @param mouseY the y coordinate of the mouse
	 */
	public void click(int mouseX, int mouseY){
		for(int i = 0; i < buttons.length; i++){
			if(buttons[i].clicked(mouseX,mouseY)){
				//System.out.println(String.format("Button %d clicked",i));
				score += 4 - Math.abs(buttons[i].getScore());
				return;
			}
		}
	}
	
	/**
	 * creates an array of the scores of all the buttons
	 * @return the score counter of the buttons
	 */
	public int[] getScore(){
		int[] scores = new int[8];
		for(Button b:buttons){
			scores[b.getScore() + 3]++;
		}
		return scores;
	}
	
	/**
	 * iteratively reset all the buttons, score, and time
	 */
	public void reset(){
		for(Button b:buttons){
			b.reset();
		}
		score = 0;
		Button.updateTime(0);
	}
	
}
