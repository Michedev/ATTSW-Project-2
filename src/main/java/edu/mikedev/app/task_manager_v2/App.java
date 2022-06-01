package edu.mikedev.app.task_manager_v2;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import edu.mikedev.app.task_manager_v2.controller.TaskManagerController;
import edu.mikedev.app.task_manager_v2.model.HibernateTransactionManager;
import edu.mikedev.app.task_manager_v2.model.TransactionManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App
{
    public static void main( String[] args ) {
        Module module = new AbstractModule(){
            @Override
            public void configure(){
                Path mainResourceDirectory = Paths.get("src", "test", "resources");
                File hibernateConfigFile = new File(mainResourceDirectory.resolve("hibernate.inmemory.cfg.xml").toAbsolutePath().toString());

                bind(Path.class).annotatedWith(Names.named("ResourcePath")).toInstance(mainResourceDirectory);
                bind(File.class).annotatedWith(Names.named("HibernateConfigFile")).toInstance(hibernateConfigFile);
                bind(String.class).annotatedWith(Names.named("URL")).toInstance("localhost");
                bind(Integer.class).annotatedWith(Names.named("port")).toInstance(5432);
                bind(String.class).annotatedWith(Names.named("DBName")).toInstance("test");
                bind(TransactionManager.class).to(HibernateTransactionManager.class);
            }

            @Provides
            public SessionFactory sessionFactoryProvider(@Named("HibernateConfigFile") File configFile){
                Configuration cfg = new Configuration();
                cfg = cfg.configure(configFile);
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
    }
}
