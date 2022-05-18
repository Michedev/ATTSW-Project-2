package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;

public class UserTaskRepositoryTest {

    private static SessionFactory sessionFactory;
    private static HibernateDBUtilsInMemory dbUtils;
    private UserTaskRepository userTaskRepository;
    private Session session;

    @BeforeClass
    public static void setUpHibernate(){
        Path testResourceDirectory = Paths.get("src", "test", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        sessionFactory = cfg.configure(hibernateConfigFile).buildSessionFactory();
        dbUtils = new HibernateDBUtilsInMemory();
    }



    @Before
    public void setUp(){
        try {
            dbUtils.initDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session = sessionFactory.openSession();
        userTaskRepository = new UserTaskRepository(session);
    }

    @Test
    public void testAddNewUser(){
        String newUsername = "username41";
        User u = new User(newUsername, "passw1", "email@mail.it", new HashSet<Task>());
        Assert.assertFalse(dbUtils.getDBUsernames().contains(newUsername));
        Transaction t = session.beginTransaction();
        userTaskRepository.add(u);
        t.commit();
        session.close();
        Assert.assertTrue(dbUtils.getDBUsernames().contains(newUsername));
    }

    @Test
    public void testUserDelete(){
        User toDelete = dbUtils.users.get(0);

        Assert.assertTrue(dbUtils.getDBUsernames().contains(toDelete.getUsername()));
        Transaction t = session.beginTransaction();
        userTaskRepository.delete(toDelete);
        t.commit();
        session.close();
        Assert.assertFalse(dbUtils.getDBUsernames().contains(toDelete.getUsername()));
    }
}
