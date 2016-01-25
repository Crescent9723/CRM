package sqlDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import data.ConnectionStatus;

public class SQLDatabase {
	private static final String CONNECTION_URL = "jdbc:sqlserver://192.168.1.72\\SQLEXPRESS:1433;databaseName=CRM;";
	private static final SQLDatabase instance = new SQLDatabase();
	private String userID;
	private String userPass;
	private final String MASTER_USER = "yychoi";
	private final String MASTER_PASS = "1q2w3e4r";
	private Connection con;

	
	// Getters
	public Connection getCon() { return con; }
	public String getUserID() {	return userID; }
	public String getUserPass() { return userPass; }
	public static SQLDatabase getInstance() { return instance; }
	
	// Connect Methods
	public ConnectionStatus connect(String userID, String userPass) throws ClassNotFoundException{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String connectionURL = CONNECTION_URL +
		        "user = " + userID + ";" +
		        "password = " + userPass;
		try {
			con = DriverManager.getConnection(connectionURL);
			this.userID = userID;
			this.userPass = userPass;
			
			return ConnectionStatus.SUCCESS_WITH_CUSTOMER;
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Authentication failed\nCreate New Account Please", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return ConnectionStatus.FAILURE;
	}
	
	public void disconnect() throws SQLException{
		con.close();
	}
	private void connectWithMasterUser() throws SQLException {
		String connectionURL = CONNECTION_URL +
				"user = " + MASTER_USER + ";" +
				"password = " + MASTER_PASS;
		con = DriverManager.getConnection(connectionURL);
	}
	
	// Create Account Methods
	public boolean createNewAccount(String userID, String userPass, String lastName, String firstName, String address, String city, String country, 
			String postalCode, String phoneNumber){
		try{
			connectWithMasterUser();
			createUser(userID, userPass);
			giveCustomerPermission(userID);
				
			insertAccountInfo(userID, userPass, lastName, firstName, address, city, country, postalCode, phoneNumber);
			// Close the connection for master user
			con.close();
		} catch (SQLException e) {
			e.getMessage();
			JOptionPane.showMessageDialog(null, "Account is not created");
			return false;
		}
		return true;
	}
	private void createUser(String userID, String userPass) throws SQLException {
		String query = "CREATE LOGIN " + userID + " WITH PASSWORD = '" + userPass + "';";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "CREATE USER " + userID + " FOR LOGIN " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
	}
	private void insertAccountInfo(String userID, String userPass, String lastName, String firstName, String address, String city, String country, 
			String postalCode, String phoneNumber) throws SQLException {
		String query = "INSERT INTO Accounts (AccountID, Password, LastName, FirstName, Address, City, Country, PostalCode, Phone, Activated) VALUES ('" + userID
				+ "', '" + userPass + "', '" + lastName + "', '" + firstName + "', '" + address + "', '" + city + "', '" + country + "', '" + postalCode + "', '" + phoneNumber + "', '1');";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
	}
	private void giveCustomerPermission(String userID) throws SQLException {
		String query = "GRANT INSERT, UPDATE, SELECT ON Orders TO " + userID + ";";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT INSERT, UPDATE, SELECT ON Queries TO " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT INSERT, UPDATE, SELECT ON Accounts TO " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT SELECT ON Products TO " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
	}
		
	// Order Methods
	public void makeOrder(int productId, String payment, String description) throws SQLException{
		String query = "INSERT INTO Orders (AccountID, Date, ProductID, Payment, Description, Delivered) VALUES (?, ?, ?, ?, ?, '0')";
		PreparedStatement stmt = con.prepareStatement(query);
		Calendar currentTime = Calendar.getInstance();
		Date date = new Date((currentTime.getTime()).getTime());
		stmt.setString(1, userID);
		stmt.setDate(2, date);
		stmt.setInt(3, productId);
		stmt.setString(4, payment);
		stmt.setString(5, description);
		stmt.execute();
	}
	
	public ResultSet getOrderList(boolean sorted) throws SQLException{
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Orders WHERE AccountID = '" + userID + "' ORDER BY Delivered DESC;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Orders WHERE AccountID = '" + userID + "';";
			cstmt = con.prepareCall(query);
		}
		return cstmt.executeQuery();
	}
	public String getProductName(String productID) throws SQLException {
		String query = "SELECT Name FROM Products WHERE ProductID = '" + productID + "';";
		CallableStatement cstmt = con.prepareCall(query);
		ResultSet rs = cstmt.executeQuery();
		rs.next();
		return rs.getString(1);
	}
	
	// Query Methods
	public void makeQuery(String category, String description) throws SQLException {
		String query = "INSERT INTO Queries (AccountID, Category, Description, Date, Answer) VALUES (?, ?, ?, ?, '0')";
		PreparedStatement stmt = con.prepareStatement(query);
		Calendar currentTime = Calendar.getInstance();
		Date date = new Date((currentTime.getTime()).getTime());
		stmt.setString(1, userID);
		stmt.setString(2, category);
		stmt.setString(3, description);
		stmt.setDate(4, date);
		stmt.execute();
	}
	
	public ResultSet getQueryList(boolean sorted) throws SQLException {
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Queries WHERE AccountID = '" + userID + "' ORDER BY case when Answer IS NULL then 0 else 1 end, Date;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Queries WHERE AccountID = '" + userID + "';";
			cstmt = con.prepareCall(query);
		}
		return cstmt.executeQuery();
	}
	
	// Change Info Methods
	public boolean checkPassword(String pass) {
		if (pass.equals(userPass)){
			return true;
		}
		return false;
	}
	public void changePassword(String newPass) throws SQLException {
		disconnect();
		connectWithMasterUser();
		String query = "ALTER LOGIN " + userID + " WITH PASSWORD = '" + newPass + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "UPDATE Accounts SET Password = '" + newPass + "' WHERE AccountID = '" + userID + "';";
		stmt = con.prepareCall(query);
		stmt.execute();
		userPass = newPass;
		disconnect();
	}
	public void deleteAccount() throws SQLException {
		disconnect();
		connectWithMasterUser();
		String query = "UPDATE Accounts SET Activated = '0' WHERE AccountID = '" + userID + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "DROP USER " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
		query = "DROP LOGIN " + userID + ";";
		stmt = con.prepareCall(query);
		stmt.execute();
		disconnect();
	}
	public void changeInformation(String lastName, String firstName, String address,
			String city, String postalCode, String country, String phone) throws SQLException {
		String query = "UPDATE Accounts SET LastName = '" + lastName + "', FirstName = '" + firstName + "', Address = '" + address + "', City = '" + city
				+ "', PostalCode = '" + postalCode + "', Country = '" + country + "', Phone = '" + phone + "' WHERE AccountID = '" + userID + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
	}
	public ResultSet getAccountInfo() throws SQLException {
		String query = "SELECT * FROM Accounts WHERE AccountID = '" + userID + "';";
		CallableStatement cstmt = con.prepareCall(query);
		ResultSet rs = cstmt.executeQuery();
		rs.next();
		return rs;
	}
}
