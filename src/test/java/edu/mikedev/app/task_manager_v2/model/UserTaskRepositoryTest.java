package edu.mikedev.app.task_manager_v2.model;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import edu.mikedev.app.task_manager_v2.utils.HibernateDBUtilsInMemory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public class UserTaskRepositoryTest {

    private static SessionFactory sessionFactory;
    private static HibernateDBUtilsInMemory dbUtils;
    private UserTaskRepository userTaskRepository;
    private Session session;

    @BeforeClass
    public static void setUpHibernate(){
        Path testResourceDirectory = Paths.get("src", "test", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        sessionFactory = cfg.configure(hibernateConfigFile).buildSessionFactory();
        dbUtils = new HibernateDBUtilsInMemory();
    }



    @Before
    public void setUp(){
        try {
            dbUtils.initDBTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session = sessionFactory.openSession();
        userTaskRepository = new UserTaskRepository(session);
    }

    @Test
    public void testUserAdd(){
        String newUsername = "username41";
        User u = new User(newUsername, "passw1", "email@mail.it", new HashSet<Task>());
        Assert.assertFalse(dbUtils.getDBUsernames().contains(newUsername));
        Transaction t = session.beginTransaction();
        userTaskRepository.add(u);
        t.commit();

        Assert.assertTrue(dbUtils.getDBUsernames().contains(newUsername));
    }

    @Test
    public void testUserDelete(){
        User toDelete = dbUtils.users.get(0);

        Assert.assertTrue(dbUtils.getDBUsernames().contains(toDelete.getUsername()));
        Transaction t = session.beginTransaction();
        userTaskRepository.delete(toDelete);
        t.commit();

        Assert.assertFalse(dbUtils.getDBUsernames().contains(toDelete.getUsername()));
        List<String> dbTaskTitles = dbUtils.getDBTaskTitles();
        for(Task task: toDelete.getTasks()){
            Assert.assertFalse(dbTaskTitles.contains(task.getTitle()));
        }
    }

    @Test
    public void testUserGetById(){
        User expectedUser = dbUtils.users.get(0);
        User actual = userTaskRepository.getUserById(expectedUser.getId());

        Assert.assertNotNull(actual);

        Assert.assertEquals(expectedUser.getUsername(), actual.getUsername());
        Assert.assertEquals(expectedUser.getPassword(), actual.getPassword());
        Assert.assertEquals(expectedUser.getEmail(), actual.getEmail());
    }

    @Test
    public void testTaskAdd(){
        Task newTask = new Task("Task123", "Subtask123", "Subtask234", "Subtask345");
        User taskOwner = dbUtils.users.get(0);
        newTask.setTaskOwner(taskOwner);
        List<String> dbTaskTitlesPreAdd = dbUtils.getDBTaskTitles();


        Transaction t = session.beginTransaction();
        userTaskRepository.add(newTask);
        t.commit();

        List<String> dbTaskTitles = dbUtils.getDBTaskTitles();
        Assert.assertEquals(dbTaskTitles.size(), dbTaskTitlesPreAdd.size() + 1);
        Assert.assertTrue(dbTaskTitles.contains(newTask.getTitle()));

        List<Task> userTasks = dbUtils.getUsersTask(taskOwner.getId());
        Assert.assertTrue(userTasks.stream().anyMatch((task) -> task.getTitle().equals(newTask.getTitle())));
    }

    @Test
    public void testTaskUpdate(){
        Task toUpdate = dbUtils.users.get(0).getTasks().iterator().next();
        String oldtitle = toUpdate.getTitle();
        String newTitle = "New Title";
        toUpdate.setTitle(newTitle);
        Assert.assertFalse(session.contains(toUpdate));

        Transaction t = session.beginTransaction();
        userTaskRepository.update(toUpdate);
        t.commit();

        List<String> dbTaskTitles = dbUtils.getDBTaskTitles();
        Assert.assertTrue(dbTaskTitles.contains(newTitle));
        Assert.assertFalse(dbTaskTitles.contains(oldtitle));
    }

    @Test
    public void testTaskDelete(){
        Task toDelete = dbUtils.users.get(1).getTasks().iterator().next();

        Transaction t = session.beginTransaction();
        userTaskRepository.delete(toDelete);
        t.commit();

        Assert.assertFalse(dbUtils.getDBTaskTitles().contains(toDelete.getTitle()));
    }

    @Test
    public void testTaskGetById(){
        Task expected = dbUtils.users.get(1).getTasks().iterator().next();

        Transaction t = session.beginTransaction();
        Task actual = userTaskRepository.getTaskById(expected.getId());
        t.commit();

        Assert.assertEquals(expected.getTitle(), actual.getTitle());
        Assert.assertEquals(expected.getSubtask1(), actual.getSubtask1());
        Assert.assertEquals(expected.getSubtask2(), actual.getSubtask2());
        Assert.assertEquals(expected.getSubtask3(), actual.getSubtask3());
    }

    @After
    public void closeSession(){
        session.close();
    }

}
