package whitner.swing.textpad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

public class TextPadFrame extends JFrame {

	public TextPadFrame() {

		// set frame title
		setTitle(TITLE);

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// create menu
		createMenu();

		// create toolbar
		createToolbar();
		
		fontsizeComboBox.addItem(12);
		fontsizeComboBox.addItem(14);
		fontsizeComboBox.addItem(16);
		
		this.add(textArea, BorderLayout.SOUTH);
	}

	private void createMenu() {
		
		JMenu formatMenu = new JMenu("Format");
		
		JMenu colorMenu = new JMenu("Color");
		colorMenu.add(blueAction);
		colorMenu.add(redAction);
		colorMenu.add(greenAction);
		
		JMenu fontMenu = new JMenu("Font");
		fontMenu.add(arialAction);
		fontMenu.add(timesNewRomanAction);
		fontMenu.add(courierAction);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutAction);
		
		formatMenu.add(colorMenu);
		formatMenu.add(fontMenu);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(formatMenu);
		menuBar.add(helpMenu);
		
		this.setJMenuBar(menuBar);
	}

	private void createToolbar() {
		
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(blueAction);
		toolBar.add(redAction);
		toolBar.add(greenAction);
		
		toolBar.addSeparator();
		
		toolBar.add(arialAction);
		toolBar.add(timesNewRomanAction);
		toolBar.add(courierAction);

		toolBar.addSeparator();

		toolBar.add(fontsizeComboBox);
		
		this.add(toolBar, BorderLayout.NORTH);
	}
	
	private final TextArea textArea = new TextArea(SAMPLE_TEXT);
	private final JComboBox fontsizeComboBox = new JComboBox();

	private final ColorAction blueAction = new ColorAction("Blue", new ImageIcon(TextPadFrame.class.getResource("images/1018-point_blue.png")), "Change text color to blue", Color.BLUE);
	private final ColorAction redAction = new ColorAction("Red", new ImageIcon(TextPadFrame.class.getResource("images/1019-point_red.png")), "Change text color to red", Color.RED);
	private final ColorAction greenAction = new ColorAction("Green", new ImageIcon(TextPadFrame.class.getResource("images/1020-point_green.png")), "Change text color to green", Color.GREEN);
	private final FontAction arialAction = new FontAction("Arial", null, "Change font to Arial", "Arial");
	private final FontAction timesNewRomanAction = new FontAction("Times New Roman", null, "Change font to Times New Roman", "Times New Roman");
	private final FontAction courierAction = new FontAction("Courier", null, "Change font to Courier", "Courier");
	
	private final AbstractAction aboutAction = new AbstractAction ("About") {

		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println(getValue(Action.NAME) + " selected.");
		}
	};
			
	private static final String SAMPLE_TEXT = "Now is the time for all good men to come to the aid of their country.";
	private static final String TITLE = "Text Pad";
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 400;
	private static final long serialVersionUID = 1L;

	private class ColorAction extends AbstractAction {
		public ColorAction(String name, Icon icon, String tooltip, Color c) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Color", c);
		}

		public void actionPerformed(ActionEvent event) {
			System.out.println(getValue(Action.NAME) + " selected.");
			textArea.setForeground((Color)getValue("Color"));
		}
	}
	
	private class FontAction extends AbstractAction {
		public FontAction(String name, Icon icon, String tooltip, String fontName) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Font", fontName);
		}

		public void actionPerformed(ActionEvent event) {
			System.out.println(getValue(Action.NAME) + " selected.");
			Integer fontSize = (Integer) fontsizeComboBox.getSelectedItem();
			Font f = new Font((String)getValue("Font"), Font.PLAIN, fontSize);
			textArea.setFont(f);
		}
	}
}
