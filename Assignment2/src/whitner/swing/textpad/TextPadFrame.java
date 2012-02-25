package whitner.swing.textpad;

import javax.swing.JFrame;

public class TextPadFrame  extends JFrame {

	public TextPadFrame() {
		
	// set frame title
	setTitle(TITLE);

	// set frame size
	setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public static final String TITLE = "Text Pad";
	public static final int DEFAULT_WIDTH = 480;
	public static final int DEFAULT_HEIGHT = 400;

}
