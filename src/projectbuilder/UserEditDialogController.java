package projectbuilder;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditDialogController {

	@FXML	private TextField name;
    @FXML   private TextField surname;
    @FXML   private TextField login;
    @FXML   private TextField email;
    @FXML	private TextField password;
    private Stage dialogStage;
    private boolean okClicked = false;
    private DataBase db;

    @FXML
    private void initialize() {
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDB(DataBase db){
    	this.db = db;
    }
    
    public void setData(DataBase dbase) {
        db = dbase;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() throws SQLException {
    	if(name.getText().length()>0 && surname.getText().length()>0 && login.getText().length()>0 && email.getText().length()>0 
    			&& password.getText().length()>0){
        	String isadminSelected = "0";
            String[] values_user = {name.getText(), surname.getText(), login.getText(), email.getText(), (password.getText()), isadminSelected};
            db.add_row("user", 7, values_user);
            okClicked = true;
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