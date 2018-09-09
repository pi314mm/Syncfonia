package song;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import buttons.Button;

/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class SongPlayer{
	private static SongPlayer currentlyPlayingSong;
	private AudioFormat targetFormat;
	private String fileName;
	private boolean isPlaying = false, stopped = false, paused = false;
	private Object syncObject = new Object();
	private static String loadingText = "Loading...";
	private static long lengthOfSong;
	
	public static String getLoadingText(){
		return loadingText;
	}
	
	public void play(){
		if(!isPlaying){
			if(currentlyPlayingSong != null && currentlyPlayingSong.isPlaying()){
				currentlyPlayingSong.stop();
			}
			new Thread(){
				@Override
				public void run(){
					try{
						read();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}.start();
			currentlyPlayingSong = this;
		}
	}
	
	public void stop(){
		if(isPlaying){
			stopped = true;
			if(paused){
				resume();
			}
			synchronized(syncObject){
				while(stopped){
					try{
						syncObject.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void resume(){
		if(isPlaying){
			paused = false;
			synchronized(syncObject){
				syncObject.notify();
			}
		}
	}
	
	public void pause(){
		if(isPlaying){
			paused = true;
		}
	}
	
	public SongPlayer(String fileName) throws UnsupportedAudioFileException, IOException{
		this.fileName = fileName;
		File file = new File(fileName);
		AudioInputStream in = AudioSystem.getAudioInputStream(file);
		AudioFormat baseFormat = in.getFormat();
		targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
	}
	
	private static int numSamples = 16;
	
	public long[] extractBeatsTimes(int timeTolerance) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
		long[] times = extractBeatsCounter(timeTolerance);
		loadingText = "Timing Beats...";
		for(int i = 0; i < times.length; i++){
			times[i] = counterToMillis(times[i]);
		}
		loadingText = "Loading...";
		return times;
	}
	
	private long[] extractBeatsCounter(int timeTolerance) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
		PriorityQueue<Beat> pq = analyze();
		long minTime = millisToCounter(Button.getMaxTime());//give a delay before buttons appear
		loadingText = "Extracting beats...";
		long[] times = new long[pq.size()];
		double[] values = new double[pq.size()];
		while(!pq.isEmpty()){
			Beat b = pq.poll();
			boolean found = false;
			if(b.time < minTime){
				found = true;
			}
			int index;
			for(index = 0; index < times.length && times[index] != 0 && !found; index++){
				if(Math.abs(b.time - times[index]) <= timeTolerance){
					if(b.value > values[index]){
						times[index] = b.time;
						values[index] = b.value;
					}
					found = true;
				}
			}
			if(!found){
				times[index] = b.time;
				values[index] = b.value;
			}
		}
		
		loadingText = "Organizing beats...";
		int index = times.length - 1;
		while(index >= 0 && times[index] == 0){
			index--;
		}
		long[] result = new long[index + 1];
		for(int i = 0; i < result.length; i++){
			result[i] = times[i];
		}
		Arrays.sort(result);
		return result;
	}
	
	private void read() throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		isPlaying = true;
		//open file
		File file = new File(fileName);
		AudioInputStream din = AudioSystem.getAudioInputStream(targetFormat,AudioSystem.getAudioInputStream(file));
		
		//read samples
		byte[] data = new byte[numSamples];
		SourceDataLine line = getLine(targetFormat);
		if(line != null){
			// Start
			line.start();
			int nBytesRead = 0;
			while(nBytesRead != -1 && !stopped){
				nBytesRead = din.read(data,0,numSamples);
				if(nBytesRead != -1){
					line.write(data,0,nBytesRead);
				}
				synchronized(syncObject){
					while(paused){
						try{
							syncObject.wait();
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
		if(stopped){
			stopped = false;
			synchronized(syncObject){
				syncObject.notify();
			}
		}
		isPlaying = false;
	}
	
	private class Beat implements Comparable<Beat>{
		public long time;
		public double value;
		
		public Beat(long time, double value){
			super();
			this.time = time;
			this.value = value;
		}
		
		@Override
		public int compareTo(Beat b){
			if(this.value > b.value){
				return -1;
			}
			if(this.value < b.value){
				return 1;
			}
			return 0;
		}
		
		@Override
		public String toString(){
			return this.time + ":" + this.value;
		}
		
	}
	
	private PriorityQueue<Beat> analyze() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
		loadingText = "Opening file...";
		File file = new File(fileName);
		AudioInputStream din = AudioSystem.getAudioInputStream(targetFormat,AudioSystem.getAudioInputStream(file));
		
		loadingText = "Initializing arrays...";
		byte[] data = new byte[numSamples];
		long counter = 1;
		FFT fft = new FFT(numSamples);
		SourceDataLine line = getLine(targetFormat);
		
		double[] previous = null, previous2 = null;
		PriorityQueue<Beat> pq = new PriorityQueue<Beat>();
		
		loadingText = "Reading song...";
		if(line != null){
			// Start
			line.start();
			int nBytesRead = 0;
			while(nBytesRead != -1){
				nBytesRead = din.read(data,0,numSamples);
				if(nBytesRead != -1){
					double[] re = new double[numSamples];
					for(int i = 0; i < numSamples; i++){
						re[i] = data[i];
					}
					double[] im = new double[numSamples];
					fft.fft(re,im);
					double[] abs = new double[numSamples];
					double max = 0;
					for(int i = 0; i <= numSamples / 4; i++){//only sample from lower frequencies
						abs[i] = Math.sqrt(re[i] * re[i] + im[i] * im[i]);
						if(previous != null && previous2 != null){//make sure they are intialized
							if(previous[i] > previous2[i] && previous[i] > abs[i]){//if relative max
								max = Math.max(max,previous[i]);
							}
						}
					}
					previous2 = previous;
					previous = abs;
					if(max > 0){
						pq.add(new Beat(counter - 1,max));
					}
					counter++;
				}
			}
			lengthOfSong = counterToMillis(counter);
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
		return pq;
	}
	
	public static long getLengthOfSong(){
		return lengthOfSong;
	}
	
	private long counterToMillis(long counter){
		return (long)(counter * numSamples * 8000 / (targetFormat.getSampleRate() * targetFormat.getChannels() * targetFormat.getSampleSizeInBits()));
	}
	
	private long millisToCounter(long time){
		return (long)((time * targetFormat.getSampleRate() * targetFormat.getChannels() * targetFormat.getSampleSizeInBits()) / (numSamples * 8000));
	}
	
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException{
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat);
		res = (SourceDataLine)AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}
	
	public boolean isPlaying(){
		return isPlaying;
	}
}