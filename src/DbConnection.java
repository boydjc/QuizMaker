import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

class DbConnection {

	private Connection con;
	private Statement stmt;
	private ResultSet rs;

	DbConnection() {
		stmt = null;
		rs = null;
	}

	public void makeConnection(String dbName,
							   String port,
							   String username,
							   String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + dbName, username, password);
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
