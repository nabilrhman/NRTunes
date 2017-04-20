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

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	/** The data representing the list of photos in the album (the "model") */
	private PlayList playList;
	private File songFile;

	/** The GUI representation of the list of photos in the album (the "view") */
	private JList<Song> uiSongList;

	private JLabel nowPlayingLabel;
	private JLabel nowPlayingTitleLabel; 
	private JLabel nowPlayingArtistLabel;
	
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
	
	private Object panelBackground;
	private Object panelForeground;
	private Object buttonBackground;
	private Object buttonForeground;
	private Object buttonSelect;
	private Object buttonFocus;
	private Object buttonBorder;
	
	public MyTunesGUIPanel()
	{	
		panelBackground = UIManager.get("Panel.background");
		panelForeground = UIManager.get("Panel.foreground");
		buttonBackground = UIManager.get("Button.background");
		buttonForeground = UIManager.get("Button.foreground");
		buttonSelect = UIManager.get("Button.select");
		buttonFocus = UIManager.get("Button.focus");
		buttonBorder = UIManager.get("Button.border");
		
		UIManager.put("Panel.background", Style.PRIMARY_BACKBROUND_COLOR);
		UIManager.put("Panel.foreground", Style.PRIMARY_FONT_COLOR);
		
		UIManager.put("Button.background", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		
		UIManager.put("Button.select", Style.BUTTON_SELECT_COLOR);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
			
		UIManager.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Style.PRIMARY_BACKBROUND_COLOR), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
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
		this.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		this.setLayout(new BorderLayout());
		
		playList = new PlayList("New Playlist", "playlist.txt");
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
		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
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
		
		JPanel playerControlPanel = new JPanel();
		playButton = new JButton("Play");
		stopButton = new JButton("Stop");
		nextButton = new JButton("Next");
		previousButton = new JButton("Previous");
		playerControlPanel.add(playButton);
		playerControlPanel.add(stopButton);
		playerControlPanel.add(nextButton);
		playerControlPanel.add(previousButton);
			
		nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		songInfoPanel.add(playerControlPanel);
		
		JPanel playlistControlPanel = new JPanel();
		addButton = new JButton("Add");
		addButton.addActionListener(new AddButtonListener());
		removeButton = new JButton("Remove");
		moveUpButton = new JButton("Move Up");
		moveDownButton = new JButton("Move Down");
		playlistControlPanel.add(addButton);
		playlistControlPanel.add(removeButton);
		playlistControlPanel.add(moveUpButton);
		playlistControlPanel.add(moveDownButton);
		
		JPanel playlistInfoPanel = new JPanel();
		
		playlistNameLabel = new JLabel();
		playlistNameLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		playlistNameLabel.setText(playList.getName());
		
		totalPlaytimeLabel = new JLabel();
		totalPlaytimeLabel.setForeground(Style.PRIMARY_FONT_COLOR);
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
	
	private class UiSongListSelectionListener implements ListSelectionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ListSelectionListener#valueChanged(java.awt.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent event)
		{
			System.out.println(uiSongList.getSelectedValue().getTitle());
			System.out.println(uiSongList.getSelectedValue().getArtist());
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
				UIManager.put("Panel.background", panelBackground);
				UIManager.put("Panel.foreground", panelForeground);
				
				UIManager.put("Button.background", buttonBackground);
				UIManager.put("Button.foreground", buttonForeground);
				
				UIManager.put("Button.select", buttonSelect);
				UIManager.put("Button.focus", buttonFocus);
					
				UIManager.put("Button.border", buttonBorder);
				
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
}