package whitner.swing.todolist;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ToDoList {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ToDoFrame frame = new ToDoFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}