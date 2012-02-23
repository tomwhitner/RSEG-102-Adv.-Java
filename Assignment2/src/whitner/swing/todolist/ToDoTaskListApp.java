package whitner.swing.todolist;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ToDoTaskListApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ToDoTaskListFrame frame = new ToDoTaskListFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}