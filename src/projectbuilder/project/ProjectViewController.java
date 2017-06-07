package projectbuilder.project;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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

public class ProjectViewController {
	
	private DataBase db = MainClass.get_database();
	
	@FXML	private TextField name;
    @FXML   private TextField description;
    @FXML   private ComboBox<String> state = new ComboBox<String>();
    @FXML   private DatePicker date;
	
	private ObservableList<ProjectData> listProjectData = FXCollections.observableArrayList();
	@FXML private TableView<ProjectData> table = new TableView<ProjectData>(listProjectData);
    @FXML private TableColumn<ProjectData, String> one;
    @FXML private TableColumn<ProjectData, String> two;
    @FXML private TableColumn<ProjectData, String> three;
    @FXML private TableColumn<ProjectData, String> four;
	
	public static void main(String[] args) {
		
    }
	@FXML
	private void initialize() throws SQLException {
		state.getItems().addAll("Zakonczone","W toku","Zatwierdzone","Oczekujace","Wstrzymane");
		one.setCellValueFactory(new PropertyValueFactory<ProjectData,String>("name"));
		two.setCellValueFactory(new PropertyValueFactory<ProjectData,String>("description"));
		three.setCellValueFactory(new PropertyValueFactory<ProjectData,String>("state"));
		four.setCellValueFactory(new PropertyValueFactory<ProjectData,String>("date"));
		//Tworzy menu kontekstowe w tabeli
		table.setRowFactory(new Callback<TableView<ProjectData>, TableRow<ProjectData>>() {  
            @Override  
            public TableRow<ProjectData> call(TableView<ProjectData> tableView) {  
                final TableRow<ProjectData> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                final MenuItem removeMenuItem = new MenuItem("Usun");  
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        ProjectData dane = table.getSelectionModel().getSelectedItem();
                        System.out.println(dane.get_name());
                        try {
                        	String id_project = db.show_stricte("id", "project", "name", dane.get_name());
                        	db.delete_row("assign", "id_task", db.show_stricte("id", "task", "id_project", id_project));
                        	db.delete_row("task_state", "id_task", db.show_stricte("id", "task", "id_project", id_project));
                        	db.delete_row("task", "id_project", id_project);
                        	db.delete_row("project_state", "id_project", id_project);
                        	db.delete_row("project", "id", id_project);
                        	table.getItems().removeAll(listProjectData);
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
						ProjectData selectedProjectData = table.getSelectionModel().getSelectedItem();
						showProjectDataEditDialog(selectedProjectData);
					}
                });
                
                contextMenu.getItems().add(removeMenuItem);  
                contextMenu.getItems().add(editMenuItem);
                
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu));  
                return row ;  
            }  
        });
		table.setItems(listProjectData);
		
		start();
    }
	//Inicjuje dane do tabeli z bazy danych
	@FXML
	private void start() throws SQLException{
		table.getItems().removeAll(listProjectData);
		for(int i = 0; i <= db.last_id("project"); i++){
			String[] toTable = db.show("project", "id", i+"");
			String[] toTable2 = db.show("project_state", "id_project", i+"");
			if(toTable[1] != null || toTable2[1] != null && toTable.length>0 && toTable2.length>0){
				listProjectData.add(new ProjectData(toTable[1], toTable[2], toTable2[2], toTable2[3]));
			}
		}
		table.setItems(listProjectData);
	}
	
	@FXML
	private void addProject() throws SQLException{
		if(name.getText().length()>0&&description.getText().length()>0&&state.getValue()!=null&&date.getValue()!=null){
		LocalDate lDate = date.getValue();
		String[] values_project = {name.getText(), description.getText()};
		db.add_row("project", 3, values_project);
        String[] values_project_state = {db.last_id("project")+"", state.getValue(), lDate+" 00:00:00.0"};
        db.add_row("project_state", 4, values_project_state);
        listProjectData.add(new ProjectData(name.getText(), description.getText(), state.getValue(), lDate+" 00:00:00.0"));
        start();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText("Dodano projekt");

        alert.showAndWait();
		}else{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blad");
			alert.setHeaderText(null);
			alert.setContentText("Prosze podac wszystkie dane");

			alert.showAndWait();
    	}
	}
	
	public void showProjectDataEditDialog(ProjectData projectData) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("project/ProjectEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit ProjectData");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ProjectEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setData(projectData, db);

            dialogStage.showAndWait();
            table.getItems().removeAll(listProjectData);
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