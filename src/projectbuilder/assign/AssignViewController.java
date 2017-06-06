package projectbuilder.assign;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import projectbuilder.DataBase;
import projectbuilder.MainClass;

public class AssignViewController {

    private DataBase db = MainClass.get_database();
    private boolean edit = false;

    @FXML
    private ComboBox<String> id_user = new ComboBox<String>();
    @FXML
    private ComboBox<String> id_task = new ComboBox<String>();
    @FXML
    private ComboBox<String> id_role = new ComboBox<String>();
    @FXML
    private DatePicker start_date;
    @FXML
    private DatePicker end_date;

    private ObservableList<AssignData> listAssignData = FXCollections.observableArrayList();
    @FXML
    private TableView<AssignData> table = new TableView<AssignData>(listAssignData);
    @FXML
    private TableColumn<AssignData, String> one;
    @FXML
    private TableColumn<AssignData, String> two;
    @FXML
    private TableColumn<AssignData, String> three;
    @FXML
    private TableColumn<AssignData, String> four;
    @FXML
    private TableColumn<AssignData, String> five;

    public static void main(String[] args) {

    }

    @FXML
    private void initialize() throws SQLException {
        id_role.getItems().addAll("Admin", "Manager", "Kierownik", "Pracownik");
        for (int i = 0; i <= db.last_id("user"); i++) {
            String temp = db.show_stricte("login", "user", "id", i + "");
            if (temp == null) {
            } else {
                System.out.println(temp);
                id_user.getItems().add(temp);
            }
        }

        for (int i = 0; i <= db.last_id("task"); i++) {
            String temp = db.show_stricte("name", "task", "id", i + "");
            if (temp == null) {
            } else {
                System.out.println(temp);
                id_task.getItems().add(temp);
            }
        }
        table.getItems().removeAll(listAssignData);
        one.setCellValueFactory(new PropertyValueFactory<AssignData, String>("id_user"));
        two.setCellValueFactory(new PropertyValueFactory<AssignData, String>("id_task"));
        three.setCellValueFactory(new PropertyValueFactory<AssignData, String>("id_role"));
        four.setCellValueFactory(new PropertyValueFactory<AssignData, String>("start_date"));
        five.setCellValueFactory(new PropertyValueFactory<AssignData, String>("end_date"));
        //Tworzy menu kontekstowe w tabeli
        table.setRowFactory(new Callback<TableView<AssignData>, TableRow<AssignData>>() {
            @Override
            public TableRow<AssignData> call(TableView<AssignData> tableView) {
                final TableRow<AssignData> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();

                final MenuItem removeMenuItem = new MenuItem("Usun wszystkie przydzialy uzytkownika");
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        AssignData data = table.getSelectionModel().getSelectedItem();
                        try {
                            db.delete_row("assign", "id_user", db.show_stricte("id", "user", "login", data.get_id_user()));
                            table.getItems().removeAll(listAssignData);
                            start();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                final MenuItem editMenuItem = new MenuItem("Edytuj");
                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        AssignData selectedAssignData = table.getSelectionModel().getSelectedItem();
                        edit = true;
                        showAssignDataEditDialog(selectedAssignData, edit);
                    }
                });

                contextMenu.getItems().add(removeMenuItem);
                contextMenu.getItems().add(editMenuItem);

                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                return row;
            }
        });
        table.setItems(listAssignData);

        start();
    }
    //Inicjuje dane do tabeli z bazy danych

    @FXML
    private void start() throws SQLException {
        table.getItems().removeAll(listAssignData);
        for (int i = 0; i <= db.last_id("assign"); i++) {
            String[] toTable = db.show("assign", "id", i + "");
            if (toTable.length > 0) {
                if (toTable[2] != null || toTable[1] != null && toTable.length > 0) {
                    listAssignData.add(new AssignData(db.show_stricte("login", "user", "id", toTable[1]),
                            db.show_stricte("name", "task", "id", toTable[2]),
                            db.show_stricte("name", "role", "id", toTable[3]),
                            toTable[4], toTable[5]));
                }
            }
        }
    }

    @FXML
    private void addAssign() throws SQLException {
        if (id_user.getValue() != null && id_task.getValue() != null && id_role.getValue() != null && end_date.getValue() != null && start_date.getValue() != null) {
            LocalDate lDate = start_date.getValue();
            LocalDate lDate2 = end_date.getValue();

            String[] values_new = {db.show_stricte("id", "user", "login", id_user.getValue()),
                db.show_stricte("id", "task", "name", id_task.getValue()),
                db.show_stricte("id", "role", "name", id_role.getValue()),
                lDate2 + " 00:00:00.0", lDate + " 00:00:00.0"};
            listAssignData.add(new AssignData(id_user.getValue(), db.show_stricte("id", "task", "name", id_task.getValue()),
                    id_role.getValue(), lDate + " 00:00:00.0", lDate2 + " 00:00:00.0"));
            db.add_row("assign", 6, values_new);
            start();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Dodano przydzial");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Blad");
            alert.setHeaderText(null);
            alert.setContentText("Prosze podac wszystkie dane");

            alert.showAndWait();
        }
    }

    public void showAssignDataEditDialog(AssignData AssignData, boolean bool) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("assign/AssignEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit AssignData");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainClass.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AssignEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setData(AssignData, db);

            dialogStage.showAndWait();
            table.getItems().removeAll(listAssignData);
            start();
            bool = false;

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void show_MainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainClass.class.getResource("MainView.fxml"));
            AnchorPane loginView = (AnchorPane) loader.load();

            Scene scene = new Scene(loginView);
            Stage primaryStage = MainClass.getPrimaryStage();
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void show_ProjectView() {
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

    @FXML
    private void show_TaskView() {
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

    @FXML
    private void show_UserView() {
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

    @FXML
    private void generateReport() throws SQLException {
        db.generate();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText("Wygenerowano raport");

        alert.showAndWait();
    }
}
