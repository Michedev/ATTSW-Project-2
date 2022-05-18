package edu.mikedev.app.task_manager_v2.data;

import java.util.HashSet;
import java.util.Set;

public class User {
	
	private int id;
	private String username;
	private String password;
	private String email;
	private Set<Task> tasks;
	
	public User() {}

	public User(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.tasks = new HashSet<>();
	}


	public User(String username, String password, String email, Set<Task> tasks) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.tasks = tasks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
}
