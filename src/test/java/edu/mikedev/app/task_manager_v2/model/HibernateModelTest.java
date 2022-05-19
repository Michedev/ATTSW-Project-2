package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

public class HibernateModelTest {

    private static HibernateDBUtilsInMemory dbUtils;
    private static SessionFactory sessionFactory;
    private HibernateModel model;
    private final String USERNAME = "username1";
    private final String PASSWORD = "password1";
    private User user;


    @BeforeClass
    public static void setUpClass(){
        dbUtils = new HibernateDBUtilsInMemory();
        sessionFactory = dbUtils.buildSessionFactory();

    }

    @Before
    public void setUp(){
        model = new HibernateModel(sessionFactory);
        try {
            dbUtils.initDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user = dbUtils.users.stream().filter(u -> u.getUsername().equals(USERNAME) && u.getPassword().equals(PASSWORD)).findFirst().get();

    }

    @Test
    public void testModelLogin(){
        User actual = model.login(USERNAME, PASSWORD);

        Assert.assertEquals(user.getId(), actual.getId());
        Assert.assertEquals(user.getUsername(), actual.getUsername());
        Assert.assertEquals(user.getPassword(), actual.getPassword());
        Assert.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    public void testModelGetTaskLoggedUser(){
        model.login(USERNAME, PASSWORD);

        Task expected = user.getTasks().iterator().next();

        Task actual = model.getUserTask(expected.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getSubtask1(), actual.getSubtask1());
        Assert.assertEquals(expected.getSubtask2(), actual.getSubtask2());
        Assert.assertEquals(expected.getSubtask3(), actual.getSubtask3());
    }

    @Test(expected = IllegalAccessException.class)
    public void testModelGetTaskUnloggedUser(){
        Task expected = user.getTasks().iterator().next();

        model.getUserTask(expected.getId());
    }

}
