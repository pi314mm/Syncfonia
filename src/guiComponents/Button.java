package guiComponents;

import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * customized version of JButton
 * 
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class Button extends JButton{
	private static final long serialVersionUID = 1L;
	
	public Button(String s){
		super(s);
		removeDefault(this);//remove the ugly default for button
		
		//Set font
		setFont(FontList.buttonFont);
	}
	
	public static void removeDefault(JButton b){
		//disable default JButton background
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
		b.setOpaque(false);
	}
	
	public Button(){
		super();
		removeDefault(this);//remove the ugly default for button
	}
	
	/**
	 * renames the method setForeground to make it more understandable
	 * @param c the color to set the text to
	 */
	public void setTextColor(Color c){
		setForeground(c);
	}
	
	/**
	 * sets the background image to the file specified
	 * @param file the location of the file
	 */
	public void setBackground(String file){
		URL url = this.getClass().getClassLoader().getResource(file);
		setIcon(new ImageIcon(url));
	}
	
}
