package whitner.swing.todolist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class ToDoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public ToDoFrame() {
		
		// set frame title
		setTitle("TextArea Demo");

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		createTree();
		
		//createPanel();
	}
	

	
	

	private final int numberOfPriorities = 10;
	private final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tasks");
	private final Map<Integer, DefaultMutableTreeNode> priorityNodes = new HashMap<Integer, DefaultMutableTreeNode>(numberOfPriorities);

	private void createTree() {

		for (int i = 1; i <= numberOfPriorities; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("P" + i);
			priorityNodes.put(i, node);
			root.add(node);
		}

		JTree tree = new JTree(root);
		JScrollPane scrollPane = new JScrollPane(tree);
		this.add(scrollPane, BorderLayout.NORTH);
	}

	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 200;
}
