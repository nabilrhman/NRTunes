import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
	private JLabel nowPlayingTitleLabel; 
	private JLabel nowPlayingArtistLabel;
	
	private JPanel leftPanel;
	
	private JPanel songInfoPanel;
	private JPanel playerControlPanel;
	
	private JPanel songInfoPanelWrapper;
	
	private JButton playButton;
	private JButton pauseButton;
	private JButton nextButton;
	private JButton previousButton;
	
	private JPanel musicSquarePanel;
	
	public MyTunesGUIPanel()
	{
		playList = new PlayList("New", "playlist.txt");
		uiSongList = new JList();
		uiSongList.setListData(playList.getSongArray());
		uiSongList.setFont(Style.LIST_FONT);
		uiSongList.setForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		uiSongList.setSelectionBackground(Style.SECONDARY_BACKBROUND_COLOR);
		uiSongList.setSelectionForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setFixedCellHeight(Style.LIST_ITEM_HEIGHT);
		
		JScrollPane scrollPane = new JScrollPane(uiSongList,
				 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// The entire frame will have a BorderLayout (we will be adding more to it
		// in the next lab).
		this.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		this.setLayout(new BorderLayout());
		
		songInfoPanel = new JPanel();
		
		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
				
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing");
		nowPlayingLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		
		nowPlayingTitleLabel = new JLabel("Song Title");
		nowPlayingTitleLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		nowPlayingArtistLabel = new JLabel("Song Artist");
		nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setMaximumSize(Style.SONG_INFO_PANEL_DIMENSION);
		leftPanel.add(songInfoPanel);
		leftPanel.add(scrollPane);
		
		musicSquarePanel =  new JPanel();
		musicSquarePanel.setLayout(new GridLayout(4, 4));
		nextButton = new JButton();
		nextButton.setMinimumSize(Style.MUSIC_SQUARE_DIMENSION);
		musicSquarePanel.add(nextButton);
		musicSquarePanel.add(nextButton);
		
		this.add(leftPanel, BorderLayout.CENTER);
		this.add(musicSquarePanel, BorderLayout.EAST);
		//this.add(scrollPane, BorderLayout.PAGE_END);
		
		
		
		
		
		
	}
}