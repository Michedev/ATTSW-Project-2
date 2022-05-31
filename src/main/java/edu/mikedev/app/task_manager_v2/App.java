package edu.mikedev.app.task_manager_v2;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App
{
    public static void main( String[] args ) {
        Module module = new AbstractModule(){
            @Override
            public void configure(){
                Path mainResourceDirectory = Paths.get("src", "main", "resources");
                File hibernateConfigFile = new File(mainResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

                bind(Path.class).annotatedWith(Names.named("ResourcePath")).toInstance(mainResourceDirectory);
                bind(File.class).annotatedWith(Names.named("HibernateConfigFile")).toInstance(hibernateConfigFile);
                bind(String.class).annotatedWith(Names.named("URL")).toInstance("localhost");
                bind(Integer.class).annotatedWith(Names.named("port")).toInstance(5432);
                bind(String.class).annotatedWith(Names.named("DBName")).toInstance("test");
                bind(TransactionManager.class).to(HibernateTransactionManager.class);
            }

            @Provides
            public SessionFactory sessionFactoryProvider(@Named("HibernateConfigFile") File configFile,
                                                         @Named("URL") String url,
                                                         @Named("port") int port,
                                                         @Named("DBName") String dbName){
                Configuration cfg = new Configuration();
                cfg = cfg.configure(configFile);
                cfg = cfg.setProperty("hibernate.connection.url", String.format("jdbc:postgresql://%s:%d/%s", url, port, dbName));
                return cfg.buildSessionFactory();
            }
        };
        Injector injector = Guice.createInjector(module);
        TaskManagerController controller = injector.getInstance(TaskManagerController.class);
        controller.initApplication();
    }
}
