package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
        User loggedUser = null;
        try {
            loggedUser = model.login(expectedUser.getUsername(), expectedUser.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(loggedUser);
        Assert.assertEquals(expectedUser.getId(), loggedUser.getId());
        Assert.assertEquals(expectedUser.getUsername(), loggedUser.getUsername());
        Assert.assertEquals(expectedUser.getPassword(), loggedUser.getPassword());
        Assert.assertEquals(expectedUser.getEmail(), loggedUser.getEmail());
    }

    @Test
    public void testAddTask(){
        User user = dbUtils.users.iterator().next();

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Task newTask = new Task("AAA", "1", "2", "3");
        try {
            model.addUserTask(newTask);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        List<Task> userTasks = dbUtils.getUserTasks(user.getId());
        List<String> dbTaskTitles = dbUtils.getDBTaskTitles();
        Optional<Task> optionalMatchedTask = userTasks.stream().filter(task -> task.getTitle().equals(newTask.getTitle())).findFirst();
        Assert.assertTrue(optionalMatchedTask.isPresent());
        Task matchedTask = optionalMatchedTask.get();
        Assert.assertEquals(dbTaskTitles.size(), matchedTask.getId());
    }

    @Test
    public void testRemoveTask(){
        User user = dbUtils.users.iterator().next();

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task toRemove = user.getTasks().iterator().next();
        try {
            model.deleteTask(toRemove);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(toRemove.getTitle()));
    }

    @Test
    public void testUpdateTask(){
        User user = dbUtils.users.iterator().next();

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task toUpdate = user.getTasks().iterator().next();
        String oldTitle = toUpdate.getTitle();
        String newTitle = "NewTitle";
        toUpdate.setTitle(newTitle);

        try {
            model.updateTask(toUpdate);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        List<String> dbTaskTitles = dbUtils.getDBTaskTitles();
        Assert.assertFalse(dbTaskTitles.contains(oldTitle));
        Assert.assertTrue(dbTaskTitles.contains(newTitle));
    }

    @Test
    public void testGetTaskById(){
        User user = dbUtils.users.iterator().next();

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

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

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        model.logout();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getUserTask(userTask.getId()));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
    }

    @After
    public void closeSessionFactory(){
        sessionFactory.close();
    }
}
