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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	/** The data representing the list of photos in the album (the "model") */
	private PlayList playList;
	private Song[] songs;
	private File songFile;

	/** The GUI representation of the list of photos in the album (the "view") */
	private JList<Song> uiSongList;

	private JLabel nowPlayingLabel;
	private JLabel nowPlayingTitleLabel; 
	private JLabel nowPlayingArtistLabel;
	private JLabel nowPlayingTimerLabel;
	
	private JLabel playlistNameLabel;
	private JLabel totalPlaytimeLabel;
	
	private JLabel songFilePath;
	
	private JPanel leftPanel;
	
	private JPanel songInfoPanel;
	private JPanel playerControlPanel;
	
	private JPanel songInfoPanelWrapper;
	
	private JButton playButton;
	private JButton stopButton;
	private JButton nextButton;
	private JButton previousButton;
	
	private JButton addButton;
	private JButton removeButton;
	private JButton moveUpButton;
	private JButton moveDownButton;
	
	private JPanel musicSquarePanel;
	private JButton[][] musicSquareButtons;
	
	private Object defaultPanelBackground;
	private Object defaultPanelForeground;
	private Object defaultButtonBackground;
	private Object defaultButtonForeground;
	private Object defaultButtonSelect;
	private Object defaultButtonFocus;
	private Object defaultButtonBorder;
	private Object defaultLabelForeground;
	
	public MyTunesGUIPanel()
	{	
		defaultPanelBackground = UIManager.get("Panel.background");
		defaultPanelForeground = UIManager.get("Panel.foreground");
		defaultButtonBackground = UIManager.get("Button.background");
		defaultButtonForeground = UIManager.get("Button.foreground");
		defaultButtonSelect = UIManager.get("Button.select");
		defaultButtonFocus = UIManager.get("Button.focus");
		defaultButtonBorder = UIManager.get("Button.border");
		defaultLabelForeground = UIManager.get("Label.foreground");
		
		setModifiedUIStyle();	
		this.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		this.setLayout(new BorderLayout());
		
		playList = new PlayList("New Playlist", "playlist.txt");
		songs = playList.getSongArray();
		uiSongList = new JList();
		uiSongList.setListData(playList.getSongArray());
		uiSongList.addListSelectionListener(new UiSongListSelectionListener());
		uiSongList.setSelectedIndex(0);
		uiSongList.setFont(Style.LIST_FONT);
		uiSongList.setForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		uiSongList.setSelectionBackground(Style.SECONDARY_BACKBROUND_COLOR);
		uiSongList.setSelectionForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setFixedCellHeight(Style.LIST_ITEM_HEIGHT);
		
		JScrollPane playlistScrollPane = new JScrollPane(uiSongList,
				 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		/*
		JScrollBar scrollBar = playlistScrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(10, 0));
		
		//playlistScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		scrollBar.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		*/
		
		songInfoPanel = new JPanel();
		songInfoPanel.setBorder(Style.DEFAULT_BORDER);
		
		
		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		
		//songInfoPanel.setLayout(new GridLayout(1,1));
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing");
		nowPlayingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingLabel.setBorder(Style.DEFAULT_BORDER);
						
		nowPlayingTitleLabel = new JLabel("SONG TITLE .. .. .. . ... ");
		nowPlayingTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);
		
		nowPlayingArtistLabel = new JLabel("Song Artist");
		nowPlayingArtistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		nowPlayingTimerLabel = new JLabel("10:00");
		nowPlayingTimerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTimerLabel.setFont(Style.HEADING1_FONT);
		nowPlayingTimerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
				
		JPanel playerControlPanel = new JPanel();
		playerControlPanel.setBorder(Style.DEFAULT_BORDER);
		playerControlPanel.setBackground(Style.ACCENT_COLOR);
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");
		nextButton = new JButton("Next");
		previousButton = new JButton("Previous");
		playerControlPanel.add(playButton);
		playerControlPanel.add(stopButton);
		playerControlPanel.add(nextButton);
		playerControlPanel.add(previousButton);
			
		//nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		songInfoPanel.add(nowPlayingTimerLabel);
		songInfoPanel.add(playerControlPanel);
		
		JPanel playlistControlPanel = new JPanel();
		
		addButton = new JButton("Add");
		addButton.addActionListener(new AddButtonListener());
		removeButton = new JButton("Remove");
		moveUpButton = new JButton("Move Up");
		moveUpButton.addActionListener(new PlayerControlListener());
		moveDownButton = new JButton("Move Down");
		moveDownButton.addActionListener(new PlayerControlListener());
		playlistControlPanel.add(addButton);
		playlistControlPanel.add(removeButton);
		playlistControlPanel.add(moveUpButton);
		playlistControlPanel.add(moveDownButton);
		
		JPanel playlistInfoPanel = new JPanel();
		
		playlistNameLabel = new JLabel();
		playlistNameLabel.setText(playList.getName());
		
		totalPlaytimeLabel = new JLabel();
		totalPlaytimeLabel.setText("| Playtime: " + Integer.toString(playList.getTotalPlayTime()));
		playlistInfoPanel.add(playlistNameLabel);
		playlistInfoPanel.add(totalPlaytimeLabel);
		
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		//leftPanel.setMaximumSize(Style.SONG_INFO_PANEL_DIMENSION);
		//leftPanel.add(songInfoPanel);
		leftPanel.add(songInfoPanel);
		//leftPanel.add(playerControlPanel);
		leftPanel.add(playlistInfoPanel);
		leftPanel.add(playlistControlPanel);
		leftPanel.add(playlistScrollPane);
		
		this.add(leftPanel, BorderLayout.WEST);
		
		
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
		//musicSquarePanel.setLayout(new GridLayout(4, 5));
		
		for (int row = 0; row < musicSquareButtons.length; row++)
		{
			for(int col = 0; col < musicSquareButtons[row].length; col++)
			{
				musicSquarePanel.add(musicSquareButtons[row][col]);
			}
		}
				
		this.add(musicSquarePanel, BorderLayout.CENTER);
		
		
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
	
	public void setDefaultUIStyle()
	{
		UIManager.put("Panel.background", defaultPanelBackground);
		UIManager.put("Panel.foreground", defaultPanelForeground);
		
		UIManager.put("Button.background", defaultButtonBackground);
		UIManager.put("Button.foreground", defaultButtonForeground);
		
		UIManager.put("Button.select", defaultButtonSelect);
		UIManager.put("Button.focus", defaultButtonFocus);
			
		UIManager.put("Button.border", defaultButtonBorder);
		
		UIManager.put("Label.foreground", defaultLabelForeground);
	}
	
	public void setModifiedUIStyle()
	{
		UIManager.put("Panel.background", Style.PRIMARY_BACKBROUND_COLOR);
		UIManager.put("Panel.foreground", Style.PRIMARY_FONT_COLOR);
		
		UIManager.put("Button.background", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		
		UIManager.put("Button.select", Style.BUTTON_SELECT_COLOR);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
			
		UIManager.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Style.PRIMARY_BACKBROUND_COLOR), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		UIManager.put("Label.foreground", Style.PRIMARY_FONT_COLOR);
		/*
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		
		Button.darkShadow
		
		
		Button.highlight
		Button.light
		Button.select
		Button.shadow
		*/
	}
	
	public void refreshPlaylist()
	{
		
	}
	
	private class UiSongListSelectionListener implements ListSelectionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ListSelectionListener#valueChanged(java.awt.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent event)
		{
			if(uiSongList.getSelectedIndex() >= 0)
			{
				System.out.println(uiSongList.getSelectedValue().getTitle());
				System.out.println(uiSongList.getSelectedValue().getArtist());
			}
		}
	}
	
	private class PlayerControlListener implements ActionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			// Find index of photo that is currently selected.
			int index = uiSongList.getSelectedIndex();
			
			if(event.getSource() == moveUpButton)
            {	
				playList.moveUp(index);
				uiSongList.setListData(playList.getSongArray());
				
				if(index - 1 >= 0)
					uiSongList.setSelectedIndex(index-1);
				else
					uiSongList.setSelectedIndex(index);
            }
            else if(event.getSource() == moveDownButton)
            {
            	playList.moveDown(index);
            	uiSongList.setListData(playList.getSongArray());
            	
            	if(index + 1 < playList.getSongArray().length)
					uiSongList.setSelectedIndex(index+1);
				else
					uiSongList.setSelectedIndex(index);
            }
			
			
		}
	}
	
	private class AddButtonListener implements ActionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == addButton)
            {	
				setDefaultUIStyle();
            	JPanel formPanel = new JPanel();
            	formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            	Song song;
            	
            	JButton chooseFileButton = new JButton("Choose File");
            	chooseFileButton.addActionListener(new FileChooserButtonActionListener());
            	JTextField titleField = new JTextField(20);
        		JTextField artistField = new JTextField(20);
        		JTextField playtimeField = new JTextField(20);
        		        		
        		formPanel.add(new JLabel("Name: "));
        		formPanel.add(titleField);
        		formPanel.add(new JLabel("File: "));
        		formPanel.add(artistField);
        		songFilePath = new JLabel("No file selected");
        		formPanel.add(songFilePath);
        		
        		formPanel.add(chooseFileButton);
            	
            	int result = JOptionPane.showConfirmDialog(null, formPanel, "Add Photo",
            			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        		// If they click okay, then we will process the form data. The validation here isn't
        		// very good. We could make sure they didn't enter an empty name. We could keep asking
        		// them for valid input.
        		if (result == JOptionPane.OK_OPTION)
        		{   
        			
        			if(songFile.exists()) 
        			{
        				song = new Song(titleField.getText(), artistField.getText(), Integer.parseInt(playtimeField.getText()), songFile.getAbsolutePath());
						playList.addSong(song);
						uiSongList.setListData(playList.getSongArray());
						uiSongList.setSelectedValue(song, true);
        			} 
        			else 
        			{
        				
        				System.err.println("File not found:: ");
        			}
        			
        		
        		}
        		setModifiedUIStyle();
  
			}

		}
		
	}
	
	private class FileChooserButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			// The following starts in the home folder
			//JFileChooser chooser = new JFileChooser();
			
			// The following starts in the current folder, which is often convenient
			JFileChooser chooser = new JFileChooser(".");
			
			int status = chooser.showOpenDialog(null);

			if (status != JFileChooser.APPROVE_OPTION) 
			{
				songFilePath.setText("No File Chosen");
			} 
			else 
			{
				songFile = chooser.getSelectedFile();
			}
		}
	}
}