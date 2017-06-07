package projectbuilder.user;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import projectbuilder.DataBase;

public class UserEditDialogController {

	@FXML	private TextField name;
    @FXML   private TextField surname;
    @FXML   private TextField login;
    @FXML   private TextField email;
    @FXML	private TextField password;
    @FXML   private CheckBox isadmin;
    private Stage dialogStage;
    private UserData data;
    private boolean okClicked = false;
    private DataBase db;

    @FXML
    private void initialize() {
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setData(UserData data, DataBase dbase) {
        this.data = data;
        name.setText(data.get_name());
        surname.setText(data.get_surname());
        login.setText(data.get_login());
        email.setText(data.get_email());
        password.setText(data.get_password());
        isadmin.setText(data.get_isadmin());
        isadmin.setSelected(data.get_isadmin().equals("1"));
        db = dbase;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() throws SQLException {
    	if(name.getText().length()>0 && surname.getText().length()>0 && login.getText().length()>0 && email.getText().length()>0 
    			&& password.getText().length()>0){
    	String id_user = db.show_stricte("id", "user", "login", data.get_login());
        data.set_name(name.getText());
        data.set_surname(surname.getText());
        data.set_login(login.getText());
        data.set_email(email.getText());
        data.set_password(password.getText());
        if(isadmin.isSelected()){
        	data.set_isadmin("1");
        }else{
        	data.set_isadmin("0");
        }
        String[] values_user = {null,name.getText(), surname.getText(), login.getText(), email.getText(), db.encrypt(password.getText()), data.get_isadmin()};
        db.edit_row("user", values_user, "id", id_user);
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