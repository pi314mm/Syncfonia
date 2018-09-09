package fileIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class SongFileHandler{
	private static String dataDirectory = "data", songInfoFileName = "songs.txt";//location of the file
	private static HashMap<String, SongInfo> loadedSongs = new HashMap<String, SongInfo>();//instance of the songs in the file
	private static final String separator = "" + (char)0;//text file delimiter
	static{//initial reading of file
		File infoFile = new File(dataDirectory + "\\" + songInfoFileName);
		if(infoFile.exists()){//if it exits
			try{//read it
				Scanner scan = new Scanner(infoFile);
				while(scan.hasNext()){
					parseSong(scan.nextLine());//send each line to parseSong
				}
				scan.close();
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}else{//if it doesn't exist, create a new file
			File directory = new File(dataDirectory);
			if(!directory.exists()){
				directory.mkdirs();//create directory if it doesn't exist
			}
			try{
				infoFile.createNewFile();//create file
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * removes the specified song from the file and stored values
	 * @param file the filename of the song to remove
	 */
	public static void remove(String file){
		loadedSongs.remove(file);//remove from loaded songs
		SongScoreHandler.remove(file);//remove from score file
		refreshFile();//refresh the file
	}
	
	/**
	 * 
	 * @return a list of all the songs
	 */
	public static Collection<SongInfo> getSongs(){
		return loadedSongs.values();
	}
	
	/**
	 * reads in a line from the input file
	 * @param line the line to be analyzed
	 */
	private static void parseSong(String line){
		String[] a = line.split(separator);//split by the separator
		if(a.length < 4){//if it is too short, make it bigger so it doesn't throw an error
			String[] temp = new String[4];
			Arrays.fill(temp,"N/A");
			for(int i = 0; i < a.length; i++){
				temp[i] = a[i];
			}
			a = temp;
		}
		loadedSongs.put(a[0],new SongInfo(a[0],a[1],a[2],a[3]));//add it to the loaded songs
	}
	
	/**
	 * adds a song by the song filename
	 * @param absolutePath the file of the song to add
	 * @return true if it was added, false if it was already in it and not added
	 */
	public static boolean add(String absolutePath){
		if(loadedSongs.containsKey(absolutePath)){//if there is an instance of the song already
			return false;//don't add it and return false
		}
		SongInfo song = new SongInfo(absolutePath);//create new SongInfo object
		loadedSongs.put(absolutePath,song);//add it to the loaded songs
		try{
			//append to file instead of editing the whole thing
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(dataDirectory + "\\" + songInfoFileName,true)));
			out.println(song.getFile() + separator + song.getName() + separator + song.getArtist() + separator + song.getAlbum());
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * returns the song by the filename of the song
	 * @param absolutePath the filename of the song
	 * @return the SongInfo of the song
	 */
	public static SongInfo getSong(String absolutePath){
		return loadedSongs.get(absolutePath);
	}
	
	/**
	 * rewrites file to match the information stored in the loaded songs
	 */
	private static void refreshFile(){
		try{
			BufferedWriter infoFile = new BufferedWriter(new FileWriter(dataDirectory + "\\" + songInfoFileName));
			PrintWriter out = new PrintWriter(infoFile);
			for(SongInfo song:loadedSongs.values()){
				out.println(song.getFile() + separator + song.getName() + separator + song.getArtist() + separator + song.getAlbum());
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * changes information in a particular song
	 * @param file the original filename of the song to change
	 * @param songInfo the updated information of the song
	 */
	public static void edit(String file, SongInfo songInfo){
		loadedSongs.remove(file);//remove it
		loadedSongs.put(songInfo.getFile(),songInfo);//add a new one
		SongScoreHandler.edit(file,songInfo);//transfer over the score
		refreshFile();//refresh the file
	}
}
