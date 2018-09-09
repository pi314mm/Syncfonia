package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import buttons.GameplayGUI;
import fileIO.SongInfo;
import fileIO.SongScoreHandler;
import guiComponents.Backable;
import guiComponents.Button;
import guiComponents.EscapeListener;
import guiComponents.FontList;
import guiComponents.ScoreGraph;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class ScoreGUI extends JFrame implements Backable{
	private static final long serialVersionUID = 1L;//default for extending JFrame
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//screen width
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//screen height
	private int maxScore, points;//the max score and the total amount of points earned
	
	public ScoreGUI(int[] scores, SongInfo song, int difficulty){
		EscapeListener.set(this);//this is the current frame
		
		//calculate score
		maxScore = 0;
		points = 0;
		for(int i = 0; i < scores.length; i++){
			maxScore += scores[i] * 4;
			points += scores[i] * Math.abs(i - 3);
		}
		points = maxScore - points;
		
		//find score percentage
		double percentage = Math.round(points * 10000.00 / maxScore) / 100.00;
		
		//save the max score
		SongScoreHandler.saveScore(song.getFile(),difficulty,Math.max((int)(percentage * 100),song.getScore()[difficulty]));
		
		//make full screen
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		//setAlwaysOnTop(true);
		setVisible(true);
		
		//create grid bag layout
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {width / 2, width / 2, 0};
		gbl_contentPane.rowHeights = new int[] {height / 2, height / 4, height / 4, 0};
		getContentPane().setLayout(gbl_contentPane);
		
		//create a score graph to display score distribution
		JPanel panel = new ScoreGraph(scores,300);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		gbc_panel.gridwidth = 2;
		getContentPane().add(panel,gbc_panel);
		
		//create score label
		JLabel lblScore = new JLabel(String.format("Score: %d/%d = %.2f%s",points,maxScore,percentage,"%"));
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setFont(FontList.subTitle);
		GridBagConstraints gbc_lblScore = new GridBagConstraints();
		gbc_lblScore.fill = GridBagConstraints.BOTH;
		gbc_lblScore.gridx = 0;
		gbc_lblScore.gridy = 1;
		gbc_lblScore.gridwidth = 2;
		getContentPane().add(lblScore,gbc_lblScore);
		
		//make a retry button
		Button btnRetry = new Button("Retry");
		btnRetry.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				GameplayGUI.getInstance().reset();
				dispose();
			}
		});
		btnRetry.setTextColor(Color.RED);
		GridBagConstraints gbc_btnRetry = new GridBagConstraints();
		gbc_btnRetry.fill = GridBagConstraints.BOTH;
		gbc_btnRetry.gridx = 0;
		gbc_btnRetry.gridy = 2;
		getContentPane().add(btnRetry,gbc_btnRetry);
		
		//make an exit button
		Button btnSongMenu = new Button("Song Menu");
		btnSongMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				back();
			}
		});
		btnSongMenu.setTextColor(Color.GREEN);
		GridBagConstraints gbc_btnSongMenu = new GridBagConstraints();
		gbc_btnSongMenu.fill = GridBagConstraints.BOTH;
		gbc_btnSongMenu.gridx = 1;
		gbc_btnSongMenu.gridy = 2;
		getContentPane().add(btnSongMenu,gbc_btnSongMenu);
		
		revalidate();
		repaint();
	}
	
	@Override
	public void back(){//goes back to menu
		SongSelectGUI.setVisible();
		dispose();
	}
	
}
