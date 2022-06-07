package edu.mikedev.app.task_manager_v2.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
	
	private int id;
	private String username;
	private String password;
	private String email;
	private List<Task> tasks;
	
	public User() {}

	public User(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.tasks = new ArrayList<>();
	}


	public User(String username, String password, String email, List<Task> tasks) {
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

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(tasks, user.tasks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password, email, tasks);
	}
}
