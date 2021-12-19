import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Running...");

		//QuizEngine qEng = new QuizEngine(args[0],
		//								 args[1],
		//								 args[2],
		//								 args[3]);

		QuizGui qGui = new QuizGui();

		//qEng.getAllQuestions("chapterOne");
		//qEng.generateQuiz(10);
		//qEng.displayQuiz();

		qGui.display();

	
	}
}
