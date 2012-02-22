package week6.discussions.textareas;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class TextAreaDemoApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TextAreaDemoFrame frame = new TextAreaDemoFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
