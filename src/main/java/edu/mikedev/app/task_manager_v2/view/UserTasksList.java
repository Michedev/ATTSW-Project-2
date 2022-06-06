package edu.mikedev.app.task_manager_v2.view;

import edu.mikedev.app.task_manager_v2.data.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserTasksList extends JPanel {
	private static final long serialVersionUID = -1418499913982165702L;

	private final transient List<Task> tasks;
	private final JButton btnNew;
	private final JLabel lblError;
	private List<JButton> buttons;

	public UserTasksList(List<Task> tasks) {
		this.tasks = tasks;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		buttons = new ArrayList<>();
		for(int i = 0; i < tasks.size(); i++) {
			JLabel lblTitleTask = new JLabel(tasks.get(i).getTitle());
			GridBagConstraints gbc_lblTask = new GridBagConstraints();
			gbc_lblTask.insets = new Insets(0, 0, 5, 5);
			gbc_lblTask.gridx = 0;
			gbc_lblTask.gridy = i;
			lblTitleTask.setName("lblTitleTask" + (i+1));
			add(lblTitleTask, gbc_lblTask);
			
			JButton btnDetailTask = new JButton("Detail");
			GridBagConstraints gbc_btnDetail = new GridBagConstraints();
			gbc_btnDetail.insets = new Insets(0, 0, 5, 0);
			gbc_btnDetail.gridx = 1;
			gbc_btnDetail.gridy = i;
			btnDetailTask.setName("btnDetailTask" + (i+1));
			add(btnDetailTask, gbc_btnDetail);
			buttons.add(btnDetailTask);
		}

		lblError = new JLabel("Error message");
		lblError.setName("lblError");
		lblError.setForeground(Color.RED);
		lblError.setVisible(false);
		GridBagConstraints gbc_lblLoginError = new GridBagConstraints();
		gbc_lblLoginError.gridwidth = 2;
		gbc_lblLoginError.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoginError.gridx = 0;
		gbc_lblLoginError.gridy = tasks.size();
		add(lblError, gbc_lblLoginError);


		btnNew = new JButton("New");
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.gridwidth = 2;
		gbc_btnNew.insets = new Insets(0, 0, 0, 5);
		gbc_btnNew.gridx = 0;
		gbc_btnNew.gridy = tasks.size()+1;
		btnNew.setName("btnNew");
		add(btnNew, gbc_btnNew);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void addActionListenerTaskDetail(int i, ActionListener listener) {
		JButton button = buttons.get(i);
		button.addActionListener(listener);
	}

	public void addActionListenerNewButton(ActionListener listener) {
		btnNew.addActionListener(listener);
	}

	public void setErrorMessage(String message) {
		lblError.setVisible(true);
		lblError.setText(message);
	}

	public JLabel getLabelError() {
		return lblError;
	}
}
