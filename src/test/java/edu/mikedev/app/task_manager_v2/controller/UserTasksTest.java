package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserTasksTest {

    @Mock
    private Model model;
    @Mock
    private UserTasksList userTasksList;
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

        userTasksController.onClickDetailPage(detailTask);

        ArgumentCaptor<TaskDetailController> captor = ArgumentCaptor.forClass(TaskDetailController.class);
        verify(mainController, times(1)).setViewController(captor.capture());

        TaskDetailController taskDetailController = captor.getValue();
        Task actualDetailTask = taskDetailController.getView().getTask();

        Assert.assertEquals(detailTask.getTitle(), actualDetailTask.getTitle());
        Assert.assertEquals(detailTask.getSubtask1(), actualDetailTask.getSubtask1());
        Assert.assertEquals(detailTask.getSubtask2(), actualDetailTask.getSubtask2());
        Assert.assertEquals(detailTask.getSubtask3(), actualDetailTask.getSubtask3());
    }


}
