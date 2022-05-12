package edu.mikedev.app.task_manager_v2.data;

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
	
}
