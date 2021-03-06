package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;

import java.util.List;

public interface UserTaskRepository {
    void add(User user);

    void delete(User user);

    void update(Task task);

    User getUserById(int id);

    Task getTaskById(int id);

    List<Task> getUserTasks(int userId);

    void add(Task task);

    void delete(Task toDelete);

    User getUserByUsernamePassword(String username, String password);
}
