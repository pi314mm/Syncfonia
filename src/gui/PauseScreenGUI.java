package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import buttons.GameplayGUI;
import guiComponents.Backable;
import guiComponents.Button;
import guiComponents.EscapeListener;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class PauseScreenGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//screen width
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//screen height
	private JPanel contentPane;
	
	
	/**
	 * Create the frame.
	 */
	public PauseScreenGUI(){
		EscapeListener.set(this);
		
		//half the height
		setSize(width / 2,height / 2);
		setLocation(width / 4,height / 4);
		setUndecorated(true);
		setOpacity(0.75f);
		//setAlwaysOnTop(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//grid layout
		contentPane = new JPanel();
		contentPane.setBackground(new Color(184,209,249));//light blue
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0,1,0,0));
		
		
		//create resume button
		Button btnResume = new Button("Resume");
		btnResume.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GameplayGUI.getInstance().continuePlaying();
				dispose();
			}
		});
		btnResume.setTextColor(new Color(0,239,79));//light green
		contentPane.add(btnResume);
		
		
		//create restart button
		Button btnRestart = new Button("Restart");
		btnRestart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				GameplayGUI.getInstance().reset();
				dispose();
			}
		});
		btnRestart.setTextColor(new Color(232,18,18));//bright red
		contentPane.add(btnRestart);
		
		
		//create exit button
		Button btnExit = new Button("Exit");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		btnExit.setTextColor(new Color(191,0,239));//purple
		contentPane.add(btnExit);
		
		
		revalidate();
		repaint();
	}
	
	
	@Override
	public void back(){
		GameplayGUI.getInstance().goToMenu();//exit from the currently running game
		dispose();
	}
	
}
