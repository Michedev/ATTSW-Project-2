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
        Session session = sessionFactory.openSession();
        Task toAdd = new Task("123", "1", "2", "3");
        doAnswer(answer((Task t) -> {
            session.save(t);
            return null;
        })).when(repository).add(any(Task.class));

        Assert.assertFalse(inMemory.getDBTaskTitles().contains(toAdd.getTitle()));

        transactionManager.doInTransaction(() -> repository.add(toAdd));

        Assert.assertTrue(inMemory.getDBTaskTitles().contains(toAdd.getTitle()));

    }
}
