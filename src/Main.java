import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Running...");

		ArrayList<Question> questionSet = new ArrayList<Question>();

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
				// rs.getString(1) question id		rs.getString(2) type number
				// rs.getString(3) Question text	rs.getString(4) Ans One
				// rs.getString(5) Ans Two			rs.getString(6) Ans Three
				// rs.getString(7) Ans Four			rs.getString(8) Ans Five
				// rs.getString(9) Ans Six			rs.getString(10) Ans Seven
				// rs.getString(11) Ans Eight		rs.getString(12) Ans Nine
				// rs.getString(13) Ans Ten

				ArrayList<String> answers = new ArrayList<String>();

				// go through the answers and only add them if they are not blank
				for(int i=4; i<=13; i++) {
					if(!(rs.getString(i).equals(""))) {
						answers.add(rs.getString(i));
					}
				}

				// create new question

				Question testQ = new Question(rs.getInt(1),
											  rs.getInt(2),
											  rs.getString(3),
											  answers);

				System.out.println("Question Created Successfully");

				System.out.println(testQ.getQuesText());

				for(int i=0; i<testQ.getQuesAns().size(); i++){
					System.out.println(testQ.getQuesAns().get(i));
				}

			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
}
