package guiComponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class ScoreGraph extends JPanel{
	private static final long serialVersionUID = 1L;//default value
	private static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//the screen width
	private int height;//the height is passed in by the constructor
	private int[] barHeights;//the heights of each bar in the bar graph
	
	public ScoreGraph(int[] scores, int height){
		this.height = height;//set the height
		this.barHeights = new int[scores.length];
		
		int maxScore = 0;//for calculating the maximum height
		for(int i = 0; i < scores.length; i++){
			maxScore += scores[i];
		}
		
		//the heights of the bars are represented proportionally to the height
		for(int i = 0; i < barHeights.length; i++){
			barHeights[i] = scores[i] * height / maxScore;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		//the background
		g.setColor(Color.WHITE);
		g.fillRect(0,0,width,height);
		
		//array for the colors of the bar
		Color[] colors = new Color[] {new Color(102,0,102), new Color(102,0,255), Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED, Color.GRAY};
		
		//paint each bar to the corresponding height by drawing rectangles
		for(int i = 0; i < colors.length; i++){
			g.setColor(colors[i]);
			g.fillRect(i * width / 8,height - barHeights[i],width / 8,barHeights[i]);
		}
	}
}