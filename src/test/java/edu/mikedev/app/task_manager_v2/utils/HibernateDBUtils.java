package edu.mikedev.app.task_manager_v2.utils;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class HibernateDBUtils {

    public List<User> users;

    public HibernateDBUtils(){
    }

    public abstract SessionFactory buildSessionFactory();

    protected abstract void initDBTables(Statement statement) throws SQLException;

    public void initAndFillDBTables() throws SQLException {
        Connection connection = initDBConnection();
        Statement statement = connection.createStatement();

        initDBTables(statement);

        statement.execute("DELETE FROM Tasks");
        statement.execute("DELETE FROM Users");

        fillDBTables(statement);

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void fillDBTables(Statement statement) {
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

        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        task4.setId(3);
        task5.setId(4);
        task6.setId(5);

        List<Task> taskList1 = Arrays.asList(task1, task2, task3);
        List<Task> taskList2 = Arrays.asList(task4, task5, task6);

        User user1 = new User(username1, password1, email);
        user1.setTasks(taskList1);
        for(Task t: taskList1){
            t.setTaskOwner(user1);
        }
        user1.setId(1);
        insertUser(statement, user1);

        User user2 = new User(username2, password2, email);
        user2.setTasks(taskList2);
        for(Task t: taskList2){
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
    }

    protected abstract Connection initDBConnection() throws SQLException;

    protected Connection initDBConnection(String url, String username, String password) throws SQLException {
        Properties props = new Properties();
        final String userField = "user";
        final String passwordField = "password";
        props.setProperty(userField, username);
        props.setProperty(passwordField, password);

        return DriverManager.getConnection(url, props);
    }

    public List<String> getDBTaskTitles() {
        return pullListStringFromDB("select * from Tasks", "title");
    }

    public List<String> getDBUsernames() {
        return pullListStringFromDB("select * from Users", "username");
    }

    public List<String> getDBTaskTitlesOfUser(int userID){
        return pullListStringFromDB(String.format("select * from Tasks where id_user = %d", userID), "title");
    }

    protected List<String> pullListStringFromDB(String query, String fieldName) {
        List<String> resultList = new ArrayList<>();
        Connection connection = null;
        ResultSet valuesDBIterator = null;
        try {
            connection = initDBConnection();
            valuesDBIterator = connection.createStatement().executeQuery(query);
            while (valuesDBIterator.next()) {
                String value = valuesDBIterator.getString(fieldName);
                resultList.add(value);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    protected void insertUser(Statement statement, User u){
        try {
            statement.execute("INSERT INTO Users (id, username, password, email) " +
                    String.format("VALUES (%d, '%s', '%s', '%s')", u.getId(), u.getUsername(), u.getPassword(), u.getEmail()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insertTask(Statement statement, Task t){
        try {
            statement.execute("INSERT INTO Tasks (id, title, subtask1, subtask2, subtask3, ID_USER) " +
                    String.format("VALUES (%d, '%s', '%s', '%s', '%s', '%d'); ", t.getId(), t.getTitle(), t.getSubtask1(), t.getSubtask2(), t.getSubtask3(), t.getTaskOwner().getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Task getTaskFromQuery(ResultSet resultSet) throws SQLException {
        Task task = new Task(resultSet.getString("title"), resultSet.getString("subtask1"),
                resultSet.getString("subtask2"), resultSet.getString("subtask3"));
        task.setId(resultSet.getInt("id"));
        int idUser = resultSet.getInt("id_user");
        User taskUser = getUserById(idUser);
        task.setTaskOwner(taskUser);
        return task;
    }

    public User getUserById(int id){
        User user = null;
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Users where id = " + id);
            resultSet.next();
            user = getUserFromQuery(resultSet);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    protected User getUserFromQuery(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email")
        );
        user.setId(resultSet.getInt("id"));
        return user;
    }

    public Task getTaskById(int id){
        Task task = null;
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Tasks where id = " + id);
            resultSet.next();
            task = getTaskFromQuery(resultSet);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public List<Task> getUserTasks(int userId){
        List<Task> result = new ArrayList<>();
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Tasks where id_user = " + userId + " order by id");
            while(resultSet.next()){
                Task task = getTaskFromQuery(resultSet);
                result.add(task);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public void deleteTask(Task task) {
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            statement.execute(String.format("DELETE FROM Tasks where id = %d", task.getId()));
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            statement.execute(String.format("DELETE FROM Tasks where ID_USER = %d", user.getId()));
            statement.execute(String.format("DELETE FROM Users where id = %d", user.getId()));
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
