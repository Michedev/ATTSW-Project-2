package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TaskDetailTest {

    @Mock
    private Model model;
    @Mock
    private TaskDetail view;
    @Mock
    private TaskManagerController mainController;
    @InjectMocks
    private TaskDetailController taskDetailController;
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
    public void testClickUpdateButton(){
        Task task = new Task("vbnm", "5", "6", "7");

        when(view.getTask()).thenReturn(task);

        taskDetailController.onClickUpdateButton();

        ArgumentCaptor<NewUpdateTaskController> captor = ArgumentCaptor.forClass(NewUpdateTaskController.class);

        verify(mainController, times(1)).setViewController(captor.capture());
        NewUpdateTaskController newUpdateTaskController = captor.getValue();
        Task taskToUpdate = newUpdateTaskController.getView().getTaskToUpdate();

        Assert.assertEquals(task, taskToUpdate);
    }

    @Test
    public void testClickDeleteButton(){

    }

}
