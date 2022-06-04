package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.DeleteTaskResponse;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;
import sun.nio.cs.ext.MacTurkish;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelIT {

    @ClassRule
    public final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3-alpine")
            .withDatabaseName("test").withUsername("root").withPassword("root");
    private static HibernateDBUtilsPostgre dbUtils;
    private SessionFactory sessionFactory;
    private Model model;
    private final String LOGIN_ERROR_MESSAGE = "You must login by calling the login() method before calling this one.";

    @BeforeClass
    public static void setUpClass(){
        dbUtils = new HibernateDBUtilsPostgre(postgreSQLContainer.getContainerIpAddress(), postgreSQLContainer.getMappedPort(5432), "test");
    }

    @Before
    public void setUp(){
        try {
            dbUtils.initAndFillDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sessionFactory = dbUtils.buildSessionFactory();
        TransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        model = new Model(transactionManager);
    }

    @Test
    public void testLogin(){
        User expectedUser = dbUtils.users.iterator().next();
        List<Task> tasks = null;
        try {
            tasks = model.loginGetTasks(expectedUser.getUsername(), expectedUser.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(tasks);
        Assert.assertArrayEquals(expectedUser.getTasks().toArray(), tasks.toArray());
    }

    @Test
    public void testAddTask(){
        User user = dbUtils.users.iterator().next();

        doLogin(user);

        Task newTask = new Task("AAA", "1", "2", "3");
        List<Task> actualUserTasks = null;
        try {
            actualUserTasks = model.addUserTaskGetTasks(newTask);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        List<Task> expectedUserTask = dbUtils.getUserTasks(user.getId());
        Assert.assertTrue(expectedUserTask.stream().anyMatch(task -> task.equals(newTask)));
        Assert.assertEquals(expectedUserTask.size(), actualUserTasks.size());
    }

    @Test
    public void testRemoveTask(){
        User user = dbUtils.users.iterator().next();

        doLogin(user);

        Task toRemove = user.getTasks().iterator().next();
        DeleteTaskResponse response = null;
        try {
            response = model.deleteTaskGetUserTasks(toRemove);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        List<Task> dbUserTasks = dbUtils.getUserTasks(user.getId());

        Assert.assertEquals(-1, response.getMissingTaskId());
        Assert.assertFalse(dbUserTasks.contains(toRemove));
        Assert.assertArrayEquals(dbUserTasks.toArray(), response.getTasks().toArray());
    }

    private void doLogin(User user) {
        try {
            model.loginGetTasks(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateTask(){
        User user = dbUtils.users.iterator().next();

        doLogin(user);

        List<Task> userTasksPreadd = dbUtils.getUserTasks(user.getId());
        System.out.println("%%%%%%%%%");
        System.out.println(userTasksPreadd.stream().map(Task::getId).collect(Collectors.toList()));
        System.out.println(userTasksPreadd.stream().map(Task::getTitle).collect(Collectors.toList()));


        Task toUpdate = user.getTasks().get(0);
        String oldTitle = toUpdate.getTitle();
        String newTitle = "NewTitle";
        toUpdate.setTitle(newTitle);
        List<Task> actualUserTasks = null;
        try {
            actualUserTasks = model.updateTaskGetTasks(toUpdate);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        List<Task> dbUserTasks = dbUtils.getUserTasks(user.getId());

        Assert.assertArrayEquals(dbUserTasks.toArray(), actualUserTasks.toArray());
    }

    @Test
    public void testGetTaskById(){
        User user = dbUtils.users.iterator().next();

        doLogin(user);

        Iterator<Task> taskIterator = user.getTasks().iterator();
        taskIterator.next();
        Task expected = taskIterator.next();

        Task actual = null;
        try {
            actual = model.getUserTask(expected.getId());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getSubtask1(), actual.getSubtask1());
        Assert.assertEquals(expected.getSubtask2(), actual.getSubtask2());
        Assert.assertEquals(expected.getSubtask3(), actual.getSubtask3());
    }

    @Test
    public void testRegisterUser(){
        User newUser = new User("NewUser", "pass", "email");

        model.registerUser(newUser);

        List<String> dbUsernames = dbUtils.getDBUsernames();
        Assert.assertTrue(dbUsernames.contains(newUser.getUsername()));
        Assert.assertEquals(dbUtils.users.size() + 1, dbUsernames.size());
    }

    @Test
    public void testLogout(){
        User user = dbUtils.users.iterator().next();
        Task userTask  = user.getTasks().iterator().next();

        doLogin(user);

        model.logout();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getUserTask(userTask.getId()));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
    }

    @After
    public void closeSessionFactory(){
        sessionFactory.close();
    }
}
