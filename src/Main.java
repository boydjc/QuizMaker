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

		dbConn.executeStatement("SELECT * FROM chapterOne");

		ResultSet rs = dbConn.getResultSet();

		try {
			while(rs.next()) {

				/* rs.getString(1) qId
				/ rs.getString(2) qType
				/ rs.getString(3) qText
				/ rs.getString(4) choiceOne
				/ rs.getString(5) choiceTwo
				/ rs.getString(6) choiceThree
				/ rs.getString(7) choiceFour
				/ rs.getString(8) choiceFive
				/ rs.getString(9) choiceSix
				/ rs.getString(10) choiceSeven
				/ rs.getString(11) choiceEight
				/ rs.getString(12) choiceNine
				/ rs.getString(13) choiceTen
				/ rs.getString(14) ansOne
				/ rs.getString(15) ansTwo
				/ rs.getString(16) ansThree
				/ rs.getString(17) ansFour
				/ rs.getString(18) ansFive
				/ rs.getString(19) ansSix
				/ rs.getString(20) ansSeven
				/ rs.getString(21) ansEight
				/ rs.getString(22) ansNine
				/ rs.getString(23) ansTen	*/

				ArrayList<String> choices = new ArrayList<String>();
			
				ArrayList<String> answers = new ArrayList<String>();

				// go through the choices  and only add them if they are not blank
				for(int i=4; i<=13; i++) {
					if(rs.getString(i) != null) {
						choices.add(rs.getString(i));
					}
				}

				// same with the answers
				for(int i=14; i<=23; i++) {
					if(rs.getString(i) != null) {
						answers.add(rs.getString(i));
					}
				}


				// create new question

				Question testQ = new Question(rs.getInt(1),
											  rs.getInt(2),
											  rs.getString(3),
											  choices,
											  answers);

				System.out.println("Question Created Successfully");

				System.out.println(testQ.getQuesText());

				for(int i=0; i<testQ.getChoices().size(); i++){
					System.out.println(testQ.getChoices().get(i));
				}

			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}
}
