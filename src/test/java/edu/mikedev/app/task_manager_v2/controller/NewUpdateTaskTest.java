package edu.mikedev.app.task_manager_v2.controller;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.PermissionException;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        inOrder.verify(model).addUserTask(task);
        inOrder.verify(model).getLoggedUserTasks();
        inOrder.verify(mainController).setViewController(any(UserTasksController.class));

        verify(model, never()).updateTask(any());
    }

    @Test
    public void testUpdateTask() throws PermissionException {
        String subtask1 = "R";
        String subtask2 = "T";
        String subtask3 = "Y";
        Task toUpdate = new Task("AAA", subtask1, subtask2, subtask3);
        toUpdate.setId(100);
        when(view.getTaskToUpdate()).thenReturn(toUpdate);

        String newTaskTitle = "TTT";

        when(view.getTaskTitle()).thenReturn(newTaskTitle);
        when(view.getTaskSubtask1()).thenReturn(subtask1);
        when(view.getTaskSubtask2()).thenReturn(subtask2);
        when(view.getTaskSubtask3()).thenReturn(subtask3);

        newUpdateTaskController.onClickMakeButton();

        Task updatedTask = new Task(newTaskTitle, subtask1, subtask2, subtask3);

        InOrder inOrder = inOrder(model, mainController);
        inOrder.verify(model).updateTask(updatedTask);
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
        listener.actionPerformed(new ActionEvent("", 0, ""));
        verify(newUpdateTaskController).onClickMakeButton();
    }
}
