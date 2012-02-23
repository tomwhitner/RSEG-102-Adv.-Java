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

public class ToDoTaskListFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public ToDoTaskListFrame() {
		
		// set frame title
		setTitle("To-Do Task List");

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		createTaskTree();
		
		createButtonPanel();

	}
	
	private JTextField taskTextField = new JTextField(20);
	private JComboBox priorityComboBox = new JComboBox();
	
	private void createButtonPanel() {
		JPanel panel = new JPanel();
			
		JLabel taskLabel = new JLabel("Task");
		panel.add(taskLabel);
		
		panel.add(taskTextField);
		
		JLabel priorityLabel = new JLabel("Priority");
		panel.add(priorityLabel);
		
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

	private void createTaskTree() {

		// add the priority nodes to the root
		// also add to the map so they can be easily located later.
		for (int i = 1; i <= numberOfPriorities; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("P" + i);
			priorityNodes.put(i, node);  
			root.add(node);
		}

		// create the tree 
		JTree tree = new JTree(root);
		// create a scroll pane for the tree
		JScrollPane scrollPane = new JScrollPane(tree);
		// add everything to the frame
		this.add(scrollPane, BorderLayout.CENTER);
	}

	public static final int DEFAULT_WIDTH = 480;
	public static final int DEFAULT_HEIGHT = 400;
}
