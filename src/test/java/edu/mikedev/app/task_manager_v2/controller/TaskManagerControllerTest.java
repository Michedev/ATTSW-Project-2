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

import javax.swing.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskManagerControllerTest {

    @Mock
    private JFrame jFrame;
    @Mock
    private Model model;
    @InjectMocks
    private TaskManagerController mainController;
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
    public void testInitApplication(){
        mainController = spy(mainController);
        mainController.initApplication();

        verify(mainController).setViewController(any(LoginController.class));
        verify(jFrame).setContentPane(any(LoginPage.class));
    }

    @Test
    public void testSetViewController(){
        RegisterController viewController = mock(RegisterController.class);
        when(viewController.getView()).thenReturn(mock(RegisterPage.class));

        mainController.setViewController(viewController);

        verify(jFrame).setContentPane(any(RegisterPage.class));
        verify(viewController).addEvents();
        verify(jFrame).pack();
    }
}
