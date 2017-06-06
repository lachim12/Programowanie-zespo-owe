package projectbuilder.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectData{
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleStringProperty state;
    private final SimpleStringProperty date;
 
    public ProjectData() {
        this(null, null, null, null);
    }
    public ProjectData(String name, String description, String state, String date) {
    	this.name = new SimpleStringProperty(this, "nazwa", name);
        this.description = new SimpleStringProperty(this, "opis", description);
        this.state = new SimpleStringProperty(this, "status", state);
        this.date = new SimpleStringProperty(this, "data", date);
    }
 
    public String get_name() {
        return name.get();
    }
    public void set_name(String fName) {
        this.name.set(fName);
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