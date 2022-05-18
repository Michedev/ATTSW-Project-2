package edu.mikedev.app.task_manager_v2.utils;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.SessionFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class HibernateDBUtils {
    protected SessionFactory sessionFactory;

    public HibernateDBUtils(){
        this.sessionFactory = buildSessionFactory();
    }

    public HibernateDBUtils(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public abstract SessionFactory buildSessionFactory();

    public abstract void initDBTables() throws SQLException;

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
            statement.execute("INSERT INTO users (id, username, password, email) " +
                    String.format("VALUES (%d, '%s', '%s', '%s')", u.getId(), u.getUsername(), u.getPassword(), u.getEmail()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insertTask(Statement statement, Task t){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            statement.execute("INSERT INTO tasks (id, title, subtask1, subtask2, subtask3, ID_USER) " +
                    String.format("VALUES (%d, '%s', '%s', '%s 00:00:00', %b, '%d'); ", t.getId(), t.getTitle(), t.getSubtask1(), t.getSubtask2(), t.getSubtask3(), t.getTaskOwner().getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Task t){
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            insertTask(statement, t);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void insert(User u){
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            insertUser(statement, u);
            connection.close();
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
            ResultSet resultSet = statement.executeQuery("select * from users where id = " + id);
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
            ResultSet resultSet = statement.executeQuery("select * from tasks where id = " + id);
            resultSet.next();
            task = getTaskFromQuery(resultSet);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    public List<Task> getUsersTask(int userId){
        List<Task> result = new ArrayList<>();
        try {
            Connection connection = initDBConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from tasks where id_user = " + userId);
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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
