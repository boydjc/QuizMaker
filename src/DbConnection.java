import java.sql.*;

class DbConnection {

	DbConnection() {
	}

	public void makeConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaquiz", "s7341", "wtfomglol123");
			System.out.println("Connection Successful.");
		} catch(Exception e) {
			System.out.println(e);
		}	
	}
}
