package sqlDatabase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class SQLDatabase {
	private static final SQLDatabase instance = new SQLDatabase();
	private String customerID;
	private final String SERVER_NAME = "localhost";
	private final String DB_NAME = "crm_db";
	private final String MASTER_USER = "root";
	private final String MASTER_PASS = "1q2w3e4r";
	private Connection con;

	
	// Getters
	public Connection getCon() { return con; }
	public String getCustomerID() {	return customerID; }
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public static SQLDatabase getInstance() { return instance; }
	
	// Connect Methods
	public void disconnect() throws SQLException{
		con.close();
	}
	public void connectWithMasterUser() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		MysqlDataSource source = new MysqlDataSource();
		source.setUser(MASTER_USER);
		source.setPassword(MASTER_PASS);
		source.setServerName(SERVER_NAME);
		source.setDatabaseName(DB_NAME);
		con = source.getConnection();
	}
	
	// Order Methods
	public ResultSet getFullOrderList(boolean sorted) throws SQLException{
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Orders ORDER BY Delivered DESC;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Orders;";
			cstmt = con.prepareCall(query);
		}
		return cstmt.executeQuery();
	}
	
	public void setDelivered(int orderID, int value) throws SQLException{
		String query;
		CallableStatement cstmt;
		query = "UPDATE Orders SET Delivered = '" + value + "' WHERE OrderID = " + orderID + ";";
		cstmt = con.prepareCall(query);
		cstmt.execute();
	}
	
	public ResultSet getCustomerOrderList(boolean sorted) throws SQLException{
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Orders WHERE AccountID = '" + getCustomerID() + "' ORDER BY Delivered DESC;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Orders WHERE AccountID = '" + getCustomerID() + "';";
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
	
	public ResultSet getCustomerQueryList(boolean sorted) throws SQLException {
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Queries WHERE AccountID = '" + getCustomerID() + "' ORDER BY case when Answer IS NULL then 0 else 1 end, Date;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Queries WHERE AccountID = '" + getCustomerID() + "';";
			cstmt = con.prepareCall(query);
		}
		return cstmt.executeQuery();
	}
	
	public ResultSet getFullQueryList(boolean sorted) throws SQLException {
		String query;
		CallableStatement cstmt;
		if (sorted){
			query = "SELECT * FROM Queries ORDER BY case when Answer IS NULL then 0 else 1 end, Date;";
			cstmt = con.prepareCall(query);
		} else {
			query = "SELECT * FROM Queries;";
			cstmt = con.prepareCall(query);
		}
		return cstmt.executeQuery();
	}
	
	public void setRespondToQuery(int queryID, String respond) throws SQLException{
		String query;
		CallableStatement cstmt;
		query = "UPDATE Queries SET Answer = '" + respond + "' WHERE QueryID = " + queryID + ";";
		cstmt = con.prepareCall(query);
		cstmt.execute();
	}
	
	// Customer Info Methods
	
	public ResultSet getAccountInfo() throws SQLException {
		String query = "SELECT * FROM Accounts WHERE AccountID = '" + getCustomerID() + "';";
		CallableStatement cstmt = con.prepareCall(query);
		ResultSet rs = cstmt.executeQuery();
		rs.next();
		return rs;
	}
	public void deleteAccount() throws SQLException, ClassNotFoundException {
		disconnect();
		connectWithMasterUser();
		String query = "UPDATE Accounts SET Activated = '0' WHERE AccountID = '" + customerID + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "DROP USER '" + customerID + "'@'%';";
		stmt = con.prepareCall(query);
		stmt.execute();
		disconnect();
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
	
	public void reactivateAccount() throws SQLException, ClassNotFoundException {
		String query = "UPDATE Accounts SET Activated = '1' WHERE AccountID = '" + customerID + "';";
		PreparedStatement stmt = con.prepareCall(query);
		stmt.execute();
		query = "SELECT * FROM Accounts WHERE AccountID = '" + getCustomerID() + "';";
		CallableStatement cstmt = con.prepareCall(query);
		ResultSet rs = cstmt.executeQuery();
		rs.next();
		createUser(customerID, rs.getString("Password"));
		giveCustomerPermission(customerID);
	}

	
}
