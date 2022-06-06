package edu.mikedev.app.task_manager_v2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterPage extends JPanel {
	private static final long serialVersionUID = 6957886775685813752L;

	private final JLabel lblPasswordError;
	private final JLabel lblUsernameError;
	private final JLabel lblEmailError;
	private final JButton btnConfirm;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtEmail;
	public RegisterPage() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setName("lblUsername");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 0);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		add(lblUsername, gbc_lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setName("txtUsername");
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 0;
		gbc_txtUsername.gridy = 1;
		add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);

		lblUsernameError = new JLabel("Username Error");
		lblUsernameError.setVisible(false);
		lblUsernameError.setForeground(Color.RED);
		lblUsernameError.setName("lblUsernameError");
		GridBagConstraints gbc_lblUsernameError = new GridBagConstraints();
		gbc_lblUsernameError.insets = new Insets(0, 0, 5, 0);
		gbc_lblUsernameError.gridx = 0;
		gbc_lblUsernameError.gridy = 2;
		add(lblUsernameError, gbc_lblUsernameError);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setName("lblPassword");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		add(lblPassword, gbc_lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setName("txtPassword");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 4;
		add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);

		lblPasswordError = new JLabel("Password Error");
		lblPasswordError.setVisible(false);
		lblPasswordError.setForeground(Color.RED);
		lblPasswordError.setName("lblPasswordError");
		GridBagConstraints gbc_lblPasswordError = new GridBagConstraints();
		gbc_lblPasswordError.insets = new Insets(0, 0, 5, 0);
		gbc_lblPasswordError.gridx = 0;
		gbc_lblPasswordError.gridy = 5;
		add(lblPasswordError, gbc_lblPasswordError);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setName("lblEmail");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 6;
		add(lblEmail, gbc_lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setText("");
		txtEmail.setName("txtEmail");
		GridBagConstraints gbc_txtEmail = new GridBagConstraints();
		gbc_txtEmail.insets = new Insets(0, 0, 5, 0);
		gbc_txtEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmail.gridx = 0;
		gbc_txtEmail.gridy = 7;
		add(txtEmail, gbc_txtEmail);
		txtEmail.setColumns(10);

		lblEmailError = new JLabel("Email Error");
		lblEmailError.setVisible(false);
		lblEmailError.setName("lblEmailError");
		lblEmailError.setForeground(Color.RED);
		GridBagConstraints gbc_lblEmailError = new GridBagConstraints();
		gbc_lblEmailError.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmailError.gridx = 0;
		gbc_lblEmailError.gridy = 8;
		add(lblEmailError, gbc_lblEmailError);

		btnConfirm = new JButton("Confirm");
		btnConfirm.setName("btnConfirm");
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 9;
		add(btnConfirm, gbc_btnConfirm);
	}

	public String getUsername() {
		return txtUsername.getText();
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	public String getEmail() {
		return txtEmail.getText();
	}

	public void setErrorLabelUsername(String errorMessage) {
		lblUsernameError.setText(errorMessage);
		lblUsernameError.setVisible(true);
	}

	public void setErrorLabelPassword(String errorMessage) {
		lblPasswordError.setText(errorMessage);
		lblPasswordError.setVisible(true);
	}

	public void setErrorLabelEmail(String errorMessage) {
		lblEmailError.setText(errorMessage);
		lblEmailError.setVisible(true);
	}

	public void addActionListenerConfirmBtn(ActionListener actionListener) {
		btnConfirm.addActionListener(actionListener);
	}
}
