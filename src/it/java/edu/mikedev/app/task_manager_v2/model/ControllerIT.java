package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import edu.mikedev.app.task_manager_v2.view.NewUpdateTask;
import edu.mikedev.app.task_manager_v2.view.RegisterPage;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.swing.*;

import java.util.HashSet;
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
        Set<Task> tasks = new HashSet<>();
        Task task1 = new Task("A", "B", "C", "D");
        task1.setId(1);
        Task task2 = new Task("123", "1", "2", "3");
        task2.setId(2);
        tasks.add(task1);
        tasks.add(task2);

        String username = "user123";
        String password = "pass123";
        User newUser = new User(username, password, "email@email.it", tasks);
        when(mockedModel.login(username, password)).thenReturn(newUser);

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

}
