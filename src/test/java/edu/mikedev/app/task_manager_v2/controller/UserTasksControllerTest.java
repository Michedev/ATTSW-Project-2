package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserTasksControllerTest {

    @Mock
    private Model model;
    @Mock
    private UserTasksList view;
    @Mock
    private TaskManagerController mainController;
    @InjectMocks
    private UserTasksController userTasksController;
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
    public void testClickDetailButton(){
        Task detailTask = new Task("111", "1", "2", "3");

        userTasksController.onClickDetailButton(detailTask);

        ArgumentCaptor<TaskDetailController> captor = ArgumentCaptor.forClass(TaskDetailController.class);
        verify(mainController, times(1)).setViewController(captor.capture());

        TaskDetailController taskDetailController = captor.getValue();
        Task actualDetailTask = taskDetailController.getView().getTask();

        Assert.assertEquals(detailTask.getTitle(), actualDetailTask.getTitle());
        Assert.assertEquals(detailTask.getSubtask1(), actualDetailTask.getSubtask1());
        Assert.assertEquals(detailTask.getSubtask2(), actualDetailTask.getSubtask2());
        Assert.assertEquals(detailTask.getSubtask3(), actualDetailTask.getSubtask3());
    }

    @Test
    public void testClickNewTaskButton(){
        userTasksController.onClickNewTaskButton();

        verify(mainController, times(1)).setViewController(any(NewUpdateTaskController.class));
    }

    @Test
    public void testAddEvents(){
        List<Task> taskList = Arrays.asList(new Task("12345", "a", "b", "c"),
                                            new Task("qwerty", "g", "h", "j"),
                                            new Task("uu", "o", "p", "Q"));
        when(view.getTasks()).thenReturn(taskList);
        ArgumentCaptor<ActionListener> captorTasksDetails = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<ActionListener> captorNewTask = ArgumentCaptor.forClass(ActionListener.class);

        userTasksController = spy(userTasksController);
        doNothing().when(userTasksController).onClickNewTaskButton();
        doNothing().when(userTasksController).onClickDetailButton(any());

        userTasksController.addEvents();

        verify(view, times(taskList.size())).addActionListenerTaskDetail(anyInt(), captorTasksDetails.capture());
        verify(view).addActionListenerNewButton(captorNewTask.capture());
        List<ActionListener> detailListeners = captorTasksDetails.getAllValues();
        for (ActionListener l :
                detailListeners) {
            l.actionPerformed(null);
        }
        ActionListener newBtnListener = captorNewTask.getValue();
        newBtnListener.actionPerformed(null);

        Assert.assertEquals(taskList.size(), detailListeners.size());
        verify(userTasksController, times(detailListeners.size())).onClickDetailButton(any(Task.class));
        verify(userTasksController).onClickNewTaskButton();
    }
}
