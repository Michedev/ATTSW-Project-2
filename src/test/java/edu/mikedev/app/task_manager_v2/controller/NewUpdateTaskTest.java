package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.awt.event.ActionListener;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class NewUpdateTaskTest {

    @Mock
    private Model model;
    @Mock
    private NewUpdateTask view;
    @Mock
    private TaskManagerController mainController;
    @InjectMocks
    private NewUpdateTaskController newUpdateTaskController;
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
    public void testNewTaskWithoutTaskTitle(){
        when(view.getTaskTitle()).thenReturn("");
        when(view.getTaskSubtask1()).thenReturn("123");
        when(view.getTaskSubtask2()).thenReturn("JJ");
        when(view.getTaskSubtask3()).thenReturn("789");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(view).setTitleErrorLabelText("Missing title");
    }

    @Test
    public void testNewTaskWithoutTaskSubtask1(){
        when(view.getTaskTitle()).thenReturn("TTT");
        when(view.getTaskSubtask1()).thenReturn("");
        when(view.getTaskSubtask2()).thenReturn("JJ");
        when(view.getTaskSubtask3()).thenReturn("789");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(view).setStep1ErrorLabelText("Missing subtask 1");
    }

    @Test
    public void testNewTaskWithoutTaskSubtask2(){
        when(view.getTaskTitle()).thenReturn("TTT");
        when(view.getTaskSubtask1()).thenReturn("EEE");
        when(view.getTaskSubtask2()).thenReturn("");
        when(view.getTaskSubtask3()).thenReturn("789");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(view).setStep2ErrorLabelText("Missing subtask 2");
    }

    @Test
    public void testNewTaskWithoutTaskSubtask3(){
        when(view.getTaskTitle()).thenReturn("TTT");
        when(view.getTaskSubtask1()).thenReturn("EEE");
        when(view.getTaskSubtask2()).thenReturn("YYY");
        when(view.getTaskSubtask3()).thenReturn("");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(view).setStep3ErrorLabelText("Missing subtask 3");
    }


    @Test
    public void testTaskWithoutAnything(){
        when(view.getTaskTitle()).thenReturn("");
        when(view.getTaskSubtask1()).thenReturn("");
        when(view.getTaskSubtask2()).thenReturn("");
        when(view.getTaskSubtask3()).thenReturn("");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(view).setTitleErrorLabelText("Missing title");
        verify(view).setStep1ErrorLabelText("Missing subtask 1");
        verify(view).setStep2ErrorLabelText("Missing subtask 2");
        verify(view).setStep3ErrorLabelText("Missing subtask 3");
    }

    @Test
    public void testMakeNewTask() throws PermissionException {
        String taskTitle = "TTT";
        String subtask1 = "EEE";
        String subtask2 = "ZZZ";
        String subtask3 = "789";

        when(view.getTaskTitle()).thenReturn(taskTitle);
        when(view.getTaskSubtask1()).thenReturn(subtask1);
        when(view.getTaskSubtask2()).thenReturn(subtask2);
        when(view.getTaskSubtask3()).thenReturn(subtask3);

        newUpdateTaskController.onClickMakeButton();

        Task task = new Task(taskTitle, subtask1, subtask2, subtask3);
        InOrder inOrder = inOrder(model, mainController);
        inOrder.verify(model).addUserTaskGetTasks(task);
        inOrder.verify(model).getLoggedUserTasks();
        inOrder.verify(mainController).setViewController(any(UserTasksController.class));

        verify(model, never()).updateTaskGetTasks(any());
    }

    @Test
    public void testUpdateTaskWhenThrowPermissionError() throws PermissionException {
        String taskTitle = "TTT";
        String subtask1 = "EEE";
        String subtask2 = "ZZZ";
        String subtask3 = "789";

        when(view.getTaskToUpdate()).thenReturn(new Task("Title", "1", "2", "3"));
        when(view.getTaskTitle()).thenReturn(taskTitle);
        when(view.getTaskSubtask1()).thenReturn(subtask1);
        when(view.getTaskSubtask2()).thenReturn(subtask2);
        when(view.getTaskSubtask3()).thenReturn(subtask3);

        doThrow(PermissionException.class).when(model).updateTaskGetTasks(any());

        newUpdateTaskController.onClickMakeButton();

        verify(mainController, never()).setViewController(any());
        verify(mainController).initApplication();
    }

    @Test
    public void testThrowPermissionExceptionWhenGetUserTasks() throws PermissionException {
        when(model.getLoggedUserTasks()).thenThrow(PermissionException.class);
        when(view.getTaskTitle()).thenReturn("TTT");
        when(view.getTaskSubtask1()).thenReturn("EEE");
        when(view.getTaskSubtask2()).thenReturn("YYY");
        when(view.getTaskSubtask3()).thenReturn("DDD");

        newUpdateTaskController.onClickMakeButton();

        verify(mainController).initApplication();
        verify(mainController, never()).setViewController(any(UserTasksController.class));
        verify(model).addUserTaskGetTasks(any());
    }

    @Test
    public void testUpdateTask() throws PermissionException {
        Task toUpdate = new Task("AAA", "1", "2", "3");
        toUpdate.setId(100);
        when(view.getTaskToUpdate()).thenReturn(toUpdate);

        String newTaskTitle = "TTT";
        String newSubtask1 = "R";
        String newSubtask2 = "T";
        String newSubtask3 = "Y";

        when(view.getTaskTitle()).thenReturn(newTaskTitle);
        when(view.getTaskSubtask1()).thenReturn(newSubtask1);
        when(view.getTaskSubtask2()).thenReturn(newSubtask2);
        when(view.getTaskSubtask3()).thenReturn(newSubtask3);

        newUpdateTaskController.onClickMakeButton();

        Task updatedTask = new Task(newTaskTitle, newSubtask1, newSubtask2, newSubtask3);
        updatedTask.setId(toUpdate.getId());

        InOrder inOrder = inOrder(model, mainController);
        inOrder.verify(model).updateTaskGetTasks(updatedTask);
        inOrder.verify(model).getLoggedUserTasks();
        inOrder.verify(mainController).setViewController(any(UserTasksController.class));
    }

    @Test
    public void testAddEvents(){
        newUpdateTaskController = spy(newUpdateTaskController);
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        doNothing().when(newUpdateTaskController).onClickMakeButton();

        newUpdateTaskController.addEvents();

        verify(view).addActionListenerMakeButton(captor.capture());
        ActionListener listener = captor.getValue();
        listener.actionPerformed(null);
        verify(newUpdateTaskController).onClickMakeButton();
    }

    @Test
    public void testUpdateTaskWhenNotExistingOntoTheDB() throws PermissionException {
        String subtask1 = "R";
        String subtask2 = "T";
        String subtask3 = "Y";
        Task toUpdate = new Task("AAA", subtask1, subtask2, subtask3);
        toUpdate.setId(100);
        when(view.getTaskToUpdate()).thenReturn(toUpdate);
        Task loggedTask = new Task("1", "2", "3", "4");
        loggedTask.setId(40);
        when(model.getLoggedUserTasks()).thenReturn(Arrays.asList(loggedTask));
        UserTasksList mockedUserTasksListView = mock(UserTasksList.class);
        NewUpdateTaskController spiedController = spy(newUpdateTaskController);
        when(spiedController.makeUserTasksList(anyList())).thenReturn(mockedUserTasksListView);

        String newTaskTitle = "TTT";

        when(view.getTaskTitle()).thenReturn(newTaskTitle);
        when(view.getTaskSubtask1()).thenReturn(subtask1);
        when(view.getTaskSubtask2()).thenReturn(subtask2);
        when(view.getTaskSubtask3()).thenReturn(subtask3);

        spiedController.onClickMakeButton();

        Task updatedTask = new Task(newTaskTitle, subtask1, subtask2, subtask3);
        updatedTask.setId(100);

        InOrder inOrder = inOrder(model, mainController);
        inOrder.verify(model).updateTaskGetTasks(updatedTask);
        inOrder.verify(model).getLoggedUserTasks();
        inOrder.verify(mainController).setViewController(any(UserTasksController.class));

        verify(mockedUserTasksListView).setErrorMessage(String.format("The task with id %d is missing", toUpdate.getId()));
    }
}
