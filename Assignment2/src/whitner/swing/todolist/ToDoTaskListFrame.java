package whitner.swing.todolist;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ToDoTaskListFrame extends JFrame {

	public ToDoTaskListFrame() {

		// set frame title
		setTitle(TITLE);

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		createTaskTree();

		createButtonPanel();

	}

	private final JTextField taskTextField = new JTextField(20);
	private final JComboBox priorityComboBox = new JComboBox();

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
		addButton.addActionListener(new AddListener());
		panel.add(addButton);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new UpdateListener());
		panel.add(updateButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteListener());
		panel.add(deleteButton);

		Border border = BorderFactory.createEtchedBorder();
		panel.setBorder(border);

		this.add(panel, BorderLayout.SOUTH);
	}

	private final int numberOfPriorities = 10;
	private DefaultMutableTreeNode rootNode;
	private Map<Integer, DefaultMutableTreeNode> priorityNodes = new HashMap<Integer, DefaultMutableTreeNode>(
			numberOfPriorities);
	private DefaultTreeModel model = null;
	private JTree tree = null;

	private void createTaskTree() {

		rootNode = new DefaultMutableTreeNode("Tasks");

		// create the model
		model = new DefaultTreeModel(rootNode);

		// create the tree
		tree = new JTree(rootNode);

		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);

		// add the priority nodes to the root
		// also add to the map so they can be easily located later.
		for (int i = 1; i <= numberOfPriorities; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("P" + i);
			priorityNodes.put(i, node);
			rootNode.add(node);
		}

		// create a scroll pane for the tree
		JScrollPane scrollPane = new JScrollPane(tree);
		// add everything to the frame
		this.add(scrollPane, BorderLayout.CENTER);
	}

	private static final String TITLE = "To-Do Task List";
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 400;
	private static final long serialVersionUID = 1L;

	private class AddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// get task text
			String task = taskTextField.getText();

			// get task priority
			Integer priority = (Integer) priorityComboBox.getSelectedItem();

			// create new task node
			DefaultMutableTreeNode newTaskNode = new DefaultMutableTreeNode(
					task);

			// locate parent priority node
			DefaultMutableTreeNode parent = priorityNodes.get(priority);

			// insert new node in model
			int location = parent.getChildCount();
			model.insertNodeInto(newTaskNode, parent, location);
			// parent.add(newTaskNode);

			// display new node
			tree.scrollPathToVisible(new TreePath(newTaskNode.getPath()));

			// this should not be needed.
			tree.updateUI();
		}
	}

	private class DeleteListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// locate selected node
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			// proceed if a node is selected and it has a parent
			if (selectedNode != null && selectedNode.getParent() != null) {

				// get the parent node
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode
						.getParent();

				// if the parent is root then selected is a priority node
				// which should not be deleted, therefore, exit.
				if (parentNode == rootNode)
					return;

				// remove the node from the model
				model.removeNodeFromParent(selectedNode);

				// this should not be needed.
				tree.updateUI();

			}

		}

	}

	private class UpdateListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// locate selected node
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
					.getLastSelectedPathComponent();

			// proceed if a node is selected and it has a parent
			if (selectedNode != null && selectedNode.getParent() != null) {

				// get the parent node
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode
						.getParent();

				// if the parent is root then selected is a priority node
				// which should not be updated, therefore, exit.
				if (parentNode == rootNode) return;
				
				// remove the node from the model
				model.removeNodeFromParent(selectedNode);

				// get task priority
				Integer priority = (Integer) priorityComboBox.getSelectedItem();

				// locate parent priority node
				DefaultMutableTreeNode parent = priorityNodes.get(priority);

				// insert new node in model
				int location = parent.getChildCount();
				model.insertNodeInto(selectedNode, parent, location);
				// parent.add(newTaskNode);

				// display new node
				tree.scrollPathToVisible(new TreePath(selectedNode.getPath()));

				// this should not be needed.
				tree.updateUI();


			}
		}
	}

}
