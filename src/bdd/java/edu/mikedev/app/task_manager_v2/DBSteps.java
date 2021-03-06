package edu.mikedev.app.task_manager_v2;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import org.jbehave.core.annotations.*;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DBSteps {

    private final InitApp initApp;
    private int loggedUserId = 1;
    private final String loginUsername = "username1";
    private final String loginPassword = "password1";

    private HibernateDBUtils dbUtils;

    public DBSteps(InitApp initApp){
        this.initApp = initApp;
    }

    @BeforeStories(order=1)
    public void setDbUtils(){
        this.dbUtils = initApp.getDbUtils();
    }

    @BeforeScenario
    public void initDB(){
        try{
            dbUtils.initAndFillDBTables();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Then("it should exists a task with title \"$title\" and subtasks \"$subtask1\", \"$subtask2\", \"$subtask3\"")
    public void thenShouldExistsATask(String title, String subtask1, String subtask2, String subtask3){
        List<Task> userTasks = dbUtils.getUserTasks(loggedUserId);
        Task task = userTasks.stream().filter(t -> t.getTitle().equals(title)).findFirst().get();

        Assert.assertEquals(title, task.getTitle());
        Assert.assertEquals(subtask1, task.getSubtask1());
        Assert.assertEquals(subtask2, task.getSubtask2());
        Assert.assertEquals(subtask3, task.getSubtask3());
    }

    @Then("the user with username \"$username\" should not exists")
    public void thenMissingUser(String username){
        Assert.assertFalse(dbUtils.getDBUsernames().contains(username));
    }

    @Then("the new task should not exists")
    public void thenNewTaskShouldNotExists(){
        Assert.assertEquals(6, dbUtils.getDBTaskTitles().size());
    }

    @Then("the first task should have the old title")
    public void thenSameTitle(){
        Task task = getFirstTask();
        Assert.assertEquals("title1", task.getTitle());
    }

    private Task getFirstTask() {
        List<Task> userTasks = this.dbUtils.getUserTasks(loggedUserId).stream()
                .sorted(Comparator.comparingInt(Task::getId)).collect(Collectors.toList());
        Task task = userTasks.get(0);
        return task;
    }

    @Given("the first task is deleted from the Database")
    public void givenFirstTaskDeleted(){
        Task first = getFirstTask();

        dbUtils.deleteTask(first);
    }

    @When("I delete the user from the Database")
    public void whenIDeleteTheuserFromTheDatabase(){
        User loggedUser = new User(loginUsername, loginPassword, "dontremember");
        loggedUser.setId(loggedUserId);

        dbUtils.deleteUser(loggedUser);
    }

    @Then("the user should not exists anymore")
    public void thenTheUserShouldNotExistsAnymore(){
        List<String> dbUsernames = dbUtils.getDBUsernames();

        Assert.assertFalse(dbUsernames.contains(loginUsername));
    }
}
