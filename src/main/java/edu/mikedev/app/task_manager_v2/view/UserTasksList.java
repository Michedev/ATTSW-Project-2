package edu.mikedev.app.task_manager_v2.view;

import javax.swing.JPanel;

import edu.mikedev.app.task_manager_v2.data.Task;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.util.List;

public class UserTasksList extends JPanel {
	public UserTasksList(List<Task> tasks) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
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
			}
//		
//		JLabel lblTask = new JLabel("Task 1");
//		GridBagConstraints gbc_lblTask = new GridBagConstraints();
//		gbc_lblTask.insets = new Insets(0, 0, 5, 5);
//		gbc_lblTask.gridx = 0;
//		gbc_lblTask.gridy = 0;
//		add(lblTask, gbc_lblTask);
//		
//		JButton btnDetail = new JButton("Detail");
//		GridBagConstraints gbc_btnDetail = new GridBagConstraints();
//		gbc_btnDetail.insets = new Insets(0, 0, 5, 0);
//		gbc_btnDetail.gridx = 1;
//		gbc_btnDetail.gridy = 0;
//		add(btnDetail, gbc_btnDetail);
//		
		JButton btnNew = new JButton("New");
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.gridwidth = 2;
		gbc_btnNew.insets = new Insets(0, 0, 0, 5);
		gbc_btnNew.gridx = 0;
		gbc_btnNew.gridy = tasks.size();
		btnNew.setName("btnNew");
		add(btnNew, gbc_btnNew);
	}
}