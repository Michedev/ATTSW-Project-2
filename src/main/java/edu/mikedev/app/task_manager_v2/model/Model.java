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

    public Task getUserTask(int taskId) throws PermissionException, IllegalArgumentException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        Task task = transactionManager.doInTransaction(repository -> repository.getTaskById(taskId));
        if(task == null){
            throw new IllegalArgumentException(String.format("Task with id %d not found", taskId));
        }
        if(task.getTaskOwner().getId() != logged.getId()){
            throw new PermissionException(TASK_OWNER_ERROR_MESSAGE);
        }
        return task;
    }

    public void updateTask(Task updatedTask) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        if(this.logged.getId() != updatedTask.getTaskOwner().getId()){
            throw new PermissionException(TASK_OWNER_ERROR_MESSAGE);
        }
        transactionManager.doInTransaction(repository -> {
            repository.update(updatedTask);
            return null;
        });
    }

    public void removeTask(Task task) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        if(this.logged.getId() != task.getTaskOwner().getId()){
            throw new PermissionException(TASK_OWNER_ERROR_MESSAGE);
        }
        transactionManager.doInTransaction(repository -> {
            repository.delete(task);
            return null;
        });
    }

    public void addTask(Task task) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        task.setTaskOwner(logged);
        transactionManager.doInTransaction(repository -> {
            repository.add(task);
            return null;
        });
    }

    public void addUser(User newUser) {
        transactionManager.doInTransaction(repository -> {
           repository.add(newUser);
           return null;
        });
    }
}
