package projectbuilder.user;

import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import projectbuilder.DataBase;
import projectbuilder.MainClass;
import projectbuilder.task.TaskEditDialogController;

public class UserViewController {

    private DataBase db = MainClass.get_database();
    
    private ObservableList<UserData> listUserData = FXCollections.observableArrayList();
    @FXML private TableView<UserData> table = new TableView<UserData>(listUserData);
    @FXML private TableColumn<UserData, String> one;
    @FXML private TableColumn<UserData, String> two;
    @FXML private TableColumn<UserData, String> three;
    @FXML private TableColumn<UserData, String> four;
    @FXML private TableColumn<UserData, String> five;
    @FXML private TableColumn<UserData, String> six;
    
    @FXML	private TextField name;
    @FXML   private TextField surname;
    @FXML   private TextField login;
    @FXML   private TextField email;
    @FXML	private TextField password;
    @FXML   private CheckBox isadmin;
    
public static void main(String[] args) {
		
    }
	@FXML
	private void initialize() throws SQLException {
		
		one.setCellValueFactory(new PropertyValueFactory<UserData,String>("name"));
		two.setCellValueFactory(new PropertyValueFactory<UserData,String>("surname"));
		three.setCellValueFactory(new PropertyValueFactory<UserData,String>("login"));
		four.setCellValueFactory(new PropertyValueFactory<UserData,String>("email"));
		five.setCellValueFactory(new PropertyValueFactory<UserData,String>("password"));
		six.setCellValueFactory(new PropertyValueFactory<UserData,String>("isadmin"));
		//Tworzy menu kontekstowe w tabeli
		table.setRowFactory(new Callback<TableView<UserData>, TableRow<UserData>>() {  
            @Override  
            public TableRow<UserData> call(TableView<UserData> tableView) {  
                final TableRow<UserData> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                
                final MenuItem removeMenuItem = new MenuItem("Usuń");  
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
						try {
							UserData dane = table.getSelectionModel().getSelectedItem();
							String id_user = db.show_stricte("id", "user", "login", dane.get_login());
							db.delete_row("assign", "id_user", id_user);
	                    	db.delete_row("login_history", "id_user", id_user);
	                        db.delete_row("user", "id", id_user);
	                        table.getItems().removeAll(listUserData);
                        	start();
						} catch (SQLException e) {
							e.printStackTrace();
						}
                    }  
                });  
                final MenuItem editMenuItem = new MenuItem("Edytuj");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						UserData selectedUserData = table.getSelectionModel().getSelectedItem();
						showUserDataEditDialog(selectedUserData);
					}
                });
                
                contextMenu.getItems().add(removeMenuItem);  
                contextMenu.getItems().add(editMenuItem);
                
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu));  
                return row ;  
            }  
        });
		table.setItems(listUserData);
		
		start();
    }
	//Inicjuje dane do tabeli z bazy danych
	@FXML
	private void start() throws SQLException{
		table.getItems().removeAll(listUserData);
		for(int i = 0; i <= db.last_id("user"); i++){
			String[] toTable = db.show("user", "id", i+"");
			if(toTable[2] != null || toTable[1] != null && toTable.length>0){
				listUserData.add(new UserData(toTable[1], toTable[2], toTable[3], toTable[4], db.decrypt(toTable[5]), toTable[6]));
			}
		}
	}
	
	@FXML
	private void addUser() throws SQLException{
		if(name.getText().length()>0 && surname.getText().length()>0 && login.getText().length()>0 && email.getText().length()>0 
    			&& password.getText().length()>0){
			String admin;
			if(isadmin.isSelected()){
				admin = "1";
			}else{
				admin = "0";
			}
			String[] values_user = {name.getText(), surname.getText(), login.getText(), email.getText(), db.encrypt(password.getText()), admin};
			db.add_row("user", 7, values_user);
			table.getItems().removeAll(listUserData);
			start();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Informacja");
			alert.setHeaderText(null);
			alert.setContentText("Dodano użytkownika");

			alert.showAndWait();
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blad");
			alert.setHeaderText(null);
			alert.setContentText("Prosze podac wszystkie dane");

			alert.showAndWait();
		}
	}
        
	public void showUserDataEditDialog(UserData UserData) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("user/UserEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit UserData");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            UserData userData=table.getSelectionModel().getSelectedItem();
            UserEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setData(userData, db);

            dialogStage.show();
       
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@FXML private void show_MainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("MainView.fxml"));
            AnchorPane loginView = (AnchorPane) loader.load();

            Scene scene = new Scene(loginView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
           
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void show_AssignView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("assign/AssignView.fxml"));
            AnchorPane mainView = (AnchorPane) loader.load();

            Scene scene = new Scene(mainView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
     
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void show_ProjectView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("project/ProjectView.fxml"));
            AnchorPane projectView = (AnchorPane) loader.load();

            Scene scene = new Scene(projectView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
          
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	@FXML private void show_TaskView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("task/TaskView.fxml"));
            AnchorPane taskView = (AnchorPane) loader.load();
            Stage primaryStage = MainClass.getPrimaryStage();
            Scene scene = new Scene(taskView);
            primaryStage.setScene(scene);  
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    @FXML private void generateReport(){
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText("Wygenerowano raport");

        alert.showAndWait();
    }
}