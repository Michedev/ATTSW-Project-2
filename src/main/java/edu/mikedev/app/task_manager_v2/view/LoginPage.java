package edu.mikedev.app.task_manager_v2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPage extends JPanel {
	private static final long serialVersionUID = 4732237797397541277L;
	
	private final JLabel lblLoginError;
	private final JButton btnLogin;
	private final JButton btnRegister;
	private JTextField txtUsername;
	private JTextField txtPassword;
	public LoginPage() {
				
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setName("lblUsername");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.gridwidth = 2;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 0);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 0;
		add(lblUsername, gbc_lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setName("txtUsername");
		GridBagConstraints gbc_txtUsername = new GridBagConstraints();
		gbc_txtUsername.gridwidth = 2;
		gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsername.gridx = 0;
		gbc_txtUsername.gridy = 1;
		add(txtUsername, gbc_txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setName("lblPassword");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.gridwidth = 2;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 0);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		add(lblPassword, gbc_lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setName("txtPassword");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 2;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 0);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 0;
		gbc_txtPassword.gridy = 3;
		add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);

		lblLoginError = new JLabel("Error message");
		lblLoginError.setName("lblLoginError");
		lblLoginError.setForeground(Color.RED);
		lblLoginError.setVisible(false);
		GridBagConstraints gbc_lblLoginError = new GridBagConstraints();
		gbc_lblLoginError.gridwidth = 2;
		gbc_lblLoginError.insets = new Insets(0, 0, 5, 5);
		gbc_lblLoginError.gridx = 0;
		gbc_lblLoginError.gridy = 4;
		add(lblLoginError, gbc_lblLoginError);

		btnLogin = new JButton("Login");
		btnLogin.setName("btnLogin");
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 5;
		add(btnLogin, gbc_btnLogin);

		btnRegister = new JButton("Register");
		btnRegister.setName("btnRegister");
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 5;
		add(btnRegister, gbc_btnRegister);
	}

	public String getUsername() {
		return txtUsername.getText();
	}

	public String getPassword() {
		return txtPassword.getText();
	}

	public void setErrorLabelText(String errorMessage) {
		lblLoginError.setVisible(true);
		lblLoginError.setText(errorMessage);
	}

	public void addActionListenerBtnLogin(ActionListener l){
		btnLogin.addActionListener(l);
	}

	public void addActionListenerBtnRegister(ActionListener l){
		btnRegister.addActionListener(l);
	}

}
