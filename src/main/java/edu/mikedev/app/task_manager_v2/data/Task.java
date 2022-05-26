package edu.mikedev.app.task_manager_v2.data;

import java.util.Objects;

public class Task {
	
	private int id;
	private String title;
	private String subtask1;
	private String subtask2;
	private String subtask3;
	private User taskOwner;
	
	public Task() {}

	public Task(String title, String subtask1, String subtask2, String subtask3) {
		super();
		this.title = title;
		this.subtask1 = subtask1;
		this.subtask2 = subtask2;
		this.subtask3 = subtask3;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtask1() {
		return subtask1;
	}

	public void setSubtask1(String subtask1) {
		this.subtask1 = subtask1;
	}

	public String getSubtask2() {
		return subtask2;
	}

	public void setSubtask2(String subtask2) {
		this.subtask2 = subtask2;
	}

	public String getSubtask3() {
		return subtask3;
	}

	public void setSubtask3(String subtask3) {
		this.subtask3 = subtask3;
	}

	public User getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(User taskOwner) {
		this.taskOwner = taskOwner;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return Objects.equals(title, task.title) && Objects.equals(subtask1, task.subtask1) && Objects.equals(subtask2, task.subtask2) && Objects.equals(subtask3, task.subtask3);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, subtask1, subtask2, subtask3);
	}
}
