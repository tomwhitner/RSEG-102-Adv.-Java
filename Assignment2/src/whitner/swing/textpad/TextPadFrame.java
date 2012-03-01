package whitner.swing.textpad;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import whitner.swing.GBC;

/*
 * This class implements the TextPad frame.
 */
public class TextPadFrame extends JFrame {

	/*
	 * Standard Java Swing start-up method
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TextPadFrame frame = new TextPadFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	// components that are referenced by actions / methods
	private final JComboBox fontsizeComboBox = createComboBox();
	private final JTextArea textArea = new JTextArea(SAMPLE_TEXT);

	// actions that are used more than once
	private Action[] colorActions = createColorActions();
	private Action[] fontNameActions = createFontNameActions();
	
	// constants
	private static final int DEFAULT_HEIGHT = 600;
	private static final int DEFAULT_WIDTH = 800;
	private static final String SAMPLE_TEXT = "Advanced Java Programming Swing Assignment written by Tom Whitner - \n" + 
			"this sample text specified at the time of creation is used for testing the module.";
	private static final String TITLE = "Text Pad";
	private static final String ABOUT_TITLE = "About " + TITLE;
	private static final String ABOUT_MESSAGE = "Text Pad Application written by Tom Whitner\n" + 
			"for 121RSEG-102-1DL: Advanced Programming in Java";

	// constructor
	public TextPadFrame() {

		// set frame title
		setTitle(TITLE);

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// use a grid layout
		setLayout(new GridBagLayout());

		// create menuBar
		setJMenuBar(createMenuBar());

		// create and add the toolbar
		add(createToolbar(), new GBC(0, 0).setAnchor(GBC.WEST).setWeight(0, 0));

		// create and add the scrollable text area
		add(new JScrollPane(textArea), new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 100));
		
		// select the default font
		fontNameActions[0].actionPerformed(null);
	}

	/*
	 * Creates the menu bar
	 */
	private JMenuBar createMenuBar() {

		// create the format menu
		JMenu formatMenu = new JMenu("Format");
		formatMenu.setMnemonic('F');

		// create the color sub-menu
		JMenu colorMenu = new JMenu("Color");
		colorMenu.setMnemonic('C');
		
		// add each color action to the format menu
		for (Action ca : colorActions) {
			colorMenu.add(ca);
		}

		// create the font sub-menu
		JMenu fontMenu = new JMenu("Font");
		fontMenu.setMnemonic('F');
		
		// add each font action to the font menu
		for (Action fna : fontNameActions) {
			fontMenu.add(fna);
		}

		// create the help sub-menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		
		// add the about action to the help menu
		helpMenu.add(new AbstractAction("About") {

			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(TextPadFrame.this, ABOUT_MESSAGE,
						ABOUT_TITLE, JOptionPane.PLAIN_MESSAGE);
			}
			});

		// the the sub-menus to the format menu
		formatMenu.add(colorMenu);
		formatMenu.add(fontMenu);

		// create the menu bar
		JMenuBar menuBar = new JMenuBar();
		
		// add the menus to the menu bar
		menuBar.add(formatMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	/*
	 * Creates the toolbar
	 */
	private JToolBar createToolbar() {

		// create the toolbar
		JToolBar toolBar = new JToolBar();

		// add each color action to the toolbar
		for (Action ca : colorActions) {
			toolBar.add(ca);
		}

		// add a separator
		toolBar.addSeparator();

		// add each font action to the toolbar
		for (Action fna : fontNameActions) {
			toolBar.add(fna);
		}

		// add a separator
		toolBar.addSeparator();

		// add the combo box to the toolbar
		toolBar.add(fontsizeComboBox);

		return toolBar;
	}
	
	/*
	 * Creates the font size combo box
	 */
	private JComboBox createComboBox() {
		
		// create the combo box
		JComboBox comboBox = new JComboBox();
		
		// add a few font sizes
		comboBox.addItem(12);
		comboBox.addItem(16);
		comboBox.addItem(20);

		// set the action to change font size
		comboBox.setAction(new FontSizeAction("FontSize", null, "Change the font size"));
		
		return comboBox;
	}

	/*
	 * Creates the array of possible color actions
	 */
	private Action[] createColorActions() {
		
		// allocate the array
		Action[] actions = new ColorAction[4];
		
		// create the actions and store in the array
		actions[0] = createColorAction("Blue", Color.BLUE,
				"images/1018-point_blue.png");
		actions[1] = createColorAction("Red", Color.RED,
				"images/1019-point_red.png");
		actions[2] = createColorAction("Green", Color.GREEN,
				"images/1020-point_green.png");
		actions[3] = createColorAction("Yellow", Color.YELLOW,
				"images/1017-point_yellow.png");
		
		return actions;
	}

	/*
	 * Utility method to create color actions
	 */
	private ColorAction createColorAction(String colorName, Color color,
			String iconFile) {
		return new ColorAction(colorName, new ImageIcon(
				TextPadFrame.class.getResource(iconFile)),
				"Change text color to " + colorName, color, colorName.charAt(0));
	}

	/*
	 * Creates the array of possible font actions
	 */
	private Action[] createFontNameActions() {
		
		// allocate the array
		Action[] actions = new FontNameAction[5];
		
		// create the actions and store in the array
		actions[0] = createFontNameAction("Arial",
				"images/2080-triangle_blue.png");
		actions[1] = createFontNameAction("Calibri",
				"images/2083-triangle_red.png");
		actions[2] = createFontNameAction("Courier",
				"images/2082-triangle_green.png");
		actions[3] = createFontNameAction("Times New Roman",
				"images/2084-triangle_yellow.png");
		actions[4] = createFontNameAction("Veranda",
				"images/2081-triangle_cyan.png");
		
		return actions;
	}
	
	/*
	 * Utility method to create font actions
	 */
	private FontNameAction createFontNameAction(String fontName, String iconFile) {
		return new FontNameAction(fontName, new ImageIcon(
				TextPadFrame.class.getResource(iconFile)), "Change font to "
				+ fontName, fontName, fontName.charAt(0));
	}

	/*
	 * Implements the color action.  Changes text area fore color.
	 */
	private class ColorAction extends AbstractAction {
		
		public ColorAction(String name, Icon icon, String tooltip, Color c, char mnemonic) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Color", c);
			putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
		}

		
		@Override
		public void actionPerformed(ActionEvent event) {
			
			// set the foreground color of the text area
			textArea.setForeground((Color) getValue("Color"));
		}
	}

	/*
	 * Implements the font name action.  Changes text area font.
	 */
	private class FontNameAction extends AbstractAction {
		
		public FontNameAction(String name, Icon icon, String tooltip,
				String fontName, char mnemonic) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Font", fontName);
			putValue(Action.MNEMONIC_KEY, new Integer(mnemonic));
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			
			// get the current font
			Font currentFont = textArea.getFont();
			
			// create a new font w/ new font name
			Font f = new Font((String) getValue("Font"), currentFont.getStyle(), currentFont.getSize());
			
			// set the new font
			textArea.setFont(f);
		}
	}

	/*
	 * Implements the font size action.  Changes text area font size.
	 */
	private class FontSizeAction extends AbstractAction {
		
		public FontSizeAction(String name, Icon icon, String tooltip) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			
			// get the new font size
			Integer fontSize = (Integer) fontsizeComboBox.getSelectedItem();
			
			// get the current font
			Font currentFont = textArea.getFont();
			
			// create a new font w/ new font name
			Font f = new Font(currentFont.getFontName(), currentFont.getStyle(), fontSize);
			
			// set the new font
			textArea.setFont(f);
		}
	}
}
