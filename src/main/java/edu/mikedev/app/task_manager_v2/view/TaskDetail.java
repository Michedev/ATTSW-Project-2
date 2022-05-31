package edu.mikedev.app.task_manager_v2.view;

import edu.mikedev.app.task_manager_v2.data.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TaskDetail extends JPanel {

	private final JButton btnDelete;
	private final JButton btnUpdate;
	private final transient Task task;

	public TaskDetail(Task task) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTaskTitle = new JLabel(task.getTitle());
		lblTaskTitle.setName("lblTaskTitle");
		GridBagConstraints gbc_lblTaskTitle = new GridBagConstraints();
		gbc_lblTaskTitle.gridwidth = 2;
		gbc_lblTaskTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskTitle.gridx = 0;
		gbc_lblTaskTitle.gridy = 0;
		add(lblTaskTitle, gbc_lblTaskTitle);
		
		JLabel lblSubtask1 = new JLabel(task.getSubtask1());
		lblSubtask1.setName("lblSubtask1");
		GridBagConstraints gbc_lblSubtask1 = new GridBagConstraints();
		gbc_lblSubtask1.gridwidth = 2;
		gbc_lblSubtask1.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubtask1.gridx = 0;
		gbc_lblSubtask1.gridy = 2;
		add(lblSubtask1, gbc_lblSubtask1);
		
		JLabel lblSubtask2 = new JLabel(task.getSubtask2());
		lblSubtask2.setName("lblSubtask2");
		GridBagConstraints gbc_lblSubtask2 = new GridBagConstraints();
		gbc_lblSubtask2.gridwidth = 2;
		gbc_lblSubtask2.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubtask2.gridx = 0;
		gbc_lblSubtask2.gridy = 3;
		add(lblSubtask2, gbc_lblSubtask2);
		
		JLabel lblSubtask3 = new JLabel(task.getSubtask3());
		lblSubtask3.setName("lblSubtask3");
		GridBagConstraints gbc_lblSubtask3 = new GridBagConstraints();
		gbc_lblSubtask3.gridwidth = 2;
		gbc_lblSubtask3.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubtask3.gridx = 0;
		gbc_lblSubtask3.gridy = 4;
		add(lblSubtask3, gbc_lblSubtask3);

		btnUpdate = new JButton("Update");
		btnUpdate.setName("btnUpdate");
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 0, 5);
		gbc_btnUpdate.gridx = 0;
		gbc_btnUpdate.gridy = 6;
		add(btnUpdate, gbc_btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setName("btnDelete");
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 6;
		add(btnDelete, gbc_btnDelete);

		this.task = task;
	}

	public Task getTask() {
		return task;
	}

	public void addActionListenerUpdateButton(ActionListener actionListener) {
		btnUpdate.addActionListener(actionListener);
	}

	public void addActionListenerDeleteButton(ActionListener actionListener) {
		btnDelete.addActionListener(actionListener);
	}
}
