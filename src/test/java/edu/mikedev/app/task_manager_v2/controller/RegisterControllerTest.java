package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        when(registerPage.getEmail()).thenReturn("1");
        
        registerController.onRegisterButtonClick();
        
        verify(mainController, times(0)).setViewController(any());
        verify(registerPage, times(1)).setErrorLabelUsername("Missing username");
    }

    @Test
    public void testMissingPassword(){
        when(registerPage.getUsername()).thenReturn("ff");
        when(registerPage.getPassword()).thenReturn("");
        when(registerPage.getEmail()).thenReturn("1");

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
}
