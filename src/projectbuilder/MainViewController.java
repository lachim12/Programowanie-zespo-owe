package projectbuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainViewController {
	
	private static DataBase db = MainClass.get_database();
	
	private static ObservableList<Data> listData = FXCollections.observableArrayList();
	@FXML private TableView<Data> table = new TableView<Data>(listData);
    @FXML private TableColumn<Data, String> one;
    @FXML private TableColumn<Data, String> two;
    @FXML private TableColumn<Data, String> three;
    @FXML private TableColumn<Data, String> four;
    @FXML private TableColumn<Data, String> five;
    @FXML private TableColumn<Data, String> six;
    @FXML private TableColumn<Data, String> seven;
    
    private static String search = null;
	
	public static void main(String[] args) {
		
    }
	//Ustawia szukana fraze z dialogu
	public static void set_search(String searching){
		try {
			search = db.show_stricte("id", "user", "name", searching+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() throws SQLException {
		//inicjalizacja kolumn
		one.setCellValueFactory(new PropertyValueFactory<Data,String>("nazwa"));
		two.setCellValueFactory(new PropertyValueFactory<Data,String>("projekt"));
		three.setCellValueFactory(new PropertyValueFactory<Data,String>("opis"));
		four.setCellValueFactory(new PropertyValueFactory<Data,String>("data_rozpoczecia"));
		five.setCellValueFactory(new PropertyValueFactory<Data,String>("deadline"));
		six.setCellValueFactory(new PropertyValueFactory<Data,String>("status"));
		seven.setCellValueFactory(new PropertyValueFactory<Data,String>("id"));
		//Tworzy menu kontekstowe w tabeli
		table.setRowFactory(new Callback<TableView<Data>, TableRow<Data>>() {  
            @Override  
            public TableRow<Data> call(TableView<Data> tableView) {  
                final TableRow<Data> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                
                final MenuItem wstrzymaneMenuItem = new MenuItem("Oznacz jako Wstrzymane");  
                wstrzymaneMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        Data dane = table.getSelectionModel().getSelectedItem();
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_nazwa());
							String[] output = db.show("task_state", "id_task", id_task);
							output[2] = "Wstrzymane";
							db.edit_row("task_state", output, "id_task", id_task);
							table.getItems().removeAll(listData);
                        	start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });
                
                final MenuItem oczekujaceMenuItem = new MenuItem("Oznacz jako Oczekujace");  
                oczekujaceMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        Data dane = table.getSelectionModel().getSelectedItem();
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_nazwa());
							String[] output = db.show("task_state", "id_task", id_task);
							output[2] = "Oczekujace";
							db.edit_row("task_state", output, "id_task", id_task);
							table.getItems().removeAll(listData);
                        	start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });
                
                final MenuItem trwajaceMenuItem = new MenuItem("Oznacz jako Trwajace");  
                trwajaceMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        Data dane = table.getSelectionModel().getSelectedItem();
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_nazwa());
							String[] output = db.show("task_state", "id_task", id_task);
							output[2] = "Trwajace";
							db.edit_row("task_state", output, "id_task", id_task);
							table.getItems().removeAll(listData);
                        	start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });
                
                final MenuItem zakonczoneMenuItem = new MenuItem("Oznacz jako Zakonczone");  
                zakonczoneMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        Data dane = table.getSelectionModel().getSelectedItem();
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_nazwa());
							String[] output = db.show("task_state", "id_task", id_task);
							output[2] = "Zakonczone";
							db.edit_row("task_state", output, "id_task", id_task);
							table.getItems().removeAll(listData);
                        	start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });
                
                final MenuItem zatwierdzoneMenuItem = new MenuItem("Oznacz jako Zatwierdzone");  
                zatwierdzoneMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        Data dane = table.getSelectionModel().getSelectedItem();
                        try {
							String id_task = db.show_stricte("id", "task", "name", dane.get_nazwa());
							String[] output = db.show("task_state", "id_task", id_task);
							output[2] = "Zatwierdzone";
							db.edit_row("task_state", output, "id_task", id_task);
							table.getItems().removeAll(listData);
                        	start();
						} catch (SQLException e) {
							System.out.println(e);
						}
                    }  
                });
                
                contextMenu.getItems().add(wstrzymaneMenuItem);
                contextMenu.getItems().add(oczekujaceMenuItem);
                contextMenu.getItems().add(trwajaceMenuItem);
                contextMenu.getItems().add(zakonczoneMenuItem);
                contextMenu.getItems().add(zatwierdzoneMenuItem);
                
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu)null).otherwise(contextMenu));  
                return row ;  
            }  
        });
		table.setItems(listData);
		
		start();
    }
	//Inicjuje dane do tabeli z bazy danych
	@FXML
	private void start() throws SQLException{
		table.getItems().removeAll(listData);
		for(int x = 0; x <= db.last_id("user"); x++){
			if(db.show_stricte("login", "user", "id", x+"") != null){
				String id_user = x+"";
				String[] id_tasks = db.show_stricte_all("id_task", "assign", "id_user", id_user);
			
				int size = id_tasks.length;
				String[] id_project = new String[size];
				String[] tasks_names = new String[size];
				String[] tasks_dates = new String[size];
				String[] tasks_deadlines = new String[size];
				String[] tasks_state = new String[size];
				if(id_tasks.length>0){
					System.out.println(id_tasks[0] + " ss " + db.show_stricte("name", "task", "id", id_tasks[0]));//+ id_tasks[1]);
					int a = 0; System.out.println(a+" :to a1"); while(a < size && id_tasks[a] != null)
					{tasks_names[a] = db.show_stricte("name", "task", "id", id_tasks[a]);	a++;}
					int b = 0; while(b < size && id_tasks[b] != null)
					{id_project[b] = db.show_stricte("id_project", "task", "id", id_tasks[b]); b++;}
					int c = 0; while(c < size && id_tasks[c] != null)
					{Timestamp date = db.show_date("start_date", "task", "id", id_tasks[c]); tasks_dates[c] = date.toString(); c++;}
					int d = 0; while(d < size && id_tasks[d] != null)
					{Timestamp date = db.show_date("deadline", "task", "id", id_tasks[d]); tasks_deadlines[d] = date.toString(); d++;}
					int e = 0; while(e < size && id_tasks[e] != null)
					{tasks_state[e] = db.show_stricte("state", "task_state", "id_task", id_tasks[e]); e++; }
				}
				int size2 = id_project.length;
				String[] project_names = new String[size2];
				String[] project_desc = new String[size2];
				for(int i = 0; i < size2; i++){project_names[i] = db.show_stricte("name", "project", "id", id_project[i]);}
				for(int i = 0; i < size2; i++){project_desc[i] = db.show_stricte("description", "project", "id", id_project[i]);}
			
				for(int i = 0; i < size2; i++){
					if(project_names[i] != null){
						if(!(search != null)){
							listData.add(new Data(db.show_stricte("login", "user", "id", id_user), tasks_names[i], project_names[i], 
									project_desc[i], tasks_dates[i], tasks_deadlines[i], tasks_state[i]));	
						}else if(search.equals(id_user)){
							listData.add(new Data(db.show_stricte("login", "user", "id", id_user), tasks_names[i], project_names[i],
									project_desc[i], tasks_dates[i], tasks_deadlines[i], tasks_state[i]));
						}
					}
				}
			}
		}
	}
	//POkazuje dialog
	public void showEditDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("DataEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Szukaj");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DataEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            table.getItems().removeAll(listData);
            start();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
	
	public void showSearchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("SearchDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Szukaj");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SearchDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            table.getItems().removeAll(listData);
            start();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
	//Odnosniki do poszczegolnych okien programu
	@FXML private void show_DefaultView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("defaultView/DefaultView.fxml"));
            AnchorPane taskView = (AnchorPane) loader.load();

            Scene scene = new Scene(taskView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
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
            primaryStage.setFullScreen(true);
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
            primaryStage.setFullScreen(true);
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
            primaryStage.setFullScreen(true);
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
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //generuje raport
    @FXML private void generateReport() throws SQLException{
    	db.generate();
    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText("Wygenerowano raport");

        alert.showAndWait();
    }
}