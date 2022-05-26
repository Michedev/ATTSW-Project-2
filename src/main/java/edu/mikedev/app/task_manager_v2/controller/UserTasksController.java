package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

public class UserTasksController implements ViewController<UserTasksList>{
    private final TaskManagerController managerController;
    private final UserTasksList view;
    private final Model model;

    public UserTasksController(Model model, UserTasksList view, TaskManagerController managerController) {
        this.model = model;
        this.view = view;
        this.managerController = managerController;
    }

    @Override
    public void addEvents(UserTasksList view) {

    }

    @Override
    public UserTasksList getView() {
        return view;
    }

    public void onClickDetailPage(Task task) {

    }
}
