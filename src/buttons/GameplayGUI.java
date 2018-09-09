package buttons;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import fileIO.SongInfo;
import gui.PauseScreenGUI;
import gui.ScoreGUI;
import gui.SongSelectGUI;
import guiComponents.Backable;
import guiComponents.EscapeListener;
import guiComponents.FontList;
import song.SongPlayer;
/**
 * Senior Project
 * Syncfonia Rythm Based Game
 * Slatery Period 5
 * @author Matias Scharager 
 */
public class GameplayGUI extends Thread implements Backable{
	private boolean isRunning = true;//the game is currently playing
	private Canvas canvas;//everything is being drawn on the canvas
	private BufferStrategy bufferStrategy;//screen buffering for smoother graphics
	private BufferedImage background;//the background of the graphic
	private Graphics2D graphics2D, graphics;//for painting the game
	private JFrame frame;//the frame it is being displayed on
	private static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();//gets the width of fullscreen
	private static int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();//gets the height of fullscreen
	
	private ButtonController buttonController;//the buttoncontroller containing the game data
	private Timer timer;//the timer to measure the amount of time the song has been playing
	private static int topBarThickness = (int)(.08 * height);//the top bar has the name and pause button
	private GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();//config for graphics
	private SongPlayer songPlayer;//instance of the currently playing song
	private SongInfo song;//information of the the chosen song
	private boolean stopped = false;//the game has stopped playing
	private int difficultyIndex;//the difficulty level of the song
	private static final int bottomBarHeight = 15;//the height of the scrolling indicator bar at the bottom
	
	private static GameplayGUI instance = null;//instance for Singleton style class
	
	public static int getBottombarheight(){
		return bottomBarHeight;
	}
	
	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}
	
	/**
	 * creates a GameplayGUI based on the song
	 * @param song the song information of the song to be played
	 */
	private GameplayGUI(SongInfo song){
		EscapeListener.set(null);//temporarily set to null while the song is being loaded
		this.song = song;//store song
		try{
			songPlayer = new SongPlayer(song.getFile());//create the SongPlayer
		}catch(Exception e){
			e.printStackTrace();
		}
		createFrame();//set up the GUI
	}
	
	/**
	 * Used when the buttonController was already previously generated, but the GameplayGUI thread finished playing
	 * Since threads can't be restarted, it's easier to just make a new one with the same settings
	 * @param buttonController the buttonController syncronized to the song
	 * @param song the song information
	 */
	public GameplayGUI(ButtonController buttonController, SongInfo song){
		this(song);
		this.buttonController = buttonController;
	}
	
	/**
	 * 
	 * @return the current instance of the Singleton style class
	 */
	public static GameplayGUI getInstance(){
		return instance;
	}
	
	public void createFrame(){
		// JFrame
		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setSize(width,height);
		//frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		
		// Canvas
		canvas = new Canvas(config);
		canvas.setSize(width,height);
		frame.add(canvas,0);
		
		// Background & Buffer
		background = config.createCompatibleImage(width,height,Transparency.OPAQUE);
		canvas.createBufferStrategy(2);//bufferStrategies allow for smoother loading of the canvas
		do{
			bufferStrategy = canvas.getBufferStrategy();
		}while(bufferStrategy == null);
	}
	
	// Screen and buffer stuff
	private Graphics2D getBuffer(){
		if(graphics == null){
			try{
				graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
			}catch(IllegalStateException e){
				return null;
			}
		}
		return graphics;
	}
	
	//method for switching the buffer canvas being displayed on the screen
	private boolean updateScreen(){
		graphics.dispose();
		graphics = null;
		try{
			bufferStrategy.show();
			Toolkit.getDefaultToolkit().sync();
			return(!bufferStrategy.contentsLost());
			
		}catch(Exception e){
			return true;
		}
	}
	
	/**
	 * executes the game, main thread
	 */
	public void run(){
		graphics2D = (Graphics2D)background.getGraphics();//get the graphics
		long fpsWait = (long)(1.0 / 30 * 1000);//frames per second wait time
		while(buttonController == null){//loading
			do{//paints the loading screen information on the screen
				Graphics2D bg = getBuffer();
				drawLoadingScreen(graphics2D);
				bg.drawImage(background,0,0,null);
				bg.dispose();
			}while(!updateScreen());
		}
		canvas.addMouseListener(new ButtonMouse(buttonController));//add mouse
		
		long countdownTimeStart = System.currentTimeMillis();//countdown before starting game
		while(System.currentTimeMillis() - countdownTimeStart < 4000){//4 second countdown on go
			do{//paint the countdown onto the screen
				Graphics2D bg = getBuffer();
				drawCountdownScreen(graphics2D,(int)(System.currentTimeMillis() - countdownTimeStart) / 1000);
				bg.drawImage(background,0,0,null);
				bg.dispose();
			}while(!updateScreen());
		}
		reset();//create timer and start playing song
		while(isRunning){
			long renderStart = System.nanoTime();//get current time
			do{
				Graphics2D bg = getBuffer();
				if(isRunning){
					drawGame(graphics2D);
					bg.drawImage(background,0,0,null);
					bg.dispose();
				}
			}while(!updateScreen());//wait for screen to be updated
			
			
			if(isRunning){
				updateGame();// main drawing method
				long renderTime = (System.nanoTime() - renderStart) / 1000000;
				try{//wait until the rendered time reaches the frames per second wait time for balanced loading of the graphics
					Thread.sleep(Math.max(0,fpsWait - renderTime));
				}catch(InterruptedException e){
					Thread.interrupted();
					isRunning = false;
				}
				renderTime = (System.nanoTime() - renderStart) / 1000000;//reset the render time
				isRunning = songPlayer.isPlaying();//check if the song is still playing
			}
		}
		if(!stopped){//the game was not stopped by the pause menu, so the score is displayed
			frame.setVisible(false);//hide it
			frame.setEnabled(false);
			new ScoreGUI(buttonController.getScore(),song,difficultyIndex);//display score
		}
		stopped = false;
	}
	
	private void drawCountdownScreen(Graphics2D g, int time){//sets the text of the 3 second countdown
		g.setColor(Color.WHITE);//white background
		g.fillRect(0,0,width,height);
		
		g.setColor(Color.BLACK);//black text
		String text = "";
		switch(time){//intentionally no breaks because each case is additive to the other ones
			case 4:
			case 3:
				text = " 1..." + text;
			case 2:
				text = " 2..." + text;
			case 1:
				text = " 3..." + text;
		}
		text = "Ready?" + text;
		drawCenteredString(g,text,FontList.gamplayFont);//paint it to the canvas
	}
	
	private void drawLoadingScreen(Graphics2D g){//draws the loading screen while there is no button controller
		g.setColor(Color.WHITE);//white background
		g.fillRect(0,0,width,height);
		
		g.setColor(Color.BLACK);//black text
		drawCenteredString(g,SongPlayer.getLoadingText(),FontList.gamplayFont);//paint the loading message
	}
	
	public void drawCenteredString(Graphics2D g, String text, Font font){//draws a string centered on the screen by measuring its font size
		FontMetrics metrics = g.getFontMetrics(font);
		int x = (width - metrics.stringWidth(text)) / 2;//the top left point x coordinate
		int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();//the top left point y coordinate
		g.setFont(font);
		g.drawString(text,x,y);//draw it
	}
	
	public void updateGame(){
		//send updated time to update static variable in the Button class
		Button.updateTime(timer.getCurrentTime());
	}
	
	public void drawGame(Graphics2D g){
		//black background
		g.setColor(Color.BLACK);
		g.fillRect(0,0,width,height);
		
		//draw buttons
		buttonController.drawButtons(g);
		
		//draw score
		g.setFont(FontList.subTitle);
		g.setColor(Color.WHITE);
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String score = buttonController.runningScore();
		g.drawString(score,width - metrics.stringWidth(score),topBarThickness + metrics.getMaxAscent());
				
		//Top bar
		g.setColor(Color.GRAY);
		g.fillRect(0,0,width,topBarThickness);
		
		//Song name
		g.setFont(FontList.gamplayFont);
		g.setColor(Color.BLACK);
		g.drawString(song.getName(),0,(int)(topBarThickness * .75));
		
		//pause button
		g.setColor(Color.RED);
		g.fillRect(width - topBarThickness,0,width,topBarThickness);
		
		//draw bottom bar
		g.setColor(Color.WHITE);
		g.fillRect(0,height - bottomBarHeight,(int)Math.round(timer.getCurrentTime() * width * 1.0 / SongPlayer.getLengthOfSong()),height);
	}
	
	/**
	 * pauses the game and displays Pause Screen
	 */
	public void pause(){
		timer.pause();
		songPlayer.pause();
		frame.setEnabled(false);
		new PauseScreenGUI();
	}
	
	public static int getTopBarThickness(){
		return topBarThickness;
	}
	
	/**
	 * resets the game
	 * if the thread finished playing, restarts by creating a new identical thread
	 */
	public void reset(){
		if(!this.isAlive()){//thread finished playing
			//create new instance with identical values
			instance = new GameplayGUI(buttonController,song);
			instance.start();
		}else{
			//reset things that have to be reset
			buttonController.reset();
			songPlayer.stop();
			timer = new Timer();
			songPlayer.play();
			frame.setEnabled(true);
		}
		EscapeListener.set(instance);//escapeListener is the same in either case
	}
	
	/**
	 * an unpause method
	 */
	public void continuePlaying(){
		EscapeListener.set(this);
		frame.setEnabled(true);
		timer.resume();
		songPlayer.resume();
	}
	
	/**
	 * exits from the game to the menu
	 */
	public void goToMenu(){
		isRunning = false;
		songPlayer.stop();
		stopped = true;
		frame.dispose();
		SongSelectGUI.setVisible();
	}
	
	/**
	 * creates a new instance of GameplayGUI and runs it
	 * @param song the song information
	 * @param difficulty the difficulty setting
	 */
	public static void create(SongInfo song, int difficulty){
		instance = new GameplayGUI(song);
		instance.calculateDifficultyIndex(difficulty);
		instance.start();
		try{
			SongPlayer analyzer = new SongPlayer(song.getFile());
			instance.setBeats(analyzer.extractBeatsTimes(-25 * difficulty + 5000));//analyze depending on the difficulty
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * conversion between the difficulty (time in milliseconds of delay in between beats)
	 * and difficulty index (numerical difficulty indicator)
	 * @param difficulty
	 */
	private void calculateDifficultyIndex(int difficulty){
		this.difficultyIndex = difficulty * 2 / 10;
	}
	
	/**
	 * creates a button controller based on the time of the beats
	 * @param times the time in milliseconds of each beat
	 */
	public void setBeats(long[] times){
		buttonController = new ButtonController(times);
	}
	
	/**
	 * the escape key causes the pause menu to pop up
	 */
	@Override
	public void back(){
		pause();
	}
}