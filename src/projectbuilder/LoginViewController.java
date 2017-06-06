package projectbuilder;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

	/**
	kontroler widoku LoginView
	**/
public class LoginViewController {
	
	@FXML	private PasswordField password_text;
	@FXML	private TextField username_text;
	@FXML	private Button login_button;
	@FXML	private Button register;
	private DataBase db = MainClass.get_database();
	
	@FXML
	private void login() throws SQLException{
		if(password_text.getText().length()>0 && username_text.getText().length()>0){
			boolean logged = false;
			//sprawdza poprawnosci loginu i hasla
			try {
				logged = db.login(username_text.getText(), (password_text.getText()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(logged == true){
				//ustawia nazwe uzytkownika w glownej klasie i dodaje do historii logowania
				MainClass.set_username(username_text.getText());
				db.addHistory("Uzytkownik " + username_text.getText() + " zalogował sie dnia "
				+ db.show_stricte("date", "login_history", "id", db.show_stricte("id", "user", "login", username_text.getText())));
				if(db.show_stricte("isadmin", "user", "login", username_text.getText()).equals(1+"")){
					show_MainView();
				}else{
					show_DefaultView();
				}
			}else{
				System.out.println("Zle dane logowania");
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Zle dane logowania");
				alert.setHeaderText(null);
				alert.setContentText("Zostaly podane zle dane logowania");
				alert.showAndWait();
			}
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blad");
			alert.setHeaderText(null);
			alert.setContentText("Prosze podac wszystkie dane");

			alert.showAndWait();
		}
	}
	
	@FXML private void showUserDataEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("UserEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj uzytkownika");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UserEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setData(db);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	//Uruchamia poszczegolne widoki
	private void show_DefaultView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("defaultView/DefaultView.fxml"));
            AnchorPane taskView = (AnchorPane) loader.load();
          
            //AnchorPane taskView = (AnchorPane) loader.load(getClass().getResource("defaultView/DefaultView.fxml"));
            
            Scene scene = new Scene(taskView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void show_MainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("MainView.fxml"));
            AnchorPane projectView = (AnchorPane) loader.load();

            Scene scene = new Scene(projectView, 600, 400);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
