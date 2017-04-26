import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	/** The data representing the list of photos in the album (the "model") */
	private PlayList playList;
	private Song[] songs;
	private Song[][] songSquares;
	private File songFile;
	private Song removedSong;
	
	private Timer nowPlayingTimer;
	private Timer animatingTimer;
	private int nowPlayingTime;

	/** The GUI representation of the list of photos in the album (the "view") */
	private JList<Song> uiSongList;

	private JLabel nowPlayingLabel;
	private JLabel nowPlayingTitleLabel; 
	private JLabel nowPlayingArtistLabel;
	private JLabel nowPlayingTimerLabel;
	
	private JLabel playlistNameLabel;
	
	private JTextField playtimeField;
	private JTextField titleField;
	private JTextField artistField;
	
	
	private JLabel songFilePathLabel;
	
	private JPanel musicSquarePanel;
	private JPanel musicSquarePanelContainer;
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
	
	private JButton[][] musicSquareButtons;
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openPlaylistMenuItem;
	private JMenuItem savePlaylistMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu playlistMenu;
	private JMenuItem addMenuItem;
	private JMenuItem removeMenuItem;
	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;
	private JMenu controlMenu;
	private JMenuItem playtMenuItem;
	private JMenuItem stopMenuItem;
	private JMenuItem nextMenuItem;
	private JMenuItem previoustMenuItem;
	private JMenu HelpMenu;
	private JMenuItem aboutMenuItem;
	
	private Object defaultPanelBackground;
	private Object defaultPanelForeground;
	private Object defaultButtonBackground;
	private Object defaultButtonForeground;
	private Object defaultButtonFont;
	private Object defaultButtonSelect;
	private Object defaultButtonFocus;
	private Object defaultButtonBorder;
	private Object defaultLabelForeground;
	private Object defaultLabelFont;
	
	public MyTunesGUIPanel()
	{	
		initializeCustomFonts();
		Font lala = (Font) UIManager.get("Label.font");
		System.out.println(lala.getFontName());
		defaultPanelBackground = UIManager.get("Panel.background");
		defaultPanelForeground = UIManager.get("Panel.foreground");
		defaultButtonBackground = UIManager.get("Button.background");
		defaultButtonForeground = UIManager.get("Button.foreground");
		defaultButtonFont = UIManager.get("Button.font");
		defaultButtonSelect = UIManager.get("Button.select");
		defaultButtonFocus = UIManager.get("Button.focus");
		defaultButtonBorder = UIManager.get("Button.border");
		defaultLabelForeground = UIManager.get("Label.foreground");
		defaultLabelFont = UIManager.get("Label.font");
		
		setModifiedUIStyle();	
		//this.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		this.setLayout(new BorderLayout());
		
		playList = new PlayList("New Playlist", "playlist2.txt");
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
		uiSongList.setAlignmentX(CENTER_ALIGNMENT);
		
		nowPlayingTimer = new Timer(0, new NowPlayingTimerActionListener());
		animatingTimer = new Timer(1000, new AnimatingTimerActionListener());
		
		JPanel uiSongListContainer = new JPanel();
		uiSongListContainer.setLayout(new MigLayout("wrap 1, insets 0"));
		uiSongListContainer.add(uiSongList);
		
		JScrollPane playlistScrollPane = new JScrollPane(uiSongListContainer,
				 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	
		
		playlistScrollPane.setMinimumSize(new Dimension(550, 450));
		playlistScrollPane.setMaximumSize(new Dimension(550, 450));
		
		
		//JScrollBar scrollBar = playlistScrollPane.getVerticalScrollBar();
		//scrollBar.setPreferredSize(new Dimension(10, 0));
		
		//playlistScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		//scrollBar.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		
		
		songInfoPanel = new JPanel();
		songInfoPanel.setBorder(Style.DEFAULT_BORDER);
		songInfoPanel.setMinimumSize(new Dimension(550, 165));
		songInfoPanel.setMaximumSize(new Dimension(550, 165));
		
		
		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		
		//songInfoPanel.setLayout(new GridLayout(1,1));
		songInfoPanel.setBackground(Style.ACCENT_COLOR);
		
		nowPlayingLabel = new JLabel("Now playing".toUpperCase());
		nowPlayingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingLabel.setBorder(Style.DEFAULT_BORDER);
						
		nowPlayingTitleLabel = new JLabel("No song playing".toUpperCase());
		nowPlayingTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);
		
		
		nowPlayingArtistLabel = new JLabel("____________________");
		nowPlayingArtistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		nowPlayingTimerLabel = new JLabel("00:00 / 00:00");
		nowPlayingTimerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTimerLabel.setFont(Style.HEADING2_FONT);
		nowPlayingTimerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
				
		JPanel playerControlPanel = new JPanel();
		playerControlPanel.setLayout(new BoxLayout(playerControlPanel, BoxLayout.X_AXIS));
		//playerControlPanel.setBorder(Style.DEFAULT_BORDER);
		playerControlPanel.setBackground(Style.ACCENT_COLOR);
		
		PlayerControlActionListener playerControlActionListener = new PlayerControlActionListener();
		
		playButton = new JButton("Play");
		setIcon(playButton);
		playButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(playButton);
		
		
		nextButton = new JButton("Next");
		setIcon(nextButton);
		nextButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(nextButton);
		
		previousButton = new JButton("Previous");
		setIcon(previousButton);
		previousButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(previousButton);
		
		playerControlPanel.add(previousButton);
		playerControlPanel.add(playButton);
		playerControlPanel.add(nextButton);
		
			
		//nowPlayingArtistLabel.setForeground(Style.PRIMARY_FONT_COLOR);
		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		songInfoPanel.add(nowPlayingTimerLabel);
		
		songInfoPanel.add(playerControlPanel);
		
		JPanel playlistControlPanel = new JPanel();
		playlistControlPanel.setLayout(new MigLayout("insets 0, gap 0"));
		//playlistControlPanel.setLayout(new BoxLayout(playlistControlPanel, BoxLayout.X_AXIS));
		
		AddRemoveButtonListener addRemoveButtonListener = new AddRemoveButtonListener();
		addButton = new JButton("Add");
		setIcon(addButton);
		addButton.addActionListener(addRemoveButtonListener);
		addChangeListenerToButton(addButton);
		
		removeButton = new JButton("Remove");
		setIcon(removeButton);
		removeButton.addActionListener(addRemoveButtonListener);
		addChangeListenerToButton(removeButton);
		
		moveUpButton = new JButton("Move Up");
		setIcon(moveUpButton);
		moveUpButton.addActionListener(new PlaylistControllerActionListener());
		addChangeListenerToButton(moveUpButton);
		
		moveDownButton = new JButton("Move Down");
		setIcon(moveDownButton);
		moveDownButton.addActionListener(new PlaylistControllerActionListener());
		addChangeListenerToButton(moveDownButton);
		
		playlistControlPanel.add(addButton);
		playlistControlPanel.add(removeButton);
		playlistControlPanel.add(moveUpButton);
		playlistControlPanel.add(moveDownButton);
		
		JPanel playlistInfoPanel = new JPanel();
		
		playlistInfoPanel.setLayout(new MigLayout("insets 0, fill"));
		playlistInfoPanel.setMinimumSize(new Dimension(550, 35));
		playlistInfoPanel.setMaximumSize(new Dimension(550, 35));
		//playlistInfoPanel.setLayout(new MigLayout("insets 0","[right][left]",""));
		
		
		playlistNameLabel = new JLabel();
		playlistNameLabel.setText(" " + "| " + playList.getName() + " - " + ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));
		playlistNameLabel.setFont(Style.PRIMARY_FONT.deriveFont(Font.BOLD));
		
		playlistInfoPanel.add(playlistNameLabel, "growx");
		//playlistInfoPanel.add(totalPlaytimeLabel);
		//playlistInfoPanel.add(addButton);
		//playlistInfoPanel.add(removeButton);
		//playlistInfoPanel.add(moveUpButton);
		//playlistInfoPanel.add(moveDownButton);
		playlistInfoPanel.add(playlistControlPanel, "align right");
		
		/*
		JPanel playlistInfoPanelContainer = new JPanel();
		playlistInfoPanelContainer.setLayout(new GridLayout(1,2));
		playlistInfoPanelContainer.add(playlistNameLabel);
		playlistInfoPanelContainer.add(playlistControlPanel);
		*/
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout("insets 0", "[grow,fill]"));
		//leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		//leftPanel.setMaximumSize(Style.SONG_INFO_PANEL_DIMENSION);
		//leftPanel.add(songInfoPanel);
		leftPanel.add(songInfoPanel, "north");
		//leftPanel.add(playerControlPanel);
		//leftPanel.add(playlistInfoPanelContainer);
		leftPanel.add(playlistInfoPanel, "growx, wrap");
		leftPanel.add(playlistScrollPane);
		
		this.add(leftPanel, BorderLayout.WEST);
		
		
		//GRID
		
		refreshMusicSquareButtons();
		createMusicSquareButtons();
		
		//MENUBAR
		menuBar = new JMenuBar();
		   
	    // build the File menu
	    fileMenu = new JMenu("File");
	    openPlaylistMenuItem = new JMenuItem("Open Playlist");
	    savePlaylistMenuItem = new JMenuItem("Save Playlist");
	    exitMenuItem = new JMenuItem("Exit");
	    //loadPlaylistMenuItem.addActionListener(this);
	    fileMenu.add(openPlaylistMenuItem);
	    fileMenu.add(savePlaylistMenuItem);
	    fileMenu.addSeparator();
	    fileMenu.add(exitMenuItem);
	    /*
	    // build the Edit menu
	    editMenu = new JMenu("Edit");
	    cutMenuItem = new JMenuItem("Cut");
	    copyMenuItem = new JMenuItem("Copy");
	    pasteMenuItem = new JMenuItem("Paste");
	    editMenu.add(cutMenuItem);
	    editMenu.add(copyMenuItem);
	    editMenu.add(pasteMenuItem);
		*/
	    // add menus to menubar
	    menuBar.add(fileMenu);
	    //menuBar.add(editMenu);
	    
	    this.add(menuBar, BorderLayout.PAGE_START);
		
		
	}
	
	private void refreshMusicSquareButtons()
	{
		songs = playList.getSongArray();
		songSquares = playList.getSongSquare();
		
	}
	
	private void createMusicSquareButtons()
	{
		if(songSquares.length > 0)
		{
			
			musicSquareButtons = new JButton[songSquares.length][songSquares.length];
			for (int row = 0; row < musicSquareButtons.length; row++)
			{
				for(int col = 0; col < musicSquareButtons[row].length; col++)
				{
					musicSquareButtons[row][col] = new JButton();
					//musicSquareButtons[row][col].addActionListener(new PhotoSquareListener());
					try 
					{
						musicSquareButtons[row][col].setText(playList.getSongSquare()[row][col].getTitle());
						musicSquareButtons[row][col].setBackground(getHeatMapColor(playList.getSongSquare()[row][col].getPlayCount()));
						musicSquareButtons[row][col].setRolloverEnabled(false);
						//addChangeListenerToSquareButton(musicSquareButtons[row][col]);
					} catch (Exception ex) 
					{
						//musicSquareButtons[row][col].setIcon(null);
					}
				}
			}
			
			//musicSquarePanelContainer = new JPanel();
			//musicSquarePanelContainer.setLayout(new MigLayout("wrap, insets 0", "grow"));
			musicSquarePanel = new JPanel();
			musicSquarePanel.setLayout(new GridLayout(5, 5));
			//musicSquarePanel.setSize(new Dimension(645, 625));
			
			
			for (int row = 0; row < musicSquareButtons.length; row++)
			{
				for(int col = 0; col < musicSquareButtons[row].length; col++)
				{
					musicSquarePanel.add(musicSquareButtons[row][col]);
				}
			}
			//musicSquarePanelContainer.add(musicSquarePanel);
			
		}
		else
		{
			musicSquarePanel = new JPanel();
			musicSquarePanel.setLayout(new GridLayout(5, 5));
			//musicSquarePanel.setLayout(new GridLayout(4, 5));
			
		}
			
				
		this.add(musicSquarePanel, BorderLayout.CENTER);
	}
	
	private void setDefaultUIStyle()
	{
		UIManager.put("Panel.background", defaultPanelBackground);
		UIManager.put("Panel.foreground", defaultPanelForeground);
		
		UIManager.put("Button.background", defaultButtonBackground);
		UIManager.put("Button.foreground", defaultButtonForeground);
		UIManager.put("Button.font", defaultButtonFont);
		
		UIManager.put("Button.select", defaultButtonSelect);
		UIManager.put("Button.focus", defaultButtonFocus);
			
		UIManager.put("Button.border", defaultButtonBorder);
		
		UIManager.put("Label.foreground", defaultLabelForeground);
		UIManager.put("Label.font", defaultLabelFont);
	}
	
	private void setModifiedUIStyle()
	{
		UIManager.put("Panel.background", Style.PRIMARY_BACKBROUND_COLOR);
		UIManager.put("Panel.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("OptionPane.background", Style.PRIMARY_BACKBROUND_COLOR);
		
		UIManager.put("Button.background", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Button.font", Style.PRIMARY_FONT);
		
		UIManager.put("Button.select", Style.BUTTON_SELECT_COLOR);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
			
		UIManager.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Style.PRIMARY_BACKBROUND_COLOR), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		UIManager.put("Label.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Label.font", Style.PRIMARY_FONT);
		
		UIManager.put("ScrollBar.thumb", Style.PRIMARY_BACKBROUND_COLOR);
		//UIManager.put("ScrollBar.background", Style.PRIMARY_BACKBROUND_COLOR);
		//UIManager.put("ScrollBar.foreground", Style.PRIMARY_BACKBROUND_COLOR);
		//UIManager.put("ScrollBar.thumb", new javax.swing.plaf.ColorUIResource(33,129,176));
		/*
		UIManager.put("ScrollBar.thumbDarkShadow", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("ScrollBar.thumbShadow", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("ScrollBar.thumbHighlight", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("ScrollBar.track", Style.SECONDARY_BACKBROUND_COLOR);
		*/
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
	
	private void addChangeListenerToButton(JButton button)
	{
		Color oldButtonBackground = button.getBackground();
		
		button.getModel().addChangeListener(new ChangeListener() 
		{
			
            @Override
            public void stateChanged(ChangeEvent e) 
            {
            	
                if (button.getModel().isRollover()) 
                {
                    button.setBackground(oldButtonBackground.brighter());
                } 
                else if(button.getModel().isPressed() || button.getModel().isArmed())
                {
                	button.setBackground(oldButtonBackground.brighter().brighter());
                }
                else 
                {
                	button.setBackground(oldButtonBackground);
                }                
            }
        });
	}
	
	private void addChangeListenerToSquareButton(JButton button)
	{
		Color oldButtonBackground = button.getBackground();
		button.setRolloverEnabled(false);
		
		button.getModel().addChangeListener(new ChangeListener() 
		{
			
            @Override
            public void stateChanged(ChangeEvent e) 
            {
            	refreshMusicSquareButtons();
        		createMusicSquareButtons();
        		if (button.getModel().isRollover()) 
                {
                    button.setBackground(oldButtonBackground.brighter());
                } 
                else if(button.getModel().isPressed() || button.getModel().isArmed())
                {
                	
                	refreshMusicSquareButtons();
            		createMusicSquareButtons();
            		//button.setBackground(oldButtonBackground.brighter().brighter());
                }
                else 
                {
                	
                }
                
                               
            }
        });
        
	}
	

	private String ConvertSecondToHHMMSSString(int nSecondTime) 
	{
		String time;
		String format = String.format("%%0%dd", 2);
		
        long elapsedTime = nSecondTime;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        
        if(elapsedTime / 3600 != 0)
        	time =  hours + ":" + minutes + ":" + seconds;
        else
        	time =  minutes + ":" + seconds;
        return time;
	}
	
	private void startTimer(int timeInSeconds)
	{
		nowPlayingTime = 1;
		nowPlayingTimer = new Timer(timeInSeconds * 1000, new NowPlayingTimerActionListener());
		nowPlayingTimer.start();
		animatingTimer.start();
	}
	
	private void stopTimer()
	{
		nowPlayingTimer.stop();
		animatingTimer.stop();
	}
	
	private class NowPlayingTimerActionListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event) 
		{
			stopTimer();
			playList.playNextSong();
			updateSongInfoPanel();
	    } 
	}
	
	private class AnimatingTimerActionListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event) 
		{
			Timer timer = (Timer) event.getSource();
			if(nowPlayingTimer.isRunning())
			{
			
				nowPlayingTime++;
				nowPlayingTimerLabel.setText(ConvertSecondToHHMMSSString(nowPlayingTime) + " / " + ConvertSecondToHHMMSSString(playList.getPlaying().getPlayTime()));

			}
			else
			{
				timer.stop();
			}
		 } 
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
				
			}
		}
	}
	
	private class PlaylistControllerActionListener implements ActionListener
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
				{
					uiSongList.setSelectedIndex(index-1);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
				else
				{
					uiSongList.setSelectedIndex(index);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
            }
            else if(event.getSource() == moveDownButton)
            {
            	playList.moveDown(index);
            	uiSongList.setListData(playList.getSongArray());
            	
            	if(index + 1 < playList.getSongArray().length)
            	{
					uiSongList.setSelectedIndex(index+1);
            		uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
            	}
            	else
            	{
					uiSongList.setSelectedIndex(index);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
            	}
            }
			
			refreshMusicSquareButtons();
    		musicSquarePanel.removeAll();
    		musicSquarePanel.invalidate();
			createMusicSquareButtons();	
			musicSquarePanel.revalidate();
			
			
		}
		
		
	}
	
	
	
	private class AddRemoveButtonListener implements ActionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == addButton)
            {	
            	JPanel formPanel = new JPanel();
            	formPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
            	//formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            	formPanel.setLayout(new MigLayout("insets 0, fillx"));
            	Song song;
            	
            	JPanel formFieldPanel = new JPanel();
            	formFieldPanel.setLayout(new MigLayout("insets 0, fillx"));
            	titleField = new JTextField(40);
            	titleField.setAutoscrolls(true);
        		artistField = new JTextField(40);
        		artistField.setAutoscrolls(true);
        		playtimeField = new JTextField(40);
        		playtimeField.setAutoscrolls(true);
        		
        		        		
        		formFieldPanel.add(new JLabel("Title"));
        		formFieldPanel.add(titleField, "growx, wrap");
        		formFieldPanel.add(new JLabel("Artist"));
        		formFieldPanel.add(artistField, "growx, wrap");
        		formFieldPanel.add(new JLabel("Playtime"));
        		formFieldPanel.add(playtimeField, "growx, wrap");
        		
        		
        		JPanel fileChooserPanel = new JPanel();
        		fileChooserPanel.setLayout(new MigLayout("", "[center, grow]"));
        		
        		songFilePathLabel = new JLabel("No file selected");
        		fileChooserPanel.add(songFilePathLabel, "wrap, align center");
        		JButton chooseFileButton = new JButton("Choose File");
            	chooseFileButton.addActionListener(new FileChooserButtonActionListener());
            	addChangeListenerToButton(chooseFileButton);
        		fileChooserPanel.add(chooseFileButton, "align center");
        		
        		formPanel.add(formFieldPanel, "dock north");
        		formPanel.add(fileChooserPanel, "dock south");
            	
            	int result = JOptionPane.showConfirmDialog(null, formPanel, "Add new song",
            			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        		// If they click okay, then we will process the form data. The validation here isn't
        		// very good. We could make sure they didn't enter an empty name. We could keep asking
        		// them for valid input.
        		if (result == JOptionPane.OK_OPTION)
        		{   
        			
        			if(songFile.exists()) 
        			{
        				song = new Song(titleField.getText(), artistField.getText(), playList.convertColonFormattedPlaytimeToSec(playtimeField.getText()), songFile.getAbsolutePath());
						playList.addSong(song);
						uiSongList.setListData(playList.getSongArray());
						uiSongList.setSelectedValue(song, true);
        			} 
        			else 
        			{
        				
        				System.err.println("File not found.");
        			}
        			
        		
        		}
        		setModifiedUIStyle();
        		
  
			}
			else if(event.getSource() == removeButton)
			{
				int removedSongIndex = playList.getIndex(uiSongList.getSelectedValue());
				removedSong = playList.removeSong(removedSongIndex);
				uiSongList.setListData(playList.getSongArray());
				if(removedSongIndex < playList.getNumSongs() && removedSongIndex >= 0)
					uiSongList.setSelectedIndex(removedSongIndex);
				else
					uiSongList.clearSelection();
					
			}
			playlistNameLabel.setText(" " + "| " + playList.getName() + " - " + ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));
			
			refreshMusicSquareButtons();
    		musicSquarePanel.removeAll();
    		musicSquarePanel.invalidate();
			createMusicSquareButtons();	
			musicSquarePanel.revalidate();
			
			
				
		}
		
	}
	
	private class FileChooserButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			setDefaultUIStyle();
			// The following starts in the home folder
			//JFileChooser chooser = new JFileChooser();
			
			// The following starts in the current folder, which is often convenient
			JFileChooser chooser = new JFileChooser(".");
			
			int status = chooser.showOpenDialog(null);

			if (status != JFileChooser.APPROVE_OPTION) 
			{
				songFilePathLabel.setText("No file selected");
			} 
			else 
			{
				songFile = chooser.getSelectedFile();
				if(getFileExtension(songFile.getAbsolutePath()).equalsIgnoreCase("wav") || getFileExtension(songFile.getAbsolutePath()).equalsIgnoreCase("mp3"))
				{
					try
					{
						AudioFile audioFileJAudioTagger = AudioFileIO.read(songFile);
						Tag tag = audioFileJAudioTagger.getTag();
						playtimeField.setText(ConvertSecondToHHMMSSString(audioFileJAudioTagger.getAudioHeader().getTrackLength()));
						if(songFile.getAbsolutePath().length() > 75)
						{
							String SongFilePathShort = songFile.getAbsolutePath().substring(0, 75 - 3) + "...";
							songFilePathLabel.setText(SongFilePathShort);
									
						}
						else
						{
							songFilePathLabel.setText(songFile.getAbsolutePath());
						}
						
						titleField.setText(tag.getFirst(FieldKey.TITLE));
						artistField.setText(tag.getFirst(FieldKey.ARTIST));
						
					} 
					catch (CannotReadException | IOException | TagException | ReadOnlyFileException
							| InvalidAudioFrameException e)
					{
						
						e.printStackTrace();
						
					}
					
				}
				
				
			}
			setModifiedUIStyle();
		}
	}
	
	private class PlayerControlActionListener implements ActionListener
	{
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == playButton)
			{
				
				if(playList.getPlaying() == null)
				{
					playList.setPlaying(uiSongList.getSelectedValue());
					playList.playSong(playList.getPlaying());
					updateSongInfoPanel();
					
				}
				else
				{
					playList.stop();
					updateSongInfoPanel();
				}
				
			}
			else if(event.getSource() == nextButton)
			{
				if(playList.getPlaying() != null)
				{
					playList.playNextSong();
				}
				else
				{
					if(uiSongList.getSelectedIndex() + 1 <= playList.getNumSongs())
						uiSongList.setSelectedIndex(uiSongList.getSelectedIndex() + 1);
				}
				uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				updateSongInfoPanel();
			}
			else if(event.getSource() == previousButton)
			{
				if(playList.getPlaying() != null)
				{
					playList.playPreviousSong();
				}
				else
				{
					if(uiSongList.getSelectedIndex() - 1 >= 0)
						uiSongList.setSelectedIndex(uiSongList.getSelectedIndex() - 1);
				}
				uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				updateSongInfoPanel();
			}
			refreshMusicSquareButtons();
    		musicSquarePanel.removeAll();
    		musicSquarePanel.invalidate();
			createMusicSquareButtons();	
			musicSquarePanel.revalidate();
			
			
		}
	}
	
	private void updateSongInfoPanel()
	{
			if(playList.getPlaying() != null)
			{
				playButton.setText(Style.STOP_ICON);
				nowPlayingTitleLabel.setText(playList.getPlaying().getTitle().toUpperCase());
				nowPlayingArtistLabel.setText(playList.getPlaying().getArtist().toUpperCase());
				nowPlayingTimerLabel.setText("00:01 / " + ConvertSecondToHHMMSSString(playList.getPlaying().getPlayTime()));
				uiSongList.setSelectedValue(playList.getPlaying(),true);
				if(nowPlayingTimer.isRunning() && animatingTimer.isRunning())
				{
					stopTimer();
				}
				startTimer(playList.getPlaying().getPlayTime());
				Color heatMapColor = getHeatMapColor(playList.getPlaying().getPlayCount());
				songInfoPanel.setBackground(heatMapColor);
				
			}
			else
			{
				nowPlayingTitleLabel.setText("No song playing".toUpperCase());
				nowPlayingArtistLabel.setText("____________________");
				nowPlayingTimerLabel.setText("00:00 / 00:00");
				
				playButton.setText(Style.PLAY_ICON);
				if(nowPlayingTimer.isRunning() && animatingTimer.isRunning())
				{
					stopTimer();
				}
			}
			refreshMusicSquareButtons();
	}
	
	private void setIcon(JButton button)
	{
		button.setFont(Style.DEFAULT_FONT);
	
		if(button.getText().equalsIgnoreCase("play"))
		{
			button.setText(Style.PLAY_ICON);
		}
		if(button.getText().equalsIgnoreCase("next"))
		{
			button.setText(Style.NEXT_ICON);
		}
		if(button.getText().equalsIgnoreCase("previous"))
		{
			button.setText(Style.PREVIOUS_ICON);
			
		}
		if(button.getText().equalsIgnoreCase("add"))
		{
			button.setText(Style.ADD_ICON);
		}
		if(button.getText().equalsIgnoreCase("remove"))
		{
			button.setText(Style.REMOVE_ICON);
		}
		if(button.getText().equalsIgnoreCase("move up"))
		{
			button.setText(Style.MOVE_UP_ICON);
		}
		if(button.getText().equalsIgnoreCase("move down"))
		{
			button.setText(Style.MOVE_DOWN_ICON);
		}
			
	}
	
	public String getFileExtension(String filePath)
	{
		String extension = "";

		int i = filePath.lastIndexOf('.');
		if (i >= 0) 
		{
		    extension = filePath.substring(i+1);
		}
		
		return extension.trim();
	}
	
	public void initializeCustomFonts()
	{
		try 
		{
			 Font moderneSansFont = Font.createFont(Font.TRUETYPE_FONT, new File(".//res//moderne-sans.ttf"));
			 Font robotoMonoFont = Font.createFont(Font.TRUETYPE_FONT, new File(".//res//roboto-mono.ttf"));
		     GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     graphicsEnvironment.registerFont(moderneSansFont);
		     graphicsEnvironment.registerFont(robotoMonoFont);
		     Style.PRIMARY_FONT = moderneSansFont.deriveFont(12f);
		     Style.HEADING1_FONT = moderneSansFont.deriveFont(Font.BOLD, 16f);
		     Style.HEADING2_FONT = robotoMonoFont.deriveFont(Font.BOLD, 14f);
		     Style.LIST_FONT = robotoMonoFont.deriveFont(11f);
		} catch (IOException|FontFormatException e) 
		{
		     e.printStackTrace();
		}
	}
	
	private Color getHeatMapColor(int plays)
    {
         final int MAX_PLAYS = 250;
         double minPlays = 0, maxPlays = MAX_PLAYS;    // upper/lower bounds
         double value = (plays - minPlays) / (maxPlays - minPlays); // normalize play count

         // The range of colors our heat map will pass through. This can be modified if you
         // want a different color scheme.
         Color[] colors = { Style.COLOR1, Style.COLOR2, Style.COLOR3, Style.COLOR4, Style.COLOR5};
         int index1, index2; // Our color will lie between these two colors.
         float dist = 0;     // Distance between "index1" and "index2" where our value is.

         if (value <= 0) {
              index1 = index2 = 0;
         } else if (value >= 1) {
              index1 = index2 = colors.length - 1;
         } else {
              value = value * (colors.length - 1);
              index1 = (int) Math.floor(value); // Our desired color will be after this index.
              index2 = index1 + 1;              // ... and before this index (inclusive).
              dist = (float) value - index1; // Distance between the two indexes (0-1).
         }

         int r = (int)((colors[index2].getRed() - colors[index1].getRed()) * dist)
                   + colors[index1].getRed();
         int g = (int)((colors[index2].getGreen() - colors[index1].getGreen()) * dist)
                   + colors[index1].getGreen();
         int b = (int)((colors[index2].getBlue() - colors[index1].getBlue()) * dist)
                   + colors[index1].getBlue();

         return new Color(r, g, b);
    }
}