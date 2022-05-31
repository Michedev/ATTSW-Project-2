package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.LoginPage;

import javax.swing.*;

public class TaskManagerController {

    private final JFrame window;
    private final Model model;

    public TaskManagerController(JFrame window, Model model){
        this.window = window;
        this.model = model;
    }

    public void setViewController(ViewController<?> controller) {
        window.setContentPane(controller.getView());
        controller.addEvents();
        window.pack();
    }

    public void initApplication() {
        LoginController controller = new LoginController(model, new LoginPage(), this);
        setViewController(controller);
    }
}
