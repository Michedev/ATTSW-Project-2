package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;

public class TaskDetailController implements ViewController<TaskDetail> {

    private final Model model;
    private final TaskDetail taskDetail;
    private final TaskManagerController managerController;

    public TaskDetailController(Model model, TaskDetail taskDetail, TaskManagerController managerController){
        this.model = model;
        this.taskDetail = taskDetail;
        this.managerController = managerController;
    }
    @Override
    public void addEvents() {

    }

    @Override
    public TaskDetail getView() {
        return taskDetail;
    }
}
