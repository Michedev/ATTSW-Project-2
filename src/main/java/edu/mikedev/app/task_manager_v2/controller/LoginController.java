package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.LoginPage;

public class LoginController {
    private final Model model;
    private final LoginPage loginPage;

    public LoginController(Model model, LoginPage loginPage) {
        this.model = model;
        this.loginPage = loginPage;
    }

    public void onLoginButtonClick(){
        if(this.loginPage.getUsername().isEmpty() || this.loginPage.getPassword().isEmpty()){
            this.loginPage.setErrorLabelText("Missing Username/Password");
        }

    }
}
