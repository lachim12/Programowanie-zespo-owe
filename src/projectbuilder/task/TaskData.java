package projectbuilder.task;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskData{
    private final SimpleStringProperty id_project;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleStringProperty start_date;
    private final SimpleStringProperty deadline;
    private final SimpleStringProperty progress;
    private final SimpleStringProperty state;
    private final SimpleStringProperty date;
 
    public TaskData() {
        this(null, null, null, null, null, null, null, null);
    }
    
    public TaskData(String id_project, String name, String description, String start_date, String deadline,
    		String progress, String state, String date) {
    	this.id_project = new SimpleStringProperty(this, "id_project", id_project);
        this.name = new SimpleStringProperty(this, "name", name);
        this.description = new SimpleStringProperty(this, "description", description);
        this.start_date = new SimpleStringProperty(this, "start_date", start_date);
        this.deadline = new SimpleStringProperty(this, "deadline", deadline);
        this.progress = new SimpleStringProperty(this, "progress", progress);
        this.state = new SimpleStringProperty(this, "state", state);
        this.date = new SimpleStringProperty(this, "date", date);
    }
 
    public String get_id_project() {
        return id_project.get();
    }
    public void set_id_project(String fName) {
        this.id_project.set(fName);
    }
    public StringProperty id_projectProperty() {
        return id_project;
    }    
    public String get_name() {
        return name.get();
    }
    public void set_name(String fName) {
        name.set(fName);
    }
    public StringProperty nameProperty() {
        return name;
    }
    public String get_description() {
        return description.get();
    }
    public void set_description(String fName) {
        description.set(fName);
    }
    public StringProperty descriptionProperty() {
        return description;
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
    public String get_deadline() {
        return deadline.get();
    }
    public void set_deadline(String fName) {
        this.deadline.set(fName);
    }
    public StringProperty deadlineProperty() {
        return deadline;
    }    
    public String get_progress() {
        return progress.get();
    }
    public void set_progress(String fName) {
    	progress.set(fName);
    }
    public StringProperty progressProperty() {
        return progress;
    }
    public String get_state() {
        return state.get();
    }
    public void set_state(String fName) {
    	state.set(fName);
    }
    public StringProperty stateProperty() {
        return state;
    }
    public String get_date() {
        return date.get();
    }
    public void set_date(String fName) {
    	date.set(fName);
    }
    public StringProperty dateProperty() {
        return date;
    }
}