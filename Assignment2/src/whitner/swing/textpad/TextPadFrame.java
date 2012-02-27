package whitner.swing.textpad;

import java.awt.Color;
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

public class TextPadFrame extends JFrame {


	private static final int DEFAULT_HEIGHT = 600;
	private static final int DEFAULT_WIDTH = 800;
	private static final String SAMPLE_TEXT = "Advanced Java Programming Swing Assignment written by Tom Whitner - \n" + 
			"this sample text specified at the time of creation is used for testing the module.";
	private static final String TITLE = "Text Pad";
	private static final String ABOUT_TITLE = "About " + TITLE;
	private static final String ABOUT_MESSAGE = "Text Pad Application written by Tom Whitner\n" + 
			"for RSEG xxx";

	private Action[] colorActions = createColorActions();
	private Action[] fontNameActions = createFontNameActions();
	private final Action fontSizeAction = new FontSizeAction("FontSize", null, "Change the font size");
	private final Action aboutAction = new MyAbstractAction("About") {

		@Override
		public void doActionPerformed(ActionEvent event) {
			JOptionPane.showMessageDialog(TextPadFrame.this, ABOUT_MESSAGE,
					ABOUT_TITLE, JOptionPane.PLAIN_MESSAGE);
		}
	};
	
	private final JComboBox fontsizeComboBox = createComboBox();
	private final JTextArea textArea = new JTextArea(SAMPLE_TEXT);

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

	private JMenuBar createMenuBar() {

		JMenu formatMenu = new JMenu("Format");

		JMenu colorMenu = new JMenu("Color");
		for (Action ca : colorActions) {
			colorMenu.add(ca);
		}

		JMenu fontMenu = new JMenu("Font");
		for (Action fna : fontNameActions) {
			fontMenu.add(fna);
		}

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutAction);

		formatMenu.add(colorMenu);
		formatMenu.add(fontMenu);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(formatMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	private JToolBar createToolbar() {

		JToolBar toolBar = new JToolBar();

		for (Action ca : colorActions) {
			toolBar.add(ca);
		}

		toolBar.addSeparator();

		for (Action fna : fontNameActions) {
			toolBar.add(fna);
		}

		toolBar.addSeparator();

		toolBar.add(fontsizeComboBox);

		return toolBar;
	}
	
	private JComboBox createComboBox() {
		JComboBox comboBox = new JComboBox();
		comboBox.addItem(12);
		comboBox.addItem(16);
		comboBox.addItem(20);

		comboBox.setAction(fontSizeAction);
		return comboBox;
	}

	private Action[] createColorActions() {
		Action[] actions = new ColorAction[3];
		actions[0] = createColorAction("Blue", Color.BLUE,
				"images/1018-point_blue.png");
		actions[1] = createColorAction("Red", Color.RED,
				"images/1019-point_red.png");
		actions[2] = createColorAction("Green", Color.GREEN,
				"images/1020-point_green.png");
		return actions;
	}

	private ColorAction createColorAction(String colorName, Color color,
			String iconFile) {
		return new ColorAction(colorName, new ImageIcon(
				TextPadFrame.class.getResource(iconFile)),
				"Change text color to " + colorName, color);
	}

	private Action[] createFontNameActions() {
		Action[] actions = new FontNameAction[3];
		actions[0] = createFontNameAction("Arial",
				"images/2080-triangle_blue.png");
		actions[1] = createFontNameAction("Courier",
				"images/2083-triangle_red.png");
		actions[2] = createFontNameAction("Times New Roman",
				"images/2082-triangle_green.png");
		return actions;
	}

	private FontNameAction createFontNameAction(String fontName, String iconFile) {
		return new FontNameAction(fontName, new ImageIcon(
				TextPadFrame.class.getResource(iconFile)), "Change font to "
				+ fontName, fontName);
	}

	private abstract class MyAbstractAction extends AbstractAction {

		public MyAbstractAction(String name) {
			super(name);
		}

		public MyAbstractAction(String name, Icon icon) {
			super(name, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Action '" + getValue(Action.NAME)
					+ "' selected.");
			doActionPerformed(e);
		}

		public abstract void doActionPerformed(ActionEvent e);

	}

	private class ColorAction extends MyAbstractAction {
		public ColorAction(String name, Icon icon, String tooltip, Color c) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Color", c);
		}

		@Override
		public void doActionPerformed(ActionEvent event) {
			textArea.setForeground((Color) getValue("Color"));
		}
	}


	private class FontNameAction extends MyAbstractAction {
		public FontNameAction(String name, Icon icon, String tooltip,
				String fontName) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
			putValue("Font", fontName);
		}

		@Override
		public void doActionPerformed(ActionEvent event) {
			Font currentFont = textArea.getFont();
			Font f = new Font((String) getValue("Font"), currentFont.getStyle(), currentFont.getSize());
			textArea.setFont(f);
		}
	}

	private class FontSizeAction extends MyAbstractAction {
		public FontSizeAction(String name, Icon icon, String tooltip) {
			super(name, icon);
			putValue(Action.SHORT_DESCRIPTION, tooltip);
		}

		@Override
		public void doActionPerformed(ActionEvent event) {
			Integer fontSize = (Integer) fontsizeComboBox.getSelectedItem();
			Font currentFont = textArea.getFont();
			Font f = new Font(currentFont.getFontName(),
					currentFont.getStyle(), fontSize);
			textArea.setFont(f);
		}
	}
}
