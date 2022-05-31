package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;

public class LoginControllerTest {


    @Mock
    private Model model;
    @Mock
    private LoginPage view;
    @Mock
    private TaskManagerController mainController;
    @InjectMocks
    private LoginController loginController;
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
    public void testLoginButtonWithoutUsernamePassword(){
        when(view.getUsername()).thenReturn("");
        when(view.getPassword()).thenReturn("");

        loginController.onLoginButtonClick();

        verify(view, times(1)).setErrorLabelText("Missing Username/Password");
        try {
            verify(model, times(0)).login(anyString(), anyString());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testLoginButtonWithNotexistingUsernamePassword(){
        when(view.getUsername()).thenReturn("MissingUsername");
        when(view.getPassword()).thenReturn("MissingPassword");
        try {
            when(model.login(anyString(), anyString())).thenReturn(null);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        loginController.onLoginButtonClick();

        try {
            verify(model, times(1)).login(anyString(), anyString());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        verify(view, times(1)).setErrorLabelText("Username/Password aren't registered");
    }

    @Test
    public void testLoginButtonWithCorrectUsernamePassword() {
        when(view.getUsername()).thenReturn("A");
        when(view.getPassword()).thenReturn("B");

        try {
            when(model.login("A", "B")).thenReturn(new User("1", "2", "3"));
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        loginController.onLoginButtonClick();

        verify(mainController).setViewController(any(UserTasksController.class));

    }

    @Test
    public void testRegisterButtonClick(){
        loginController.onRegisterButtonClick();

        verify(mainController, times(1)).setViewController(any(RegisterController.class));
    }

    @Test
    public void testAddEvents(){

        loginController = spy(loginController);
        ArgumentCaptor<ActionListener> captorLogin = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<ActionListener> captorRegister = ArgumentCaptor.forClass(ActionListener.class);

        doNothing().when(loginController).onRegisterButtonClick();
        doNothing().when(loginController).onLoginButtonClick();

        loginController.addEvents();

        verify(view).addActionListenerBtnLogin(captorLogin.capture());
        verify(view).addActionListenerBtnRegister(captorRegister.capture());
        ActionListener listenerLogin = captorLogin.getValue();
        ActionListener listenerRegister = captorRegister.getValue();
        listenerLogin.actionPerformed(null);
        listenerRegister.actionPerformed(null);

        verify(loginController).onLoginButtonClick();
        verify(loginController).onRegisterButtonClick();
    }


}
