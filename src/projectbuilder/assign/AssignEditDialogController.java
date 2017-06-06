package projectbuilder.assign;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import projectbuilder.DataBase;

public class AssignEditDialogController {

	@FXML private ComboBox<String> id_user = new ComboBox<String>();
    @FXML private ComboBox<String> id_task = new ComboBox<String>();
    @FXML private ComboBox<String> id_role = new ComboBox<String>();
    @FXML private DatePicker start_date;
    @FXML private DatePicker end_date;

    private Stage dialogStage;
    private AssignData data;
    DataBase db;

    @FXML
    private void initialize() {
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(AssignData data, DataBase db) {
        this.data = data;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
        try {
        	for(int i = 0; i <= db.last_id("user"); i++){
				String temp = db.show_stricte("login", "user", "id", i+"");
				if(temp==null){
				}else{
					System.out.println(temp);
					id_user.getItems().add(temp);
				}
			}
			for(int i = 0; i <= db.last_id("task"); i++){
				String temp = db.show_stricte("name", "task", "id", i+"");
				if(temp==null){
				}else{
					System.out.println(temp);
					id_task.getItems().add(temp);
				}
			}
			id_user.setValue(data.get_id_user());
			id_task.setValue(data.get_id_task());
        	id_role.getItems().addAll("Admin","Manager","Kierownik","Pracownik");
        	id_role.setValue(data.get_id_role());
        	LocalDate lDate = LocalDate.parse(data.get_start_date(), formatter);
        	start_date.setValue(lDate);
        	LocalDate lDate2 = LocalDate.parse(data.get_end_date(), formatter);
        	end_date.setValue(lDate2);
        	this.db = db;
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void handleOk() throws SQLException {
    	if(id_user.getValue()!=null&&id_task.getValue()!=null&&id_role.getValue()!=null&&end_date.getValue()!=null&&start_date.getValue()!=null){
    		LocalDate lDate = start_date.getValue();
    		data.set_start_date(lDate+" 00:00:00.0");
    		LocalDate lDate2 = end_date.getValue();
    		data.set_end_date(lDate2+" 00:00:00.0");    
    		String[] values_new = {null, db.show_stricte("id", "user", "login", id_user.getValue()),
    			db.show_stricte("id", "task", "name", id_task.getValue()),
    			db.show_stricte("id", "role", "name", id_role.getValue()),
    			lDate2+" 00:00:00.0", lDate+" 00:00:00.0"};
    		String id_assign = null;
    		for(int i = 0; i <= db.last_id("assign"); i++){
    			if((db.show_stricte("id_user", "assign", "id", i+"")+db.show_stricte("id_task", "assign", "id", i+"")
    				+db.show_stricte("id_role", "assign", "id", i+"")).equals(db.show_stricte("id", "user", "login",
    						data.get_id_user())+db.show_stricte("id", "task", "name", data.get_id_task())
    						+db.show_stricte("id", "role", "name", data.get_id_role()))){
    			id_assign = i+"";
    			}
    		}
    		data.set_id_user(id_user.getValue());
    		data.set_id_task(id_task.getValue());
    		data.set_id_role(id_role.getValue());
    		db.edit_row("assign", values_new, "id", id_assign); 
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