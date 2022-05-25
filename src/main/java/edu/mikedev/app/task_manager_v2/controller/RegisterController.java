package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;

import java.util.regex.Pattern;

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

        Pattern emailPattern = Pattern.compile("\\w+@\\w+\\.\\w+$");
        if(!emailPattern.matcher(email).find()){
            page.setErrorLabelEmail("Email should have the format {username}@{domanin}.{primarydomain}");
        }
        Pattern usernamePattern = Pattern.compile("\\w+$");
        if(!usernamePattern.matcher(username).find()){
            page.setErrorLabelUsername("Username must contain only alphanumeric characters");
        }
    }
}
