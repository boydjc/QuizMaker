import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Running...");

		// args[0] = database name
		// args[1] = port
		// args[2] = username
		// args[3] = password
		QuizEngine qEng = new QuizEngine(args[0],
										 args[1],
										 args[2],
										 args[3]);

		QuizGui qGui = new QuizGui();

		qEng.getAllQuestions("chapterOne");
		qEng.generateQuiz(10);
		qEng.displayQuiz();

		qGui.display();

	
	}
}
