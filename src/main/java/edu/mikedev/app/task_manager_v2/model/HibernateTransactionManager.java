package edu.mikedev.app.task_manager_v2.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Function;

public class HibernateTransactionManager {
    private final SessionFactory sessionFactory;

    public HibernateTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void doInTransaction(Function<HibernateUserTaskRepository, Void> f) {
        Session session = sessionFactory.openSession();
        Transaction t =  session.beginTransaction();
        HibernateUserTaskRepository repository = new HibernateUserTaskRepository(session);
        f.apply(repository);
        t.commit();
        session.close();
    }
}
