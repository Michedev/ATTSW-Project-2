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
        Transaction t = null;
        T output = null;
        try {
            t = session.beginTransaction();
            HibernateUserTaskRepository repository = new HibernateUserTaskRepository(session);
            output = f.apply(repository);
            t.commit();
        } catch (Exception e){
            if(t != null){
                t.rollback();
            }
        }
        finally {
            session.close();
        }
        return output;
    }
}
