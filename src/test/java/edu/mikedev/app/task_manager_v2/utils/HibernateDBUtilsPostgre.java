package edu.mikedev.app.task_manager_v2.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class HibernateDBUtilsPostgre extends HibernateDBUtils {

    @Override
    public SessionFactory buildSessionFactory() {
        Path testResourceDirectory = Paths.get("src", "test", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        return cfg.configure(hibernateConfigFile).buildSessionFactory();
    }

    @Override
    public void initDBTables() throws SQLException {
        Connection conn = initDBConnection();
        Statement statement = conn.createStatement();

        statement.execute("DELETE FROM tasks;");
        statement.execute("DELETE FROM users;");
        statement.execute("COPY Users FROM '/db/fake-data/sample_user.csv' DELIMITER ',' CSV HEADER;");
        statement.execute("COPY Tasks FROM '/db/fake-data/sample_task.csv' DELIMITER ',' CSV HEADER;");

        conn.close();
    }

    @Override
    protected Connection initDBConnection() throws SQLException {
        try {
            return initDBConnection(
                    "jdbc:postgresql://localhost:5432/",
                    "root",
                    "root"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}