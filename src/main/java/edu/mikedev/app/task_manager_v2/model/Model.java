package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;

public class Model {
    private final TransactionManager transactionManager;
    private final String LOGIN_ERROR_MESSAGE = "You must login by calling the login() method before calling this one.";
    private final String TASK_OWNER_ERROR_MESSAGE = "The task owner is not the logged user";
    private User logged;

    public Model(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public User login(String username, String password) {
        User logged = transactionManager.doInTransaction(repository -> repository.getUserByUsernamePassword(username, password));
        this.logged = logged;
        return logged;
    }

}
