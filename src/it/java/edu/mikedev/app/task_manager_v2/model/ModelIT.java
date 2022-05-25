package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.OutputFrame;

import java.sql.SQLException;

public class ModelIT {

    @ClassRule
    public final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3-alpine")
            .withDatabaseName("test").withUsername("root").withPassword("root");
    private static HibernateDBUtilsPostgre dbUtils;
    private SessionFactory sessionFactory;
    private Model model;


    @BeforeClass
    public static void setUpClass(){
        dbUtils = new HibernateDBUtilsPostgre(postgreSQLContainer.getContainerIpAddress(), postgreSQLContainer.getMappedPort(5432), "test");
    }

    @Before
    public void setUp(){
        try {
            dbUtils.initAndFillDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sessionFactory = dbUtils.buildSessionFactory();
        TransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        model = new Model(transactionManager);
    }

    @Test
    public void testLogin(){
        User expectedUser = dbUtils.users.iterator().next();
        User loggedUser = null;
        try {
            loggedUser = model.login(expectedUser.getUsername(), expectedUser.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(loggedUser);
        Assert.assertEquals(expectedUser.getId(), loggedUser.getId());
        Assert.assertEquals(expectedUser.getUsername(), loggedUser.getUsername());
        Assert.assertEquals(expectedUser.getPassword(), loggedUser.getPassword());
        Assert.assertEquals(expectedUser.getEmail(), loggedUser.getEmail());
    }

    @Test
    public void testAddTask(){
        User user = dbUtils.users.iterator().next();

        try {
            model.login(user.getUsername(), user.getPassword());
        } catch (PermissionException e) {
            Assert.fail(e.getMessage());
        }

    }

    @After
    public void closeSessionFactory(){
        sessionFactory.close();
    }
}