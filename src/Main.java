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
	}
}
