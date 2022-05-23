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

    public User login(String username, String password) throws PermissionException, IllegalArgumentException {
        if(this.logged != null){
            throw new PermissionException("You cannot login twice");
        }
        User logged = transactionManager.doInTransaction(repository -> repository.getUserByUsernamePassword(username, password));
        if(logged == null){
            throw new IllegalArgumentException("User with this credential doesn't exists");
        }
        this.logged = logged;
        return logged;
    }

    public Task getUserTask(int taskId) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        Task task = transactionManager.doInTransaction(repository -> repository.getTaskById(taskId));
        if(this.logged.getId() != task.getTaskOwner().getId()){
            throw new PermissionException(TASK_OWNER_ERROR_MESSAGE);
        }
        return task;
    }

    public void addUserTask(Task task) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        task.setTaskOwner(logged);
        transactionManager.doInTransaction(repository -> {
            repository.add(task);
            return null;
        });
    }

    public void updateTask(Task userTask) throws PermissionException {
        if(this.logged == null){
            throw new PermissionException(LOGIN_ERROR_MESSAGE);
        }
        transactionManager.doInTransaction(repository -> {
            repository.update(userTask);
            return null;
        });
    }

    public void deleteTask(Task task) throws PermissionException {
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
}
