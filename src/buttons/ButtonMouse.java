package buttons;

/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Receives mouse click events
 */
public class ButtonMouse implements MouseListener{
	private ButtonController buttonController;
	
	public ButtonMouse(ButtonController buttonController){
		this.buttonController = buttonController;
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(pausePressed(e.getX(),e.getY())){
			GameplayGUI.getInstance().pause();
		}else if(buttonController != null){
			buttonController.click(e.getX(),e.getY());
		}
		//System.out.println(String.format("clicked: %d, %d",e.getX(),e.getY()));
	}
	
	/**
	 * checks if the pause button on the top right of the screen was clicked
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @return true if the pause button was pressed
	 */
	private boolean pausePressed(int x, int y){
		if(x < GameplayGUI.getWidth() - GameplayGUI.getTopBarThickness()){
			return false;
		}
		if(y > 80){
			return false;
		}
		return true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		
	}
	
}
