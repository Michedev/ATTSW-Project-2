package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.ActionListener;
import java.util.List;

import static org.mockito.Mockito.*;

public class RegisterControllerTest {


    @Mock
    private Model model;
    @Mock
    private RegisterPage registerPage;
    @Mock
    private TaskManagerController mainController;
    @InjectMocks
    private RegisterController registerController;
    private AutoCloseable autoCloseable;


    @Before
    public void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        autoCloseable.close();
    }
    
    @Test
    public void testMissingUsername(){
        when(registerPage.getUsername()).thenReturn("");
        when(registerPage.getPassword()).thenReturn("aa");
        when(registerPage.getEmail()).thenReturn("email@email.it");
        
        registerController.onRegisterButtonClick();
        
        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelUsername("Missing username");
    }

    @Test
    public void testMissingPassword(){
        when(registerPage.getUsername()).thenReturn("ff");
        when(registerPage.getPassword()).thenReturn("");
        when(registerPage.getEmail()).thenReturn("email@email.it");

        registerController.onRegisterButtonClick();

        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelPassword("Missing password");
    }

    @Test
    public void testMissingEmail(){
        when(registerPage.getUsername()).thenReturn("ff");
        when(registerPage.getPassword()).thenReturn("aa");
        when(registerPage.getEmail()).thenReturn("");

        registerController.onRegisterButtonClick();

        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelEmail("Missing email");
    }

    @Test
    public void testMissingAll(){
        when(registerPage.getUsername()).thenReturn("");
        when(registerPage.getPassword()).thenReturn("");
        when(registerPage.getEmail()).thenReturn("");

        registerController.onRegisterButtonClick();

        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelUsername("Missing username");
        verify(registerPage, times(1)).setErrorLabelPassword("Missing password");
        verify(registerPage, times(1)).setErrorLabelEmail("Missing email");
    }

    @Test
    public void testWrongFormattedEmail(){
        when(registerPage.getUsername()).thenReturn("ff");
        when(registerPage.getPassword()).thenReturn("aa");
        when(registerPage.getEmail()).thenReturn("dddd");

        registerController.onRegisterButtonClick();

        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelEmail("Email should have the format {username}@{domanin}.{primarydomain}");

    }

    @Test
    public void testUsernameWithNonalphanumericCharacters(){
        when(registerPage.getUsername()).thenReturn("ff4&&**");
        when(registerPage.getPassword()).thenReturn("aa");
        when(registerPage.getEmail()).thenReturn("email@email.it");

        registerController.onRegisterButtonClick();

        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1))
                .setErrorLabelUsername("Username must contain only alphanumeric characters");

    }

    @Test
    public void testCorrectRegistration(){
        String registrationUsername = "username";
        String registrationPassword = "aa";
        String registrationEmail = "email@email.it";
        when(registerPage.getUsername()).thenReturn(registrationUsername);
        when(registerPage.getPassword()).thenReturn(registrationPassword);
        when(registerPage.getEmail()).thenReturn(registrationEmail);

        registerController.onRegisterButtonClick();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(mainController, times(1)).setViewController(any(LoginController.class));
        verify(model, times(1)).registerUser(userCaptor.capture());

        List<User> capturedUsers = userCaptor.getAllValues();
        Assert.assertEquals(1, capturedUsers.size());
        User capturedUser = capturedUsers.get(0);

        Assert.assertEquals(registrationUsername, capturedUser.getUsername());
        Assert.assertEquals(registrationPassword, capturedUser.getPassword());
        Assert.assertEquals(registrationEmail, capturedUser.getEmail());
    }

    @Test
    public void testAddBindings(){
        registerController.addEvents();

        verify(registerPage, times(1)).addActionListenerConfirmBtn(any(ActionListener.class));
    }

}
