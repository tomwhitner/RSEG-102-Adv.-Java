package whitner.swing.textpad;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class TextPadApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TextPadFrame frame = new TextPadFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
