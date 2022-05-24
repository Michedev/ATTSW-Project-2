package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtils;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsPostgre;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;

public class ModelIT {

    @SuppressWarnings("rawtypes")
    @ClassRule(order=0)
    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.3")
            .withUsername("root").withPassword("root").withDatabaseName("TaskManager");

    public static HibernateDBUtils dbUtils;
    private SessionFactory sessionFactory;
    private Model model;

    @BeforeClass
    public void setUpClass(){
        dbUtils = new HibernateDBUtilsPostgre();
    }

    @Before
    public void setUp(){
        try {
            dbUtils.initDBTables();
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

    @After
    public void closeSessionFactory(){
        sessionFactory.close();
    }
}
