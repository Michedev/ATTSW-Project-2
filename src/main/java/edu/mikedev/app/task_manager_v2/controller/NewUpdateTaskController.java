package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

public class NewUpdateTaskController implements ViewController<NewUpdateTask> {

    private final Model model;
    private final NewUpdateTask view;
    private final TaskManagerController managerController;

    public NewUpdateTaskController(Model model, NewUpdateTask view, TaskManagerController managerController){
        this.model = model;
        this.view = view;
        this.managerController = managerController;
    }

    @Override
    public void addEvents() {
        view.addActionListenerMakeButton((e) -> onClickMakeButton());
    }

    @Override
    public NewUpdateTask getView() {
        return view;
    }

    public void onClickMakeButton() {
        String taskTitle = view.getTaskTitle();
        String taskSubtask1 = view.getTaskSubtask1();
        String taskSubtask2 = view.getTaskSubtask2();
        String taskSubtask3 = view.getTaskSubtask3();
        boolean anyError = false;
        if(taskTitle.isEmpty()){
            view.setTitleErrorLabelText("Missing title");
            anyError = true;
        }
        if(taskSubtask1.isEmpty()){
            view.setStep1ErrorLabelText("Missing subtask 1");
            anyError = true;
        }
        if(taskSubtask2.isEmpty()){
            view.setStep2ErrorLabelText("Missing subtask 2");
            anyError = true;
        }
        if(taskSubtask3.isEmpty()){
            view.setStep3ErrorLabelText("Missing subtask 3");
            anyError = true;
        }
        if(anyError){
            return;
        }
        Task taskToUpdate = view.getTaskToUpdate();
        if(taskToUpdate == null){
            addNewTask(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3);
        } else {
            updateTask(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3, taskToUpdate);
        }

        UserTasksList view = null;
        try {
            view = new UserTasksList(model.getLoggedUserTasks());
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
        UserTasksController viewController = new UserTasksController(model, view, managerController);
        managerController.setViewController(viewController);
    }

    private void updateTask(String taskTitle, String taskSubtask1, String taskSubtask2, String taskSubtask3, Task taskToUpdate) {
        taskToUpdate.setTitle(taskTitle);
        taskToUpdate.setSubtask1(taskSubtask1);
        taskToUpdate.setSubtask2(taskSubtask2);
        taskToUpdate.setSubtask3(taskSubtask3);

        try {
            model.updateTask(taskToUpdate);
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
    }

    private void addNewTask(String taskTitle, String taskSubtask1, String taskSubtask2, String taskSubtask3) {
        Task newTask = new Task(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3);
        try {
            model.addUserTask(newTask);
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
    }
}
