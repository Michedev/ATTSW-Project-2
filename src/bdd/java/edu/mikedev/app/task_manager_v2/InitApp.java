package edu.mikedev.app.task_manager_v2;

import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.Model;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.hibernate.SessionFactory;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.swing.*;
import java.sql.SQLException;

public class InitApp {

    private FrameFixture window;
    private JFrame jframe;
    private TaskManagerController controller;
    private Model model;
    private HibernateDBUtils dbUtils;
    private PostgreSQLContainer<?> postgreSQLContainer;
    private SessionFactory sessionFactory;
    private final String username = "username1";
    private final String password = "password1";
    private final int idUser = 1;
    private Task deletedTask;

    @BeforeStories(order=0)
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
    }

    @AfterScenario
    public void closeStory(){
        sessionFactory.close();
    }

    public JFrame getJframe() {
        return jframe;
    }

    public HibernateDBUtils getDbUtils() {
        return dbUtils;
    }

}
