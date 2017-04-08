import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * The driver class for MyTunes GUI.
 * 
 * @author CS121 Instructors
 * @version Spring 2017
 */
public class MyTunesGUI
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

		JFrame frame = new JFrame("MyTunes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MyTunesGUIPanel());
		frame.setPreferredSize(new Dimension(1200, 650));
		frame.pack();
		frame.setVisible(true);
	}
}
