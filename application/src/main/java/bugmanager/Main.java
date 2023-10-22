package bugmanager;

import bugmanager.controllers.LoginController;
import bugmanager.persistence.BugRepository;
import bugmanager.persistence.UserRepository;
import bugmanager.service.SuperService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SuperService service=instantiateSuperService();
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        Parent root=loader.load();
        primaryStage.setScene(new Scene(root));


        LoginController ctrl = loader.getController();
        ctrl.setService(service);
        ctrl.setStage(primaryStage);
        ctrl.initializeController();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private static SessionFactory instantiateSessionFactory() {
        SessionFactory sessionFactory = null;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return sessionFactory;
    }

    private static SuperService instantiateSuperService() {
        SessionFactory sessionFactory = instantiateSessionFactory();
        BugRepository bugRepository = new BugRepository(sessionFactory);
        UserRepository userRepository = new UserRepository(sessionFactory);
        return new SuperService(bugRepository, userRepository);
    }
}