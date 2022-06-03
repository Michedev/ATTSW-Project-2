package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

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
        // Done a pseudo version of parameterized tests
        // I avoided such mechanism because with JUnit 4
        // parameterized tests all test cases would have
        // been executed with the parameters
        List<Pair<String, String>> pairs = Arrays.asList(
                Pair.of("", ""),
                Pair.of("aa", ""),
                Pair.of("", "ff")
        );
        for(Pair<String, String> usernamePassword: pairs){
            when(view.getUsername()).thenReturn(usernamePassword.getLeft());
            when(view.getPassword()).thenReturn(usernamePassword.getRight());

            loginController.onLoginButtonClick();
        }

        verify(view, times(pairs.size())).setErrorLabelText("Missing Username/Password");
        try {
            verify(model, never()).login(anyString(), anyString());
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
            verify(model).login(anyString(), anyString());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        verify(view).setErrorLabelText("Username/Password aren't registered");
    }

    @Test
    public void testGetUserTasksWhenPermissionErrorIsThrown() throws PermissionException {
        when(view.getUsername()).thenReturn("A");
        when(view.getPassword()).thenReturn("B");

        when(model.login(anyString(), anyString())).thenThrow(PermissionException.class);

        loginController.onLoginButtonClick();

        verify(mainController).initApplication();
        verify(mainController, never()).setViewController(any());
    }

    @Test
    public void testLoginWhenPermissionErrorIsThrown() throws PermissionException {
        when(view.getUsername()).thenReturn("A");
        when(view.getPassword()).thenReturn("B");
        when(model.login(anyString(), anyString())).thenReturn(new User("A", "B", "C"));
        when(model.getLoggedUserTasks()).thenThrow(PermissionException.class);

        loginController.onLoginButtonClick();

        verify(mainController).initApplication();
        verify(mainController, never()).setViewController(any());
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

        verify(mainController).setViewController(any(RegisterController.class));
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
