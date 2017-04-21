import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class Style
{
	public static final Color ACCENT_COLOR = new Color(13, 71, 161);
	public static final Color PRIMARY_BACKBROUND_COLOR = Color.BLACK;
	public static final Color SECONDARY_BACKBROUND_COLOR = new Color(33, 33, 33);
	public static final Color BUTTON_SELECT_COLOR = new Color(75, 75, 75);
	
	public static final Font LIST_FONT = new Font("monospaced", Font.PLAIN, 12);
	public static final Color PRIMARY_FONT_COLOR = Color.WHITE;
	public static final int LIST_ITEM_HEIGHT = 30;
	public static final Dimension SONG_INFO_PANEL_DIMENSION = new Dimension(0, 100);
	public static final Dimension MUSIC_SQUARE_DIMENSION = new Dimension(100, 100);
	
	public static final Font HEADING1_FONT = new Font("arial", Font.BOLD, 16);
	public static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
}
