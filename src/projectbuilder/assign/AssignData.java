package projectbuilder.assign;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssignData{
	private final SimpleStringProperty id_user;
    private final SimpleStringProperty id_task;
    private final SimpleStringProperty id_role;
    private final SimpleStringProperty start_date;
    private final SimpleStringProperty end_date;
 
    public AssignData() {
        this(null,null, null, null, null);
    }
    
    public AssignData(String id_user, String id_task_, String id_role_, String start_date_, String end_date_) {
    	this.id_user = new SimpleStringProperty(this, "id_user", id_user);
    	this.id_task = new SimpleStringProperty(this, "id_task", id_task_);
        this.id_role = new SimpleStringProperty(this, "id_role", id_role_);
        this.start_date = new SimpleStringProperty(this, "start_date", start_date_);
        this.end_date = new SimpleStringProperty(this, "end_date", end_date_);
    }
    public String get_id_user() {
        return id_user.get();
    }
    public void set_id_user(String fName) {
        this.id_user.set(fName);
    }
    public StringProperty id_userProperty() {
        return id_user;
    }
    
    public String get_id_task() {
        return id_task.get();
    }
    public void set_id_task(String fName) {
        this.id_task.set(fName);
    }
    public StringProperty id_taskProperty() {
        return id_task;
    }    
    public String get_id_role() {
        return id_role.get();
    }
    public void set_id_role(String fName) {
        id_role.set(fName);
    }
    public StringProperty id_roleProperty() {
        return id_role;
    }
    public String get_start_date() {
        return start_date.get();
    }
    public void set_start_date(String fName) {
        start_date.set(fName);
    }
    public StringProperty start_dateProperty() {
        return start_date;
    }
    public String get_end_date() {
        return end_date.get();
    }
    public void set_end_date(String fName) {
    	end_date.set(fName);
    }
    public StringProperty end_dateProperty() {
        return end_date;
    }  
}