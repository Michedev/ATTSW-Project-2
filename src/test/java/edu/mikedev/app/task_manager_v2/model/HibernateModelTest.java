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
    private Model model;
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
        model = new Model(new HibernateTransactionManager(sessionFactory));
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

        Task actual = null;
        try {
            actual = model.getUserTask(expected.getId());
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getSubtask1(), actual.getSubtask1());
        Assert.assertEquals(expected.getSubtask2(), actual.getSubtask2());
        Assert.assertEquals(expected.getSubtask3(), actual.getSubtask3());
    }

    @Test(expected = PermissionException.class)
    public void testModelGetTaskUnloggedUser() throws PermissionException {
        Task expected = user.getTasks().iterator().next();

        model.getUserTask(expected.getId());
    }

    @Test(expected =  PermissionException.class)
    public void testModelGetTaskLoggedButOtherUser() throws PermissionException {
        User otherUser = dbUtils.users.stream().filter((u) -> (! u.getUsername().equals(USERNAME)) && (! u.getPassword().equals(PASSWORD))).findFirst().get();
        Task taskOtherUser = otherUser.getTasks().iterator().next();
        model.login(USERNAME, PASSWORD);

        model.getUserTask(taskOtherUser.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModelGetNonexistentTask(){
        model.login(USERNAME, PASSWORD);

        try {
            model.getUserTask(110000000);
        } catch (PermissionException e) {
            throw new RuntimeException(e);
        }
    }

}
