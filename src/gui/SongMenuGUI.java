package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;
import buttons.GameplayGUI;
import fileIO.SongFileHandler;
import fileIO.SongInfo;
import guiComponents.Backable;
import guiComponents.Button;
import guiComponents.EscapeListener;
import guiComponents.FontList;
import song.SongPlayer;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class SongMenuGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private SongPlayer songPlayer;
	private SongInfo song;
	
	public SongMenuGUI(String file, SongPlayer player){
		EscapeListener.set(this);//this is the active screen
		
		this.song = SongFileHandler.getSong(file);//get the song info
		this.songPlayer = player;//the song player
		
		//Set it to full screen
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		//setAlwaysOnTop(true);
		
		//content pane
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(184,209,249));//light blue
		setContentPane(contentPane);
		
		//Grid bag layout
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {width / 2, width / 2, 0};
		gbl_contentPane.rowHeights = new int[] {height / 8, height / 8, height / 8, height / 8, height / 8, height / 8, height / 4, 0};
		gbl_contentPane.columnWeights = new double[] {1.0, 1.0, 0.0};
		gbl_contentPane.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_contentPane);
		
		//create song name label
		JLabel lblSongName = new JLabel(song.getName());
		lblSongName.setFont(FontList.italics);
		lblSongName.setHorizontalAlignment(SwingConstants.CENTER);
		lblSongName.setForeground(FontList.titleColor);
		
		//add song name label
		GridBagConstraints gbc_lblSongName = new GridBagConstraints();
		gbc_lblSongName.insets = new Insets(0,0,5,5);
		gbc_lblSongName.fill = GridBagConstraints.BOTH;
		gbc_lblSongName.gridwidth = 2;
		gbc_lblSongName.gridx = 0;
		gbc_lblSongName.gridy = 0;
		add(lblSongName,gbc_lblSongName);
		
		//create song artist label
		JLabel lblSongArtist = new JLabel("Artist: " + song.getArtist());
		lblSongArtist.setFont(FontList.subTitle);
		
		//add song artist label
		GridBagConstraints gbc_lblSongArtist = new GridBagConstraints();
		gbc_lblSongArtist.insets = new Insets(0,0,5,0);
		gbc_lblSongArtist.gridx = 0;
		gbc_lblSongArtist.gridy = 1;
		add(lblSongArtist,gbc_lblSongArtist);
		
		//create song album label
		JLabel lblSongAlbum = new JLabel("Album: " + song.getAlbum());
		lblSongAlbum.setFont(FontList.subTitle);
		
		//add song album label
		GridBagConstraints gbc_lblSongAlbum = new GridBagConstraints();
		gbc_lblSongAlbum.insets = new Insets(0,0,5,0);
		gbc_lblSongAlbum.gridx = 1;
		gbc_lblSongAlbum.gridy = 1;
		add(lblSongAlbum,gbc_lblSongAlbum);
		
		//create overall completion label
		JLabel lblOverallTitle = new JLabel("Overall Completition");
		lblOverallTitle.setFont(FontList.normalText);
		lblOverallTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		lblOverallTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add overall completion label
		GridBagConstraints gbc_lblOverallTitle = new GridBagConstraints();
		gbc_lblOverallTitle.insets = new Insets(0,0,5,5);
		gbc_lblOverallTitle.fill = GridBagConstraints.BOTH;
		gbc_lblOverallTitle.gridx = 0;
		gbc_lblOverallTitle.gridy = 2;
		add(lblOverallTitle,gbc_lblOverallTitle);
		
		//create highest score label
		JLabel lblHighestScoreTitle = new JLabel("Score for Current Difficulty");
		lblHighestScoreTitle.setFont(FontList.normalText);
		lblHighestScoreTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		lblHighestScoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add highest score label
		GridBagConstraints gbc_lblHighestScoreTitle = new GridBagConstraints();
		gbc_lblHighestScoreTitle.insets = new Insets(0,0,5,5);
		gbc_lblHighestScoreTitle.fill = GridBagConstraints.BOTH;
		gbc_lblHighestScoreTitle.gridx = 1;
		gbc_lblHighestScoreTitle.gridy = 2;
		add(lblHighestScoreTitle,gbc_lblHighestScoreTitle);
		
		//calculate overall score
		JLabel lblOverall = new JLabel("" + getOverallScore());
		lblOverall.setFont(FontList.normalText);
		lblOverall.setVerticalAlignment(SwingConstants.TOP);
		
		//add overall score
		GridBagConstraints gbc_lblOverall = new GridBagConstraints();
		gbc_lblOverall.insets = new Insets(0,0,5,5);
		gbc_lblOverall.gridx = 0;
		gbc_lblOverall.gridy = 3;
		add(lblOverall,gbc_lblOverall);
		
		//calculate current score
		JLabel lblHighestScore = new JLabel("placeholder");
		lblHighestScore.setFont(FontList.normalText);
		lblHighestScore.setVerticalAlignment(SwingConstants.TOP);
		
		//add current score
		GridBagConstraints gbc_lblHighestScore = new GridBagConstraints();
		gbc_lblHighestScore.insets = new Insets(0,0,5,5);
		gbc_lblHighestScore.gridx = 1;
		gbc_lblHighestScore.gridy = 3;
		add(lblHighestScore,gbc_lblHighestScore);
		
		//create difficulty label
		JLabel lblSetDifficultyTitle = new JLabel("Set Difficulty:");
		lblSetDifficultyTitle.setFont(FontList.normalText);
		lblSetDifficultyTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//add difficulty label
		GridBagConstraints gbc_lblSetDifficultyTitle = new GridBagConstraints();
		gbc_lblSetDifficultyTitle.insets = new Insets(0,0,5,5);
		gbc_lblSetDifficultyTitle.fill = GridBagConstraints.BOTH;
		gbc_lblSetDifficultyTitle.gridx = 0;
		gbc_lblSetDifficultyTitle.gridy = 4;
		add(lblSetDifficultyTitle,gbc_lblSetDifficultyTitle);
		
		//calculate difficulty
		JLabel lblDifficulty = new JLabel("0.0");
		lblDifficulty.setFont(FontList.normalText);
		lblDifficulty.setHorizontalAlignment(SwingConstants.LEFT);
		
		//add difficulty
		GridBagConstraints gbc_lblDifficulty = new GridBagConstraints();
		gbc_lblDifficulty.insets = new Insets(0,0,5,5);
		gbc_lblDifficulty.fill = GridBagConstraints.BOTH;
		gbc_lblDifficulty.gridx = 1;
		gbc_lblDifficulty.gridy = 4;
		add(lblDifficulty,gbc_lblDifficulty);
		
		//JSlider for the difficulty
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){//difficulty was changed
				if(!slider.getValueIsAdjusting()){//update value
					lblDifficulty.setText(String.format("%.1f",slider.getValue() / 10.0));
					int scoreIndex = slider.getValue() * 2 / 10;
					lblHighestScore.setText(String.format("%.2f%s",song.getScore()[scoreIndex] / 100.00,"%"));
				}
			}
		});
		slider.setOpaque(false);
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setValue(0);
		//jump to mouse click
		slider.setUI(new MetalSliderUI(){
			protected void scrollDueToClickInTrack(int direction){
				slider.setValue(this.valueForXPosition(slider.getMousePosition().x));
			}
		});
		
		
		//add slider
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.BOTH;
		gbc_slider.gridwidth = 2;
		gbc_slider.insets = new Insets(0,170,5,170);
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 5;
		add(slider,gbc_slider);
		
		//create back button
		Button btnBack = new Button("Back");
		btnBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		
		//add back button
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.insets = new Insets(0,0,5,5);
		gbc_btnBack.gridx = 0;
		gbc_btnBack.gridy = 6;
		add(btnBack,gbc_btnBack);
		
		//create play button
		Button btnPlay = new Button("Play");
		btnPlay.setTextColor(new Color(1,99,19));//dark green
		btnPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				songPlayer.stop();
				dispose();
				GameplayGUI.create(song,slider.getValue());
			}
		});
		
		//add play button
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.insets = new Insets(0,0,5,5);
		gbc_btnPlay.gridx = 1;
		gbc_btnPlay.gridy = 6;
		add(btnPlay,gbc_btnPlay);
		
		revalidate();
		repaint();
		setVisible(true);
		
	}
	
	public void back(){
		dispose();
		songPlayer.stop();
		SongSelectGUI.setVisible();
	}
	
	/**
	 * finds the overall score based on all the different difficulties added together
	 * @return the overall score
	 */
	private String getOverallScore(){
		int total = 0;
		for(int i = 0; i < 21; i++){
			total += song.getScore()[i];
		}
		return String.format("%.2f%s",Math.round(total / 21.0) / 100.0,"%");
	}
	
}
