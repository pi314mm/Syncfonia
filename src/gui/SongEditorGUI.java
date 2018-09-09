package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import fileIO.SongFileHandler;
import fileIO.SongInfo;
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
public class SongEditorGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;//default for extending JFrame
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//the screen width
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//the screen height
	private SongInfo original;
	private JTextField txtFile, txtName, txtArtist, txtAlbum;
	
	public SongEditorGUI(String filelocation){
		EscapeListener.set(this);//this is the active screen
		
		original = SongFileHandler.getSong(filelocation);//get the original information
		
		//set it to 3/4 of the width and height in the middle of the screen
		setSize(width * 3 / 4,height * 3 / 4);
		setLocation(width / 8,height / 8);
		setUndecorated(true);
		setOpacity(0.95f);
		//setAlwaysOnTop(true);
		
		//grid layout
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(18,7,145));//dark blue
		setContentPane(contentPane);
		setLayout(new GridLayout(0,2,10,10));
		contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));//add a border
		
		//file label
		add(makeLabel("File:"));
		
		//file text field
		txtFile = new JTextField(original.getFile());
		txtFile.setFont(FontList.normalText);
		add(txtFile);
		
		//name Label
		add(makeLabel("Name:"));
		
		//name text field
		txtName = new JTextField(original.getName());
		txtName.setFont(FontList.normalText);
		add(txtName);
		
		//artist label
		add(makeLabel("Artist:"));
		
		//artist text field
		txtArtist = new JTextField(original.getArtist());
		txtArtist.setFont(FontList.normalText);
		add(txtArtist);
		
		//album label
		add(makeLabel("Album:"));
		
		//album text field
		txtAlbum = new JTextField(original.getAlbum());
		txtAlbum.setFont(FontList.normalText);
		add(txtAlbum);
		
		//make the cancel/back button
		Button btnRestart = new Button("Cancel");
		btnRestart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		btnRestart.setTextColor(new Color(232,18,18));//bright red
		add(btnRestart);
		
		//make the save button
		Button btnSave = new Button("Save");
		btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//save the changed values by creating a new SongInfo object
				SongFileHandler.edit(original.getFile(),new SongInfo(txtFile.getText(),txtName.getText(),txtArtist.getText(),txtAlbum.getText(),original.getScore()));
				SongSelectGUI.refresh();//update the data in the table
				back();//go back to the Song Select GUI
			}
		});
		btnSave.setTextColor(new Color(0,239,79));//light green
		add(btnSave);
		
		revalidate();
		repaint();
		setVisible(true);
	}
	
	/**
	 * Creates a JLabel object that is right aligned with the proper font and color
	 * @param text the text to display in the JLabel
	 * @return the formated JLabel
	 */
	private JLabel makeLabel(String text){
		JLabel l = new JLabel(text);
		l.setHorizontalAlignment(JLabel.RIGHT);//right align
		l.setFont(FontList.normalText);//proper font
		l.setForeground(Color.GRAY);//color
		return l;
	}
	
	
	@Override
	public void back(){
		dispose();
		SongSelectGUI.setVisible();
	}
}
