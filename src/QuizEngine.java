import java.util.ArrayList;
import java.util.Random;

public class QuizEngine {

	// set of all the questions we have in the selected table
	private ArrayList<Question> questionSet;

	// set of questions used for the current quiz
	private ArrayList<Question> quizSet;

	private Random rand = new Random();

	QuizEngine() {
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
