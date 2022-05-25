package edu.mikedev.app.task_manager_v2.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class HibernateDBUtilsPostgre extends HibernateDBUtils {

    private final String ipAddress;
    private final int port;
    private final String dbName;

    public HibernateDBUtilsPostgre(String ipAddress, int port, String dbName) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.dbName = dbName;
    }

    @Override
    public SessionFactory buildSessionFactory() {
        Path testResourceDirectory = Paths.get("src", "it", "resources");
        File hibernateConfigFile = new File(testResourceDirectory.resolve("hibernate.cfg.xml").toAbsolutePath().toString());

        Configuration cfg = new Configuration();
        cfg = cfg.configure(hibernateConfigFile);
        cfg = cfg.setProperty("hibernate.connection.url", String.format("jdbc:postgresql://%s:%d/%s", this.ipAddress, this.port, this.dbName));
        return cfg.buildSessionFactory();
    }

    @Override
    protected void initDBTables(Statement statement) throws SQLException {
        statement.execute("\n" +
                "CREATE TABLE IF NOT EXISTS Users (\n" +
                "        id serial PRIMARY KEY, \n" +
                "\tusername varchar(255),\n" +
                "\tpassword varchar(255),\n" +
                "\temail varchar(255)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS Tasks ( \n" +
                "\tid serial PRIMARY KEY,\n" +
                "\ttitle varchar(255),\n" +
                "\tsubtask1 varchar(1000),\n" +
                "\tsubtask2 varchar(1000),\n" +
                "\tsubtask3 varchar(1000),\n" +
                "\tid_user int,\n" +
                "\tFOREIGN KEY (id_user) REFERENCES Users (id)\n" +
                ");\n" +
                "\n");
    }

    @Override
    protected Connection initDBConnection() throws SQLException {
        return initDBConnection(
                String.format("jdbc:postgresql://%s:%d/%s", this.ipAddress, this.port, this.dbName),
                "root",
                "root"
        );
    }
}