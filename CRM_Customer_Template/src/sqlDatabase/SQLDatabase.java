package sqlDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import data.ConnectionStatus;

public class SQLDatabase {
	private static final SQLDatabase instance = new SQLDatabase();
	private String userID;
	private String userPass;
	private final String SERVER_NAME = "localhost";
	private final String DB_NAME = "crm_db";
	private final String MASTER_USER = "root";
	private final String MASTER_PASS = "1q2w3e4r";
	private Connection con;

	
	// Getters
	public Connection getCon() { return con; }
	public String getUserID() {	return userID; }
	public String getUserPass() { return userPass; }
	public static SQLDatabase getInstance() { return instance; }
	
	// Connect Methods
	public ConnectionStatus connect(String userID, String userPass) throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		try {
			MysqlDataSource source = new MysqlDataSource();
			source.setUser(userID);
			source.setPassword(userPass);
			source.setServerName(SERVER_NAME);
			source.setDatabaseName(DB_NAME);
			con = source.getConnection();
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
		MysqlDataSource source = new MysqlDataSource();
		source.setUser(MASTER_USER);
		source.setPassword(MASTER_PASS);
		source.setServerName(SERVER_NAME);
		source.setDatabaseName(DB_NAME);
		con = source.getConnection();
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
			e.printStackTrace();
			e.getMessage();
			JOptionPane.showMessageDialog(null, "Account is not created");
			return false;
		}
		return true;
	}
	private void insertAccountInfo(String userID, String userPass, String lastName, String firstName, String address, String city, String country, 
			String postalCode, String phoneNumber) throws SQLException {
		String query = "INSERT INTO Accounts (AccountID, Password, LastName, FirstName, Address, City, Country, PostalCode, Phone, Activated) VALUES ('" + userID
				+ "', '" + userPass + "', '" + lastName + "', '" + firstName + "', '" + address + "', '" + city + "', '" + country + "', '" + postalCode + "', '" + phoneNumber + "', '1');";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
	}
	
	private void createUser(String userID, String userPass) throws SQLException {
		String query = "flush privileges;";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "CREATE USER '" + userID + "'@'%' IDENTIFIED BY '" + userPass + "';";
		stmt = con.prepareCall(query);
		stmt.execute();
	}
	private void giveCustomerPermission(String userID) throws SQLException {
		String query = "GRANT SELECT, UPDATE ON crm_db.Accounts TO '" + userID + "'@'%';";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT INSERT, UPDATE, SELECT ON crm_db.Orders TO '" + userID + "'@'%';";
		stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT INSERT, UPDATE, SELECT ON crm_db.Queries TO '" + userID + "'@'%';";
		stmt = con.prepareCall(query);
		stmt.execute();
		query = "GRANT SELECT ON crm_db.Products TO '" + userID + "'@'%';";
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
	public boolean checkPassword(String userID, String pass) throws SQLException {
		String query = "SELECT Password FROM Accounts WHERE AccountId = '" + userID + "';";
		CallableStatement cstmt = con.prepareCall(query);
		ResultSet rs = cstmt.executeQuery();
		rs.next();
		String correctPass = rs.getString("Password");
		if (pass.equals(correctPass)){
			return true;
		}
		return false;
	}
	public void changePassword(String newPass) throws SQLException {
		disconnect();
		connectWithMasterUser();
		String query = "DROP USER '" + userID + "'@'%';";
		CallableStatement stmt = con.prepareCall(query);
		stmt.execute();
		createUser(userID, newPass);
		giveCustomerPermission(userID);
		query = "UPDATE Accounts SET Password = '" + newPass + "' WHERE AccountID = '" + userID + "';";
		stmt = con.prepareCall(query);
		stmt.execute();
		disconnect();
	}
	public void deleteAccount() throws SQLException {
		disconnect();
		connectWithMasterUser();
		String query = "UPDATE Accounts SET Activated = '0' WHERE AccountID = '" + userID + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "DROP USER '" + userID + "'@'%';";
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
