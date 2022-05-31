package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HibernateTransactionManagerTest {

    private HibernateDBUtilsInMemory dbUtils;
    private HibernateTransactionManager transactionManager;

    @Before
    public void setUp(){
        dbUtils = new HibernateDBUtilsInMemory();
        SessionFactory sessionFactory = dbUtils.buildSessionFactory();
        transactionManager = new HibernateTransactionManager(sessionFactory);
    }

    @Test
    public void testDoInTransaction(){
        User toAdd = new User("123", "1", "2");

        Assert.assertFalse(dbUtils.getDBUsernames().contains(toAdd.getUsername()));

        transactionManager.doInTransaction((repository) -> {
            repository.add(toAdd);
            return null;
        });

        Assert.assertTrue(dbUtils.getDBUsernames().contains(toAdd.getUsername()));

    }

    @Test
    public void testRollbackTransaction(){
        User toAdd = new User("ABC", "A", "B");
        User notExistingUser = new User("GHF", "AFF", "FJIIF");
        notExistingUser.setId(1323);
        transactionManager.doInTransaction((repository) -> {
            repository.add(toAdd);
            repository.delete(notExistingUser);
            return null;
        });

        Assert.assertFalse(dbUtils.getDBUsernames().contains(toAdd.getUsername()));
    }
}
