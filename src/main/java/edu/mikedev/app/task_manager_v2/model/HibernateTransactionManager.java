package edu.mikedev.app.task_manager_v2.model;

import org.hibernate.SessionFactory;

public class HibernateTransactionManager {
    public HibernateTransactionManager(SessionFactory sessionFactory) {
    }

    public void doInTransaction(Runnable r) {
    }
}
