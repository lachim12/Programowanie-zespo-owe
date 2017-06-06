package projectbuilder.task;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import projectbuilder.DataBase;

public class TaskEditDialogController {

	@FXML private ComboBox<String> id_project = new ComboBox<String>();
    @FXML   private TextField name;
    @FXML   private TextField description;
    @FXML   private DatePicker start_date;
    @FXML	private DatePicker deadline;
    @FXML	private Slider progress;
    @FXML 	private ComboBox<String> state = new ComboBox<String>();
    @FXML   private DatePicker date;
    private Stage dialogStage;
    private TaskData data;
    private DataBase db;

    @FXML
    private void initialize() {
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(TaskData data, DataBase dbase) {
    	db = dbase;
		state.getItems().addAll("Zakonczone","W toku","Zatwierdzone","Oczekujace","Wstrzymane");
		try {
			System.out.println("last id in project: " + db.last_id("project"));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			for(int i = 0; i <= db.last_id("project"); i++){
				String temp = db.show_stricte("name", "project", "id", i+"");
				if(temp==null){
				}else{
					System.out.println(temp);
					id_project.getItems().add(temp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.data = data;
		id_project.setValue(data.get_id_project());
		name.setText(data.get_name());
		description.setText(data.get_description());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
		LocalDate lDate = LocalDate.parse(data.get_start_date(), formatter);
		start_date.setValue(lDate);
		LocalDate lDate2 = LocalDate.parse(data.get_deadline(), formatter);
		deadline.setValue(lDate2);
		DecimalFormat df = new DecimalFormat("###.##");
		try {
			progress.setValue(df.parse(data.get_progress()).doubleValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		state.setValue(data.get_state());
		LocalDate lDate3 = LocalDate.parse(data.get_date(), formatter);
		date.setValue(lDate3);
    	
    }

    @FXML
    private void handleOk() throws SQLException {
    	if(id_project.getValue()!=null&&name.getText().length()>0&&description.getText().length()>0&&start_date.getValue()!=null
    			&&deadline.getValue()!=null&&state.getValue()!=null&&date.getValue()!=null){
    	String id_task = db.show_stricte("id", "task", "name", data.get_name());
    	data.set_id_project(id_project.getValue());
    	data.set_name(name.getText());
    	data.set_description(description.getText());
    	LocalDate lDate = start_date.getValue();
    	data.set_start_date(lDate+" 00:00:00.0");
    	LocalDate lDate2 = deadline.getValue();
    	data.set_deadline(lDate2+" 00:00:00.0");
    	data.set_progress(String.format(Locale.US, "%.2f", (progress.getValue())));
    	data.set_state(state.getValue());
    	LocalDate lDate3 = date.getValue();
    	data.set_date(lDate3+" 00:00:00.0");
    	String[] values_task = {null,id_project.getValue(), name.getText(), description.getText(), lDate+" 00:00:00.0", lDate2+" 00:00:00.0",
    			String.format(Locale.US, "%.2f", (progress.getValue()))};
    	System.out.println(state.getValue());
    	String[] values_task_state = {null,id_task, state.getValue(), lDate3+" 00:00:00.0"};
    	db.edit_row("task_state", values_task_state, "id_task", id_task);
    	db.edit_row("task", values_task, "id", id_task);
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