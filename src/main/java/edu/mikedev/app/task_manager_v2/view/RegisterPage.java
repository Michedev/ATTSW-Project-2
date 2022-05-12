package edu.mikedev.app.task_manager_v2.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JButton;

public class RegisterPage extends JPanel {
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
		
		JLabel lblUsernameError = new JLabel("Username Error");
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
		
		JLabel lblPasswordError = new JLabel("Password Error");
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
		txtEmail.setName("txtEmail");
		txtEmail.setVisible(false);
		GridBagConstraints gbc_txtEmail = new GridBagConstraints();
		gbc_txtEmail.insets = new Insets(0, 0, 5, 0);
		gbc_txtEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmail.gridx = 0;
		gbc_txtEmail.gridy = 7;
		add(txtEmail, gbc_txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblEmailError = new JLabel("Email Error");
		lblEmailError.setVisible(false);
		lblEmailError.setName("lblEmailError");
		lblEmailError.setForeground(Color.RED);
		GridBagConstraints gbc_lblEmailError = new GridBagConstraints();
		gbc_lblEmailError.insets = new Insets(0, 0, 5, 0);
		gbc_lblEmailError.gridx = 0;
		gbc_lblEmailError.gridy = 8;
		add(lblEmailError, gbc_lblEmailError);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setName("btnConfirm");
		GridBagConstraints gbc_btnConfirm = new GridBagConstraints();
		gbc_btnConfirm.gridx = 0;
		gbc_btnConfirm.gridy = 9;
		add(btnConfirm, gbc_btnConfirm);
	}

}
