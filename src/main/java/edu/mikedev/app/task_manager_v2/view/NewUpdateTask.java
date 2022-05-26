package edu.mikedev.app.task_manager_v2.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import edu.mikedev.app.task_manager_v2.data.Task;

import java.awt.Insets;
import javax.swing.JButton;
import java.awt.Color;

public class NewUpdateTask extends JPanel {
	private JTextField txtTaskTitle;
	private JTextField txtStep1;
	private JTextField txtStep2;
	private JTextField txtStep3;
	private JButton btnMake;
	private Task taskToUpdate;
	private JLabel lblTitleErrorLabel;
	private JLabel lblStep1ErrorLabel;
	private JLabel lblStep2ErrorLabel;
	private JLabel lblStep3ErrorLabel;

	public NewUpdateTask() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTaskTitle = new JLabel("Title");
		lblTaskTitle.setName("lblTaskTitle");
		GridBagConstraints gbc_lblTaskTitle = new GridBagConstraints();
		gbc_lblTaskTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblTaskTitle.gridx = 0;
		gbc_lblTaskTitle.gridy = 0;
		add(lblTaskTitle, gbc_lblTaskTitle);
		
		txtTaskTitle = new JTextField();
		txtTaskTitle.setName("txtTaskTitle");
		GridBagConstraints gbc_txtTaskTitle = new GridBagConstraints();
		gbc_txtTaskTitle.insets = new Insets(0, 0, 5, 0);
		gbc_txtTaskTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTaskTitle.gridx = 0;
		gbc_txtTaskTitle.gridy = 1;
		add(txtTaskTitle, gbc_txtTaskTitle);
		txtTaskTitle.setColumns(10);
		
		lblTitleErrorLabel = new JLabel("TitleErrorLabel");
		lblTitleErrorLabel.setVisible(false);
		lblTitleErrorLabel.setForeground(Color.RED);
		lblTitleErrorLabel.setName("lblTitleErrorLabel");
		GridBagConstraints gbc_lblTitleErrorLabel = new GridBagConstraints();
		gbc_lblTitleErrorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitleErrorLabel.gridx = 0;
		gbc_lblTitleErrorLabel.gridy = 2;
		add(lblTitleErrorLabel, gbc_lblTitleErrorLabel);
		
		JLabel lblStep1 = new JLabel("Step 1");
		lblStep1.setName("lblStep1");
		GridBagConstraints gbc_lblStep1 = new GridBagConstraints();
		gbc_lblStep1.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep1.gridx = 0;
		gbc_lblStep1.gridy = 4;
		add(lblStep1, gbc_lblStep1);
		
		txtStep1 = new JTextField();
		txtStep1.setName("txtStep1");
		GridBagConstraints gbc_txtStep1 = new GridBagConstraints();
		gbc_txtStep1.insets = new Insets(0, 0, 5, 0);
		gbc_txtStep1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStep1.gridx = 0;
		gbc_txtStep1.gridy = 5;
		add(txtStep1, gbc_txtStep1);
		txtStep1.setColumns(10);
		
		lblStep1ErrorLabel = new JLabel("Step1ErrorLabel");
		lblStep1ErrorLabel.setVisible(false);
		lblStep1ErrorLabel.setForeground(Color.RED);
		lblStep1ErrorLabel.setName("lblStep1ErrorLabel");
		GridBagConstraints gbc_lblStep1ErrorLabel = new GridBagConstraints();
		gbc_lblStep1ErrorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep1ErrorLabel.gridx = 0;
		gbc_lblStep1ErrorLabel.gridy = 6;
		add(lblStep1ErrorLabel, gbc_lblStep1ErrorLabel);
		
		JLabel lblStep2 = new JLabel("Step 2");
		lblStep2.setName("lblStep2");
		GridBagConstraints gbc_lblStep2 = new GridBagConstraints();
		gbc_lblStep2.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep2.gridx = 0;
		gbc_lblStep2.gridy = 7;
		add(lblStep2, gbc_lblStep2);
		
		txtStep2 = new JTextField();
		txtStep2.setName("txtStep2");
		GridBagConstraints gbc_txtStep2 = new GridBagConstraints();
		gbc_txtStep2.insets = new Insets(0, 0, 5, 0);
		gbc_txtStep2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStep2.gridx = 0;
		gbc_txtStep2.gridy = 8;
		add(txtStep2, gbc_txtStep2);
		txtStep2.setColumns(10);
		
		lblStep2ErrorLabel = new JLabel("Step2ErrorLabel");
		lblStep2ErrorLabel.setVisible(false);
		lblStep2ErrorLabel.setForeground(Color.RED);
		lblStep2ErrorLabel.setName("lblStep2ErrorLabel");
		GridBagConstraints gbc_lblStep2ErrorLabel = new GridBagConstraints();
		gbc_lblStep2ErrorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep2ErrorLabel.gridx = 0;
		gbc_lblStep2ErrorLabel.gridy = 9;
		add(lblStep2ErrorLabel, gbc_lblStep2ErrorLabel);
		
		JLabel lblStep3 = new JLabel("Step 3");
		lblStep3.setName("lblStep3");
		GridBagConstraints gbc_lblStep3 = new GridBagConstraints();
		gbc_lblStep3.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep3.gridx = 0;
		gbc_lblStep3.gridy = 10;
		add(lblStep3, gbc_lblStep3);
		
		txtStep3 = new JTextField();
		txtStep3.setName("txtStep3");
		GridBagConstraints gbc_txtStep3 = new GridBagConstraints();
		gbc_txtStep3.insets = new Insets(0, 0, 5, 0);
		gbc_txtStep3.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStep3.gridx = 0;
		gbc_txtStep3.gridy = 11;
		add(txtStep3, gbc_txtStep3);
		txtStep3.setColumns(10);
		
		lblStep3ErrorLabel = new JLabel("Step3ErrorLabel");
		lblStep3ErrorLabel.setVisible(false);
		lblStep3ErrorLabel.setForeground(Color.RED);
		lblStep3ErrorLabel.setName("lblStep3ErrorLabel");
		GridBagConstraints gbc_lblStep3ErrorLabel = new GridBagConstraints();
		gbc_lblStep3ErrorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblStep3ErrorLabel.gridx = 0;
		gbc_lblStep3ErrorLabel.gridy = 12;
		add(lblStep3ErrorLabel, gbc_lblStep3ErrorLabel);
		
		btnMake = new JButton("Add");
		btnMake.setName("btnMake");
		GridBagConstraints gbc_btnMake = new GridBagConstraints();
		gbc_btnMake.gridx = 0;
		gbc_btnMake.gridy = 14;
		add(btnMake, gbc_btnMake);
	}
	public void setUpdateMode(Task task) {
		txtTaskTitle.setText(task.getTitle());
		txtStep1.setText(task.getSubtask1());
		txtStep2.setText(task.getSubtask2());
		txtStep3.setText(task.getSubtask3());
		btnMake.setText("Update");
		taskToUpdate =  task;
	}

	public Task getTaskToUpdate() {
		return taskToUpdate;
	}

	public String getTaskTitle() {
		return txtTaskTitle.getText();
	}

	public String getTaskSubtask1() {
		return txtStep1.getText();
	}

	public String getTaskSubtask2(){
		return txtStep2.getText();
	}

	public String getTaskSubtask3(){
		return txtStep3.getText();
	}

	public void setTitleErrorLabelText(String text) {
		lblTitleErrorLabel.setText(text);
	}

	public void setStep1ErrorLabelText(String text) {
		lblStep1ErrorLabel.setText(text);
	}

	public void setStep2ErrorLabelText(String text) {
		lblStep2ErrorLabel.setText(text);
	}

	public void setStep3ErrorLabelText(String text) {
		lblStep3ErrorLabel.setText(text);
	}


}
