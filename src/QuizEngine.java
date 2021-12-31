import java.util.ArrayList;
import java.util.Random;

public class QuizEngine {

	// set of all the questions we have in the selected table
	private ArrayList<Question> questionSet;

	// set of questions used for the current quiz
	private ArrayList<Question> generatedQuizSet;
	// this is a nested String arraylist because some questions could have
	// multiple answers
	private ArrayList<ArrayList<String>> generatedQuizSetAnswers;

	private Random rand = new Random();

	private int genQuizCurQuesNum = 0;

	// the random sequence of question numbers generated for each quiz
	private ArrayList<Integer> qSequence;

	QuizEngine() {
	}

	public void setQuestionSet(ArrayList<Question> qSetIn) {
		this.questionSet = qSetIn;
	}

	public ArrayList<Question> getQuestionSet() {
		return this.questionSet;
	}

	public ArrayList<Question> getQuizSet() {
		return this.generatedQuizSet;
	}

	public ArrayList<Integer> getQuestionSequence() {
		return this.qSequence;
	}

	// prints all of the questions, mostly for testing purposes
	public void displayQuiz() {
		for(int i=0; i<generatedQuizSet.size(); i++) {
			System.out.println(generatedQuizSet.get(i).getQuesText());
		}
	}
	
	// returns a question from the generated quiz
	public Question getQuestion(int qIndex) {
		return generatedQuizSet.get(qSequence.get(qIndex));
	}

	public void incrementCurQuesNum() {
		genQuizCurQuesNum++;
	}

	public void decrementCurQuesNum() {
		genQuizCurQuesNum--;
	}
	
	// returns the number of the question from the currently
	// generated quiz
	public int getCurQuesNum() {
		return genQuizCurQuesNum;	
	}

	public void generateQuiz() {

		// clear the current quiz if there is one
		generatedQuizSet = new ArrayList<Question>();
		generatedQuizSetAnswers = new ArrayList<ArrayList<String>>();

		qSequence = getRandQuesSeq(questionSet.size());
		
		for(int i=0; i<questionSet.size(); i++) {
			generatedQuizSet.add(questionSet.get(qSequence.get(i)));
		}

		genQuizCurQuesNum = 0;
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

	// takes the choices that the user selected as input,
	// compares them with the actual answers and then returns a numerical grade
	public void gradeQuiz(ArrayList<ArrayList<String>> allUserAnswersIn) {

		int userScore = 0;

		ArrayList<ArrayList<String>> allUserAnswers = allUserAnswersIn;
		
		for(int i=0; i<generatedQuizSet.size(); i++) {

			// get the question

			Question ques = generatedQuizSet.get(i);

			// User Answers
			ArrayList<String> questionUserAnswer = allUserAnswers.get(i);

			// Actual Answers
			ArrayList<String> questionActualAnswers = generatedQuizSet.get(i).getAnswers();

			for(String userAnswer : questionUserAnswer) {
				// the method that we use to check for correct answers will
				// differ based on the question type
				if(ques.getQType() == 1) {
					// Checking JRadioButtons and there will be one answer
					if(userAnswer.equals(questionActualAnswers.get(0))) {
						System.out.println("CORRECT ANSWER FOUND");
						System.out.println("User Answer: ");
						System.out.println(userAnswer);
						System.out.println("Actual Answer: ");
						System.out.println(questionActualAnswers.get(0));
					}
				}else if(ques.getQType() == 2) {
					// Checking JCheckBoxes and there will be more than one answer
				}else if(ques.getQType() == 3) {
					// Checking JTextField and there could be one or more answers
				}
			}

		}

	}
}
