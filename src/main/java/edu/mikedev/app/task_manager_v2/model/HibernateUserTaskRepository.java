package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.Session;

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
        return session.createQuery(String.format("Select u from User u where u.id = %d", id), User.class).getSingleResult();
    }

    @Override
    public Task getTaskById(int id) {
        return session.createQuery(String.format("Select t from Task t where t.id = %d", id), Task.class).getSingleResult();
    }

    @Override
    public void add(Task task) {
        session.save(task);
    }

    @Override
    public void delete(Task toDelete) {
        session.delete(toDelete);
    }
}
