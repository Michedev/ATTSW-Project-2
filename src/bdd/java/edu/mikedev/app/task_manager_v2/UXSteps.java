package edu.mikedev.app.task_manager_v2;

import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import edu.mikedev.app.task_manager_v2.view.TaskDetail;
import edu.mikedev.app.task_manager_v2.view.UserTasksList;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.hibernate.SessionFactory;
import org.hsqldb.SessionManager;
import org.jbehave.core.annotations.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class UXSteps {
    private FrameFixture window;
    private JFrame jframe;
    private TaskManagerController controller;
    private Model model;
    private static HibernateDBUtils dbUtils;
    public static PostgreSQLContainer<?> postgreSQLContainer;
    private SessionFactory sessionFactory;
    private final String username = "username1";
    private final String password = "password1";
    private final int idUser = 1;
    private Task deletedTask;

    @BeforeStories
    public void setUpClass(){
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3-alpine")
                .withDatabaseName("test").withUsername("root").withPassword("root");
        postgreSQLContainer.start();
        dbUtils = new HibernateDBUtilsPostgre(postgreSQLContainer.getContainerIpAddress(),
                                              postgreSQLContainer.getMappedPort(5432), "test");
    }

    @AfterStories
    public void close(){
        postgreSQLContainer.close();
    }

    @BeforeScenario
    public void beforeStory() {
        try {
            dbUtils.initAndFillDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sessionFactory = dbUtils.buildSessionFactory();
        TransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        model = new Model(transactionManager);
        jframe = GuiActionRunner.execute(() -> {
            jframe = new JFrame();
            controller = new TaskManagerController(jframe, model);
            controller.initApplication();
            return jframe;
        });
        window = new FrameFixture(jframe);
        window.show(); // shows the frame to test
    }

    @AfterScenario
    public void closeStory(){
        sessionFactory.close();
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

        Assert.assertTrue(jframe.getContentPane() instanceof UserTasksList);
        Assert.assertTrue(dbUtils.getDBUsernames().contains(username));
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

    @Then("it should exists a task with title \"$title\" and subtasks \"$subtask1\", \"$subtask2\", \"$subtask3\"")
    public void thenShouldExistsATask(String title, String subtask1, String subtask2, String subtask3){
        List<Task> userTasks = dbUtils.getUserTasks(idUser);
        Task task = userTasks.stream().filter(t -> t.getTitle().equals(title)).findFirst().get();

        Assert.assertEquals(title, task.getTitle());
        Assert.assertEquals(subtask1, task.getSubtask1());
        Assert.assertEquals(subtask2, task.getSubtask2());
        Assert.assertEquals(subtask3, task.getSubtask3());
    }

    @Given("the page of the first task")
    public void givenThePageOfTheFirstTask(){
        doLogin();

        window.button("btnDetailTask1").click();
    }

    @When("I click the delete button")
    public void whenClickDeleteButton(){
        deletedTask = ((TaskDetail) jframe.getContentPane()).getTask();
        window.button("btnDelete").click();
    }

    @Then("the task should not exists")
    public void thenTheTaskShouldNotExists(){
        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(deletedTask.getTitle()));

        Assert.assertTrue(jframe.getContentPane() instanceof UserTasksList);
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
        Assert.assertTrue(dbUtils.getDBTaskTitles().contains(taskTitle));
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

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(deletedTask.getTitle()));
        List<Task> tasks = ((UserTasksList) jframe.getContentPane()).getTasks();
        Assert.assertFalse(tasks.stream().anyMatch(t -> t.getTitle().equals(deletedTask.getTitle())));
    }

    @Then("the second task should have the title \"$title\"")
    public void thenTheSecondTaskShouldHaveThetitle(String title){
        window.label("lblTitleTask2").requireText(title);

        Assert.assertTrue(dbUtils.getDBTaskTitles().contains(title));
    }

    private void doLogin() {
        window.textBox("txtUsername").enterText(username);
        window.textBox("txtPassword").enterText(password);
        window.button("btnLogin").click();
    }

}
