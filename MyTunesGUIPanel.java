import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import net.miginfocom.swing.MigLayout;

/**
 * This class implements the GUI for the audio player.
 * 
 * The audio player can play files in wav and mp3 format. The playlist can be
 * loaded from and saved to txt file. Also, it can be modified as per user's
 * desire. The player implements music square grid buttons which can be used to
 * play any song on the playlist.
 * 
 * @author Nabil Rahman
 *
 */
@SuppressWarnings("serial")
public class MyTunesGUIPanel extends JPanel
{
	private PlayList playList;
	private Song[] songs;
	private Song[][] songSquares;
	private File songFile;
	private Song removedSong;

	private Timer nowPlayingTimer;
	private Timer animatingTimer;
	private int nowPlayingTime;

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
	private JPanel leftPanel;
	private JPanel rightPanel;

	private JPanel songInfoPanel;
	private JPanel playlistHeatmapGradientPanel;

	private JButton playStopButton;
	private JButton nextButton;
	private JButton previousButton;

	private JButton addButton;
	private JButton removeButton;
	private JButton moveUpButton;
	private JButton moveDownButton;

	private JButton chooseFileButton;

	private JButton[][] musicSquareButtons;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newPlaylistMenuItem;
	private JMenuItem openPlaylistMenuItem;
	private JMenuItem savePlaylistMenuItem;
	private JMenuItem exitMenuItem;

	private JMenu playlistMenu;
	private JMenuItem editPlaylistNameMenuItem;
	private JMenuItem addMenuItem;
	private JMenuItem removeMenuItem;
	private JMenuItem moveUpMenuItem;
	private JMenuItem moveDownMenuItem;

	private JMenu playbackMenu;
	private JMenuItem playMenuItem;
	private JMenuItem stopMenuItem;
	private JMenuItem nextMenuItem;
	private JMenuItem previousMenuItem;

	private JMenu helpMenu;
	private JMenuItem aboutMenuItem;

	private Object defaultPanelBackground;
	private Object defaultPanelForeground;
	private Object defaultOptionPaneBackground;
	private Object defaultOptionPaneForeground;
	private Object defaultButtonBackground;
	private Object defaultButtonForeground;
	private Object defaultButtonFont;
	private Object defaultButtonSelect;
	private Object defaultButtonFocus;
	private Object defaultButtonBorder;
	private Object defaultLabelForeground;
	private Object defaultLabelFont;

	/**
	 * Instantiates all the variables needed for the GUI and adds all of the
	 * components to the JPanel.
	 */
	public MyTunesGUIPanel()
	{
		initializeCustomFonts();

		defaultPanelBackground = UIManager.get("Panel.background");
		defaultPanelForeground = UIManager.get("Panel.foreground");
		defaultOptionPaneForeground = UIManager.get("OptionPane.background");
		defaultOptionPaneForeground = UIManager.get("OptionPane.foreground");
		defaultButtonBackground = UIManager.get("Button.background");
		defaultButtonForeground = UIManager.get("Button.foreground");
		defaultButtonFont = UIManager.get("Button.font");
		defaultButtonSelect = UIManager.get("Button.select");
		defaultButtonFocus = UIManager.get("Button.focus");
		defaultButtonBorder = UIManager.get("Button.border");
		defaultLabelForeground = UIManager.get("Label.foreground");
		defaultLabelFont = UIManager.get("Label.font");

		setModifiedUIStyle();

		this.setLayout(new BorderLayout());

		playList = new PlayList("New Playlist", "playlist.txt");
		// playList = new PlayList("New Playlist");

		songs = playList.getSongArray();

		nowPlayingTimer = new Timer(0, new NowPlayingTimerActionListener());
		animatingTimer = new Timer(1000, new AnimatingTimerActionListener());

		// Song JList Starts
		uiSongList = new JList();
		uiSongList.setListData(playList.getSongArray());

		uiSongList.setSelectedIndex(0);
		uiSongList.setFont(Style.LIST_FONT);
		uiSongList.setForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
		uiSongList.setSelectionBackground(Style.SECONDARY_BACKBROUND_COLOR);
		uiSongList.setSelectionForeground(Style.PRIMARY_FONT_COLOR);
		uiSongList.setFixedCellHeight(Style.LIST_ITEM_HEIGHT);
		uiSongList.setAlignmentX(CENTER_ALIGNMENT);

		JPanel uiSongListContainer = new JPanel();
		uiSongListContainer.setLayout(new MigLayout("wrap 1, insets 0"));
		uiSongListContainer.add(uiSongList);

		JScrollPane playlistScrollPane = new JScrollPane(uiSongListContainer,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		playlistScrollPane.setMinimumSize(new Dimension(550, 390));
		playlistScrollPane.setMaximumSize(new Dimension(550, 390));

		// Song Info Panel Starts
		songInfoPanel = new JPanel();
		songInfoPanel.setBorder(Style.DEFAULT_BORDER);
		songInfoPanel.setMinimumSize(new Dimension(550, 170));
		songInfoPanel.setMaximumSize(new Dimension(550, 170));

		songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
		songInfoPanel.setBackground(Style.ACCENT_COLOR);

		nowPlayingLabel = new JLabel("Now playing".toUpperCase());
		nowPlayingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingLabel.setBorder(Style.DEFAULT_BORDER);

		nowPlayingTitleLabel = new JLabel("No song playing".toUpperCase());
		nowPlayingTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTitleLabel.setFont(Style.HEADING1_FONT);

		nowPlayingArtistLabel = new JLabel("---------------------------------");
		nowPlayingArtistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		nowPlayingTimerLabel = new JLabel("00:00 / 00:00");
		nowPlayingTimerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		nowPlayingTimerLabel.setFont(Style.HEADING2_FONT);
		nowPlayingTimerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		// Player Control Panel Starts
		JPanel playerControlPanel = new JPanel();
		playerControlPanel.setLayout(new BoxLayout(playerControlPanel, BoxLayout.X_AXIS));
		playerControlPanel.setBackground(Style.ACCENT_COLOR);

		PlayerControlActionListener playerControlActionListener = new PlayerControlActionListener();

		playStopButton = new JButton("Play");
		setIcon(playStopButton);
		playStopButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(playStopButton);

		nextButton = new JButton("Next");
		setIcon(nextButton);
		nextButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(nextButton);

		previousButton = new JButton("Previous");
		setIcon(previousButton);
		previousButton.addActionListener(playerControlActionListener);
		addChangeListenerToButton(previousButton);

		playerControlPanel.add(previousButton);
		playerControlPanel.add(playStopButton);
		playerControlPanel.add(nextButton);

		songInfoPanel.add(nowPlayingLabel);
		songInfoPanel.add(nowPlayingTitleLabel);
		songInfoPanel.add(nowPlayingArtistLabel);
		songInfoPanel.add(nowPlayingTimerLabel);
		songInfoPanel.add(playerControlPanel);

		// Playlist Control Panel Starts
		JPanel playlistControlPanel = new JPanel();
		playlistControlPanel.setLayout(new MigLayout("insets 0, gap 0"));

		AddRemoveButtonListener addRemoveButtonActionListener = new AddRemoveButtonListener();
		addButton = new JButton("Add");
		setIcon(addButton);
		addButton.addActionListener(addRemoveButtonActionListener);
		addChangeListenerToButton(addButton);

		removeButton = new JButton("Remove");
		setIcon(removeButton);
		removeButton.addActionListener(addRemoveButtonActionListener);
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

		// Playlist Info Panel Starts
		JPanel playlistInfoPanel = new JPanel();

		playlistInfoPanel.setLayout(new MigLayout("insets 0, fill"));
		playlistInfoPanel.setMinimumSize(new Dimension(550, 35));
		playlistInfoPanel.setMaximumSize(new Dimension(550, 35));

		playlistNameLabel = new JLabel();
		playlistNameLabel.setText(
				"  " + "I  " + playList.getName() + " - " + ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));
		playlistNameLabel.setFont(Style.PRIMARY_FONT.deriveFont(Font.BOLD));

		playlistInfoPanel.add(playlistNameLabel, "growx");
		playlistInfoPanel.add(playlistControlPanel, "align right");

		// Left Panel Starts
		leftPanel = new JPanel();
		leftPanel.setLayout(new MigLayout("insets 0", "[grow,fill]"));
		leftPanel.add(songInfoPanel, "north");
		leftPanel.add(playlistInfoPanel, "growx, wrap");
		leftPanel.add(playlistScrollPane);

		this.add(leftPanel, BorderLayout.WEST);

		// Right Panel Starts
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		// Heatmap Bar Starts
		playlistHeatmapGradientPanel = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics graphics)
			{
				super.paintComponent(graphics);
				Graphics2D g2d = (Graphics2D) graphics;

				GradientPaint gradientPaint = new GradientPaint(0, 0, Style.COLOR1, getWidth(), 0, Style.COLOR5);
				g2d.setPaint(gradientPaint);
				g2d.fillRect(0, 0, getWidth(), getHeight());

			}
		};

		playlistHeatmapGradientPanel.setLayout(new MigLayout("fill"));
		playlistHeatmapGradientPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		playlistHeatmapGradientPanel.add(new JLabel("0 Play"), "gap 1 1 1 1, align left");
		playlistHeatmapGradientPanel.add(new JLabel("250 Play"), "gap 1 1 1 1, align right");
		rightPanel.add(playlistHeatmapGradientPanel, BorderLayout.NORTH);

		// Song Grid Panel Starts
		reloadSongData();
		createMusicSquareButtons();

		this.add(rightPanel, BorderLayout.CENTER);

		// Menubar Starts
		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		MenuBarItemActionListener menuBarItemActionListener = new MenuBarItemActionListener();

		newPlaylistMenuItem = new JMenuItem("New playlist");
		newPlaylistMenuItem.addActionListener(menuBarItemActionListener);
		openPlaylistMenuItem = new JMenuItem("Open playlist");
		openPlaylistMenuItem.addActionListener(menuBarItemActionListener);
		savePlaylistMenuItem = new JMenuItem("Save playlist");
		savePlaylistMenuItem.addActionListener(menuBarItemActionListener);
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(menuBarItemActionListener);
		fileMenu.add(newPlaylistMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(openPlaylistMenuItem);
		fileMenu.add(savePlaylistMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		playlistMenu = new JMenu("Playlist");

		editPlaylistNameMenuItem = new JMenuItem("Edit playlist name");
		editPlaylistNameMenuItem.addActionListener(menuBarItemActionListener);
		addMenuItem = new JMenuItem("Add new song");
		addMenuItem.addActionListener(menuBarItemActionListener);
		removeMenuItem = new JMenuItem("Remove song");
		removeMenuItem.addActionListener(menuBarItemActionListener);
		moveUpMenuItem = new JMenuItem("Move song up");
		moveUpMenuItem.addActionListener(menuBarItemActionListener);
		moveDownMenuItem = new JMenuItem("Move song down");
		moveDownMenuItem.addActionListener(menuBarItemActionListener);
		playlistMenu.add(editPlaylistNameMenuItem);
		playlistMenu.addSeparator();
		playlistMenu.add(addMenuItem);
		playlistMenu.add(removeMenuItem);
		playlistMenu.addSeparator();
		playlistMenu.add(moveUpMenuItem);
		playlistMenu.add(moveDownMenuItem);

		playbackMenu = new JMenu("Playback");

		playMenuItem = new JMenuItem("Play");
		playMenuItem.addActionListener(menuBarItemActionListener);
		stopMenuItem = new JMenuItem("Stop");
		stopMenuItem.setEnabled(false);
		stopMenuItem.addActionListener(menuBarItemActionListener);
		nextMenuItem = new JMenuItem("Next");
		nextMenuItem.addActionListener(menuBarItemActionListener);
		previousMenuItem = new JMenuItem("Previous");
		previousMenuItem.addActionListener(menuBarItemActionListener);
		playbackMenu.add(playMenuItem);
		playbackMenu.add(stopMenuItem);
		playbackMenu.addSeparator();
		playbackMenu.add(nextMenuItem);
		playbackMenu.add(previousMenuItem);

		helpMenu = new JMenu("Help");
		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(menuBarItemActionListener);
		helpMenu.add(aboutMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(playlistMenu);
		menuBar.add(playbackMenu);
		menuBar.add(helpMenu);

		this.add(menuBar, BorderLayout.PAGE_START);

	}

	/**
	 * Reloads song arrays.
	 */
	private void reloadSongData()
	{
		songs = playList.getSongArray();
		songSquares = playList.getSongSquare();
	}

	/**
	 * Creates squared music buttons in grid.
	 */
	private void createMusicSquareButtons()
	{
		if (songSquares.length > 0)
		{

			musicSquareButtons = new JButton[songSquares.length][songSquares.length];
			MusicSquareButtonActionListener musicSquareButtonActionListener = new MusicSquareButtonActionListener();
			for (int row = 0; row < musicSquareButtons.length; row++)
			{
				for (int col = 0; col < musicSquareButtons[row].length; col++)
				{
					musicSquareButtons[row][col] = new JButton();

					try
					{
						musicSquareButtons[row][col].setText(playList.getSongSquare()[row][col].getTitle());
						musicSquareButtons[row][col]
								.setBackground(getHeatMapColor(playList.getSongSquare()[row][col].getPlayCount()));
						musicSquareButtons[row][col].addActionListener(musicSquareButtonActionListener);
						addChangeListenerToSquareButton(musicSquareButtons[row][col],
								getHeatMapColor(playList.getSongSquare()[row][col].getPlayCount()));

					}
					catch (Exception ex)
					{

					}
				}
			}

			musicSquarePanel = new JPanel();
			if (playList.getNumSongs() <= 25)
				musicSquarePanel.setLayout(new GridLayout(5, 5));
			else
				musicSquarePanel.setLayout(new GridLayout(10, 10));

			for (int row = 0; row < musicSquareButtons.length; row++)
			{
				for (int col = 0; col < musicSquareButtons[row].length; col++)
				{
					musicSquarePanel.add(musicSquareButtons[row][col]);
				}
			}

		}
		else
		{
			musicSquarePanel = new JPanel();
			musicSquarePanel.setLayout(new GridLayout(5, 5));

		}

		rightPanel.add(musicSquarePanel, BorderLayout.CENTER);
	}

	/**
	 * Recreates squared music buttons in grid.
	 */
	public void recreateMusicSquareButtons()
	{
		reloadSongData();
		musicSquarePanel.setVisible(false);
		musicSquarePanel.removeAll();
		musicSquarePanel.invalidate();

		createMusicSquareButtons();

		musicSquarePanel.revalidate();
		musicSquarePanel.setVisible(true);
	}

	/**
	 * Updates the song info panel.
	 */
	private void updateSongInfoPanel()
	{
		if (playList.getPlaying() != null)
		{
			playStopButton.setText(Style.STOP_ICON);
			playMenuItem.setEnabled(false);
			stopMenuItem.setEnabled(true);
			nowPlayingTitleLabel.setText(playList.getPlaying().getTitle().toUpperCase());
			nowPlayingArtistLabel.setText(playList.getPlaying().getArtist().toUpperCase());
			nowPlayingTimerLabel.setText("00:01 / " + ConvertSecondToHHMMSSString(playList.getPlaying().getPlayTime()));
			uiSongList.setSelectedValue(playList.getPlaying(), true);
			if (nowPlayingTimer.isRunning() && animatingTimer.isRunning())
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
			nowPlayingArtistLabel.setText("---------------------------------");
			nowPlayingTimerLabel.setText("00:00 / 00:00");
			songInfoPanel.setBackground(Style.ACCENT_COLOR);

			playStopButton.setText(Style.PLAY_ICON);
			playMenuItem.setEnabled(true);
			stopMenuItem.setEnabled(false);
			if (nowPlayingTimer.isRunning() && animatingTimer.isRunning())
			{
				stopTimer();
			}
		}
		reloadSongData();
	}

	/**
	 * Sets the icon of the given button.
	 * 
	 * @param button
	 *            The button that needs icon.
	 */
	private void setIcon(JButton button)
	{
		button.setFont(Style.DEFAULT_FONT);

		if (button.getText().equalsIgnoreCase("play"))
		{
			button.setText(Style.PLAY_ICON);
		}
		if (button.getText().equalsIgnoreCase("next"))
		{
			button.setText(Style.NEXT_ICON);
		}
		if (button.getText().equalsIgnoreCase("previous"))
		{
			button.setText(Style.PREVIOUS_ICON);
		}
		if (button.getText().equalsIgnoreCase("add"))
		{
			button.setText(Style.ADD_ICON);
		}
		if (button.getText().equalsIgnoreCase("remove"))
		{
			button.setText(Style.REMOVE_ICON);
		}
		if (button.getText().equalsIgnoreCase("move up"))
		{
			button.setText(Style.MOVE_UP_ICON);
		}
		if (button.getText().equalsIgnoreCase("move down"))
		{
			button.setText(Style.MOVE_DOWN_ICON);
		}

	}

	/**
	 * Initializes all the custom fonts.
	 */
	public void initializeCustomFonts()
	{
		try
		{
			Font moderneSansFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/moderne-sans.ttf"));
			Font robotoMonoFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/roboto-mono.ttf"));
			GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicsEnvironment.registerFont(moderneSansFont);
			graphicsEnvironment.registerFont(robotoMonoFont);
			Style.PRIMARY_FONT = moderneSansFont.deriveFont(12f);
			Style.HEADING1_FONT = moderneSansFont.deriveFont(Font.BOLD, 17f);
			Style.HEADING2_FONT = robotoMonoFont.deriveFont(Font.BOLD, 14f);
			Style.LIST_FONT = robotoMonoFont.deriveFont(11f);
		}
		catch (IOException | FontFormatException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns heat map color given the play count.
	 * 
	 * @param plays
	 *            The play count of the song.
	 * @return the heat map color
	 */
	private Color getHeatMapColor(int plays)
	{
		final int MAX_PLAYS = 250;
		double minPlays = 0, maxPlays = MAX_PLAYS;
		double value = (plays - minPlays) / (maxPlays - minPlays);

		Color[] colors =
		{ Style.COLOR1, Style.COLOR2, Style.COLOR3, Style.COLOR4, Style.COLOR5 };
		int index1, index2;
		float dist = 0;

		if (value <= 0)
		{
			index1 = index2 = 0;
		}
		else if (value >= 1)
		{
			index1 = index2 = colors.length - 1;
		}
		else
		{
			value = value * (colors.length - 1);
			index1 = (int) Math.floor(value);
			index2 = index1 + 1;
			dist = (float) value - index1;
		}

		int r = (int) ((colors[index2].getRed() - colors[index1].getRed()) * dist) + colors[index1].getRed();
		int g = (int) ((colors[index2].getGreen() - colors[index1].getGreen()) * dist) + colors[index1].getGreen();
		int b = (int) ((colors[index2].getBlue() - colors[index1].getBlue()) * dist) + colors[index1].getBlue();

		return new Color(r, g, b);
	}

	/**
	 * Sets the current UI style to the default UI style.
	 */
	private void setDefaultUIStyle()
	{
		UIManager.put("Panel.background", defaultPanelBackground);
		UIManager.put("Panel.foreground", defaultPanelForeground);

		UIManager.put("OptionPane.background", defaultOptionPaneBackground);
		UIManager.put("OptionPane.foreground", defaultOptionPaneForeground);

		UIManager.put("Button.background", defaultButtonBackground);
		UIManager.put("Button.foreground", defaultButtonForeground);
		UIManager.put("Button.font", defaultButtonFont);

		UIManager.put("Button.select", defaultButtonSelect);
		UIManager.put("Button.focus", defaultButtonFocus);

		UIManager.put("Button.border", defaultButtonBorder);

		UIManager.put("Label.foreground", defaultLabelForeground);
		UIManager.put("Label.font", defaultLabelFont);
	}

	/**
	 * Sets the UI style to modified UI style.
	 */
	private void setModifiedUIStyle()
	{
		UIManager.put("Panel.background", Style.PRIMARY_BACKBROUND_COLOR);
		UIManager.put("Panel.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("OptionPane.background", Style.PRIMARY_BACKBROUND_COLOR);
		UIManager.put("OptionPane.foreground", Style.PRIMARY_FONT_COLOR);

		UIManager.put("Button.background", Style.SECONDARY_BACKBROUND_COLOR);
		UIManager.put("Button.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Button.font", Style.PRIMARY_FONT);

		UIManager.put("Button.select", Style.BUTTON_SELECT_COLOR);
		UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));

		UIManager.put("Button.border",
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Style.PRIMARY_BACKBROUND_COLOR),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		UIManager.put("Label.foreground", Style.PRIMARY_FONT_COLOR);
		UIManager.put("Label.font", Style.PRIMARY_FONT);
	}

	/**
	 * Returns file extension given a file path.
	 * 
	 * @param filePath
	 *            The path of the file
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

	/**
	 * Adds change listener to button. This is needed to implements the effects
	 * of the buttons.
	 * 
	 * @param button
	 *            The button that needs change listener added
	 */
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
				else if (button.getModel().isPressed() || button.getModel().isArmed())
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

	/**
	 * Adds change listener to squared music button. This is needed to
	 * implements the effects of the buttons.
	 * 
	 * @param button
	 *            The button that needs change listener added
	 * @param color
	 *            The background color of that button
	 */
	private void addChangeListenerToSquareButton(JButton button, Color color)
	{
		button.getModel().addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{

				if (button.getModel().isRollover())
				{
					button.setBackground(color.brighter());
				}
				else if (button.getModel().isPressed())
				{

					button.setBackground(color.brighter().brighter());
				}
				else
				{
					button.setBackground(color);
				}

			}
		});

	}

	/**
	 * Converts seconds to colon formatted string (HH:MM:SS).
	 * 
	 * @param nSecondTime
	 *            Time in seconds
	 * @return The colon formatted string
	 */
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

	/**
	 * Starts all the timers.
	 * 
	 * @param timeInSeconds
	 *            The timer duration in seconds
	 */
	private void startTimer(int timeInSeconds)
	{
		nowPlayingTime = 1;
		nowPlayingTimer = new Timer(timeInSeconds * 1000, new NowPlayingTimerActionListener());
		nowPlayingTimer.start();
		animatingTimer.start();
	}

	/**
	 * Stops all the timers.
	 */
	private void stopTimer()
	{
		nowPlayingTimer.stop();
		animatingTimer.stop();
	}

	private class NowPlayingTimerActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event)
		{
			stopTimer();
			playList.playNextSong();
			updateSongInfoPanel();
		}
	}

	private class AnimatingTimerActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event)
		{
			Timer timer = (Timer) event.getSource();
			if (nowPlayingTimer.isRunning())
			{
				nowPlayingTime++;
				nowPlayingTimerLabel.setText(ConvertSecondToHHMMSSString(nowPlayingTime) + " / "
						+ ConvertSecondToHHMMSSString(playList.getPlaying().getPlayTime()));
			}
			else
			{
				timer.stop();
			}
		}
	}

	private class PlaylistControllerActionListener implements ActionListener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			int index = uiSongList.getSelectedIndex();

			if (event.getSource() == moveUpButton)
			{
				playList.moveUp(index);
				uiSongList.setListData(playList.getSongArray());

				if (index - 1 >= 0)
				{
					uiSongList.setSelectedIndex(index - 1);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
				else
				{
					uiSongList.setSelectedIndex(index);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
			}
			else if (event.getSource() == moveDownButton)
			{
				playList.moveDown(index);
				uiSongList.setListData(playList.getSongArray());

				if (index + 1 < playList.getSongArray().length)
				{
					uiSongList.setSelectedIndex(index + 1);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
				else
				{
					uiSongList.setSelectedIndex(index);
					uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				}
			}

			recreateMusicSquareButtons();

		}

	}

	private class AddRemoveButtonListener implements ActionListener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == addButton)
			{
				JPanel formPanel = new JPanel();
				formPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				// formPanel.setLayout(new BoxLayout(formPanel,
				// BoxLayout.Y_AXIS));
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
				formFieldPanel.add(new JLabel("Playtime"), "gapRight 5");
				formFieldPanel.add(playtimeField, "growx, wrap");

				JPanel fileChooserPanel = new JPanel();
				fileChooserPanel.setLayout(new MigLayout("", "[center, grow]"));

				songFilePathLabel = new JLabel("No file selected");
				fileChooserPanel.add(songFilePathLabel, "wrap, align center, gap 5 5 5 5");
				chooseFileButton = new JButton("Choose File");
				chooseFileButton.addActionListener(new FileChooserButtonActionListener());
				addChangeListenerToButton(chooseFileButton);
				fileChooserPanel.add(chooseFileButton, "align center");

				formPanel.add(formFieldPanel, "dock north");
				formPanel.add(fileChooserPanel, "dock south");

				int result = JOptionPane.showConfirmDialog(null, formPanel, "Add new song",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION)
				{
					JFrame frame = new JFrame();
					if (titleField.getText().isEmpty())
					{

						JOptionPane.showMessageDialog(frame, new JLabel("Missing title."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else if (artistField.getText().isEmpty())
					{
						JOptionPane.showMessageDialog(frame, new JLabel("Missing artist."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else if (playtimeField.getText().isEmpty())
					{
						JOptionPane.showMessageDialog(frame, new JLabel("Missing playtime."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else if (songFilePathLabel.getText().equalsIgnoreCase("No file selected"))
					{
						JOptionPane.showMessageDialog(frame, new JLabel("No file selected."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						song = new Song(titleField.getText().trim(), artistField.getText().trim(),
								playList.convertColonFormattedPlaytimeToSec(playtimeField.getText().trim()),
								songFile.getAbsolutePath());
						playList.addSong(song);
						uiSongList.setListData(playList.getSongArray());
						uiSongList.setSelectedValue(song, true);
					}

				}
				setModifiedUIStyle();

			}
			else if (event.getSource() == removeButton)
			{
				if (uiSongList != null)
					if (!uiSongList.isSelectionEmpty())
					{
						JLabel messageLabel = new JLabel("Do you want to remove the song" + "\u003F");
						messageLabel.setForeground(Style.PRIMARY_FONT_COLOR);
						int response = JOptionPane.showConfirmDialog(null, messageLabel, "Confirm",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (response == JOptionPane.YES_OPTION)
						{
							int removedSongIndex = playList.getIndex(uiSongList.getSelectedValue());
							removedSong = playList.removeSong(removedSongIndex);
							uiSongList.setListData(playList.getSongArray());
							if (removedSongIndex < playList.getNumSongs() && removedSongIndex >= 0)
								uiSongList.setSelectedIndex(removedSongIndex);
							else
								uiSongList.clearSelection();
						}
					}

			}
			playlistNameLabel.setText("  " + "I  " + playList.getName() + " - "
					+ ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));

			recreateMusicSquareButtons();
		}
	}

	private class FileChooserButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			setDefaultUIStyle();
			if (event.getSource() == chooseFileButton)
			{
				// The following starts in the home folder
				// JFileChooser chooser = new JFileChooser();

				// The following starts in the current folder, which is often
				// convenient
				JFileChooser chooser = new JFileChooser(".")
				{
					@Override
					protected javax.swing.JDialog createDialog(java.awt.Component parent)
							throws java.awt.HeadlessException
					{
						javax.swing.JDialog dialog = super.createDialog(parent);

						dialog.setIconImage(new javax.swing.ImageIcon("res/icon.png").getImage());

						return dialog;

					}
				};

				int status = chooser.showOpenDialog(null);

				if (status == JFileChooser.APPROVE_OPTION)
				{
					songFile = chooser.getSelectedFile();
					if (getFileExtension(songFile.getAbsolutePath()).equalsIgnoreCase("wav")
							|| getFileExtension(songFile.getAbsolutePath()).equalsIgnoreCase("mp3"))
					{
						try
						{
							AudioFile audioFileJAudioTagger = AudioFileIO.read(songFile);
							Tag tag = audioFileJAudioTagger.getTag();
							playtimeField.setText(ConvertSecondToHHMMSSString(
									audioFileJAudioTagger.getAudioHeader().getTrackLength()));
							if (songFile.getAbsolutePath().length() > 75)
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
			}

			setModifiedUIStyle();
		}
	}

	private class PlayerControlActionListener implements ActionListener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == playStopButton)
			{

				if (playList.getPlaying() == null)
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
			else if (event.getSource() == nextButton)
			{
				if (playList.getPlaying() != null)
				{
					playList.playNextSong();
				}
				else
				{
					if (uiSongList.getSelectedIndex() + 1 <= playList.getNumSongs())
						uiSongList.setSelectedIndex(uiSongList.getSelectedIndex() + 1);
				}
				uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				updateSongInfoPanel();
			}
			else if (event.getSource() == previousButton)
			{
				if (playList.getPlaying() != null)
				{
					playList.playPreviousSong();
				}
				else
				{
					if (uiSongList.getSelectedIndex() - 1 >= 0)
						uiSongList.setSelectedIndex(uiSongList.getSelectedIndex() - 1);
				}
				uiSongList.ensureIndexIsVisible(uiSongList.getSelectedIndex());
				updateSongInfoPanel();
			}

			recreateMusicSquareButtons();

		}
	}

	private class MusicSquareButtonActionListener implements ActionListener
	{
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent event)
		{
			for (int row = 0; row < musicSquareButtons.length; row++)
			{
				for (int col = 0; col < musicSquareButtons[row].length; col++)
				{
					if (event.getSource() == musicSquareButtons[row][col])
					{

						if (playList.getPlaying() != null)
						{
							playStopButton.doClick();
							uiSongList.setSelectedValue(songSquares[row][col], true);
							playStopButton.doClick();
						}
						else
						{
							uiSongList.setSelectedValue(songSquares[row][col], true);
							playStopButton.doClick();
						}

					}

				}
			}

		}
	}

	private class MenuBarItemActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == newPlaylistMenuItem)
			{
				if (playStopButton.getText().equalsIgnoreCase(Style.STOP_ICON))
				{
					playStopButton.doClick();
				}
				playList = new PlayList("New Playlist");
				uiSongList.setListData(playList.getSongArray());
				playlistNameLabel.setText("  " + "I  " + playList.getName() + " - "
						+ ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));
				recreateMusicSquareButtons();
			}
			else if (event.getSource() == savePlaylistMenuItem)
			{
				JPanel formPanel = new JPanel();
				formPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				formPanel.setLayout(new MigLayout("insets 0, fillx"));

				JTextField playlistNameField = new JTextField(40);
				playlistNameField.setText(playList.getName());
				playlistNameField.setAutoscrolls(true);

				formPanel.add(new JLabel("Playlist Name"), "gapRight 5");
				formPanel.add(playlistNameField, "growx, wrap");

				int result = JOptionPane.showConfirmDialog(null, formPanel, "Edit playlist name",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION)
				{
					JFrame frame = new JFrame();
					if (playlistNameField.getText().isEmpty())
					{

						JOptionPane.showMessageDialog(frame, new JLabel("Missing playlist name."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						playList.setName(playlistNameField.getText());
						playlistNameLabel.setText("  " + "I  " + playList.getName() + " - "
								+ ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));

					}

					setDefaultUIStyle();
					JFileChooser chooser = new JFileChooser(".")
					{
						@Override
						protected javax.swing.JDialog createDialog(java.awt.Component parent)
								throws java.awt.HeadlessException
						{
							javax.swing.JDialog dialog = super.createDialog(parent);

							dialog.setIconImage(new javax.swing.ImageIcon("res/icon.png").getImage());

							return dialog;

						}
					};

					String fileName = "";

					chooser.setFileFilter(new FileNameExtensionFilter("Txt file", "txt"));
					int status = chooser.showSaveDialog(null);

					System.out.println(fileName);

					if (status == JFileChooser.APPROVE_OPTION)
					{

						fileName = chooser.getSelectedFile().toString();
						if (!fileName.endsWith(".txt"))
							fileName += ".txt";

						playList.saveToFile(fileName);

					}
				}

				setModifiedUIStyle();
			}
			else if (event.getSource() == openPlaylistMenuItem)
			{
				setDefaultUIStyle();
				JFileChooser chooser = new JFileChooser(".")
				{
					@Override
					protected javax.swing.JDialog createDialog(java.awt.Component parent)
							throws java.awt.HeadlessException
					{
						javax.swing.JDialog dialog = super.createDialog(parent);

						dialog.setIconImage(new javax.swing.ImageIcon("res/icon.png").getImage());

						return dialog;

					}
				};

				String fileName = "";
				chooser.setFileFilter(new FileNameExtensionFilter("Txt file", "txt"));
				int status = chooser.showOpenDialog(null);

				System.out.println(fileName);

				if (status == JFileChooser.APPROVE_OPTION)
				{

					chooser.getSelectedFile();
					playList = new PlayList("New Playlist");
					playList.loadFromFile(chooser.getSelectedFile());
					uiSongList.setListData(playList.getSongArray());
					uiSongList.setSelectedIndex(0);

				}
				playlistNameLabel.setText("  " + "I  " + playList.getName() + " - "
						+ ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));
				setModifiedUIStyle();
			}
			else if (event.getSource() == exitMenuItem)
			{
				System.exit(0);
			}
			else if (event.getSource() == addMenuItem)
			{
				addButton.doClick();
			}
			else if (event.getSource() == removeMenuItem)
			{
				removeButton.doClick();
			}
			else if (event.getSource() == moveUpMenuItem)
			{
				moveUpButton.doClick();
			}
			else if (event.getSource() == moveDownMenuItem)
			{
				moveDownButton.doClick();
			}
			else if (event.getSource() == editPlaylistNameMenuItem)
			{
				JPanel formPanel = new JPanel();
				formPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				formPanel.setLayout(new MigLayout("insets 0, fillx"));

				JTextField playlistNameField = new JTextField(40);
				playlistNameField.setText(playList.getName());
				playlistNameField.setAutoscrolls(true);

				formPanel.add(new JLabel("Playlist Name"), "gapRight 5");
				formPanel.add(playlistNameField, "growx, wrap");

				int result = JOptionPane.showConfirmDialog(null, formPanel, "Edit playlist name",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (result == JOptionPane.OK_OPTION)
				{
					JFrame frame = new JFrame();
					if (playlistNameField.getText().isEmpty())
					{

						JOptionPane.showMessageDialog(frame, new JLabel("Missing playlist name."), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						playList.setName(playlistNameField.getText());
						playlistNameLabel.setText("  " + "I  " + playList.getName() + " - "
								+ ConvertSecondToHHMMSSString(playList.getTotalPlayTime()));

					}
				}
			}
			else if (event.getSource() == playMenuItem)
			{
				playStopButton.doClick();
			}
			else if (event.getSource() == stopMenuItem)
			{
				playStopButton.doClick();
			}
			else if (event.getSource() == nextMenuItem)
			{
				nextButton.doClick();
			}
			else if (event.getSource() == previousMenuItem)
			{
				previousButton.doClick();
			}
			else if (event.getSource() == aboutMenuItem)
			{
				JPanel mainPanel = new JPanel();
				mainPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				mainPanel.setLayout(new MigLayout("insets 0, fill"));

				JLabel logoImageLabel = new JLabel();

				BufferedImage logo;
				try
				{
					logo = ImageIO.read(new File("res/icon.png"));
					logoImageLabel = new JLabel(new ImageIcon(logo));
				}
				catch (IOException e)
				{

					e.printStackTrace();
				}

				JPanel imagePanel = new JPanel();
				imagePanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				imagePanel.setLayout(new MigLayout("insets 0, fillx"));
				imagePanel.add(logoImageLabel);

				JPanel labelPanel = new JPanel();
				labelPanel.setBackground(Style.PRIMARY_BACKBROUND_COLOR);
				labelPanel.setLayout(new MigLayout("insets 0, fillx"));

				JTextField playlistNameField = new JTextField(40);
				playlistNameField.setText(playList.getName());
				playlistNameField.setAutoscrolls(true);

				JLabel applicationNameLabel = new JLabel("NR Tunes");
				applicationNameLabel.setFont(Style.HEADING1_FONT);

				labelPanel.add(applicationNameLabel, "growx, wrap");
				labelPanel.add(new JLabel("Version: 1.0 Beta"), "growx, wrap");
				labelPanel.add(new JLabel("Developed by Nabil Rahman"), "gapTop 5, growx, wrap");
				labelPanel.add(new JLabel("This application was developed as the final project for"),
						"gapTop 5, growx, wrap");
				labelPanel.add(new JLabel("CS121 - Spring 2017, Boise State University"), "growx, wrap");
				labelPanel.add(new JLabel("www.nr-creation.com"), "gapTop 5, growx, wrap");

				mainPanel.add(imagePanel, "gapRight 5, align left");
				mainPanel.add(labelPanel, "growx");

				int result = JOptionPane.showConfirmDialog(null, mainPanel, "About", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE);
			}

			recreateMusicSquareButtons();

		}

	}

}