package fileIO;

import song.AudioParser;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class SongInfo{
	private String file, name, artist, album;
	private int[] score;
	
	public SongInfo(String file, String name, String artist, String album, int[] score){
		this.file = file;
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.score = score;
	}
	
	public SongInfo(String file, String name, String artist, String album){
		this(file,name,artist,album,SongScoreHandler.getScore(file));
	}
	
	public SongInfo(String file){
		this(file,AudioParser.getName(file),AudioParser.getArtist(file),AudioParser.getAlbum(file));
	}
	
	public String getFile(){
		return file;
	}
	
	public void setFile(String file){
		this.file = file;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public String getAlbum(){
		return album;
	}
	
	public void setAlbum(String album){
		this.album = album;
	}
	
	public int[] getScore(){
		return score;
	}
	
	public void setScore(int[] score){
		this.score = score;
	}
}
