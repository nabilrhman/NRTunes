import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * This class manages songs in a simplified play list.
 * @author Nabil Rahman
 *
 */
public class PlayList 
{
	private String name;
	private Song playing;
	private ArrayList<Song> songList;
	
	/**
	 * Constructor: Instantiates a play list given its name. 
	 * @param name the name of the play list
	 */
	public PlayList(String name)
	{
		this.name = name;
		playing = null;
		songList = new ArrayList<Song>();
	}
	
	/**
	 * Play list name accessor.
	 * @return the name of the play list
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Play list name mutator.
	 * @param name the name of the play list
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Playing song accessor.
	 * @return the playing song
	 */
	public Song getPlaying()
	{
		return playing;
	}
	
	/**
	 * Playing song mutator.
	 * @param playing the playing song
	 */
	public void setPlaying(Song playing)
	{
		this.playing = playing;
	}
	
	/**
	 * Song list accessor.
	 * @return the song list
	 */
	public ArrayList<Song> getSongList()
	{
		return songList;
	}
	
	/**
	 * Song list mutator.
	 * @param songList the song list
	 */
	public void setSongList(ArrayList<Song> songList)
	{
		this.songList = songList;
	}
	
	/**
	 * Adds given song to the play list.
	 * @param song the song to be added
	 */
	public void addSong(Song song)
	{
		songList.add(song);
	}
	
	/**
	 * Removes song at the given index.
	 * @param index the index of the song to be removed
	 * @return removed song if the index is in bound. Otherwise, null.
	 */
	public Song removeSong(int index)
	{
		if(index >= 0 && index < songList.size())
		{
			Song removedSong = songList.get(index);
			songList.remove(index);
			return removedSong;
		}
		else
			return null;
	}
	
	/**
	 * Returns the total number of songs in the play list.
	 * @return the total number of songs in the play list
	 */
	public int getNumSongs()
	{
		return songList.size();
	}
	
	/**
	 * Returns the total play time of songs in the play list.
	 * @return the total play time of songs in the play list
	 */
	public int getTotalPlayTime()
	{
		int totalPlayTime = 0;
		for(Song song: songList)
		{
			totalPlayTime += song.getPlayTime();
		}
		return totalPlayTime;
	}
	
	/**
	 * Returns the song at the given index.
	 * @param index the index of the song
	 * @return the song at the index if index is in bound. Otherwise, null.
	 */
	public Song getSong(int index)
	{
		if(index >= 0 && index < songList.size())
			return songList.get(index);
		else
			return null;
	}
	
	/**
	 * Plays the song at the given index.
	 * @param index the index of the song
	 */
	public void playSong(int index)
	{
		if(getSong(index) != null)
		{
			playing = getSong(index);
			getSong(index).play();
		}
	}
	
	/**
	 * Returns the info of the play list.
	 * @return the info of the play list
	 */
	public String getInfo()
	{
		String info;
		DecimalFormat decimalFormatter = new DecimalFormat("0.00");
		
		if(getNumSongs() != 0)
		{
			double averagePlayTime = (double) getTotalPlayTime()/getNumSongs();
			averagePlayTime = (double) Math.round(averagePlayTime * 100) / 100;
	
			//Keeps the unsorted version of the song list
			ArrayList<Song> lastStateOfSongList = (ArrayList<Song>) songList.clone();
			
			//Sorts the play list
			Comparator<Song> playTimeComparator = new Comparator<Song>() 
			{
		        @Override
		        public int compare(Song song1, Song song2) 
		        {
		            return Integer.compare(song1.getPlayTime(), song2.getPlayTime());
		        }
			}; 
			Collections.sort(songList, playTimeComparator);
			
			info = "\nThe average play time is: " + decimalFormatter.format(averagePlayTime) + " seconds" + 
				   "\nThe shortest song is: " + getSong(0).toString() + 
				   "\nThe longest song is: " + getSong(getNumSongs()-1).toString() + 
				   "\nTotal play time: " + getTotalPlayTime() + " seconds";
					
			//Reverts back to the unsorted version of the song list
			songList = lastStateOfSongList;
			
			return info;
		}
		else 
			return "There are no songs.";
	}
	/**
	 * Searches for songs matching the query string and returns the search result as a list of songs.
	 * @param query query string
	 * @return the search result as a list of songs
	 */
	public ArrayList<Song> search(String query)
	{
		ArrayList<Song> result = new ArrayList<Song>();
		if(!query.isEmpty())
		{
			for(Song song: songList)
			{
				if(song.getTitle().contains(query) || song.getArtist().contains(query))
					result.add(song);
			}
		}
		return result;
	}
	
	/**
	 * Returns a string representation of the play list.
	 */
	@Override
	public String toString()
	{
		String playListInfo;
		
		playListInfo = "------------------" + 
					   "\n" + getName() + " (" + getNumSongs() +  " songs)" + "\n" + 
					   "------------------";
		
		if(getNumSongs() != 0)
		{
			for(Song song: songList)
			{
				playListInfo += "\n(" + songList.indexOf(song) + ") " + song.toString(); 
			}
		}
		else 
			playListInfo += "\nThere are no songs.";
		
		playListInfo += "\n------------------";
		
		return playListInfo;
	}
}
