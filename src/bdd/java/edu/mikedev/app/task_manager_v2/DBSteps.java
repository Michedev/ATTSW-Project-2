package edu.mikedev.app.task_manager_v2;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Then;
import org.junit.Assert;

import java.sql.SQLException;
import java.util.List;

public class DBSteps {

    private final InitApp initApp;
    private int idUser = 1;
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
        System.out.println(dbUtils);
        try{
            dbUtils.initAndFillDBTables();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
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
}
