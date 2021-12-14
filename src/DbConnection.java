import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

class DbConnection {

	private String dbName;
	private String dbPort;
	private String dbUser;
	private String dbPass;

	private Connection con;
	private Statement stmt;
	private ResultSet rs;

	DbConnection(String dbNameIn, String dbPortIn,
				 String dbUserIn, String dbPassIn) {
		
		this.dbName = dbNameIn;
		this.dbPort = dbPortIn;
		this.dbUser = dbUserIn;
		this.dbPass = dbPassIn;

		stmt = null;
		rs = null;
	}

	// change the details of the connection so we can connect to different things
	public void setConnectionDetails(String dbNameIn, String dbPortIn,
									 String dbUserIn, String dbPassIn) {
		
		this.dbName = dbNameIn;
		this.dbPort = dbPortIn;
		this.dbUser = dbUserIn;
		this.dbPass = dbPassIn;
	}

	public void closeConnection() {
		try {
			con.close();
			System.out.println("Connection Closed");
		} catch(SQLException e) {
			// ignore
		}
	}

	public void makeConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:" + this.dbPort + "/" + this.dbName, this.dbUser, this.dbPass);
			System.out.println("Connection Successful.");
		} catch(Exception e) {
			System.out.println(e);
		}	
	}

	public void executeStatement(String stmtIn) {

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(stmtIn);

		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} 
	}

	public ResultSet getResultSet() {
		return rs;
	}
}
