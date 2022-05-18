package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.Session;

public class UserTaskRepository {

    private final Session session;

    public UserTaskRepository(Session session) {
        this.session = session;
    }

    public void add(User user){
        session.save(user);
    }

    public void delete(User user) {
        User persistentUser = session.find(User.class, user.getId());
        session.delete(persistentUser);
    }
}