package edu.mikedev.app.task_manager_v2.data;

import java.util.List;

public class DeleteTaskResponse {
    private List<Task> tasks;
    private int missingTaskId;

    public DeleteTaskResponse(List<Task> tasks, int missingTaskId) {
        this.tasks = tasks;
        this.missingTaskId = missingTaskId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getMissingTaskId() {
        return missingTaskId;
    }
}
