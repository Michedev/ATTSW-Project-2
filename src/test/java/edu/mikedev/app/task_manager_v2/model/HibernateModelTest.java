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
import java.util.List;

public class HibernateModelTest {

    private static HibernateDBUtilsInMemory dbUtils;
    private static SessionFactory sessionFactory;
    private final String OTHER_USER_ERROR_MESSAGE = "The task owner is not the logged user";
    private final String LOGIN_ERROR_MESSAGE = "You must login by calling the login() method before calling this one.";
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

    @Test
    public void testModelGetTaskUnloggedUser() {
        Task expected = user.getTasks().iterator().next();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getUserTask(expected.getId()));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testModelGetTaskLoggedButOtherUser() {
        User otherUser = getOtherUser();
        Task taskOtherUser = otherUser.getTasks().iterator().next();
        model.login(USERNAME, PASSWORD);

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getUserTask(taskOtherUser.getId()));
        Assert.assertEquals(OTHER_USER_ERROR_MESSAGE, e.getMessage());
    }

    private User getOtherUser() {
        return dbUtils.users.stream().filter((u) -> (!u.getUsername().equals(USERNAME)) && (!u.getPassword().equals(PASSWORD))).findFirst().get();
    }

    @Test
    public void testModelGetNonexistentTask(){
        model.login(USERNAME, PASSWORD);

        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () -> model.getUserTask(110000000));
        Assert.assertEquals("Task with id 110000000 not found", e.getMessage());
    }

    @Test
    public void testUpdateTaskNonloggedUser(){
        Task task = user.getTasks().iterator().next();
        task.setTitle("NewTitleABC");

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.updateTask(task));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(task.getTitle()));
    }

    @Test
    public void testUpdateTask(){
        model.login(USERNAME, PASSWORD);

        Task task = user.getTasks().iterator().next();
        String newTitle = "NewTitle1234";
        task.setTitle(newTitle);

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(newTitle));

        try {
            model.updateTask(task);
        } catch (PermissionException e) {
            Assert.fail();
        }

        Assert.assertTrue(dbUtils.getDBTaskTitles().contains(newTitle));
    }

    @Test
    public void testUpdateTaskOtherUser() {
        model.login(USERNAME, PASSWORD);

        User otherUser = getOtherUser();
        Task otherUserTask = otherUser.getTasks().iterator().next();
        otherUserTask.setTitle("NewTitle1");
        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.updateTask(otherUserTask));
        Assert.assertEquals(OTHER_USER_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testUpdateTaskMissingId(){
        model.login(USERNAME, PASSWORD);

        Task toUpdate = user.getTasks().iterator().next();
        toUpdate.setId(4199812);
        toUpdate.setTitle("Task title new");

        try {
            model.updateTask(toUpdate);
        } catch (PermissionException e) {
            Assert.fail();
        }

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(toUpdate.getTitle()));
    }

    @Test
    public void testRemoveTaskUnloggedUser(){
        Task task = user.getTasks().iterator().next();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.removeTask(task));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testRemoveTask(){
        model.login(USERNAME, PASSWORD);

        Task task = user.getTasks().iterator().next();

        try {
            model.removeTask(task);
        } catch (PermissionException e) {
            Assert.fail(String.format("Caught PermissionException when shouldn't with message '%s'", e.getMessage()));
        }

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(task.getTitle()));
    }

    @Test
    public void testRemoveTaskOtherUser(){
        model.login(USERNAME, PASSWORD);
        Task taskOtherUser = getOtherUser().getTasks().iterator().next();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.removeTask(taskOtherUser));
        Assert.assertEquals(OTHER_USER_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void testRemoveTaskNonexisting(){
        model.login(USERNAME, PASSWORD);

        Task task = user.getTasks().iterator().next();

        task.setId(439403);
        task.setTitle("ijr3ri3ori3oi");

        List<String> dbTaskTitlesPreRemove = dbUtils.getDBTaskTitles();

        try {
            model.removeTask(task);
        } catch (PermissionException e) {
            Assert.fail(String.format("Caught PermissionException when shouldn't with message '%s'", e.getMessage()));
        }

        List<String> dbTaskTitlesAfterRemove = dbUtils.getDBTaskTitles();

        Assert.assertEquals(dbTaskTitlesPreRemove.size(), dbTaskTitlesAfterRemove.size());
        Assert.assertArrayEquals(dbTaskTitlesPreRemove.toArray(), dbTaskTitlesAfterRemove.toArray());
    }

    @Test
    public void testAddTaskNonloggedUser(){
        Task newTask = new Task("TASK123", "A", "B", "C");

        List<String> dbTaskTitlesPreAdd = dbUtils.getDBTaskTitles();

        Assert.assertThrows(PermissionException.class, () -> model.addTask(newTask));

        List<String> dbTaskTitlesAfterAdd = dbUtils.getDBTaskTitles();

        Assert.assertArrayEquals(dbTaskTitlesPreAdd.toArray(), dbTaskTitlesAfterAdd.toArray());
    }

    @Test
    public void testAddTask(){
        model.login(USERNAME, PASSWORD);

        Task newTask = new Task("TASK123", "A", "B", "C");

        List<Task> dbUserTasksPreAdd = dbUtils.getUsersTask(user.getId());


        try {
            model.addTask(newTask);
        } catch (PermissionException e) {
            Assert.fail(String.format("Caught PermissionException when shouldn't with message '%s'", e.getMessage()));
        }

        List<Task> dbUserTasks = dbUtils.getUsersTask(user.getId());

        Assert.assertEquals(dbUserTasksPreAdd.size() + 1, dbUserTasks.size());
        Assert.assertTrue(dbUserTasks.stream().anyMatch(task -> task.getTitle().equals(newTask.getTitle())
                                                                && task.getSubtask1().equals(newTask.getSubtask1())
                                                                && task.getSubtask2().equals(newTask.getSubtask2())
                                                                && task.getSubtask3().equals(newTask.getSubtask3())));
    }

    @Test
    public void testRegisterUser(){
        User newUser = new User("NewUser", "NewPassword", "NewEmail@newmail.net");

        List<String> dbUsernamesPreAdd = dbUtils.getDBUsernames();
        model.registerUser(newUser);

        List<String> dbUsernamesPostAdd = dbUtils.getDBUsernames();

        Assert.assertTrue(dbUsernamesPostAdd.contains(newUser.getUsername()));
        Assert.assertEquals(dbUsernamesPreAdd.size() + 1, dbUsernamesPostAdd.size());
    }

    @Test
    public void testRemoveNonexistentUser(){

        User nonExistentUser = new User("FakeUser", "FakePass", "FakeEmail");

        List<String> dbUsernamesPreDel = dbUtils.getDBUsernames();

        model.removeUser(nonExistentUser);

        List<String> dbUsernamesPostDel = dbUtils.getDBUsernames();

        Assert.assertArrayEquals(dbUsernamesPreDel.toArray(), dbUsernamesPostDel.toArray());
    }

    @Test
    public void testRemoveUser(){

        List<String> dbUsernamesPreDel = dbUtils.getDBUsernames();

        model.removeUser(user);

        List<String> dbUsernamesPostDel = dbUtils.getDBUsernames();

        Assert.assertEquals(dbUsernamesPreDel.size()-1, dbUsernamesPostDel.size());
        Assert.assertFalse(dbUsernamesPostDel.contains(user.getUsername()));
        Assert.assertTrue(dbUsernamesPreDel.contains(user.getUsername()));
    }
}
