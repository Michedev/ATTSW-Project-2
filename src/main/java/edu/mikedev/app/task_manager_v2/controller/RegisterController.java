package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;

public class RegisterController implements ViewController<RegisterPage> {
    private final Model model;
    private final RegisterPage page;
    private final TaskManagerController managerController;

    public RegisterController(Model model, RegisterPage page, TaskManagerController managerController) {
        this.model = model;
        this.page = page;
        this.managerController = managerController;
    }

    @Override
    public void addEvents(RegisterPage view) {

    }

    @Override
    public RegisterPage getView() {
        return page;
    }

    public void onRegisterButtonClick() {
        String username = page.getUsername();
        String password = page.getPassword();
        String email = page.getEmail();
        if(username.isEmpty()){
            page.setErrorLabelUsername("Missing username");
        }
        if(password.isEmpty()){
            page.setErrorLabelPassword("Missing password");
        }
        if(email.isEmpty()){
            page.setErrorLabelEmail("Missing email");
        }

    }
}
