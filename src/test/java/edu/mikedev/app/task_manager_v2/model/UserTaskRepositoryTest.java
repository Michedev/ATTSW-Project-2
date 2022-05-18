package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class UserTaskRepositoryTest {

    private static SessionFactory sessionFactory;
    private static HibernateDBUtils dbUtils;
    private UserTaskRepository userTaskRepository;

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
        userTaskRepository = new UserTaskRepository();
    }

    @Test
    public void testAddNewUser(){
        String newUsername = "username41";
        User u = new User(newUsername, "passw1", "email@mail.it", new HashSet<Task>());
        Assert.assertFalse(dbUtils.getDBUsernames().contains(newUsername));

        userTaskRepository.add(u);

        Assert.assertTrue(dbUtils.getDBUsernames().contains(newUsername));
    }
}
