package projectbuilder.project;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import projectbuilder.DataBase;

public class ProjectEditDialogController {

    @FXML	private TextField name;
    @FXML   private TextField description;
    @FXML 	private ComboBox<String> status = new ComboBox<String>();
    @FXML   private DatePicker date;
    private Stage dialogStage;
    private ProjectData data;
    private DataBase db;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(ProjectData data, DataBase dbase) {
    	status.getItems().addAll("Zakonczone","W toku","Zatwierdzone","Oczekujace","Wstrzymane");
        this.data = data;
        name.setText(data.get_name());
        description.setText(data.get_description());
        status.setValue(data.get_state());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
        LocalDate lDate = LocalDate.parse(data.get_date(), formatter);
        date.setValue(lDate);
        db = dbase;
    }

    @FXML
    private void handleOk() throws SQLException {
    	if(name.getText().length()>0&&description.getText().length()>0&&status.getValue()!=null&&date.getValue()!=null){
    		String id_project = db.show_stricte("id", "project", "name", data.get_name());
    		data.set_name(name.getText());
    		data.set_description(description.getText());
    		data.set_state(status.getValue());
    		LocalDate lDate = date.getValue();
    		data.set_date(lDate+" 00:00:00.0");
    		String[] values_project = {null,name.getText(), description.getText()};
    		String[] values_project_state = {null,id_project, status.getValue(), lDate+" 00:00:00.0"};
    		db.edit_row("project_state", values_project_state, "id_project", id_project);
    		db.edit_row("project", values_project, "id", id_project);
    		dialogStage.close();
    	}else{
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Blad");
			alert.setHeaderText(null);
			alert.setContentText("Prosze podac wszystkie dane");

			alert.showAndWait();
    	}
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}