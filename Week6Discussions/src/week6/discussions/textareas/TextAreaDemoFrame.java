package week6.discussions.textareas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextAreaDemoFrame extends JFrame {
	
	public TextAreaDemoFrame() {

		// set frame title
		setTitle("TextArea Demo");
		
		// set frame size
		this.setSize(400, 300);
		
		// Create a panel w/ default flow layout
		JPanel panel = new JPanel(); 
		
		// add panel to frame
		this.add(panel, BorderLayout.WEST); 
		
		// create text area
		JTextArea textArea = new JTextArea(8, 20);
		
		// enable multi-line
		textArea.setLineWrap(true);
		
		// change font to serif, bold, 16 point
		textArea.setFont(new Font("Serif", Font.BOLD, 16));
		
		// set text color to blue
		textArea.setForeground(Color.BLUE);
		
		// create a scroll pane for the text area
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		// add scroll pane to panel
		panel.add(scrollPane);
	}
}
