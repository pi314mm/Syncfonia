package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
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
public class SongSelectGUI extends JFrame implements Backable{
	private static SongSelectGUI instance = new SongSelectGUI();
	private static final long serialVersionUID = 1L;
	private int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	//private JTextField txtSongInput;
	private JTable table;
	private DefaultTableModel tableModel;
	private String[] tableHeaders = {"File Location", "Song Name", "Artist", "Album"};
	private JLabel lblSelectedSong;
	private JFileChooser fileChooser;
	private String selectedSongLocation;
	private SongPlayer songPlayer;
	
	public static void setVisible(){
		EscapeListener.set(instance);
		instance.setVisible(true);
		instance.setEnabled(true);
	}
	
	private SongSelectGUI(){
		//Create a file chooser
		LookAndFeel previousLF = UIManager.getLookAndFeel();
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			fileChooser = new JFileChooser();
			UIManager.setLookAndFeel(previousLF);
		}catch(Exception e){
			e.printStackTrace();
		}
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		//make full screen
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setSize(width,height);
		//setAlwaysOnTop(true);
		setVisible(true);
		
		//create grid bag layout
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(184,209,249));//light blue
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {width / 5, (int)(width * 3.0 / 5), width / 5, 0};
		gbl_contentPane.rowHeights = new int[] {height / 8, height / 8, height / 2, height / 4, 0};
		gbl_contentPane.columnWeights = new double[] {0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_contentPane);
		
		//create main title
		JLabel lblTitle = new JLabel("Select Your Song");
		lblTitle.setFont(FontList.italics);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(FontList.titleColor);
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 3;
		gbc_lblTitle.insets = new Insets(0,0,5,5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		add(lblTitle,gbc_lblTitle);
		
		//create edit button for editing songs
		Button btnEdit = new Button("Edit");
		btnEdit.setTextColor(Color.DARK_GRAY);
		btnEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = table.getSelectedRow();
				if(row != -1){//if a row is selected
					new SongEditorGUI(table.getModel().getValueAt(row,0).toString());
				}
			}
		});
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0,0,5,5);
		gbc_btnEdit.gridx = 0;
		gbc_btnEdit.gridy = 1;
		add(btnEdit,gbc_btnEdit);
		
		//remove a song
		Button btnRemove = new Button("Remove");
		btnRemove.setTextColor(Color.RED);
		btnRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = table.getSelectedRow();
				if(row != -1){
					row = table.convertRowIndexToModel(row);//get the correct reference
					//safety check to make sure you actually want to remove the song and its saved score
					int reply = JOptionPane.showConfirmDialog(contentPane,"Are you sure you want to remove this song?","Remove?",JOptionPane.YES_NO_OPTION);
					if(reply == JOptionPane.YES_OPTION){
						SongFileHandler.remove(table.getModel().getValueAt(row,0).toString());
						tableModel.removeRow(row);
						setSelectedSong();
					}
				}
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0,0,5,5);
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 1;
		add(btnRemove,gbc_btnRemove);
		
		//button for uploading songs using the filechooser
		Button btnUploadSong = new Button("Upload");
		btnUploadSong.setTextColor(Color.DARK_GRAY);
		btnUploadSong.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int result = fileChooser.showOpenDialog(contentPane);
				if(result == JFileChooser.APPROVE_OPTION){
					File selectedFile = fileChooser.getSelectedFile();
					if(selectedFile.getAbsolutePath().endsWith(".mp3")){//is mp3 file
						addSongFromInput(selectedFile.getAbsolutePath());
					}else{
						JOptionPane.showMessageDialog(contentPane,"Error: the file selected is not an MP3 file","Not MP3",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		GridBagConstraints gbc_btnUploadSong = new GridBagConstraints();
		gbc_btnUploadSong.insets = new Insets(0,0,5,5);
		gbc_btnUploadSong.gridx = 2;
		gbc_btnUploadSong.gridy = 1;
		add(btnUploadSong,gbc_btnUploadSong);
		
		//table that holds all the songs
		table = new JTable(new DefaultTableModel(tableHeaders,0){
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column){
				//all cells false
				return false;
			}
		});
		
		//listen to selected row
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				setSelectedSong();
			}
		});
		
		tableModel = (DefaultTableModel)table.getModel();
		
		//set table sort
		table.setAutoCreateRowSorter(true);
		
		//scroll in case the table gets too long
		JScrollPane scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(600,600));
		
		//add the table
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0,30,5,30);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridy = 2;
		gbc_table.gridx = 0;
		gbc_table.gridwidth = 4;
		gbc_table.gridx = 0;
		gbc_table.gridy = 2;
		add(scrollPane,gbc_table);
		
		//back button to go back to main menu
		Button btnBack = new Button("Back");
		btnBack.setTextColor(Color.RED);
		btnBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				back();
			}
		});
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.insets = new Insets(0,0,5,5);
		gbc_btnBack.gridx = 0;
		gbc_btnBack.gridy = 3;
		add(btnBack,gbc_btnBack);
		
		//the name of the currently selected song
		lblSelectedSong = new JLabel(" ");
		lblSelectedSong.setFont(FontList.italics);
		lblSelectedSong.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblSelectedSong = new GridBagConstraints();
		gbc_lblSelectedSong.fill = GridBagConstraints.BOTH;
		gbc_lblSelectedSong.insets = new Insets(0,0,5,0);
		gbc_lblSelectedSong.gridx = 1;
		gbc_lblSelectedSong.gridy = 3;
		add(lblSelectedSong,gbc_lblSelectedSong);
		
		//choose the selected song to play
		Button btnSelect = new Button("Select");
		btnSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(selectedSongLocation != null){
					setVisible(false);
					setEnabled(false);
					new SongMenuGUI(selectedSongLocation,songPlayer);
				}
			}
		});
		btnSelect.setTextColor(new Color(1,99,19));//dark green
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0,0,5,5);
		gbc_btnSelect.gridx = 2;
		gbc_btnSelect.gridy = 3;
		add(btnSelect,gbc_btnSelect);
		
		//populates the table with the saved songs
		addSongsToTable();
		
		revalidate();
		repaint();
	}
	
	private void addSongFromInput(String absolutePath){
		//add to SongFileHandler
		if(SongFileHandler.add(absolutePath)){//if doesn't already exist
			//add to table
			addRowToTable(SongFileHandler.getSong(absolutePath));
		}
	}
	
	private void setSelectedSong(){
		int row = table.getSelectedRow();//get the selected row
		if(row != -1 && isValidSong(table.getModel().getValueAt(table.convertRowIndexToModel(row),0).toString())){//if it's a valid song
			row = table.convertRowIndexToModel(row);//get actual row
			lblSelectedSong.setText(table.getModel().getValueAt(row,1).toString());//change the selected song label
			selectedSongLocation = table.getModel().getValueAt(row,0).toString();//change the selected song location
			if(songPlayer != null){//if a song is playing
				songPlayer.stop();//stop playing the song
			}
			try{
				songPlayer = new SongPlayer(selectedSongLocation);//create a new song player
			}catch(Exception e){
				e.printStackTrace();
			}
			songPlayer.play();//play the song
		}else{//the song is invalid
			lblSelectedSong.setText("Song not found");
			selectedSongLocation = null;
			if(songPlayer != null){
				songPlayer.stop();
			}
		}
	}
	
	/**
	 * populates the table with the songs currently in the song file
	 */
	private void addSongsToTable(){
		Collection<SongInfo> songs = SongFileHandler.getSongs();//get list of all the songs
		for(SongInfo song:songs){
			addRowToTable(song);//add each song to the table
		}
	}
	
	/**
	 * clears the table and adds the songs back in
	 */
	public static void refresh(){
		instance.tableModel.setNumRows(0);//clear table
		instance.addSongsToTable();
	}
	
	/**
	 * checks if the song file exists, in case the game is being run in a different location
	 * @param file the file location of the song
	 * @return true if the song exists
	 */
	private boolean isValidSong(String file){
		return new File(file).exists();
	}
	
	/**
	 * go back to main menu
	 */
	public void back(){
		EscapeListener.set(new MainMenuGUI());
		setVisible(false);
		setEnabled(false);
		if(songPlayer != null){
			songPlayer.stop();
		}
	}
	
	/**
	 * adds a song to the table
	 * @param song the song to be added
	 */
	private void addRowToTable(SongInfo song){
		tableModel.addRow(new String[] {song.getFile(), song.getName(), song.getArtist(), song.getAlbum()});
	}
}
