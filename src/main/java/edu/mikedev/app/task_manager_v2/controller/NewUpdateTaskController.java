package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;

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

        if(taskTitle.isEmpty()){
            view.setTitleErrorLabelText("Missing title");
        }
        if(taskSubtask1.isEmpty()){
            view.setStep1ErrorLabelText("Missing subtask 1");
        }
        if(taskSubtask2.isEmpty()){
            view.setStep2ErrorLabelText("Missing subtask 2");
        }
        if(taskSubtask3.isEmpty()){
            view.setStep3ErrorLabelText("Missing subtask 3");
        }
    }
}
