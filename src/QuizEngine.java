import java.util.ArrayList;
import java.util.Random;
import java.sql.ResultSet;
import java.sql.SQLException;

class QuizEngine {
	
	private DbConnection dbConn;

	// set of all the questions we have in the selected table
	private ArrayList<Question> questionSet;

	// set of questions used for the current quiz
	private ArrayList<Question> quizSet;

	private Random rand = new Random();

	QuizEngine(String dbNameIn, String dbPortIn, String dbUserIn, String dbPassIn) {
		dbConn = new DbConnection(dbNameIn, dbPortIn, dbUserIn, dbPassIn);
	}

	public void setConnectionDetails(String dbNameIn, String dbPortIn,
									 String dbUserIn, String dbPassIn) {
		dbConn.setConnectionDetails(dbNameIn, dbPortIn,
									dbUserIn, dbPassIn);
	}

	public void setQuestionSet(ArrayList<Question> qSetIn) {
		this.questionSet = qSetIn;
	}

	public ArrayList<Question> getQuestionSet() {
		return this.questionSet;
	}

	public ArrayList<Question> getQuizSet() {
		return this.quizSet;
	}

	public void displayQuiz() {
		for(int i=0; i<quizSet.size(); i++) {
			System.out.println(quizSet.get(i).getQuesText());
		}
	}

	public void generateQuiz(int numOfQuestions) {

		// clear the current quiz if there is one
		quizSet = new ArrayList<Question>();

		ArrayList<Integer> qSequence = getRandQuesSeq(numOfQuestions);
		
		for(int i=0; i<numOfQuestions; i++) {
			quizSet.add(questionSet.get(qSequence.get(i)));
		}
		System.out.println("Quiz Generated Successfully");
	}

	public void getAllQuestions(String tableName) {

		dbConn.makeConnection();

		dbConn.executeStatement("SELECT * FROM " + tableName);

		questionSet = new ArrayList<Question>();
		
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

				// go through the choices and only add them is they are not blank
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
				Question q = new Question(rs.getInt(1),
										  rs.getInt(2),
										  rs.getString(3),
										  choices,
										  answers);
				
				// add the question to our questionSet
				questionSet.add(q);

			} 
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}

		dbConn.closeConnection();

		System.out.println("Questions for " + tableName + " retrieved.");
	}

	// generates a random array of numbers 1 - x
	private ArrayList<Integer> getRandQuesSeq(int numOfQuestions) {
		ArrayList<Integer> qSequence = new ArrayList<Integer>();

		while(!(qSequence.size() >= numOfQuestions)){
			if(qSequence.size() >= numOfQuestions) {
				break;
			}else{
				// roll a random number
				int randNum = rand.nextInt((numOfQuestions));
				if(!(qSequence.contains(randNum))) {
					qSequence.add(randNum);
				}
			}
		}
		return qSequence;
	}
}
