package edu.mikedev.app.task_manager_v2.utils;

import edu.mikedev.app.task_manager_v2.data.Task;
import edu.mikedev.app.task_manager_v2.data.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.runner.Description;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HibernateDBUtilsInMemory extends HibernateDBUtils {

    @Override
    public SessionFactory buildSessionFactory() {
        Path testResourceDirectory = Paths.get("src", "test", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        return cfg.configure(hibernateConfigFile).buildSessionFactory();
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
