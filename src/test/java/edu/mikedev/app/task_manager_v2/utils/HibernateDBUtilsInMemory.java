package edu.mikedev.app.task_manager_v2.utils;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HibernateDBUtilsInMemory extends HibernateDBUtils {

    public List<User> users = null;

    @Override
    public SessionFactory buildSessionFactory() {
        Path testResourceDirectory = Paths.get("src", "test", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        return cfg.configure(hibernateConfigFile).buildSessionFactory();
    }

    @Override
    public void initDBTables() throws SQLException {
        Connection connection = initDBConnection();
        Statement statement = connection.createStatement();

        statement.execute("DELETE FROM tasks");
        statement.execute("DELETE FROM users");

        Set<Task> taskSet1 = new HashSet<>();
        Task task1 = new Task("title1", "subtask1-1", "subtask2-1", "subtask3-1");
        Task task2 = new Task("title2", "subtask1-2", "subtask2-2", "subtask3-2");
        Task task3 = new Task("title3", "subtask1-3", "subtask2-3", "subtask3-3");
        Task task4 = new Task("title4", "subtask1-4", "subtask2-4", "subtask3-4");
        Task task5 = new Task("title5", "subtask1-5", "subtask2-5", "subtask3-5");
        Task task6 = new Task("title6", "subtask1-6", "subtask2-6", "subtask3-6");
        String username1 = "username1";
        String password1 = "password1";
        String username2 = "username";
        String password2 = "password";
        String email = "email@email.com";
        Set<Task> taskSet2 = new HashSet<>();

        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);
        task5.setId(5);
        task6.setId(6);

        taskSet1.add(task1);
        taskSet1.add(task2);
        taskSet1.add(task3);
        taskSet2.add(task4);
        taskSet2.add(task5);
        taskSet2.add(task6);

        User user1 = new User(username1, password1, email);
        user1.setTasks(taskSet1);
        for(Task t: taskSet1){
            t.setTaskOwner(user1);
        }
        user1.setId(1);
        insertUser(statement, user1);

        User user2 = new User(username2, password2, email);
        user2.setTasks(taskSet2);
        for(Task t: taskSet2){
            t.setTaskOwner(user2);
        }
        user2.setId(2);

        insertUser(statement, user2);

        insertTask(statement, task1);
        insertTask(statement, task2);
        insertTask(statement, task3);
        insertTask(statement, task4);
        insertTask(statement, task5);
        insertTask(statement, task6);

        users = Arrays.asList(user1, user2);

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected Connection initDBConnection() {
        try {
            return initDBConnection(
                    "jdbc:hsqldb:mem:inmemorydb",
                    "fakeuser",
                    "fakepassword"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
