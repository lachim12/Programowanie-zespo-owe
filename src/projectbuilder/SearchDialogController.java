package projectbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SearchDialogController {
	@FXML private TextField search;
	private Stage dialogStage;
	
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	@FXML
    private void initialize() {
		
    }
	@FXML
	private void search(){
		if(!(search.getText() != null) &&  search.getText().trim().isEmpty()){
			MainViewController.set_search(null);
		}else{
			MainViewController.set_search(search.getText());
		}
		dialogStage.close();
	}
}
