package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.UpdateDeleteTransactionOutcome;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

import java.util.List;

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
        view.addActionListenerMakeButton(e -> onClickMakeButton());
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
        boolean anyError = setErrorLabelsTxtFields(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3);
        if(anyError){
            return;
        }

        Task taskToUpdate = view.getTaskToUpdate();
        List<Task> loggedUserTasks = null;
        int missingId = -1;
        try{
            if(taskToUpdate == null){
                loggedUserTasks = addNewTask(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3);
            } else {
                UpdateDeleteTransactionOutcome<List<Task>> response = updateTask(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3, taskToUpdate);
                loggedUserTasks = response.getData();
                missingId = response.getMissingId();
            }
        } catch (PermissionException e){
            managerController.initApplication();
            return;
        }


        UserTasksList userTasksListView = makeUserTasksList(loggedUserTasks);
        UserTasksController viewController = new UserTasksController(model, userTasksListView, managerController);
        managerController.setViewController(viewController);
        if(missingId != -1){
            userTasksListView.setErrorMessage(String.format("The task with id %d is missing", missingId));
        }
    }

    public UserTasksList makeUserTasksList(List<Task> loggedUserTasks) {
        return new UserTasksList(loggedUserTasks);
    }

    private boolean setErrorLabelsTxtFields(String taskTitle, String taskSubtask1, String taskSubtask2, String taskSubtask3) {
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
        return anyError;
    }

    private UpdateDeleteTransactionOutcome<List<Task>> updateTask(String taskTitle, String taskSubtask1, String taskSubtask2, String taskSubtask3, Task taskToUpdate) throws PermissionException {
        taskToUpdate.setTitle(taskTitle);
        taskToUpdate.setSubtask1(taskSubtask1);
        taskToUpdate.setSubtask2(taskSubtask2);
        taskToUpdate.setSubtask3(taskSubtask3);

        return model.updateTaskGetTasks(taskToUpdate);
    }

    private List<Task> addNewTask(String taskTitle, String taskSubtask1, String taskSubtask2, String taskSubtask3) throws PermissionException {
        Task newTask = new Task(taskTitle, taskSubtask1, taskSubtask2, taskSubtask3);

        return model.addUserTaskGetTasks(newTask);
    }
}
