package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        User loggedUser = model.login(USERNAME, PASSWORD);

        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
        Assert.assertEquals(mockedUser, loggedUser);
    }

    @Test
    public void testModelLoginWithWrongCredential(){
        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class ,() -> model.login("A", "B"));
        Assert.assertEquals("User with this credential doesn't exists", e.getMessage());
        verify(repository, times(1)).getUserByUsernamePassword(any(), any());
    }

}
