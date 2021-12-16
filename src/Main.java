import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Running...");

		/*QuizSet testQSet = new QuizSet("Test");

		System.out.println("Quiz Set Created Successfully at " + testQSet.getCreatedDate());

		Question testQuestionOne = new Question();
		testQuestionOne.setQId(1);
		testQuestionOne.setQType(1);
		testQuestionOne.setQuesText("Is this a test question?");
		ArrayList<String> testQOneChoices = new ArrayList<String>();
		testQOneChoices.add("Choice One");
		testQOneChoices.add("Choice Two");
		testQOneChoices.add("Choice Three");
		testQOneChoices.add("Choice Four");
		testQuestionOne.setChoices(testQOneChoices);
		ArrayList<String> testQOneAnswers = new ArrayList<String>();
		testQOneAnswers.add("Answer One");
		testQuestionOne.setAnswers(testQOneAnswers);

		// test serialization 
		try {
			FileOutputStream fileOut = new FileOutputStream("./data/test.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(testQuestionOne);
			out.close();
			fileOut.close();
			System.out.println("Data Serialized successfully");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} */

		// args[0] = database name
		// args[1] = port
		// args[2] = username
		// args[3] = password
		//QuizEngine qEng = new QuizEngine(args[0],
		//								 args[1],
		//								 args[2],
		//								 args[3]);

		WelcomeGui qGui = new WelcomeGui();

		//qEng.getAllQuestions("chapterOne");
		//qEng.generateQuiz(10);
		//qEng.displayQuiz();

		qGui.display();

	
	}
}
