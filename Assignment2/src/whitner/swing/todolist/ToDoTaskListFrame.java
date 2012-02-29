package whitner.swing.todolist;

import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import whitner.swing.GBC;

/*
 * This class implements the To-Do List frame.
 */
public class ToDoTaskListFrame extends JFrame {
	
	/*
	 * Standard Java Swing start-up method
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ToDoTaskListFrame frame = new ToDoTaskListFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	// components that are referenced by actions
	private JTree tree = null;
	private final JTextField taskTextField = new JTextField(20);
	private final JComboBox priorityComboBox = new JComboBox();
	
	// actions that are enabled/disabled
	private UpdateAction updateAction = new UpdateAction();
	private DeleteAction deleteAction = new DeleteAction();

	// constants
	private static final String TITLE = "To-Do Task List";
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 400;
	private static final int numberOfPriorities = 10;

	/*
	 * constructor
	 */
	public ToDoTaskListFrame() {

		// set frame title
		setTitle(TITLE);

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// use a grid layout
		setLayout(new GridBagLayout());

		// create the main tree
		tree = createTaskTree();
		
		// add the tree to the frame w/i a scroll pane
		add(new JScrollPane(tree), new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100));

		// create and add the panel w/ edit controls
		add(createEditPanel(), new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
		
		// create and add the panel w/ action controls
		add(createActionPanel(), new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0));
	}

	/*
	 * Creates the task tree UI component
	 */
	private JTree createTaskTree() {
		
		// create root node
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Tasks");
		
		// create model
		DefaultTreeModel model = new DefaultTreeModel(rootNode);

		// create the tree
		JTree tree = new JTree(model);
		
		// allow only single selection
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		/*
		 * Class to manage UI behavior on selection
		 * Update task text, enable/disable buttons
		 */
		tree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				// locate selected nodes
				SelectedNodes selectedNodes = getSelectedNodes();
				DefaultMutableTreeNode taskNode = selectedNodes.getTaskNode();
				
				boolean taskIsSelected = taskNode != null;
				
				// enable/disable actions based on task selection
				updateAction.setEnabled(taskIsSelected);
				deleteAction.setEnabled(taskIsSelected);
				
				// display or clear task text
				String taskText = "";
				if (taskIsSelected) {
					taskText = (String)taskNode.getUserObject();
				} 
				ToDoTaskListFrame.this.taskTextField.setText(taskText);
			}		
		});

		DefaultMutableTreeNode priorityNode = null;
		
		// add the priority nodes to the root
		for (int i = 1; i <= numberOfPriorities; i++) {
			priorityNode = new DefaultMutableTreeNode("P" + i);
			model.insertNodeInto(priorityNode, rootNode, i-1);
		}
		
		// display the tree by showing last added node
		tree.scrollPathToVisible(new TreePath(priorityNode.getPath()));
		
		return tree;
	}

	/*
	 * Creates the edit panel with task text and priority components
	 */
	private JPanel createEditPanel() {
		
		// create the panel
		JPanel panel = new JPanel();

		// create and add the task label
		JLabel taskLabel = new JLabel("Task");
		panel.add(taskLabel);

		// add the text field
		panel.add(taskTextField);

		// create and add the priority label
		JLabel priorityLabel = new JLabel("Priority");
		panel.add(priorityLabel);

		// add the priorities to the combo box
		for (int i = 1; i <= numberOfPriorities; i++) {
			priorityComboBox.addItem(i);
		}
		// add the combo box to the panel
		panel.add(priorityComboBox);

		return panel;
	}
	
	/*
	 * Creates the Action panel containing Add, Update, and Delete buttons
	 */
	private JPanel createActionPanel() {
		
		// create the panel
		JPanel panel = new JPanel();

		// Add the buttons with related actions
		panel.add(new JButton(new AddAction()));
		panel.add(new JButton(updateAction));
		panel.add(new JButton(deleteAction));

		return panel;
	}

	/*
	 * Class implements Add action.  Adds new task to the tree.
	 */
	private class AddAction extends AbstractAction {

		public AddAction() {
			super("Add");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {

			// get task text
			String task = taskTextField.getText();
			
			// clear text field
			taskTextField.setText("");

			// get task priority
			Integer priority = (Integer) priorityComboBox.getSelectedItem();

			// create new task node
			DefaultMutableTreeNode newTaskNode = new DefaultMutableTreeNode(task);

			// locate parent priority node
			DefaultMutableTreeNode priorityNode = getPriorityNode(priority);

			// insert new node in model
			getModel().insertNodeInto(newTaskNode, priorityNode, priorityNode.getChildCount());
			
			selectAndDisplayNode(newTaskNode);			
		}
	}
		
	/*
	 * Class implements Delete action.  Removes selected task from the tree.
	 */
	private class DeleteAction extends AbstractAction {
		
		public DeleteAction() {
			super("Delete");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			// locate selected nodes
			SelectedNodes selectedNodes = getSelectedNodes();
			DefaultMutableTreeNode taskNode = selectedNodes.getTaskNode();
			
			// if their is a task selected
			if (taskNode != null) {
				
				// remove the node from the model
				getModel().removeNodeFromParent(taskNode);
				
				selectAndDisplayNode(selectedNodes.getPriorityNode());
			}
		}
	}

	/*
	 * Class implements Update action.  Updates task text and priority.
	 */
	private class UpdateAction extends AbstractAction {
		
		public UpdateAction() {
			super("Update");
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// locate selected nodes
			SelectedNodes selectedNodes = getSelectedNodes();
			DefaultMutableTreeNode taskNode = selectedNodes.getTaskNode();
			
			// if their is a task selected
			if (taskNode != null) {
				
				// get task text
				String task = taskTextField.getText();
				
				// remove the node from the model
				getModel().removeNodeFromParent(taskNode);
				
				// update the text
				taskNode.setUserObject(task);

				// get task priority
				Integer priority = (Integer) priorityComboBox.getSelectedItem();

				// locate parent priority node
				DefaultMutableTreeNode newPriorityNode = getPriorityNode(priority);

				// insert new node in model
				getModel().insertNodeInto(taskNode, newPriorityNode, newPriorityNode.getChildCount());

				selectAndDisplayNode(taskNode);			
			}
		}
	}
	
	/*
	 * Utility method to get the currently selected nodes of interest (priority and task)
	 */
	private SelectedNodes getSelectedNodes() {
		
		// locate selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		// proceed if a node is selected and it has a parent
		if (selectedNode != null && selectedNode.getParent() != null) {

			// get the parent node
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
			
			// if parent is root, selected is priority
			if (parentNode == getModel().getRoot()) {
				return new SelectedNodes(selectedNode, null);
			} else { // parent is priority; selected is task
				return new SelectedNodes(parentNode, selectedNode);
			}				
		}
		
		// neither priority nor task node is selected
		return new SelectedNodes(null, null);
	}
	
	/*
	 * Class for returning selected node information
	 */
	private class SelectedNodes {
		
		public SelectedNodes(DefaultMutableTreeNode priorityNode, DefaultMutableTreeNode taskNode) {
			this.priorityNode = priorityNode;
			this.taskNode = taskNode;
		}
		
		public DefaultMutableTreeNode getPriorityNode() {
			return priorityNode;
		}
		
		public DefaultMutableTreeNode getTaskNode() {
			return taskNode;
		}
		
		private DefaultMutableTreeNode taskNode;		
		private DefaultMutableTreeNode priorityNode;
	}
	
	/*
	 * Utility method to get the specified priority node
	 */
	private DefaultMutableTreeNode getPriorityNode(int priority) {
		return (DefaultMutableTreeNode) getModel().getChild(getModel().getRoot(), priority - 1);
	}
	
	/*
	 * Utility method to get the model from the tree
	 */
	private DefaultTreeModel getModel() {
		return tree == null ? null : (DefaultTreeModel)tree.getModel();
	}

	/*
	 * Utility method to select and display the specified node
	 */
	private void selectAndDisplayNode(DefaultMutableTreeNode node) {
		
		TreePath path = new TreePath(node.getPath());
		
		// select node
		tree.setSelectionPath(path);

		// display node
		tree.scrollPathToVisible(path);	
	}
}
