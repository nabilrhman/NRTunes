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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

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
	private JButton[][] musicSquareButtons;
	
	public MyTunesGUIPanel()
	{	
		
		//UIManager.put(this.getBackground(), Style.SECONDARY_BACKBROUND_COLOR);
		this.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		this.setLayout(new BorderLayout());
		
		playList = new PlayList("New", "playlist.txt");
		uiSongList = new JList();
		uiSongList.setListData(playList.getSongArray());
		uiSongList.setFont(Style.LIST_FONT);
		uiSongList.setForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		uiSongList.setSelectionBackground(Style.SECONDARY_BACKBROUND_COLOR);
		uiSongList.setSelectionForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setFixedCellHeight(Style.LIST_ITEM_HEIGHT);
		
		JScrollPane playlistScrollPane = new JScrollPane(uiSongList,
				 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JScrollBar scrollBar = playlistScrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(10, 0));
		
		//playlistScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		scrollBar.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		
		songInfoPanel = new JPanel();
		
		//songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		//songInfoPanel.setLayout();		
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing");
		nowPlayingLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		
		nowPlayingTitleLabel = new JLabel("SONG TITLE");
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);
		nowPlayingTitleLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		nowPlayingArtistLabel = new JLabel("Song Artist");
		nowPlayingArtistLabel.setBackground(Style.ACCENT_COLOR);
		nowPlayingArtistLabel.setOpaque(true);
		
		nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		
		musicSquarePanel =  new JPanel();
		musicSquarePanel.setLayout(new GridLayout(4, 4));
		nextButton = new JButton();
		nextButton.setMinimumSize(Style.MUSIC_SQUARE_DIMENSION);
		musicSquarePanel.add(nextButton);
		musicSquarePanel.add(nextButton);
		
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		//leftPanel.setMaximumSize(Style.SONG_INFO_PANEL_DIMENSION);
		//leftPanel.add(songInfoPanel);
		leftPanel.add(songInfoPanel);
		leftPanel.add(playlistScrollPane);
		
		this.add(leftPanel, BorderLayout.CENTER);
		
		
		//GRID
		
		musicSquareButtons = new JButton[playList.getSongSquare().length][playList.getSongSquare().length];
		for (int row = 0; row < musicSquareButtons.length; row++)
		{
			for(int col = 0; col < musicSquareButtons[row].length; col++)
			{
				musicSquareButtons[row][col] = new JButton();
				//musicSquareButtons[row][col].addActionListener(new PhotoSquareListener());
				try 
				{
					musicSquareButtons[row][col].setText(playList.getSongSquare()[row][col].getTitle());
				} catch (Exception ex) 
				{
					//musicSquareButtons[row][col].setIcon(null);
				}
			}
		}
		
		JPanel musicSquarePanel = new JPanel();
		musicSquarePanel.setLayout(new GridLayout(playList.getSongSquare().length, playList.getSongSquare().length));
		
		for (int row = 0; row < musicSquareButtons.length; row++)
		{
			for(int col = 0; col < musicSquareButtons[row].length; col++)
			{
				musicSquarePanel.add(musicSquareButtons[row][col]);
			}
		}
		
		this.add(musicSquarePanel, BorderLayout.EAST);
		//.............................
		
		//scrollBar.setForeground(Style.SECONDARY_BACKBROUND_COLOR);
		
		// The entire frame will have a BorderLayout (we will be adding more to it
		// in the next lab).
		
		//GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		/*
		songInfoPanel = new JPanel();
		
		//songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		songInfoPanel.setLayout(new MigLayout("insets 0"));		
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing");
		nowPlayingLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		
		nowPlayingTitleLabel = new JLabel("SONG TITLE");
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);
		nowPlayingTitleLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		nowPlayingArtistLabel = new JLabel("Song Artist");
		nowPlayingArtistLabel.setBackground(Style.ACCENT_COLOR);
		nowPlayingArtistLabel.setOpaque(true);
		
		nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		
		
		
		musicSquarePanel =  new JPanel();
		musicSquarePanel.setLayout(new GridLayout(4, 4));
		nextButton = new JButton();
		nextButton.setMinimumSize(Style.MUSIC_SQUARE_DIMENSION);
		musicSquarePanel.add(nextButton);
		musicSquarePanel.add(nextButton);
		
		this.add(leftPanel);
		//this.add(musicSquarePanel, BorderLayout.EAST);
		
		*/
		/*
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		songInfoPanel = new JPanel();
		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing");
		nowPlayingLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		
		nowPlayingTitleLabel = new JLabel("SONG TITLE");
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);
		nowPlayingTitleLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		nowPlayingArtistLabel = new JLabel("Song Artist");
		nowPlayingArtistLabel.setBackground(Style.ACCENT_COLOR);
		nowPlayingArtistLabel.setOpaque(true);
		
		nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		
		this.add(songInfoPanel);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		this.add(scrollPane);
		
		
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		musicSquarePanel =  new JPanel();
		musicSquarePanel.setLayout(new GridLayout(4, 4));
		this.add(musicSquarePanel);	

		*/
		
	}
}