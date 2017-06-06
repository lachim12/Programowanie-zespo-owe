package projectbuilder.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData{
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty login;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;
    private final SimpleStringProperty isadmin;
 
    public UserData() {
        this(null, null, null, null, null, null);
    }
    
    public UserData(String name, String surname, String login, String email, String password, String isadmin) {
    	this.name = new SimpleStringProperty(this, "id_project", name);
        this.surname = new SimpleStringProperty(this, "name", surname);
        this.login = new SimpleStringProperty(this, "description", login);
        this.email = new SimpleStringProperty(this, "start_date", email);
        this.password = new SimpleStringProperty(this, "deadline", password);
        this.isadmin = new SimpleStringProperty(this, "progress", isadmin);
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
    public String get_surname() {
        return surname.get();
    }
    public void set_surname(String fName) {
        surname.set(fName);
    }
    public StringProperty surnameProperty() {
        return surname;
    }
    public String get_login() {
        return login.get();
    }
    public void set_login(String fName) {
        login.set(fName);
    }
    public StringProperty loginProperty() {
        return login;
    }
    public String get_email() {
        return email.get();
    }
    public void set_email(String fName) {
    	email.set(fName);
    }
    public StringProperty emailProperty() {
        return email;
    }   
    public String get_password() {
        return password.get();
    }
    public void set_password(String fName) {
        this.password.set(fName);
    }
    public StringProperty passwordProperty() {
        return password;
    }    
    public String get_isadmin() {
        return isadmin.get();
    }
    public void set_isadmin(String fName) {
    	isadmin.set(fName);
    }
    public StringProperty isadminProperty() {
        return isadmin;
    }
}