package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import guiComponents.Backable;
import guiComponents.Button;
import guiComponents.EscapeListener;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class TutorialGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//width of screen
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//height of screen
	private JLabel lblImage;
	private JPanel contentPane;
	private Button btnLeft, btnRight;
	private int imageIndex = 1;
	
	public TutorialGUI(){
		EscapeListener.set(this);//this is the active screen
		
		//make fullscreen
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		//setAlwaysOnTop(true);
		setVisible(true);
		
		//gridbag layout
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {width / 3, width / 3, width / 3};
		gbl_contentPane.rowHeights = new int[] {height * 7 / 8, height / 8};
		contentPane.setLayout(gbl_contentPane);
		
		//add the JLabel that holds the image
		lblImage = new JLabel();
		GridBagConstraints gbc_lblImage = new GridBagConstraints();
		gbc_lblImage.gridwidth = 3;
		gbc_lblImage.gridx = 0;
		gbc_lblImage.gridy = 0;
		contentPane.add(lblImage,gbc_lblImage);
		
		//left button
		btnLeft = new Button("<");
		btnLeft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				imageIndex--;//lower imageIndex
				setImage();//update
			}
		});
		btnLeft.setTextColor(Color.GREEN);
		
		GridBagConstraints gbc_btnLeft = new GridBagConstraints();
		gbc_btnLeft.gridx = 0;
		gbc_btnLeft.gridy = 1;
		contentPane.add(btnLeft,gbc_btnLeft);
		
		//back button
		Button btnExit = new Button("Back");
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		btnExit.setTextColor(Color.RED);
		
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.gridx = 1;
		gbc_btnExit.gridy = 1;
		contentPane.add(btnExit,gbc_btnExit);
		
		
		//right button
		btnRight = new Button(">");
		btnRight.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				imageIndex++;//raise imageIndex
				setImage();//update
			}
		});
		btnRight.setTextColor(Color.GREEN);
		
		GridBagConstraints gbc_btnRight = new GridBagConstraints();
		gbc_btnRight.gridx = 2;
		gbc_btnRight.gridy = 1;
		contentPane.add(btnRight,gbc_btnRight);
		
		setImage();//update image
		
		revalidate();
		repaint();
	}
	
	private void setImage(){
		if(imageIndex == 1){//leftmost image
			btnLeft.setVisible(false);
		}else{
			btnLeft.setVisible(true);
		}
		if(imageIndex == 8){//rightmost image
			btnRight.setVisible(false);
		}else{
			btnRight.setVisible(true);
		}
		//set the label to a 3/4 * screen scaled image of the current index
		lblImage.setIcon(new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource(imageIndex + ".png")).getImage().getScaledInstance(width * 3 / 4,height * 3 / 4,Image.SCALE_SMOOTH)));
	}
	
	@Override
	public void back(){
		new MainMenuGUI();
		dispose();
	}
	
	
}
