package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.view.*;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(GUITestRunner.class)
public class ControllerIT extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private JFrame jframe;
    private TaskManagerController controller;
    private Model mockedModel;

    @Override
    protected void onSetUp() throws Exception {
        mockedModel = mock(Model.class);
        jframe = GuiActionRunner.execute(() -> {
            jframe = new JFrame();
            controller = new TaskManagerController(jframe, mockedModel);
            controller.initApplication();
            return jframe;
        });
        window = new FrameFixture(robot(), jframe);
        window.show(); // shows the frame to test
    }

    @Test
    @GUITest
    public void testInitialState(){
        Assert.assertTrue(jframe.getContentPane() instanceof LoginPage);
    }

    @Test
    @GUITest
    public void testGoIntoRegistrationPage(){
        window.button("btnRegister").click();

        Assert.assertTrue(jframe.getContentPane() instanceof RegisterPage);
        Assert.assertEquals(1, window.button().target().getActionListeners().length);
    }

    @Test
    @GUITest
    public void testSuccessfulLogin() throws PermissionException {
        Task task1 = new Task("A", "B", "C", "D");
        task1.setId(1);
        Task task2 = new Task("123", "1", "2", "3");
        task2.setId(2);
        List<Task> tasks = Arrays.asList(task1, task2);

        String username = "user123";
        String password = "pass123";
        User newUser = new User(username, password, "email@email.it", tasks);
        when(mockedModel.loginGetTasks(username, password)).thenReturn(tasks);
        when(mockedModel.getLoggedUserTasks()).thenReturn(Arrays.asList(task1, task2));

        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();

        window.label("lblTitleTask1").requireText(task1.getTitle());
        window.label("lblTitleTask2").requireText(task2.getTitle());
    }

    @Test
    @GUITest
    public void testRegistrationNewUser(){
        String username = "username123";
        String password = "password56";
        String email = "email@email.com";

        window.button("btnRegister").click();

        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.textBox("txtEmail").enterText(email);

        window.button("btnConfirm").click();

        verify(mockedModel).registerUser(any(User.class));
    }

    @Test
    @GUITest
    public void testViewDetailPage() throws PermissionException {
        Pair<Task, Task> tasks = doLogin();
        Task task1 = tasks.getLeft();

        window.button("btnDetailTask1").click();

        Container contentPane = jframe.getContentPane();
        Assert.assertTrue(contentPane instanceof TaskDetail);

        TaskDetail taskDetail = (TaskDetail) contentPane;
        Assert.assertEquals(task1, taskDetail.getTask());

        window.label("lblTaskTitle").requireText(task1.getTitle());
    }

    @Test
    @GUITest
    public void testGoTONewTaskPage() throws PermissionException {
        doLogin();

        window.button("btnNew").click();

        Assert.assertTrue(jframe.getContentPane() instanceof NewUpdateTask);
    }

    @Test
    @GUITest
    public void testNewTask() throws PermissionException {
        doLogin();

        window.button("btnNew").click();

        String newTaskTitle = "AAA";
        String newTaskSubtask1 = "1";
        String newTaskSubtask2 = "2";
        String newTaskSubtask3 = "3";
        window.textBox("txtTaskTitle").enterText(newTaskTitle);
        window.textBox("txtStep1").enterText(newTaskSubtask1);
        window.textBox("txtStep2").enterText(newTaskSubtask2);
        window.textBox("txtStep3").enterText(newTaskSubtask3);

        window.button("btnMake").click();

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(mockedModel).addUserTaskGetTasks(captor.capture());

        Task addedTask = captor.getValue();

        Assert.assertEquals(newTaskTitle, addedTask.getTitle());
        Assert.assertEquals(newTaskSubtask1, addedTask.getSubtask1());
        Assert.assertEquals(newTaskSubtask2, addedTask.getSubtask2());
        Assert.assertEquals(newTaskSubtask3, addedTask.getSubtask3());
    }

    @Test
    @GUITest
    public void testUpdateTask() throws PermissionException {
        Pair<Task, Task> tasks = doLogin();

        Task task1 = tasks.getLeft();

        window.button("btnDetailTask1").click();
        window.button("btnUpdate").click();

        String taskNewTitle = "NewTask23";
        window.textBox("txtTaskTitle").deleteText().enterText(taskNewTitle);
        window.button("btnMake").click();

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(mockedModel).updateTaskGetTasks(captor.capture());

        Task updatedTask = captor.getValue();

        Assert.assertEquals(taskNewTitle, updatedTask.getTitle());
        Assert.assertEquals(task1.getSubtask1(), updatedTask.getSubtask1());
        Assert.assertEquals(task1.getSubtask2(), updatedTask.getSubtask2());
        Assert.assertEquals(task1.getSubtask3(), updatedTask.getSubtask3());

    }

    @Test
    @GUITest
    public void testDeleteTask() throws PermissionException {
        Pair<Task, Task> tasks = doLogin();

        Task task2 = tasks.getRight();

        window.button("btnDetailTask2").click();
        window.button("btnDelete").click();

        Assert.assertTrue(jframe.getContentPane() instanceof UserTasksList);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(mockedModel).deleteTask(captor.capture());

        Task deletedTask = captor.getValue();

        Assert.assertEquals(task2, deletedTask);
    }

    private Pair<Task, Task> doLogin() throws PermissionException {
        Task task1 = new Task("A", "B", "C", "D");
        task1.setId(1);
        Task task2 = new Task("123", "1", "2", "3");
        task2.setId(2);

        List<Task> tasks = Arrays.asList(task1, task2);

        String username = "user123";
        String password = "pass123";

        when(mockedModel.loginGetTasks(username, password)).thenReturn(tasks);
        when(mockedModel.getLoggedUserTasks()).thenReturn(Arrays.asList(task1, task2));

        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();

        return Pair.of(task1, task2);
    }

}
