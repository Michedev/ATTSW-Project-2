package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.Session;

import java.util.List;

public class HibernateUserTaskRepository implements UserTaskRepository {

    private final Session session;

    public HibernateUserTaskRepository(Session session) {
        this.session = session;
    }

    @Override
    public void add(User user){
        session.save(user);
    }
    @Override
    public void delete(User user) {
        User persistentUser = session.find(User.class, user.getId());
        session.delete(persistentUser);
    }
    @Override
    public void update(Task task) {
        session.update(task);
    }
    @Override
    public User getUserById(int id) {
        return session.find(User.class, id);
    }
    @Override
    public Task getTaskById(int id) {
        return session.find(Task.class, id);
    }

    @Override
    public List<Task> getUserTasks(int userId) {
        return session.createQuery(String.format("SELECT t FROM Task t WHERE ID_USER = %d ORDER BY ID", userId), Task.class).getResultList();
    }

    @Override
    public void add(Task task) {
        session.save(task);
    }
    @Override
    public void delete(Task toDelete) {
        session.delete(toDelete);
    }
    @Override
    public User getUserByUsernamePassword(String username, String password) {
        return session.createQuery(String.format("SELECT u FROM User u where u.username = '%s' and u.password = '%s'", username, password), User.class).getSingleResult();
    }
}
