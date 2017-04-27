import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * This class contains values and objects for styling the GUI panel.
 * 
 * @author Nabil Rahman
 *
 */
public final class Style
{
	public static final Color ACCENT_COLOR = new Color(0, 120, 215);
	public static final Color PRIMARY_BACKBROUND_COLOR = Color.BLACK;
	public static final Color SECONDARY_BACKBROUND_COLOR = new Color(33, 33, 33);
	public static final Color BUTTON_SELECT_COLOR = new Color(75, 75, 75);

	public static Font LIST_FONT;
	public static final Color PRIMARY_FONT_COLOR = Color.WHITE;
	public static final int LIST_ITEM_HEIGHT = 30;
	public static final Dimension SONG_INFO_PANEL_DIMENSION = new Dimension(0, 100);
	public static final Dimension MUSIC_SQUARE_DIMENSION = new Dimension(100, 100);

	public static Font DEFAULT_FONT = new Font("Dialog", Font.BOLD, 12);
	public static Font PRIMARY_FONT;

	public static Font HEADING1_FONT;
	public static Font HEADING2_FONT;

	public static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(10, 10, 15, 10);

	public static String PLAY_ICON = "\u25B6";
	public static String STOP_ICON = "\u25A0";
	public static String NEXT_ICON = "\u25B6" + "|";
	public static String PREVIOUS_ICON = "|" + "\u25C0";

	public static String ADD_ICON = "\u2795";
	public static String REMOVE_ICON = "\u2796";
	public static String MOVE_UP_ICON = "\u25B2";
	public static String MOVE_DOWN_ICON = "\u25BC";

	public static final Color COLOR1 = SECONDARY_BACKBROUND_COLOR;
	public static final Color COLOR2 = new Color(24, 54, 78);
	public static final Color COLOR3 = new Color(16, 76, 124);
	public static final Color COLOR4 = new Color(8, 98, 169);
	public static final Color COLOR5 = ACCENT_COLOR;

}
