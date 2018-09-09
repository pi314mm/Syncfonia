package fileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class SongScoreHandler{
	private static String dataDirectory = "data", songInfoFileName = "scores.txt";//location of score data
	private static HashMap<String, int[]> loadedSongs = new HashMap<String, int[]>();
	private static final String separator = "" + (char)0;
	static{//initial reading of file
		File infoFile = new File(dataDirectory + "\\" + songInfoFileName);
		if(infoFile.exists()){//if the file exists
			try{
				Scanner scan = new Scanner(infoFile);
				while(scan.hasNext()){
					parseScore(scan.nextLine());//read in every line
				}
				scan.close();
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}else{//if it doesn't exist, create a new file
			File directory = new File(dataDirectory);
			if(!directory.exists()){//create the directory if it doesn't exist
				directory.mkdirs();
			}
			try{
				infoFile.createNewFile();//create the file
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	private static void parseScore(String line){
		String[] a = line.split(separator);//split by the separator
		int[] scores = new int[21];//store the score here
		try{
			for(int i = 1; i < a.length; i++){
				scores[i - 1] = Integer.parseInt(a[i]);//parse the integer values
			}
		}catch(Exception e){//the file was modified maliciously
			e.printStackTrace();
			scores = new int[21];//reset score
		}
		loadedSongs.put(a[0],scores);//add the score to the table
	}
	
	/**
	 * add a new song by the filename
	 * @param absolutePath the filename of the song
	 */
	public static void add(String absolutePath){
		loadedSongs.put(absolutePath,new int[21]);//add it to the loaded songs
		try{
			//append to file
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dataDirectory + "\\" + songInfoFileName,true)));
			out.print(absolutePath);
			for(int i = 0; i < 21; i++){
				out.print(separator + "0");
			}
			out.println();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static int[] getScore(String absolutePath){
		if(loadedSongs.get(absolutePath) == null){//if it isn't already in the loaded songs
			add(absolutePath);//add it
		}
		return loadedSongs.get(absolutePath);//return the value
	}
	
	/**
	 * edit the score of a song at a particular index
	 * @param file the filename of the song
	 * @param difficultyIndex the index of the difficulty setting
	 * @param score the score that it is being changed to
	 */
	public static void saveScore(String file, int difficultyIndex, int score){
		getScore(file)[difficultyIndex] = score;//change it
		refreshFile();//refresh the file
	}
	
	/**
	 * writes the contents of the loaded scores to the file
	 */
	private static void refreshFile(){
		try{
			BufferedWriter infoFile = new BufferedWriter(new FileWriter(dataDirectory + "\\" + songInfoFileName));
			PrintWriter out = new PrintWriter(infoFile);
			for(String file:loadedSongs.keySet()){
				out.print(file);
				int[] a = loadedSongs.get(file);
				for(int i = 0; i < 21; i++){
					out.print(separator + a[i]);
				}
				out.println();
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void remove(String file){
		loadedSongs.remove(file);//remove from loaded songs
		refreshFile();//refresh the file
	}
	
	/**
	 * changes the entire score array of a song
	 * @param file the filename of the song
	 * @param songInfo the song being changed
	 */
	public static void edit(String file, SongInfo songInfo){
		loadedSongs.put(file,songInfo.getScore());//replace the value
		refreshFile();//refresh the file
	}
}
