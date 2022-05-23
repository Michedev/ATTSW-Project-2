package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.function.Function;
import java.util.List;

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


    @Before
    public void setUp(){
        transactionManager = mock(TransactionManager.class);
        repository = mock(UserTaskRepository.class);
        mockedUser = new User(USERNAME, PASSWORD, "email@email.com");
        mockedUser.setId(userId);
        when(repository.getUserByUsernamePassword(USERNAME, PASSWORD)).thenReturn(mockedUser);

        when(transactionManager.doInTransaction(any())).thenAnswer(answer((Function<UserTaskRepository, ?> f) -> {
            return f.apply(repository);
        }));
        model = new Model(transactionManager);
    }


    @Test
    public void testModelLogin(){
        User loggedUser = null;
        try {
            loggedUser = model.login(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
        Assert.assertEquals(mockedUser, loggedUser);
    }

    @Test
    public void testModelLoginWithWrongCredential(){
        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class ,() -> model.login("A", "B"));
        Assert.assertEquals("User with this credential doesn't exists", e.getMessage());
        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
    }

    @Test
    public void testDoubleLogin() {
        try {
            model.login(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        PermissionException e = Assert.assertThrows(PermissionException.class, () -> model.login(USERNAME, PASSWORD));

        Assert.assertEquals("You cannot login twice", e.getMessage());
        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
    }

    @Test
    public void testGetUserTask(){
        Task userTask = getUserTask();
        when(repository.getTaskById(10)).thenReturn(userTask);
        try {
            model.login(USERNAME, PASSWORD);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        Task actualTask = model.getUserTask(10);

        Assert.assertEquals(userTask, actualTask);
        verify(repository, times(1)).getTaskById(10);
    }

    private Task getUserTask() {
        Task userTask = new Task("AAA", "1", "2", "3");
        userTask.setTaskOwner(mockedUser);
        return userTask;
    }

}
