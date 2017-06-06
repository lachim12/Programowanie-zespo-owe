package projectbuilder;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

	/**
	Kontroler  dialogu edycji danych
	**/
public class DataEditDialogController {

	@FXML private TextField id;
    @FXML private TextField nazwa;
    @FXML private TextField projekt;
    @FXML private TextField opis;
    @FXML private TextField data_rozpoczecia;
    @FXML private TextField deadline;
    @FXML private ComboBox<String> status = new ComboBox<String>();

    private Stage dialogStage;
    private Data data;
    DataBase db;
    @FXML
    private void initialize() {
    }

    //Ustawia stage
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    //inicjuje dane
    public void setData(Data data) {
    	status.getItems().addAll("Zakonczone","W toku","Zatwierdzone","Oczekujace","Wstrzymane");
        this.data = data;
        nazwa.setText(data.get_nazwa());
        projekt.setText(data.get_projekt());
        opis.setText(data.get_opis());
        data_rozpoczecia.setText(data.get_data_rozpoczecia());
        deadline.setText(data.get_deadline());
        status.setValue(data.get_status());
    }
    //aktualizuje baze i tablice
    @FXML
    private void handleOk() throws SQLException {
    	String[] values_new = {nazwa.getText(), projekt.getText(), opis.getText(), data_rozpoczecia.getText(), deadline.getText(), status.getValue()};
    	String id_assign = db.show_stricte("id", "assign", "name", nazwa.getText());
        	
    	data.set_nazwa(nazwa.getText());
    	data.set_projekt(projekt.getText());
    	data.set_opis(opis.getText());
    	data.set_data_rozpoczecia(data_rozpoczecia.getText());
    	data.set_deadline(deadline.getText());
    	data.set_status(status.getValue());
    	db.edit_row("assign", values_new, "id", id_assign);
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}