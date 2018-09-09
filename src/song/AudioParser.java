package song;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class AudioParser{
	private static String defaultMessage = "N/A";//Default message if the metadata info isn't found
	
	/**
	 * gets the name of the song in the metadata
	 * @param fileLocation
	 * @return the song name
	 */
	public static String getName(String fileLocation){
		try{
			Metadata metadata = getMetadata(fileLocation);
			if(metadata.get("title") != null){
				return metadata.get("title");
			}
		}catch(Exception e){
		}
		String name = new File(fileLocation).getName();//get the name of the file
		return name.substring(0,name.length() - 4);//return the filename minus the .mp3 part by default
	}
	
	/**
	 * gets the album of the song in the metadata
	 * @param fileLocation
	 * @return the album name
	 */
	public static String getAlbum(String fileLocation){
		try{
			Metadata metadata = getMetadata(fileLocation);
			if(metadata.get("title") != null){
				return metadata.get("xmpDM:album");
			}
		}catch(Exception e){
		}
		return defaultMessage;
	}
	
	/**
	 * gets the artist of the song in the metadata
	 * @param fileLocation
	 * @return the artist name
	 */
	public static String getArtist(String fileLocation){
		try{
			Metadata metadata = getMetadata(fileLocation);
			if(metadata.get("title") != null){
				return metadata.get("xmpDM:artist");
			}
		}catch(Exception e){
		}
		return defaultMessage;
	}
	
	/**
	 * gets the metadata object of the song
	 * @param fileLocation
	 * @return the metadata object
	 * @throws Exception fileIO exception
	 */
	private static Metadata getMetadata(String fileLocation) throws Exception{
		InputStream input = new FileInputStream(new File(fileLocation));
		ContentHandler handler = new DefaultHandler();
		Metadata metadata = new Metadata();
		Parser parser = new Mp3Parser();
		ParseContext parseCtx = new ParseContext();
		parser.parse(input,handler,metadata,parseCtx);
		input.close();
		return metadata;
	}
}