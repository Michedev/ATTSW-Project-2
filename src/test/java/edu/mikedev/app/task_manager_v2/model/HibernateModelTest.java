package edu.mikedev.app.task_manager_v2.model;

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
    }

//    @Test
//    public void testModelLogin(){
//        final String USERNAME = "username1";
//        String PASSWORD = "password1";
//        User expected = dbUtils.users.stream().filter(u -> u.getUsername().equals(USERNAME) && u.getPassword().equals(PASSWORD)).findFirst().get();
//        User actual = model.login(USERNAME, PASSWORD);
//
//        Assert.assertEquals(expected.getUsername(), actual.getUsername());
//        Assert.assertEquals(expected.getPassword(), actual.getPassword());
//        Assert.assertEquals(expected.getEmail(), actual.getEmail());
//    }
}
