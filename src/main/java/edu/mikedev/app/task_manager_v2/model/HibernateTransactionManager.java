package edu.mikedev.app.task_manager_v2.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Function;

public class HibernateTransactionManager implements TransactionManager {
    private final SessionFactory sessionFactory;

    public HibernateTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T doInTransaction(Function<UserTaskRepository, T> f) {
        Session session = sessionFactory.openSession();
        Transaction t =  session.beginTransaction();
        HibernateUserTaskRepository repository = new HibernateUserTaskRepository(session);
        T output = f.apply(repository);
        t.commit();
        session.close();
        return output;
    }
}
