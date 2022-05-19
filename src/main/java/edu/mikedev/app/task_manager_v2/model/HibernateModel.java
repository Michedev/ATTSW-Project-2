package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.TransactionManager;

public class HibernateModel {
    private final SessionFactory sessionFactory;
    private final HibernateTransactionManager transactionManager;

    public HibernateModel(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.transactionManager = new HibernateTransactionManager(sessionFactory);
    }

    public User login(String username, String password) {
        return null;
    }
}
