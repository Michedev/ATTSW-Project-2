package edu.mikedev.app.task_manager_v2;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.data.DBType;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import picocli.CommandLine;

import javax.swing.*;
import java.util.concurrent.Callable;

public class TaskManagerApp implements Callable<Integer>
{
    @CommandLine.Option(names = "--dbtype", defaultValue = "POSTGRESSQL", type = DBType.class)
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
        com.google.inject.Module module = new AbstractModule(){
            @Override
            public void configure(){
                String hibernateFileName = DBType.INMEMORY.equals(dbType) ? "hibernate.inmemory.cfg.xml" : "hibernate.cfg.xml";

                bind(String.class).annotatedWith(Names.named("HibernateConfigFilename")).toInstance(hibernateFileName);
                bind(String.class).annotatedWith(Names.named("address")).toInstance(address);
                bind(Integer.class).annotatedWith(Names.named("port")).toInstance(port);
                bind(String.class).annotatedWith(Names.named("DBName")).toInstance(dbName);
                bind(TransactionManager.class).to(HibernateTransactionManager.class);
            }

            @Provides
            public SessionFactory sessionFactoryProvider(@Named("HibernateConfigFilename") String configFileName,
                                                         @Named("address") String address,
                                                         @Named("port") int port,
                                                         @Named("DBName") String dbName){
                Configuration cfg = new Configuration();
                cfg = cfg.configure(configFileName);
                if(dbType.equals(DBType.POSTGRESSQL)){
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
