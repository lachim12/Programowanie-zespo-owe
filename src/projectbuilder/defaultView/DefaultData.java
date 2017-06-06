package projectbuilder.defaultView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DefaultData{
    private final SimpleStringProperty nazwa;
    private final SimpleStringProperty projekt;
    private final SimpleStringProperty opis;
    private final SimpleStringProperty data_rozpoczecia;
    private final SimpleStringProperty deadline;
    private final SimpleStringProperty status;
 
    public DefaultData() {
        this(null, null, null, null, null, null);
    }
    
    public DefaultData(String nazwa_, String projekt_, String opis_, String data_rozpoczecia_, String deadline_, String status_) {
    	this.nazwa = new SimpleStringProperty(this, "nazwa", nazwa_);
        this.projekt = new SimpleStringProperty(this, "projekt", projekt_);
        this.opis = new SimpleStringProperty(this, "opis", opis_);
        this.data_rozpoczecia = new SimpleStringProperty(this, "data_rozpoczecia", data_rozpoczecia_);
        this.deadline = new SimpleStringProperty(this, "deadline", deadline_);
        this.status = new SimpleStringProperty(this, "status", status_);
    }
 
    public String get_nazwa() {
        return nazwa.get();
    }
    public void set_nazwa(String fName) {
        this.nazwa.set(fName);
    }
    public StringProperty nazwaProperty() {
        return nazwa;
    }    
    public String get_projekt() {
        return projekt.get();
    }
    public void set_projekt(String fName) {
        projekt.set(fName);
    }
    public StringProperty projektProperty() {
        return projekt;
    }
    public String get_opis() {
        return opis.get();
    }
    public void set_opis(String fName) {
        opis.set(fName);
    }
    public StringProperty opisProperty() {
        return opis;
    }
    public String get_data_rozpoczecia() {
        return data_rozpoczecia.get();
    }
    public void set_data_rozpoczecia(String fName) {
    	data_rozpoczecia.set(fName);
    }
    public StringProperty data_rozpoczeciaProperty() {
        return data_rozpoczecia;
    }
    public String get_deadline() {
        return deadline.get();
    }
    public void set_deadline(String fName) {
    	deadline.set(fName);
    }
    public StringProperty deadlineProperty() {
        return deadline;
    }
    public String get_status() {
        return status.get();
    }
    public void set_status(String fName) {
    	status.set(fName);
    }
    public StringProperty statusProperty() {
        return status;
    }   
}