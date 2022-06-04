package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HibernateModelTest {

    private final String OTHER_USER_ERROR_MESSAGE = "The task owner is not the logged user";
    private final String LOGIN_ERROR_MESSAGE = "You must login by calling the login() method before calling this one.";
    private final int userId = 1;
    private Model model;
    private final String USERNAME = "username1";
    private final String PASSWORD = "password1";
    private TransactionManager transactionManager;
    private UserTaskRepository repository;
    private User mockedUser;
    private List<Task> mockedUserTasks;


    @Before
    public void setUp(){
        Task task1 = new Task("A", "1", "2", "3");
        task1.setId(1);
        Task task2 = new Task("E", "4", "5", "6");
        task2.setId(2);
        transactionManager = mock(TransactionManager.class);
        repository = mock(UserTaskRepository.class);
        mockedUser = new User(USERNAME, PASSWORD, "email@email.com");
        mockedUser.setId(userId);
        when(repository.getUserByUsernamePassword(USERNAME, PASSWORD)).thenReturn(mockedUser);
        mockedUserTasks = Arrays.asList(
                task1,
                task2
        );
        when(repository.getUserTasks(userId)).thenReturn(mockedUserTasks);

        when(transactionManager.doInTransaction(any())).thenAnswer(answer((Function<UserTaskRepository, ?> f) -> {
            return f.apply(repository);
        }));
        model = new Model(transactionManager);
    }


    @Test
    public void testModelLogin(){
        List<Task> userTasks = null;
        try {
            userTasks = model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotNull(userTasks);
        verify(repository).getUserByUsernamePassword(any(), any());
        Assert.assertEquals(mockedUserTasks, userTasks);
    }

    @Test
    public void testModelLoginWithWrongCredential() {
        List<Task> userTasks = null;
        try {
            userTasks = model.loginGetTasks("A", "B");
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNull(userTasks);
        verify(repository, never()).getTaskById(anyInt());
        verify(repository).getUserByUsernamePassword(any(), any());
        try {
            Assert.assertNotNull(model.loginGetTasks(USERNAME, PASSWORD));
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDoubleLogin() {
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.loginGetTasks(USERNAME, PASSWORD));

        Assert.assertEquals("You cannot login twice", e.getMessage());
        verify(repository).getUserByUsernamePassword(any(), any());
        verify(repository).getUserTasks(anyInt());
    }

    @Test
    public void testGetUserTask(){
        Task userTask = getUserTask();
        when(repository.getTaskById(10)).thenReturn(userTask);
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task actualTask = null;
        try {
            actualTask = model.getUserTask(10);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(userTask, actualTask);
        verify(repository, times(1)).getTaskById(10);
    }

    @Test
    public void testGetUsertasksWhenNotLogged(){
        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getLoggedUserTasks());
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        verify(repository, never()).getUserTasks(anyInt());
    }

    @Test
    public void testGetUserTasks(){
        List<Task> taskList = Arrays.asList(
          new Task("QWERTY", "F", "G", "H"),
          new Task("AAA", "B", "C", "D"),
          new Task("111", "#", "$", "^")
        );
        User mockedUser = new User("AAAA", "CCC", "E");
        int userId = 10043;
        mockedUser.setId(userId);
        when(repository.getUserByUsernamePassword(USERNAME, PASSWORD)).thenReturn(mockedUser);
        when(repository.getUserTasks(userId)).thenReturn(taskList);
        List<Task> loggedUserTasks = null;

        try{
            loggedUserTasks = model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e){
            Assert.fail(e.getMessage());
        }

        Assert.assertNotNull(loggedUserTasks);
        Assert.assertArrayEquals(taskList.toArray(), loggedUserTasks.toArray());
        verify(repository).getUserTasks(userId);
    }

    private Task getUserTask() {
        Task userTask = new Task("AAA", "1", "2", "3");
        userTask.setId(100);
        userTask.setTaskOwner(mockedUser);
        return userTask;
    }

    @Test
    public void testGetUserTaskOtherUser(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task otherUserTask = getOtherUserTask();
        when(repository.getTaskById(500)).thenReturn(otherUserTask);

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.getUserTask(500));

        Assert.assertEquals(OTHER_USER_ERROR_MESSAGE, e.getMessage());
        verify(repository, times(1)).getTaskById(500);
    }

    @Test
    public void testGetUserTaskWhenNonlogged(){
        PermissionException e = Assert.assertThrows(PermissionException.class, () ->model.getUserTask(100));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        verify(repository, never()).getTaskById(anyInt());
    }

    @Test
    public void testAddNewTask(){
        Task newTask = new Task("FGH", "M", "N", "O");
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        List<Task> userTasks = null;
        try {
            userTasks = model.addUserTaskGetTasks(newTask);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        verify(repository, times(1)).add(any(Task.class));
        verify(repository, times(2)).getUserTasks(anyInt());
        Assert.assertEquals(mockedUser, newTask.getTaskOwner());
        Assert.assertEquals(mockedUserTasks.size() + 1, userTasks.size());
        Assert.assertTrue(userTasks.contains(newTask));
    }

    @Test
    public void testAddNewTaskWhenNotLogged(){
        Task userTask = getUserTask();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.addUserTaskGetTasks(userTask));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        verify(repository, never()).add(any(Task.class));
    }

    @Test
    public void testUpdateTask(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task userTask = getUserTask();

        List<Task> userTasks = null;

        try {
            userTasks = model.updateTaskGetTasks(userTask);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        verify(repository).update(any(Task.class));
        verify(repository, times(2)).getUserTasks(anyInt());
        Assert.assertEquals(mockedUserTasks.size(), userTasks.size());
    }

    @Test
    public void testUpdateTaskWhenNotLogged(){
        Task userTask = getUserTask();

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.updateTaskGetTasks(userTask));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());

        verify(repository, times(0)).update(any(Task.class));
    }

    @Test
    public void testDeleteTask(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Task userTask = getUserTask();
        when(repository.getTaskById(userTask.getId())).thenReturn(userTask);
        Task deletedTask = null;
        try {
            deletedTask = model.deleteTask(userTask);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotNull(deletedTask);
        Assert.assertEquals(userTask, deletedTask);
        verify(repository).delete(any(Task.class));
    }

    @Test
    public void testDeleteTaskWhenNonLogged(){
        Task userTask = getUserTask();
        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.deleteTask(userTask));

        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        verify(repository, times(0)).delete(any(Task.class));
    }

    @Test
    public void testDeleteTaskAnotherUser(){
        Task otherUserTask = getOtherUserTask();
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.deleteTask(otherUserTask));
        Assert.assertEquals(OTHER_USER_ERROR_MESSAGE, e.getMessage());
        verify(repository, times(0)).delete(any(Task.class));
    }

    @Test
    public void testDeleteTaskWhenNotExisting(){
        Task task = getUserTask();
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        when(repository.getTaskById(anyInt())).thenReturn(null);

        Task deletedTask = null;
        try {
            deletedTask = model.deleteTask(task);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNull(deletedTask);
        verify(repository, times(2)).getUserTasks(anyInt());
        verify(repository, never()).delete(nullable(Task.class));
    }

    @Test
    public void testRegisterUser(){
        User newUser = new User("AAAA", "BBBB", "CCCC");

        model.registerUser(newUser);

        verify(repository, times(1)).add(any(User.class));
    }

    @Test
    public void testRegisterUserWithAnyMissingField(){
        User userWithoutUsername = new User(null, "AA", "BB");
        IllegalArgumentException e1 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithoutUsername));
        Assert.assertEquals("Null username", e1.getMessage());

        User userWithEmptyUsername = new User("", "AA", "BB");
        IllegalArgumentException e2 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithEmptyUsername));
        Assert.assertEquals("Empty username", e2.getMessage());

        User userWithoutPassword = new User("AA",  null, "ABC");
        IllegalArgumentException e3 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithoutPassword));
        Assert.assertEquals("Null password", e3.getMessage());

        User userWithEmptyPassword = new User("ABC", "", "email");
        IllegalArgumentException e4 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithEmptyPassword));
        Assert.assertEquals("Empty password", e4.getMessage());

        User userWithMissingEmail = new User("ABC", "A", null);
        IllegalArgumentException e5 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithMissingEmail));
        Assert.assertEquals("Null email", e5.getMessage());

        User userWithEmptyEmail = new User("ABC", "A", "");
        IllegalArgumentException e6 = Assert.assertThrows(IllegalArgumentException.class, () -> model.registerUser(userWithEmptyEmail));
        Assert.assertEquals("Empty email", e6.getMessage());

        verify(repository, times(0)).add(any(User.class));
    }

    @Test
    public void testRegisterUserWhenLogged(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        User newUser = new User("A", "B", "C");
        IllegalStateException e = Assert.assertThrows(IllegalStateException.class , () -> model.registerUser(newUser));
        Assert.assertEquals("You cannot register when an user is logged", e.getMessage());
    }

    @Test
    public void testUserLogout(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        model.logout();
        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.addUserTaskGetTasks(new Task()));
        Assert.assertEquals(LOGIN_ERROR_MESSAGE, e.getMessage());
        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
    }

    @Test
    public void testLogoutBeforeLogin(){
        IllegalStateException e = Assert.assertThrows(IllegalStateException.class, () -> model.logout());
        Assert.assertEquals("You cannot logout before login", e.getMessage());
    }

    @Test
    public void testDoubleLogout(){
        try {
            model.loginGetTasks(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        model.logout();

        IllegalStateException e = Assert.assertThrows(IllegalStateException.class, () -> model.logout());
        Assert.assertEquals("You cannot logout before login", e.getMessage());
    }

    private Task getOtherUserTask() {
        Task otherUserTask = new Task("BBB", "5", "6", "7");
        User otherUser = new User();
        otherUser.setId(10000);
        otherUserTask.setTaskOwner(otherUser);
        return otherUserTask;
    }

}
