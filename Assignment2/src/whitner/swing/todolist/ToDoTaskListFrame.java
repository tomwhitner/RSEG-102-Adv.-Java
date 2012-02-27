package whitner.swing.todolist;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import whitner.swing.GBC;

public class ToDoTaskListFrame extends JFrame {

	public ToDoTaskListFrame() {

		// set frame title
		setTitle(TITLE);

		// set frame size
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		// use a grid layout
		setLayout(new GridBagLayout());

		tree = createTaskTree();
		add(new JScrollPane(tree), new GBC(0, 0).setFill(GBC.BOTH).setWeight(100, 100));

		add(createEditPanel(), new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
		
		add(createActionPanel(), new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0));
	}

	private JTree tree = null;
	private final JTextField taskTextField = new JTextField(20);
	private final JComboBox priorityComboBox = new JComboBox();
	
	private JTree createTaskTree() {
		
		// create root node
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Tasks");
		
		// create model
		DefaultTreeModel model = new DefaultTreeModel(rootNode);

		// create the tree
		JTree tree = new JTree(model);
		
		tree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("e = " + e );
			}
			
		});

		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		DefaultMutableTreeNode priorityNode = null;
		// add the priority nodes to the root
		for (int i = 1; i <= numberOfPriorities; i++) {
			priorityNode = new DefaultMutableTreeNode("P" + i);
			model.insertNodeInto(priorityNode, rootNode, i-1);
		}
		
		// display new node
		tree.scrollPathToVisible(new TreePath(priorityNode.getPath()));
		
		return tree;
	}


	private JPanel createEditPanel() {
		
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

		panel.setBorder(BorderFactory.createEtchedBorder());

		return panel;
	}

	private JPanel createActionPanel() {
		
		JPanel panel = new JPanel();

		panel.add(new JButton(new AddAction()));
		panel.add(new JButton(new UpdateAction()));
		panel.add(new JButton(new DeleteAction()));

		panel.setBorder(BorderFactory.createEtchedBorder());

		return panel;
	}

	private static final String TITLE = "To-Do Task List";
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 400;
	private final int numberOfPriorities = 10;

	private DefaultMutableTreeNode getPriorityNode(int priority) {
		return (DefaultMutableTreeNode) getModel().getChild(getModel().getRoot(), priority-1);
	}
	
	private DefaultTreeModel getModel() 
	{
		return (tree == null ? null : (DefaultTreeModel)(tree.getModel()));
	}

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
			DefaultMutableTreeNode parent = getPriorityNode(priority);

			// insert new node in model
			getModel().insertNodeInto(newTaskNode, parent, parent.getChildCount());
			
			selectAndDisplayNode(newTaskNode);			
		}
	}
	
	private void selectAndDisplayNode(DefaultMutableTreeNode node) {
		
		TreePath path = new TreePath(node.getPath());
		
		// select node
		tree.setSelectionPath(path);

		// display node
		tree.scrollPathToVisible(path);
		
	}
	
	private class DeleteAction extends AbstractAction {
		
		public DeleteAction() {
			super("Delete");
		}

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
				if (parentNode == getModel().getRoot())
					return;

				// remove the node from the model
				getModel().removeNodeFromParent(selectedNode);
				
				selectAndDisplayNode(parentNode);

			}

		}

	}

	private class UpdateAction extends AbstractAction {
		
		public UpdateAction() {
			super("Update");
		}

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
				if (parentNode == getModel().getRoot()) return;
				
				// remove the node from the model
				getModel().removeNodeFromParent(selectedNode);
				
				// get task text
				String task = taskTextField.getText();
				
				// clear text field
				taskTextField.setText("");
				
				// update the text
				selectedNode.setUserObject(task);

				// get task priority
				Integer priority = (Integer) priorityComboBox.getSelectedItem();

				// locate parent priority node
				DefaultMutableTreeNode parent = getPriorityNode(priority);

				// insert new node in model
				getModel().insertNodeInto(selectedNode, parent, parent.getChildCount());

				selectAndDisplayNode(selectedNode);			

			}
		}
	}

}
