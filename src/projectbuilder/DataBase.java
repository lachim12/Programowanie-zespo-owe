package projectbuilder;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 Klasa zajmujaca sie przetwarzaniem danych z bazy danych
 **/
public class DataBase {

	private String dburl;
	private static Connection conn;
	
	//Konstruktor
	public DataBase(){
		
	}
	//Tworzy polaczenie z baza
	public Connection connection(String adress) throws SQLException{
		dburl = adress;
		addHistory(dburl);
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("noConnected");
        	System.out.println(ex);
        }
		
		conn = DriverManager.getConnection(dburl);
		addHistory("Poczatek polaczenia");
		return conn;
	}
	//Zamyka polaczenie - wywolywac na koncu programu
	public void end_connection() throws SQLException{
		addHistory("Koniec polaczenia\r\n");
		conn.close();
	}
	//Zwraca liczbe kolumn w danej tabeli
	public int numb_columns(String table, String column) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT "+ column +" FROM " + table + " WHERE id LIKE '" + last_id(table) + "'");
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		
		rs.close();
		stmt.close();
		return numberOfColumns;
	}
	//Zwraca caly wiersz z tabeli wedlug danej kolumny i jej wartosci
	public String[] show(String table, String column, String value) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "SELECT * FROM " + table + " WHERE " + column + " = '" + value + "'";
		//addHistory(query);
		ResultSet rs = stmt.executeQuery(query);
		int numberOfColumns = numb_columns(table, "*");
		String[] wszystko =  new String[numberOfColumns];
		while(rs.next()){
			for(int i = 1; i <= numberOfColumns; i++){
				wszystko[i-1] = rs.getString(i) + "";
				}
			}	
		rs.close();
		stmt.close();
		return wszystko;
	}
	
	//Zwraca tylko jedna potrzebna wartosc
	public String show_stricte(String namespace, String table, String column, String value) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "SELECT "+ namespace +" FROM " + table + " WHERE " + column + " = '" + value + "'";
		//addHistory(query);
		ResultSet rs = stmt.executeQuery(query);
		String output = null;
		while(rs.next()){
			output = rs.getString(namespace);
		}
		rs.close();
		stmt.close();
		return output;
	}
	
	//Zwraca cala dana kolumne
	public String[] show_stricte_all(String namespace, String table, String column, String value) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "SELECT "+ namespace +" FROM " + table + " WHERE " + column + " = '" + value + "'";
		//addHistory(query);
		ResultSet rs = stmt.executeQuery(query);
		String[] output = new String[last_id(table)];
		int i = 0;
		while(rs.next()){
			output[i] = rs.getString(namespace);
			i++;
		}
		rs.close();
		stmt.close();
		return output;
	}
	//Zwraca date z bazy danych
	public Timestamp show_date(String namespace, String table, String column, String value) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "SELECT "+ namespace +" FROM " + table + " WHERE " + column + " LIKE '" + value + "'";
		//addHistory(query);
		ResultSet rs = stmt.executeQuery(query);
		Timestamp date = null;
		while(rs.next()){
			date = rs.getTimestamp(namespace);
		}
		return date;
	}
	
	//Dodaje wiersz danych
	public void add_row(String table,int numb_columns, String[] values){
		int i = 2;
		String query = "INSERT INTO " + table + " VALUES (";
		query += "?,";
		for(i = 2; i < numb_columns; i++){//pomijana jest kolumna ID, generowana przez last_id()
			query += "'" + values[i-2] + "',";
		}
		query += "'" + values[i-2] + "')";
		addHistory(query);
		try {
			int id_max = last_id(table);
			id_max += 1;
			PreparedStatement pStmt = conn.prepareStatement(query);
			pStmt.setInt(1, id_max);
			System.out.println(query);
			pStmt.executeUpdate();
			pStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Zwraca nazwy wszystkich kolumn
	public String[] columns_names(String table, String column, int id) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT "+ column +" FROM " + table + " WHERE id LIKE '" + id + "'");
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		String[] names =  new String[numberOfColumns];
		for (int i = 1; i <= numberOfColumns; i++) {
		      names[i-1] = rsMetaData.getColumnName(i);
		    }
		rs.close();
		stmt.close();
		return names;
	}
	//Edytuje dany wiersz o podane dane
	public void edit_row(String table, String[] values_new, String standard_column, String standard_value) throws SQLException{
		try {
			int i = 0;
			int last_id = last_id(table);
			String[] values = show(table, standard_column, standard_value);
			String query = "UPDATE " + table + " SET ";
			String[] columns_names = columns_names(table, "*", last_id);
			for(i = 1; i < numb_columns(table, "*")-1; i++){//pomijana jest kolumna ID
				if(values_new[i] != values[i]){
					query += "`" + columns_names[i] +"`='" + values_new[i] + "',";
				}else{
					query += "`" + columns_names[i] +"`='" + values[i] + "',";
				}
			}
			if(values_new[i] != values[i]){
				query += "`" + columns_names[i] +"`='" + values_new[i] + "' WHERE " + standard_column + " LIKE '" + standard_value + "'";
			}else{
				query += "`" + columns_names[i] +"`='" + values[i] + "' WHERE " + standard_column + " LIKE '" + standard_value + "'";
			}
			
			
			System.out.println(query);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//Usuwa dany wiersz
	public void delete_row(String table,String column , String value){
		try {
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM " + table + " WHERE " + column + " = '" + value + "'";
			addHistory(query);
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Sprawdza poprawnosc hasla i loginu
	public boolean login(String login, String password) throws SQLException{
		Statement stmt = conn.createStatement();               
		String query = "SELECT * FROM user WHERE login LIKE '"+ login +"'";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			if(login.equals(rs.getString(4))){                              
				if(password.equals(rs.getString(6))){
					
					rs.close();
					stmt.close();
					return true;
				}else rs.close(); stmt.close(); return false;//operacja logowania nie powiodla sie
			}else rs.close(); stmt.close(); return false;//operacja logowania nie powiodla sie
		}
		rs.close();
		stmt.close();
		return false;
	}
	//Zwraca date z Javy w formacie odpowiednim dla SQL
	private static Timestamp getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Timestamp(today.getTime());
	}
	
	//Zwraca ostatnie ID w tablicy
	public int last_id(String table) throws SQLException {
		int id_max = 0;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(ID) AS maxID FROM " + table);
		
		if (rs.next()){
			id_max = rs.getInt("maxID");
		}
		rs.close();
		stmt.close();
		return id_max;
	}
	//Dodaje zapis do logs.txt
	public void addHistory(String history){
		Path path = FileSystems.getDefault().getPath("logs.txt");
		byte data[] = history.getBytes();
		try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, APPEND, CREATE))) {
			out.write(data, 0, data.length);
			out.write("\r\n".getBytes());
			out.close();
		} catch (IOException x) {
			System.err.println(x);
		}
	}
	//generuje raport Wygenerowany.txt
	public void generate() throws SQLException{
		
		int length;
		if(last_id("user") >= last_id("project")){
			length = last_id("user");
		}else if(last_id("project") >= last_id("task")){
			length = last_id("project");
		}else length = last_id("task");
		
		Path path = FileSystems.getDefault().getPath("Wygenerowny.html");
		String toGenerate = "<html><head><title>Raport z ProjectBuilder</title>" + 
		"</head><body><center><p><b>Raport wygenerowany z programu ProjectBuilder</b></p><table width=\"100%\"><tr><td>";
		toGenerate+= "<table border=\"2\">";
		for(int i = 0; i <= length; i++){
			if(show_stricte("login", "user", "id", i+"") != null && show_stricte("login", "user", "id", i+"").length()>0){
				toGenerate += "<tr><td><p>Uzytkownik ";
				toGenerate += show_stricte("login", "user", "id", i+"");
				toGenerate += " zalogowal sie ostatnio <font color=\"red\">";
				String x = show_stricte("date", "login_history", "id_user", i+"");
				if(x!=null && x.length()>0){
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					long milliSeconds= Long.parseLong(x);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(milliSeconds);
					toGenerate += formatter.format(calendar.getTime());
				}else toGenerate+=" ";
				toGenerate += "</tr></td></font></p>";
			}else toGenerate+="";
		}
		toGenerate+="</table></td><td>";
		toGenerate+= "<table border=\"2\">";
		for(int x = 0; x <= length; x++){
			if(show_stricte("name", "project", "id", x+"") != null && show_stricte("name", "project", "id", x+"").length()>0){
				toGenerate += "<tr><td><p>Projekt ";
				toGenerate += show_stricte("name", "project", "id", x+"");
				toGenerate += " ma status <font color=\"red\">";
				toGenerate += show_stricte("state", "project_state", "id_project", x+"");
				toGenerate += "</tr></td></font></p>";
			}else toGenerate+="";
		}
		toGenerate+="</table></td><td>";
		toGenerate+= "<table border=\"2\">";
		for(int x = 0; x <= length; x++){
			if(show_stricte("name", "task", "id", x+"") != null && show_stricte("name", "task", "id", x+"").length()>0){
				toGenerate += "<tr><td><p>Zadanie ";
				toGenerate += show_stricte("name", "task", "id", x+"");
				toGenerate += " ma status <font color=\"red\">";
				toGenerate += show_stricte("state", "task_state", "id_task", x+"");
				toGenerate+= "</tr></td></font></p>";
			}else toGenerate+="";
		}
		toGenerate+="</table></td></tr></table>";
		toGenerate+= "</center></body></html>";
		try {
			PrintWriter writer = new PrintWriter(path.toString(), "ISO-8859-2");
			writer.println(toGenerate);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	public String encrypt(String str){
		char x[ ] = str.toCharArray( );
		for( int i=0 ; i!=x.length ; i++){
			int n = x[i];
			n+=439;
			x[i]=(char)n; 
		}
		return new String(x); 
	}
	public String decrypt(String str){
		char x[ ] = str.toCharArray( );
		for( int i=0 ; i!=x.length ; i++){
			int n = x[i];
			n-=439;
			x[i]=(char)n; 
		}
		return new String(x); 
	}
}