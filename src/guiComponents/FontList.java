package guiComponents;

import java.awt.Color;
import java.awt.Font;
import buttons.GameplayGUI;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class FontList{
	public static final Font mainTitle = new Font("Tekton Pro Ext",Font.PLAIN,70);
	public static final Font normalText = new Font("Ariel",Font.BOLD,23);
	public static final Font subTitle = new Font("Ariel",Font.BOLD,25);
	public static final Font italics = new Font("Ariel",Font.ITALIC,77);
	public static final Font buttonFont = new Font("Ariel",Font.BOLD,47);
	public static final Font gamplayFont = new Font("Monospaced",Font.BOLD | Font.ITALIC,(int)(GameplayGUI.getTopBarThickness() * .8));
	public static final Color titleColor = new Color(224,15,147);//pinkish color
}
