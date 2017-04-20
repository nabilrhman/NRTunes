import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import java.net.URL;
/**
 * The <code>Song</code> class represents a song. Each song
 * has a title, artist, play time, and file path.
 *
 * Here is an example of how a song can be created.
 * <pre>
 *     Song song = new Song("Amsterdam", "Paul Oakenfold", 318, "sounds/Amsterdam.mp3");
 * </pre>
 *
 * Here is an example of how a song can be used.
 * <pre>
 *     System.out.println("Artist: " + song.getArtist());
 *     System.out.println(song);
 * </pre>
 *
 * @author CS121 Instructors 
 * @author Nabil Rahman
 */
public class Song
{
	// Used to play the song.
	private AudioClip clip;

	private String title;
	private String artist;
	private int playTime; // in seconds
	private String filePath;
	private int playCount;

	/**
	 * Constructor: Builds a song using the given parameters.
	 * @param title song's title
	 * @param artist song's artist
	 * @param playTime song's length in seconds
	 * @param filePath song file to load
	 */
	public Song(String title, String artist, int playTime, String filePath)
	{
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.filePath = filePath;
		this.playCount = 0;

		String fullPath = new File(filePath).getAbsolutePath();
		try 
		{
			this.clip = Applet.newAudioClip(new URL("file:" + fullPath));
		} catch(Exception e) 
		{
			System.out.println("Error loading sound clip for " + fullPath);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns the title of this <code>Song</code>.
	 * @return The title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Title name mutator.
	 * @param Title The title of the song
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the artist of this <code>Song</code>.
	 * @return The artist
	 */
	public String getArtist()
	{
		return artist;
	}
	
	/**
	 * Artist name mutator.
	 * @param artist The artist of the song
	 */
	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	/**
	 * Returns the play time of this <code>Song</code> in seconds.
	 * @return The playTime
	 */
	public int getPlayTime()
	{
		return playTime;
	}
	
	/**
	 * Play time mutator.
	 * @param playTime The play time of the song
	 */
	public void setPlayTime(int playTime)
	{
		this.playTime = playTime;
	}
	
	/**
	 * Returns the file path of this <code>Song</code>.
	 * @return The filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}
	
	/**
	 * File path mutator.
	 * @param filePath The file path of the song
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	/**
	 * Returns The number of times this song has been played.
	 * @return The count 
	 */
	public int getPlayCount()
	{
		return playCount;
	}
	
	

	/**
	 * Plays this song asynchronously.
	 */
	public void play()
	{
		if(clip != null) 
		{
			clip.play();
			playCount++;
		}
	}

	/**
	 * Stops this song from playing.
	 */
	public void stop()
	{
		if(clip != null) 
		{
			clip.stop();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.format(" " + "%-20s %-20s %-25s %10d" + " ",
				title, artist, filePath, playTime);
	}
}
