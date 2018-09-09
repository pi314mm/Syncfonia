package guiComponents;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/**
 * Listener for the escape key
 * 
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class EscapeListener implements KeyEventDispatcher{
	private static Backable gui;
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e){
		if(e.getID() == KeyEvent.KEY_RELEASED){//if the key was released
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){//if the key was the escape key
				if(gui != null){//if the active GUI isn't currently null
					gui.back();//go back
				}
			}
		}
		return false;
	}
	
	public static void set(Backable gui){
		EscapeListener.gui = gui;
	}
}