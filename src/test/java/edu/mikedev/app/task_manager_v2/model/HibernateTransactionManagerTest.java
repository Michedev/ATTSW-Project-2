package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HibernateTransactionManagerTest {

    private HibernateDBUtilsInMemory inMemory;
    private SessionFactory sessionFactory;
    private HibernateTransactionManager transactionManager;
    private UserTaskRepository repository;

    @Before
    public void setUp(){
        repository = mock(UserTaskRepository.class);
        inMemory = new HibernateDBUtilsInMemory();
        sessionFactory = inMemory.buildSessionFactory();
        transactionManager = new HibernateTransactionManager(sessionFactory);
    }

    @Test
    public void testDoInTransaction(){
        User toAdd = new User("123", "1", "2");

        Assert.assertFalse(inMemory.getDBUsernames().contains(toAdd.getUsername()));

        transactionManager.doInTransaction((repository) -> {
            repository.add(toAdd);
            return null;
        });

        Assert.assertTrue(inMemory.getDBUsernames().contains(toAdd.getUsername()));

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

        Assert.assertFalse(inMemory.getDBUsernames().contains(toAdd.getUsername()));
    }
}
