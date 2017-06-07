package projectbuilder.task;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

import projectbuilder.DataBase;
import projectbuilder.MainClass;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TaskViewController {
	
	private DataBase db = MainClass.get_database();
	
	@FXML private ComboBox<String> id_project = new ComboBox<String>();
    @FXML   private TextField name;
    @FXML   private TextField description;
    @FXML   private DatePicker start_date;
    @FXML	private DatePicker deadline;
    @FXML	private Slider progress;
    @FXML 	private ComboBox<String> state = new ComboBox<String>();
    @FXML   private DatePicker date;
	
	private ObservableList<TaskData> listTaskData = FXCollections.observableArrayList();
	@FXML private TableView<TaskData> table = new TableView<TaskData>(listTaskData);
    @FXML private TableColumn<TaskData, String> one;
    @FXML private TableColumn<TaskData, String> two;
    @FXML private TableColumn<TaskData, String> three;
    @FXML private TableColumn<TaskData, String> four;
    @FXML private TableColumn<TaskData, String> five;
    @FXML private TableColumn<TaskData, String> six;
    @FXML private TableColumn<TaskData, String> seven;
    @FXML private TableColumn<TaskData, String> eight;
	
	public static void main(String[] args) {
		
    }
	@FXML
	private void initialize() throws SQLException {
		state.getItems().addAll("Zakonczone","W toku","Zatwierdzone","Oczekujace","Wstrzymane");
		for(int i = 0; i <= db.last_id("project"); i++){
			String temp = db.show_stricte("name", "project", "id", i+"");
			if(temp==null){
			}else{
				System.out.println(temp);
				id_project.getItems().add(temp);
			}
		}
		one.setCellValueFactory(new PropertyValueFactory<TaskData,String>("id_project"));
		two.setCellValueFactory(new PropertyValueFactory<TaskData,String>("name"));
		three.setCellValueFactory(new PropertyValueFactory<TaskData,String>("description"));
		four.setCellValueFactory(new PropertyValueFactory<TaskData,String>("start_date"));
		five.setCellValueFactory(new PropertyValueFactory<TaskData,String>("deadline"));
		six.setCellValueFactory(new PropertyValueFactory<TaskData,String>("progress"));
		seven.setCellValueFactory(new PropertyValueFactory<TaskData,String>("state"));
		eight.setCellValueFactory(new PropertyValueFactory<TaskData,String>("date"));
		//Tworzy menu kontekstowe w tabeli
		table.setRowFactory(new Callback<TableView<TaskData>, TableRow<TaskData>>() {  
            @Override  
            public TableRow<TaskData> call(TableView<TaskData> tableView) {  
                final TableRow<TaskData> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                
                final MenuItem removeMenuItem = new MenuItem("Usun");  
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        TaskData dane = table.getSelectionModel().getSelectedItem();
                        System.out.println(dane.get_name());
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_name());
							db.delete_row("assign", "id_task", id_task);
							db.delete_row("task_state", "id_task", id_task);
							db.delete_row("task", "id", id_task);
							table.getItems().removeAll(listTaskData);
							start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });  
                final MenuItem editMenuItem = new MenuItem("Edytuj");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						TaskData selectedTaskData = table.getSelectionModel().getSelectedItem();
						showTaskDataEditDialog(selectedTaskData);
					}
                });
                
                contextMenu.getItems().add(removeMenuItem);  
                contextMenu.getItems().add(editMenuItem);
                
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu));  
                return row ;  
            }  
        });
		table.setItems(listTaskData);
		
		start();
    }
	//Inicjuje dane do tabeli z bazy danych
	@FXML
	private void start() throws SQLException{
		table.getItems().removeAll(listTaskData);
		for(int i = 0; i <= db.last_id("task"); i++){
			String[] toTable = db.show("task", "id", i+"");
			String[] toTable2 = db.show("task_state", "id", i+"");
			if(toTable[2] != null || toTable[1] != null && toTable.length>0){
				listTaskData.add(new TaskData(toTable[1], toTable[2], toTable[3], toTable[4], toTable[5], toTable[6], toTable2[2], toTable2[3]));
			}
		}
	}
	
	@FXML
	private void addTask() throws SQLException{
		if(id_project.getValue()!=null&&name.getText().length()>0&&description.getText().length()>0&&start_date.getValue()!=null
    			&&deadline.getValue()!=null&&state.getValue()!=null&&date.getValue()!=null){
			LocalDate lDate = start_date.getValue();
			LocalDate lDate2 = deadline.getValue();
			LocalDate lDate3 = date.getValue();
			String[] values_task = {db.show_stricte("id", "project", "name", id_project.getValue()), name.getText(), description.getText(),
				lDate+" 00:00:00.0", lDate2+" 00:00:00.0", String.format(Locale.US, "%.2f", (progress.getValue()))};
			db.add_row("task", 7, values_task);
       		String[] values_task_state = {db.last_id("task")+"", state.getValue(), lDate3+" 00:00:00.0"};
        	db.add_row("task_state", 4, values_task_state);
        	start();
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Informacja");
        	alert.setHeaderText(null);
        	alert.setContentText("Dodano zadanie");

       		alert.showAndWait();
		}else{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blad");
			alert.setHeaderText(null);
			alert.setContentText("Prosze podac wszystkie dane");

			alert.showAndWait();
    	}
	}
	
	public void showTaskDataEditDialog(TaskData TaskData) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("task/TaskEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edycja zadania");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TaskEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setData(TaskData, db);

            dialogStage.showAndWait();
            table.getItems().removeAll(listTaskData);
            start();
        } catch (IOException | SQLException e) {
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
    @FXML private void show_UserView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("user/UserView.fxml"));
            AnchorPane taskView = (AnchorPane) loader.load();

            Scene scene = new Scene(taskView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
         
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void generateReport() throws SQLException{
    	db.generate();
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText("Wygenerowano raport");

        alert.showAndWait();
    }
}