package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;

import java.util.List;

public class LoginController implements ViewController<LoginPage>{
    private final Model model;
    private final LoginPage loginPage;
    private final TaskManagerController managerController;

    public LoginController(Model model, LoginPage loginPage, TaskManagerController managerController) {
        this.model = model;
        this.loginPage = loginPage;
        this.managerController = managerController;
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
            managerController.initApplication();
        }
        if(userLogged == null){
            loginPage.setErrorLabelText("Username/Password aren't registered");
            return;
        }

        List<Task> tasks = null;
        try {
            tasks = model.getLoggedUserTasks();
        } catch (PermissionException e) {
            managerController.initApplication();
        }
        UserTasksList view = new UserTasksList(tasks);

        UserTasksController controller = new UserTasksController(model, view, managerController);

        this.managerController.setViewController(controller);
    }

    @Override
    public void addEvents() {
        LoginPage view = getView();
        view.addActionListenerBtnLogin((e) -> this.onLoginButtonClick());
        view.addActionListenerBtnRegister((e) -> this.onRegisterButtonClick());
    }

    @Override
    public LoginPage getView() {
        return loginPage;
    }

    public void onRegisterButtonClick() {
        RegisterPage page = new RegisterPage();
        RegisterController controller = new RegisterController(model, page, managerController);
        managerController.setViewController(controller);
    }
}
