import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The driver class for MyTunes GUI.
 * 
 * @author CS121 Instructors
 * @version Spring 2017
 */
public class NRTunes
{
	/**
	 * Creates a JFrame and adds the main JPanel to the JFrame.
	 * @param args (unused)
	 */
	public static void main(String args[])
	{
		// So it looks consistent on Mac/Windows/Linux
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("NR Tunes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new NRTunesGUIPanel());
		frame.setPreferredSize(new Dimension(1200, 650));
		frame.setResizable(false);

		try
		{
			Image iconImage = ImageIO.read(NRTunes.class.getResourceAsStream("res/icon.png"));
			frame.setIconImage(iconImage);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		frame.pack();
		frame.setVisible(true);
		
	}

}
