package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

import java.util.List;

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
    public void addEvents() {
        List<Task> tasks = view.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            final Task task = tasks.get(i);
            view.addActionListenerTaskDetail(i, (e) -> onClickDetailButton(task));
        }
        view.addActionListenerNewButton((e) -> onClickNewTaskButton());
    }

    @Override
    public UserTasksList getView() {
        return view;
    }

    public void onClickDetailButton(Task task) {
        TaskDetail taskDetailView = new TaskDetail(task);
        TaskDetailController viewController = new TaskDetailController(model, taskDetailView, managerController);
        managerController.setViewController(viewController);
    }

    public void onClickNewTaskButton() {
        NewUpdateTask newUpdateTask = new NewUpdateTask();
        NewUpdateTaskController viewController = new NewUpdateTaskController(model, newUpdateTask, managerController);
        managerController.setViewController(viewController);
    }

    public void onClickDeleteUserButton() {

    }
}
