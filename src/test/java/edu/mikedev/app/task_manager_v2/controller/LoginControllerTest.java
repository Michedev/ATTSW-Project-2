package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class LoginControllerTest {


    @Mock
    private Model model;
    @Mock
    private LoginPage loginPage;
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
        when(loginPage.getUsername()).thenReturn("");
        when(loginPage.getPassword()).thenReturn("");

        loginController.onLoginButtonClick();

        verify(loginPage, times(1)).setErrorLabelText("Missing Username/Password");
        try {
            verify(model, times(0)).login(anyString(), anyString());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testLoginButtonWithNotexistingUsernamePassword(){
        when(loginPage.getUsername()).thenReturn("MissingUsername");
        when(loginPage.getPassword()).thenReturn("MissingPassword");
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
        verify(loginPage, times(1)).setErrorLabelText("Username/Password aren't registered");
    }

    @Test
    public void testLoginButtonWithCorrectUsernamePassword() {
        when(loginPage.getUsername()).thenReturn("A");
        when(loginPage.getPassword()).thenReturn("B");

        try {
            when(model.login("A", "B")).thenReturn(new User("1", "2", "3"));
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        loginController.onLoginButtonClick();

        verify(mainController, times(1)).setViewController(any(UserTasksController.class));
    }

    @Test
    public void testRegisterButtonClick(){
        loginController.onRegisterButtonClick();

        verify(mainController, times(1)).setViewController(any(RegisterController.class));
    }

    @Test
    public void testAddEvents(){
        loginController.addEvents(loginPage);

        verify(loginPage).addActionListenerBtnLogin(any());
        verify(loginPage).addActionListenerBtnRegister(any());
    }


}
