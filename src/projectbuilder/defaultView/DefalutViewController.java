package projectbuilder.defaultView;

import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import projectbuilder.DataBase;
import projectbuilder.MainClass;

public class DefalutViewController {
	
	private DataBase db = MainClass.get_database();
	
	private ObservableList<DefaultData> listData = FXCollections.observableArrayList();
	@FXML private TableView<DefaultData> table = new TableView<DefaultData>(listData);
	private String id_user;
    @FXML private TableColumn<DefaultData, String> one;
    @FXML private TableColumn<DefaultData, String> two;
    @FXML private TableColumn<DefaultData, String> three;
    @FXML private TableColumn<DefaultData, String> four;
    @FXML private TableColumn<DefaultData, String> five;
    @FXML private TableColumn<DefaultData, String> six;
	
	public static void main(String[] args) {
		
    }
	@FXML
	private void initialize() throws SQLException {
		one.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("nazwa"));
		two.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("projekt"));
		three.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("opis"));
		four.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("data_rozpoczecia"));
		five.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("deadline"));
		six.setCellValueFactory(new PropertyValueFactory<DefaultData,String>("status"));
		//Tworzy menu kontekstowe w tabeli
		table.setRowFactory(new Callback<TableView<DefaultData>, TableRow<DefaultData>>() {  
            @Override  
            public TableRow<DefaultData> call(TableView<DefaultData> tableView) {  
                final TableRow<DefaultData> row = new TableRow<>();  
                final ContextMenu contextMenu = new ContextMenu();  
                
                final MenuItem wstrzymaneMenuItem = new MenuItem("Oznacz jako Wstrzymane");  
                wstrzymaneMenuItem.setOnAction(new EventHandler<ActionEvent>(){  
                    @Override  
                    public void handle(ActionEvent event) {  
                        DefaultData dane = table.getSelectionModel().getSelectedItem();
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
                        DefaultData dane = table.getSelectionModel().getSelectedItem();
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
                        DefaultData dane = table.getSelectionModel().getSelectedItem();
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
                        DefaultData dane = table.getSelectionModel().getSelectedItem();
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
                
                contextMenu.getItems().add(wstrzymaneMenuItem);
                contextMenu.getItems().add(oczekujaceMenuItem);
                contextMenu.getItems().add(trwajaceMenuItem);
                contextMenu.getItems().add(zakonczoneMenuItem);
                
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
		id_user = db.show_stricte("id", "user", "login", MainClass.get_username());
		String[] id_tasks = db.show_stricte_all("id_task", "assign", "id_user", id_user);
		
		int size = id_tasks.length;
		String[] id_project = new String[size];
		String[] tasks_names = new String[size];
		String[] tasks_dates = new String[size];
		String[] tasks_deadlines = new String[size];
		String[] tasks_state = new String[size];
                
                try{
                                    
                    int a = 0; while(id_tasks[a] != null && id_tasks.length>0)
                    {tasks_names[a] = db.show_stricte("name", "task", "id", id_tasks[a]);	a++;}
                    int b = 0; while(id_tasks[b] != null && id_tasks.length>0)
                    {id_project[b] = db.show_stricte("id_project", "task", "id", id_tasks[b]); b++;}
                    int c = 0; while(id_tasks[c] != null && id_tasks.length>0)
                    {Timestamp date = db.show_date("start_date", "task", "id", id_tasks[c]); tasks_dates[c] = date.toString(); c++;}
                    int d = 0; while(id_tasks[d] != null && id_tasks.length>0)
                    {Timestamp date = db.show_date("deadline", "task", "id", id_tasks[d]); tasks_deadlines[d] = date.toString(); d++;}
                    int e = 0; while(id_tasks[e] != null && id_tasks.length>0)
                    {tasks_state[e] = db.show_stricte("state", "task_state", "id_task", id_tasks[e]); e++; }

                    int size2 = id_project.length;
                    String[] project_names = new String[size2];
                    String[] project_desc = new String[size2];
                    for(int i = 0; i < size2; i++){project_names[i] = db.show_stricte("name", "project", "id", id_project[i]);}
                    for(int i = 0; i < size2; i++){project_desc[i] = db.show_stricte("description", "project", "id", id_project[i]);}

                    for(int i = 0; i < size2; i++){
                            if(project_names[i] != null){
                                    listData.add(new DefaultData(tasks_names[i], project_names[i], project_desc[i], tasks_dates[i], tasks_deadlines[i], tasks_state[i]));	
                            }
                    }
                
                }catch(ArrayIndexOutOfBoundsException ex)
                {
                    
                }
	}
    @FXML private void generateReport() throws SQLException{
    	db.generate();
    }
}