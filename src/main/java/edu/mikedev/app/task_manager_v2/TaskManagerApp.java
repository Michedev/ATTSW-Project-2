package edu.mikedev.app.task_manager_v2;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import picocli.CommandLine;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

enum DBType{
    InMemory,
    PostgreSQL,
}

public class TaskManagerApp implements Callable<Integer>
{
    @CommandLine.Option(names = "--dbtype", defaultValue = "PostgreSQL", type = DBType.class)
    private DBType dbType;

    @CommandLine.Option(names = "--address", defaultValue = "localhost")
    private String address;

    @CommandLine.Option(names = "--port", defaultValue = "5432", type = Integer.class)
    private int port;

    @CommandLine.Option(names = "--dbname", defaultValue = "test")
    private String dbName;

    public static void main( String[] args ) {
        new CommandLine(new TaskManagerApp()).execute(args);
    }

    @Override
    public Integer call() throws Exception {
        Module module = new AbstractModule(){
            @Override
            public void configure(){
                Path mainResourceDirectory = Paths.get("src", "main", "resources");
                String hibernateFileName = dbType.equals(DBType.InMemory) ? "hibernate.inmemory.cfg.xml" : "hibernate.cfg.xml";
                File hibernateConfigFile = new File(mainResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

                bind(Path.class).annotatedWith(Names.named("ResourcePath")).toInstance(mainResourceDirectory);
                bind(File.class).annotatedWith(Names.named("HibernateConfigFile")).toInstance(hibernateConfigFile);
                bind(String.class).annotatedWith(Names.named("address")).toInstance(address);
                bind(Integer.class).annotatedWith(Names.named("port")).toInstance(port);
                bind(String.class).annotatedWith(Names.named("DBName")).toInstance(dbName);
                bind(TransactionManager.class).to(HibernateTransactionManager.class);
            }

            @Provides
            public SessionFactory sessionFactoryProvider(@Named("HibernateConfigFile") File configFile,
                                                         @Named("address") String address,
                                                         @Named("port") int port,
                                                         @Named("DBName") String dbName){
                Configuration cfg = new Configuration();
                cfg = cfg.configure(configFile);
                if(dbType.equals(DBType.PostgreSQL)){
                    cfg = cfg.setProperty("hibernate.connection.url", String.format("jdbc:postgresql://%s:%d/%s", address, port, dbName));
                }
                return cfg.buildSessionFactory();
            }

            @Provides
            public JFrame jframeProvider(){
                JFrame jFrame = new JFrame();
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                return jFrame;
            }
        };
        Injector injector = Guice.createInjector(module);
        TaskManagerController controller = injector.getInstance(TaskManagerController.class);
        controller.initApplication();
        controller.showWindow();

        return 0;
    }
}
