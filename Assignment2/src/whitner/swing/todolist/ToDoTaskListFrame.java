package whitner.swing.todolist;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

public class ToDoFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public ToDoFrame() {
		
		// set frame title
		setTitle("TextArea Demo");

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		createTree();
		
		createButtonPanel();

	}
	
	private void createButtonPanel() {
		JPanel panel = new JPanel();
		
		
		JLabel taskLabel = new JLabel("Task");
		panel.add(taskLabel);
		JTextField taskTextField = new JTextField(20);
		panel.add(taskTextField);
		
		JLabel priorityLabel = new JLabel("Priority");
		panel.add(priorityLabel);
		JComboBox priorityComboBox = new JComboBox();
		for (int i = 1; i <= numberOfPriorities; i++) {
			priorityComboBox.addItem(i);
		}
		panel.add(priorityComboBox);

		JButton addButton = new JButton("Add");
		panel.add(addButton);
		JButton updateButton = new JButton("Update");
		panel.add(updateButton);
		JButton deleteButton = new JButton("Delete");
		panel.add(deleteButton);
		
		Border border = BorderFactory.createEtchedBorder();
		panel.setBorder(border);
		
		this.add(panel, BorderLayout.SOUTH);
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
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 200;
}
