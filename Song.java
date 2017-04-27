import java.applet.AudioClip;
import java.applet.Applet;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * The <code>Song</code> class represents a song. Each song has a title, artist,
 * play time, and file path.
 *
 * Here is an example of how a song can be created.
 * 
 * <pre>
 * Song song = new Song("Amsterdam", "Paul Oakenfold", 318, "sounds/Amsterdam.mp3");
 * </pre>
 *
 * Here is an example of how a song can be used.
 * 
 * <pre>
 * System.out.println("Artist: " + song.getArtist());
 * System.out.println(song);
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

	private AdvancedPlayer mp3Player;
	private Thread mp3PlayerThread;
	private BufferedInputStream mp3BufferedInputStream;
	private File mp3File;

	/**
	 * Constructor: Builds a song using the given parameters.
	 * 
	 * @param title
	 *            song's title
	 * @param artist
	 *            song's artist
	 * @param playTime
	 *            song's length in seconds
	 * @param filePath
	 *            song file to load
	 */
	public Song(String title, String artist, int playTime, String filePath)
	{
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.filePath = filePath;
		this.playCount = 0;

		String fullPath = new File(filePath).getAbsolutePath();

		if (!getFileExtension(fullPath).equalsIgnoreCase("mp3"))
		{

			try
			{
				this.clip = Applet.newAudioClip(new URL("file:" + fullPath));
			}
			catch (Exception e)
			{
				System.out.println("Error loading sound clip for " + fullPath);
				System.out.println(e.getMessage());
			}
		}
		else if (getFileExtension(fullPath).equalsIgnoreCase("mp3"))
		{

			mp3File = new File(filePath);
		}

	}

	public Song(String title, String artist, int playTime, int playCount, String filePath)
	{
		this.title = title;
		this.artist = artist;
		this.playTime = playTime;
		this.filePath = filePath;
		this.playCount = playCount;

		String fullPath = new File(filePath).getAbsolutePath();

		if (!getFileExtension(fullPath).equalsIgnoreCase("mp3"))
		{

			try
			{
				this.clip = Applet.newAudioClip(new URL("file:" + fullPath));
			}
			catch (Exception e)
			{
				System.out.println("Error loading sound clip for " + fullPath);
				System.out.println(e.getMessage());
			}
		}
		else if (getFileExtension(fullPath).equalsIgnoreCase("mp3"))
		{

			mp3File = new File(filePath);
		}

	}

	/**
	 * Returns the title of this <code>Song</code>.
	 * 
	 * @return The title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Title name mutator.
	 * 
	 * @param Title
	 *            The title of the song
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Returns the artist of this <code>Song</code>.
	 * 
	 * @return The artist
	 */
	public String getArtist()
	{
		return artist;
	}

	/**
	 * Artist name mutator.
	 * 
	 * @param artist
	 *            The artist of the song
	 */
	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	/**
	 * Returns the play time of this <code>Song</code> in seconds.
	 * 
	 * @return The playTime
	 */
	public int getPlayTime()
	{
		return playTime;
	}

	/**
	 * Play time mutator.
	 * 
	 * @param playTime
	 *            The play time of the song
	 */
	public void setPlayTime(int playTime)
	{
		this.playTime = playTime;
	}

	/**
	 * Returns the file path of this <code>Song</code>.
	 * 
	 * @return The filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}

	/**
	 * File path mutator.
	 * 
	 * @param filePath
	 *            The file path of the song
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * Returns The number of times this song has been played.
	 * 
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
		if (!getFileExtension(filePath).equalsIgnoreCase("mp3") && clip != null)
		{
			clip.play();
			playCount++;
		}
		else if (getFileExtension(filePath).equalsIgnoreCase("mp3"))
		{
			try
			{
				try
				{
					FileInputStream fileInputStream = new FileInputStream(mp3File);
					mp3BufferedInputStream = new BufferedInputStream(fileInputStream);
					mp3Player = new AdvancedPlayer(mp3BufferedInputStream);
				}
				catch (JavaLayerException e)
				{
					System.out.println("Couldn't create mp3 player for the specified file.");
				}

				mp3PlayerThread = new Thread()
				{
					public void run()
					{
						try
						{
							mp3Player.play();
						}
						catch (JavaLayerException e)
						{
							e.printStackTrace();
						}
					}
				};
				mp3PlayerThread.start();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			playCount++;
		}
	}

	/**
	 * Stops this song from playing.
	 */
	public void stop()
	{
		if (!getFileExtension(filePath).equalsIgnoreCase("mp3") && clip != null)
		{
			clip.stop();
		}
		else if (mp3Player != null)
		{
			mp3Player.close();

		}
	}

	public void pause()
	{
		if (clip != null)
		{
			try
			{
				clip.wait();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String mTitle = title;
		String mArtist = artist;
		String mFilePath = filePath;

		if (title.length() > 20)
		{
			mTitle = title.substring(0, 20 - 3) + "...";
		}
		if (artist.length() > 20)
		{
			mArtist = artist.substring(0, 20 - 3) + "...";
		}
		if (filePath.length() > 30)
		{
			mFilePath = filePath.substring(0, 22 - 3) + "...";
		}

		return String.format(" " + "%-20s %-20s %-22s %9s" + " ", mTitle, mArtist, mFilePath,
				ConvertSecondToHHMMSSString(playTime));
	}

	/**
	 * Returns file extension given a file path.
	 * 
	 * @param filePath
	 *            the path of the file
	 * @return file extension
	 */
	public String getFileExtension(String filePath)
	{
		String extension = "";

		int i = filePath.lastIndexOf('.');
		if (i >= 0)
		{
			extension = filePath.substring(i + 1);
		}

		return extension.trim();
	}

	private String ConvertSecondToHHMMSSString(int nSecondTime)
	{
		String time;
		String format = String.format("%%0%dd", 2);

		long elapsedTime = nSecondTime;
		String seconds = String.format(format, elapsedTime % 60);
		String minutes = String.format(format, (elapsedTime % 3600) / 60);
		String hours = String.format(format, elapsedTime / 3600);

		if (elapsedTime / 3600 != 0)
			time = hours + ":" + minutes + ":" + seconds;
		else
			time = minutes + ":" + seconds;
		return time;
	}
}
