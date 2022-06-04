package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.DeleteTaskResponse;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskDetailControllerTest {

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
        Task taskToDelete = new Task("vbnm", "5", "6", "7");
        when(view.getTask()).thenReturn(taskToDelete);
        DeleteTaskResponse tasksAfterDelete = new DeleteTaskResponse(Arrays.asList(
                new Task("RR", "W", "Q", "Y"),
                new Task("QQ", "1", "U", "C"),
                new Task("DD", "2", "L", "V"),
                new Task("XX", "7", "W", "M")
        ), -1);

        try {
            when(model.deleteTaskGetUserTasks(taskToDelete)).thenReturn(tasksAfterDelete);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        taskDetailController.onClickDeleteButton();

        try {
            verify(model).deleteTaskGetUserTasks(taskToDelete);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        ArgumentCaptor<UserTasksController> captor = ArgumentCaptor.forClass(UserTasksController.class);
        verify(mainController).setViewController(captor.capture());

        UserTasksController actual = captor.getValue();
        List<Task> userTasksControllers = actual.getView().getTasks();

        Assert.assertArrayEquals(tasksAfterDelete.getTasks().toArray(), userTasksControllers.toArray());
        Assert.assertFalse(actual.getView().getTasks().contains(taskToDelete));
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

    @Test
    public void testDeleteTaskWhenIsMissingOnDB() throws PermissionException {
        Task task = new Task("vbnm", "5", "6", "7");
        task.setId(1430);
        when(view.getTask()).thenReturn(task);
        when(model.deleteTaskGetUserTasks(task)).thenReturn(new DeleteTaskResponse(new ArrayList<>(), task.getId()));

        taskDetailController.onClickDeleteButton();

        try {
            verify(model).deleteTaskGetUserTasks(task);
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

        ArgumentCaptor<UserTasksController> captor = ArgumentCaptor.forClass(UserTasksController.class);
        verify(mainController).setViewController(captor.capture());

        UserTasksController userTasksController = captor.getValue();

        JLabel labelError = userTasksController.getView().getLabelError();
        Assert.assertTrue(labelError.isVisible());
        Assert.assertEquals(String.format("The task with id %d is missing", task.getId()), labelError.getText());
    }

    @Test
    public void testThrowPermissionExceptionOnGetUserTasksWhenDelete() throws PermissionException {
        when(model.deleteTaskGetUserTasks(any())).thenThrow(PermissionException.class);

        taskDetailController.onClickDeleteButton();

        verify(mainController).initApplication();
        verify(mainController, never()).setViewController(any(UserTasksController.class));
    }
}
