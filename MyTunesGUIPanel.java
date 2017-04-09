import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	/** The data representing the list of photos in the album (the "model") */
	private PlayList playList;

	/** The GUI representation of the list of photos in the album (the "view") */
	private JList<Song> uiSongList;

	private JLabel nowPlayingLabel;
	private JLabel nowPlayingSongNameLabel;  // Displays the image icon inside of the preview panel
	private JButton nextButton; // Selects the next image in the photoList
	private JButton prevButton; // Selects the previous image in the photoList */
	
	public MyTunesGUIPanel()
	{
		playList = new PlayList("New", "playlist.txt");
		// The entire frame will have a BorderLayout (we will be adding more to it
		// in the next lab).
		//setLayout(new BorderLayout());
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// All of your work in this lab will go in the left panel. 
		// The right panel will be added in the next lab.
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		//leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		this.add(leftPanel);
		this.add(rightPanel);
		
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setBackground(Style.ACCENT_COLOR);
		JPanel leftBottomPanel = new JPanel();
		
		leftPanel.add(leftTopPanel);
		leftPanel.add(leftBottomPanel);
		
		JPanel songControlPanel = new JPanel();
		songControlPanel.setLayout(new BoxLayout(songControlPanel, BoxLayout.Y_AXIS));
		songControlPanel.setAlignmentX(LEFT_ALIGNMENT);
		leftTopPanel.add(songControlPanel);
		
		nowPlayingLabel = new JLabel("Now playing");
		//nowPlayingLabel.setAlignmentX(LEFT_ALIGNMENT);
		nowPlayingSongNameLabel = new JLabel("Song Name");
		//nowPlayingLabel.setAlignmentX(LEFT_ALIGNMENT);
		//nowPlayingLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		songControlPanel.add(nowPlayingLabel);
		songControlPanel.add(nowPlayingSongNameLabel);
		
		
		// TODO: Instantiate the JList<Photo> photoList object (declared above) and
		//      set the list data to album.getPhotoArray().
		//      Set the selected index of the photoList to position 0 to select the
		//      first photo by default.
		uiSongList = new JList<Song>();
		uiSongList.setListData(playList.getSongArray());
		uiSongList.setFont(new Font("monospaced", Font.PLAIN, 14) );
		uiSongList.setSelectedIndex(0);
		uiSongList.setAl
		leftBottomPanel.add(uiSongList);

	}
}