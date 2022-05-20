package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Function;

public class Model {
    private final TransactionManager transactionManager;
    private User logged;

    public Model(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    public User login(String username, String password) {
        User logged = transactionManager.doInTransaction(repository -> repository.getUserByUsernamePassword(username, password));
        this.logged = logged;
        return logged;
    }

    public Task getUserTask(int taskId) throws PermissionException, IllegalArgumentException {
        if(this.logged == null){
            throw new PermissionException("You must login by calling the login() method before calling this one.");
        }
        Task task = transactionManager.doInTransaction(repository -> repository.getTaskById(taskId));
        if(task == null){
            throw new IllegalArgumentException(String.format("Task with id %d not found", taskId));
        }
        if(task.getTaskOwner().getId() != logged.getId()){
            throw new PermissionException("You can access only to logged user tasks");
        }
        return task;
    }

    public void updateTask(Task task) {
        if(this.logged == null){
            throw new PermissionException("You must login by calling the login() method before calling this one.");
        }
    }
}
