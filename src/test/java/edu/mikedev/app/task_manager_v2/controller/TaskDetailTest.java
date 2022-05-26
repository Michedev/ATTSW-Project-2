package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

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
        Task task = new Task("vbnm", "5", "6", "7");
        when(view.getTask()).thenReturn(task);
        List<Task> expected = Arrays.asList(
                new Task("RR", "W", "Q", "Y"),
                new Task("QQ", "1", "U", "C"),
                new Task("DD", "2", "L", "V"),
                new Task("XX", "7", "W", "M")
        );
        try {
            when(model.getLoggedUserTasks()).thenReturn(expected);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        taskDetailController.onClickDeleteButton();

        try {
            verify(model).deleteTask(task);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        ArgumentCaptor<UserTasksController> captor = ArgumentCaptor.forClass(UserTasksController.class);
        verify(mainController).setViewController(captor.capture());

        UserTasksController actual = captor.getValue();
        List<Task> userTasksControllers = actual.getView().getTasks();

        Assert.assertArrayEquals(expected.toArray(), userTasksControllers.toArray());
    }

    @Test
    public void testAddEvents(){
        taskDetailController = spy(taskDetailController);
        ArgumentCaptor<ActionListener> captorUpdate = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<ActionListener> captorDelete = ArgumentCaptor.forClass(ActionListener.class);

        doNothing().when(taskDetailController).onClickUpdateButton();
        doNothing().when(taskDetailController).onClickDeleteButton();

        taskDetailController.addEvents();

        verify(view).addActionListenerUpdateButton(captorUpdate.capture());
        verify(view).addActionListenerDeleteButton(captorDelete.capture());
        ActionListener listenerUpdate = captorUpdate.getValue();
        ActionListener listenerDelete = captorDelete.getValue();
        listenerUpdate.actionPerformed(null);
        listenerDelete.actionPerformed(null);

        verify(taskDetailController).onClickUpdateButton();
        verify(taskDetailController).onClickDeleteButton();
    }

}
