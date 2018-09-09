package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import guiComponents.Backable;
import guiComponents.Button;
import guiComponents.EscapeListener;
import guiComponents.FontList;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class MainMenuGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;//default for extending JFrame
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//width of screen
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//height of screen
	
	private JPanel contentPane;
	
	/**
	 * the main method that starts the game
	 * @param args
	 */
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainMenuGUI();//open up the main menu GUI in a swing thread
			}
		});
	}
	
	public MainMenuGUI(){
		//event handler for escape key
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new EscapeListener());
		
		EscapeListener.set(this);//set this to the current screen
		
		//set it to fullscreen
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		//setAlwaysOnTop(true);
		setVisible(true);
		
		//create a grid bag layout
		contentPane = new JPanel();
		contentPane.setBackground(new Color(184,209,249));//light blue
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {width / 2, width / 2, 0};
		gbl_contentPane.rowHeights = new int[] {(int)Math.round(height * .2), (int)Math.round(height * .2), (int)Math.round(height * .3), (int)Math.round(height * .1), 0};
		gbl_contentPane.columnWeights = new double[] {0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		//create title JLabel
		JLabel lblTitle = new JLabel("Syncfonia: Beat Your Music");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(FontList.mainTitle);
		lblTitle.setForeground(FontList.titleColor);
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 2;
		gbc_lblTitle.insets = new Insets(0,0,5,0);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		contentPane.add(lblTitle,gbc_lblTitle);
		
		//Create Play button
		Button btnPlay = new Button();
		btnPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				SongSelectGUI.setVisible();
				dispose();
			}
		});
		btnPlay.setBackground("Play.png");
		btnPlay.setTextColor(new Color(1,99,19));//dark green
		
		//add to GridBag
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.insets = new Insets(0,0,0,5);
		gbc_btnPlay.gridx = 0;
		gbc_btnPlay.gridy = 2;
		contentPane.add(btnPlay,gbc_btnPlay);
		
		
		//Create Tutorial Button
		Button btnTutorial = new Button();
		btnTutorial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new TutorialGUI();
				dispose();
			}
		});
		btnTutorial.setBackground("Tutorial.png");
		btnTutorial.setTextColor(new Color(62,2,135));//dark purple
		
		//add to GridBag
		GridBagConstraints gbc_btnTutorial = new GridBagConstraints();
		gbc_btnTutorial.gridx = 1;
		gbc_btnTutorial.gridy = 2;
		contentPane.add(btnTutorial,gbc_btnTutorial);
		
		//create exit button
		Button btnExit = new Button("Press escape or click here to exit");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		btnExit.setTextColor(Color.RED);
		
		//add to GridBag
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.gridx = 0;
		gbc_btnExit.gridy = 3;
		gbc_btnExit.gridwidth = 2;
		contentPane.add(btnExit,gbc_btnExit);
		
		revalidate();
		repaint();
	}
	
	@Override
	public void back(){
		System.exit(0);//pressing escape exits the game
	}
	
	
}
