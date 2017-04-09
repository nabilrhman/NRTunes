import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * This class manages songs in a simplified play list.
 * 
 * @author Nabil Rahman
 *
 */
public class PlayList implements MyTunesPlayListInterface
{
	private String name;
	private Song playing;
	private ArrayList<Song> songList;

	/**
	 * Constructor: Instantiates a play list given its name.
	 * 
	 * @param name
	 *            The name of the play list
	 */
	public PlayList(String name, String filePath)
	{

		this.name = name;
		playing = null;
		songList = new ArrayList<Song>();
		loadFromFile(new File(filePath));

	}

	/**
	 * Play list name accessor.
	 * 
	 * @return The name of the play list
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Play list name mutator.
	 * 
	 * @param name
	 *            The name of the play list
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Playing song accessor.
	 * 
	 * @return the playing song
	 */
	public Song getPlaying()
	{
		return playing;
	}

	/**
	 * Playing song mutator.
	 * 
	 * @param playing
	 *            The playing song
	 */
	public void setPlaying(Song playing)
	{
		this.playing = playing;
	}

	/**
	 * Song list accessor.
	 * 
	 * @return the song list
	 */
	public ArrayList<Song> getSongList()
	{
		return songList;
	}

	/**
	 * Song list mutator.
	 * 
	 * @param songList
	 *            The song list
	 */
	public void setSongList(ArrayList<Song> songList)
	{
		this.songList = songList;
	}

	/**
	 * Adds given song to the play list.
	 * 
	 * @param song
	 *            The song to be added
	 */
	public void addSong(Song song)
	{
		songList.add(song);
	}

	/**
	 * Removes song at the given index.
	 * 
	 * @param index
	 *            The index of the song to be removed
	 * @return removed song if the index is in bound. Otherwise, null.
	 */
	public Song removeSong(int index)
	{
		if (index >= 0 && index < songList.size())
		{
			Song removedSong = songList.get(index);
			songList.remove(index);
			return removedSong;
		} else
			return null;
	}

	/**
	 * Returns the total number of songs in the play list.
	 * 
	 * @return The total number of songs in the play list
	 */
	public int getNumSongs()
	{
		return songList.size();
	}

	/**
	 * Returns the total play time of songs in the play list.
	 * 
	 * @return The total play time of songs in the play list
	 */
	public int getTotalPlayTime()
	{
		int totalPlayTime = 0;
		for (Song song : songList)
		{
			totalPlayTime += song.getPlayTime();
		}
		return totalPlayTime;
	}

	/**
	 * Returns the song at the given index.
	 * 
	 * @param index
	 *            The index of the song
	 * @return the song at the index if index is in bound. Otherwise, null.
	 */
	public Song getSong(int index)
	{
		if (index >= 0 && index < songList.size())
			return songList.get(index);
		else
			return null;
	}

	/**
	 * Plays the song at the given index.
	 * 
	 * @param index
	 *            The index of the song
	 */
	public void playSong(int index)
	{
		if (getSong(index) != null)
		{
			playing = getSong(index);
			getSong(index).play();
		}
	}

	/**
	 * Returns the info of the play list.
	 * 
	 * @return The info of the play list
	 */
	public String getInfo()
	{
		String info;
		DecimalFormat decimalFormatter = new DecimalFormat("0.00");

		if (getNumSongs() != 0)
		{
			double averagePlayTime = (double) getTotalPlayTime() / getNumSongs();
			averagePlayTime = (double) Math.round(averagePlayTime * 100) / 100;

			// Keeps the unsorted version of the song list
			ArrayList<Song> lastStateOfSongList = (ArrayList<Song>) songList.clone();

			// Sorts the play list
			Comparator<Song> playTimeComparator = new Comparator<Song>()
			{
				@Override
				public int compare(Song song1, Song song2)
				{
					return Integer.compare(song1.getPlayTime(), song2.getPlayTime());
				}
			};
			Collections.sort(songList, playTimeComparator);

			info = "\nThe average play time is: " + decimalFormatter.format(averagePlayTime) + " seconds"
					+ "\nThe shortest song is: " + getSong(0).toString() + "\nThe longest song is: "
					+ getSong(getNumSongs() - 1).toString() + "\nTotal play time: " + getTotalPlayTime() + " seconds";

			// Reverts back to the unsorted version of the song list
			songList = lastStateOfSongList;

			return info;
		} else
			return "There are no songs.";
	}

	/**
	 * Searches for songs matching the query string and returns the search
	 * result as a list of songs.
	 * 
	 * @param query
	 *            query-string
	 * @return the search result as a list of songs
	 */
	public ArrayList<Song> search(String query)
	{
		ArrayList<Song> result = new ArrayList<Song>();
		if (!query.isEmpty())
		{
			for (Song song : songList)
			{
				if (song.getTitle().contains(query) || song.getArtist().contains(query))
					result.add(song);
			}
		}
		return result;
	}

	@Override
	public String toString()
	{
		String playListInfo;

		playListInfo = "------------------" + "\n" + getName() + " (" + getNumSongs() + " songs)" + "\n"
				+ "------------------";

		if (getNumSongs() != 0)
		{
			for (Song song : songList)
			{
				playListInfo += "\n(" + songList.indexOf(song) + ") " + song.toString();
			}
		} else
			playListInfo += "\nThere are no songs.";

		playListInfo += "\n------------------";

		return playListInfo;
	}

	@Override
	public void loadFromFile(File file)
	{
		if (file.exists())
		{
			try
			{
				Scanner scan = new Scanner(file);
				while (scan.hasNextLine())
				{
					String title = scan.nextLine().trim();
					
					String artist = scan.nextLine().trim();
					String playtime = scan.nextLine().trim();
					String songPath = scan.nextLine().trim();

					int colon = playtime.indexOf(':');
					int minutes = Integer.parseInt(playtime.substring(0, colon));
					int seconds = Integer.parseInt(playtime.substring(colon + 1));
					int playTime = (minutes * 60) + seconds;

					Song song = new Song(title, artist, playTime, songPath);
					
					songList.add(song);
				}
				scan.close();
			} catch (FileNotFoundException e)
			{
				System.err.println("Could not read the playlist file: " + e.getMessage());
			}
		} else
		{
			System.err.println("Playlist not found:: " + file);
		}

	}

	@Override
	public void playSong(Song song)
	{
		// TODO Auto-generated method stub
		if (songList.contains(song))
		{
			song.play();
		}

	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub
		playing.stop();

	}

	@Override
	public Song[] getSongArray()
	{
		// TODO Auto-generated method stub
		Song[] copy = new Song[songList.size()];

		for (int i = 0; i < copy.length; i++)
		{
			copy[i] = songList.get(i);
		}
		return copy;
	}

	@Override
	public int moveUp(int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveDown(int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Song[][] getSongSquare()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
