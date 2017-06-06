package projectbuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Glowna klasa odpowiadajaca za inicjacje danych
  *
 */
public class MainClass extends Application {

    private static MainClass MainClass;
    private static Stage primaryStage;
    private static String username;
    private static DataBase db = new DataBase();
    private String db_adress;

    public static void set_username(String user) {
        username = user;
    }

    public static String get_username() {
        return username;
    }

    public static DataBase get_database() {
        return db;
    }

    @SuppressWarnings("static-access")
    @Override
    public void start(Stage primaryStage) throws SQLException {
        try {
            String path = MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String path2 = "/C:/Users/Daniel/Documents/NetBeansProjects/ProjectBuilder/src/projectbuilder/";                         
            db_adress = "jdbc:mysql://localhost:3306/projektbaza?" + "user=root&password=";
            db.connection(db_adress);
            db.generate();
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
        MainClass.primaryStage = primaryStage;
        MainClass.primaryStage.setTitle("Project Builder");
        MainClass.primaryStage.getIcons().add(new Image(MainClass.class.getResource("project-builder2.png").toString()));
        //Wywolywane gdy program jest zamykany
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                try {
                    db.end_connection();
                    Platform.exit();
                    System.exit(0);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        show_LoginView();
    }

    public void show_LoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("LoginView.fxml"));
            BorderPane loginView = (BorderPane) loader.load();

            Scene scene = new Scene(loginView);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
