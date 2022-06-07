package edu.mikedev.app.task_manager_v2.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;
import java.util.function.Function;

public class HibernateTransactionManager implements TransactionManager {
    private final SessionFactory sessionFactory;

    @Inject
    public HibernateTransactionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T doInTransaction(Function<UserTaskRepository, T> f) {
        Session session = sessionFactory.openSession();
        Transaction t = null;
        HibernateUserTaskRepository repository = new HibernateUserTaskRepository(session);
        T output = null;
        try{
            t = session.beginTransaction();
            output = f.apply(repository);
            t.commit();
        } catch (Exception e){
            t.rollback();
            System.err.println("Found exception while executing the transaction with the following error message: \"" + e.getMessage() + "\"");
        }
        finally {
            session.close();
        }
        return output;
    }
}
