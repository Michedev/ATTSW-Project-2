package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.UpdateDeleteTransactionOutcome;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

import java.util.List;

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
        getView().addActionListenerDeleteButton((e) -> onClickDeleteButton());
        getView().addActionListenerUpdateButton((e) -> onClickUpdateButton());
    }

    @Override
    public TaskDetail getView() {
        return taskDetail;
    }

    public void onClickUpdateButton() {
        Task task = taskDetail.getTask();
        NewUpdateTask view = new NewUpdateTask();
        view.setUpdateMode(task);
        NewUpdateTaskController viewController = new NewUpdateTaskController(model, view, managerController);
        managerController.setViewController(viewController);
    }

    public void onClickDeleteButton() {
        UpdateDeleteTransactionOutcome<List<Task>> updateDeleteTransactionOutcome = null;
        int missingId = -1;
        try {
            Task toDelete = getView().getTask();
            updateDeleteTransactionOutcome = model.deleteTaskGetUserTasks(toDelete);
            missingId = updateDeleteTransactionOutcome.getMissingId();
        } catch (PermissionException e) {
            managerController.initApplication();
            return;
        }
        UserTasksList view = new UserTasksList(updateDeleteTransactionOutcome.getData());
        UserTasksController userTasksController = new UserTasksController(model, view, managerController);
        managerController.setViewController(userTasksController);
        if(missingId != -1){
            view.setErrorMessage(String.format("The task with id %d is missing", missingId));
        }
    }
}
