package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;

public class LoginController {
    private final Model model;
    private final LoginPage loginPage;

    public LoginController(Model model, LoginPage loginPage) {
        this.model = model;
        this.loginPage = loginPage;
    }

    public void onLoginButtonClick(){
        String username = this.loginPage.getUsername();
        String password = this.loginPage.getPassword();
        if(username.isEmpty() || password.isEmpty()){
            this.loginPage.setErrorLabelText("Missing Username/Password");
            return;
        }
        User userLogged = null;
        try {
            userLogged = model.login(username, password);
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
        if(userLogged == null){
            loginPage.setErrorLabelText("Username/Password aren't registered");
        }
    }
}
