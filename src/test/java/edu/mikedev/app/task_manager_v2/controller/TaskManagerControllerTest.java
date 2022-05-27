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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TaskManagerControllerTest {

    @Mock
    private JFrame jFrame;
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
        mainController.initApplication();
    }

    @Test
    public void testSetViewController(){
        RegisterController viewController = mock(RegisterController.class);

        mainController.setViewController(viewController);

        verify(jFrame).setContentPane(any(RegisterPage.class));
        verify(viewController).addEvents();
    }
}
