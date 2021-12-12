import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		System.out.println("Testing");

		DbConnection dbConn = new DbConnection();

		// args[0] = database name
		// args[1] = port
		// args[2] = username
		// args[3] = password
		dbConn.makeConnection(args[0],
							  args[1],
							  args[2],
							  args[3]);

		dbConn.executeStatement("SELECT * FROM SecPlus");

		ResultSet rs = dbConn.getResultSet();

		try {
			while(rs.next()) {
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
				System.out.println(rs.getString(4));
				System.out.println(rs.getString(5));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
}
