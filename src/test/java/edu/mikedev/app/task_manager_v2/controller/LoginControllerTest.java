package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginController loginController;
    private Model model;
    private LoginPage loginPage;

    @Before
    public void setUp(){
        model = mock(Model.class);
        loginPage = mock(LoginPage.class);
        this.loginController = new LoginController(model, loginPage);
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

        verify(loginPage, times(1)).setErrorLabelText("Username/Password aren't registered");
    }

}
