package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;

public class RegisterController implements ViewController<RegisterPage> {
    public RegisterController(Model model, RegisterPage page, TaskManagerController managerController) {
    }

    @Override
    public void addEvents(RegisterPage view) {

    }

    @Override
    public RegisterPage getView() {
        return null;
    }

    public void onRegisterButtonClick() {

    }
}
