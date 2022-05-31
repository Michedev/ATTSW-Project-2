package edu.mikedev.app.task_manager_v2;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.view.LoginPage;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.jbehave.core.annotations.*;
import org.junit.Assert;

import javax.swing.*;
import java.util.List;

public class GUISteps {


    private final String username = "username1";
    private final String password = "password1";
    private final InitApp initApp;
    private Task deletedTask;
    private FrameFixture window;
    private JFrame jframe;

    public GUISteps(InitApp initApp){
        this.initApp = initApp;
    }

    @BeforeScenario(order = 1)
    public void beforeStory() {
        jframe = initApp.getJframe();
        window = new FrameFixture(jframe);
        window.show(); // shows the frame to test
    }

    @AfterScenario(order = 1)
    public void closeStory(){
        window.cleanUp();
    }


    @Given("an unlogged user")
    public void givenAnUnloggedUser(){
    }

    @When("I register with username \"$username\", password \"$password\" and email \"$email\"")
    public void registerUser(String username, String password, String email){
        window.button("btnRegister").click();

        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.textBox("txtEmail").enterText(email);

        window.button("btnConfirm").click();
    }

    @Then("I login by prompting the username \"$username\" and password \"$password\"")
    public void thenLoginPrompting(String username, String password){
        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();
    }

    @Given("the page to add a new task")
    public void givenAddNewTask(){
        doLogin();

        window.button("btnNew").click();
    }

    @When("I add a new task with title \"$title\" and subtasks \"$subtask1\", \"$subtask2\", \"$subtask3\"")
    public void whenAddNewTask(String title, String subtask1, String subtask2, String subtask3){
        if(jframe.getContentPane() instanceof UserTasksList){
            window.button("btnNew").click();
        }

        window.textBox("txtTaskTitle").enterText(title);
        window.textBox("txtStep1").enterText(subtask1);
        window.textBox("txtStep2").enterText(subtask2);
        window.textBox("txtStep3").enterText(subtask3);

        window.button("btnMake").click();
    }

    @Given("the page of the first task")
    public void givenThePageOfTheFirstTask(){
        doLogin();

        window.button("btnDetailTask1").click();
    }

    @When("I click the delete button")
    @Alias("the user clicks the Delete button")
    public void whenClickDeleteButton(){
        deletedTask = ((TaskDetail) jframe.getContentPane()).getTask();
        window.button("btnDelete").click();
    }

    @Then("the task should not exists")
    public void thenTheTaskShouldNotExists(){
        UserTasksList list = ((UserTasksList) jframe.getContentPane());
        List<Task> taskList = list.getTasks();
        Assert.assertFalse(taskList.stream().anyMatch(t -> t.getTitle().equals(deletedTask.getTitle())));
    }

    @Given("the update page of the first task")
    public void givenTheUpdatePageOfTheFirstTask(){
        doLogin();

        window.button("btnDetailTask1").click();
        window.button("btnUpdate").click();
    }

    @When("I update the title with \"$newTitle\"")
    public void whenIUpdateTheTitle(String newTitle){
        window.textBox("txtTaskTitle").deleteText().enterText(newTitle);

        window.button("btnMake").click();
    }

    @Then("the first task has the title \"$taskTitle\"")
    public void thenTheFirstTaskHasTheTitle(String taskTitle){
        window.label("lblTitleTask1").requireText(taskTitle);
    }

    @Given("a logged user")
    public void givenALoggedUser(){
        doLogin();
    }

    @When("I delete the first task")
    public void whenIDeleteTheFirstTask(){
        window.button("btnDetailTask1").click();
        deletedTask = ((TaskDetail) jframe.getContentPane()).getTask();
        window.button("btnDelete").click();
    }

    @When("I update the second task with the new title \"$newTitle\"")
    public void whenIUpdateTheSecondTask(String newTitle){
        window.button("btnDetailTask2").click();
        window.button("btnUpdate").click();

        window.textBox("txtTaskTitle").deleteText().enterText(newTitle);
        window.button("btnMake").click();
    }

    @Then("the first task should not exists")
    public void thenTheFirstTaskShouldNotExists(){
        Assert.assertNotNull(deletedTask);

        List<Task> tasks = ((UserTasksList) jframe.getContentPane()).getTasks();
        Assert.assertFalse(tasks.stream().anyMatch(t -> t.getTitle().equals(deletedTask.getTitle())));
    }

    @Then("the second task should have the title \"$title\"")
    public void thenTheSecondTaskShouldHaveThetitle(String title){
        window.label("lblTitleTask2").requireText(title);
    }

    @When("I update with the new title \"\"")
    public void updateMissingTitle(){
        window.textBox("txtTaskTitle").deleteText();

        window.button("btnMake").click();
    }

    @When("I update with the new title \"$newTitle\"")
    public void whenUpdateTitleOnly(String newTitle){
        window.textBox("txtTaskTitle").deleteText().enterText(newTitle);

        window.button("btnMake").click();
    }

    @Then("it should show an error message saying that the task doesn't exists anymore")
    public void thenItShouldShowAnErrorMessage(){
        List<Task> actualTasks = ((UserTasksList) jframe.getContentPane()).getTasks();
        Assert.assertEquals(2, actualTasks.size());
        window.label("lblError").requireVisible()
                .requireText("The task with id 1 doesn't exists anymore");
    }

    private void doLogin() {
        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();
    }

}
