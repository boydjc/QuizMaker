import java.util.ArrayList;
import java.util.Random;

public class QuizEngine {

	// set of all the questions we have in the selected table
	private ArrayList<Question> questionSet;

	// set of questions used for the current quiz
	private ArrayList<Question> generatedQuizSet;

	// set of questions that the user has gotten incorrect
	private ArrayList<Question> incorrectQuestionSet;

	// the answers that the user had that were incorrect
	private ArrayList<ArrayList<String>> incorrectUserAnswers;

	private Random rand = new Random();

	private int genQuizCurQuesNum = 0;

	// the random sequence of question numbers generated for each quiz
	private ArrayList<Integer> qSequence;

	QuizEngine() { 
		generatedQuizSet = new ArrayList<Question>();
		incorrectQuestionSet = new ArrayList<Question>();
		incorrectUserAnswers = new ArrayList<ArrayList<String>>();
		qSequence = new ArrayList<Integer>();
	}

	public void setQuestionSet(ArrayList<Question> qSetIn) {
		this.questionSet = qSetIn;
	}

	public ArrayList<Question> getIncorrectQuestionSet() {
		return this.incorrectQuestionSet;
	}

	public ArrayList<ArrayList<String>> getIncorrectUserAnswers() {
		return this.incorrectUserAnswers;
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
		return generatedQuizSet.get(qIndex);
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

		qSequence = getRandQuesSeq(questionSet.size());
		
		for(int i=0; i<questionSet.size(); i++) {
			generatedQuizSet.add(questionSet.get(qSequence.get(i)));
		}

		genQuizCurQuesNum = 0;
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
	public float gradeQuiz(ArrayList<ArrayList<String>> allUserAnswersIn) {

		float userScore = 0;

		ArrayList<ArrayList<String>> allUserAnswers = allUserAnswersIn;

		// clear the incorrect question set and incorrect answers
		incorrectQuestionSet = new ArrayList<Question>();
		incorrectUserAnswers = new ArrayList<ArrayList<String>>();
		
		for(int i=0; i<generatedQuizSet.size(); i++) {

			// get the question
			Question ques = generatedQuizSet.get(i);

			int questionType = ques.getQType();

			// User Answers
			ArrayList<String> questionUserAnswer = allUserAnswers.get(i);

			// Actual Answers
			ArrayList<String> questionActualAnswers = generatedQuizSet.get(i).getAnswers();

			ArrayList<String> incorrectAnswer = new ArrayList<String>();

			for(String userAnswer : questionUserAnswer) {

				// the userAnswer will still contain the <html> wrapping tags from the label
				// these need to be removed before we can check the answer

				userAnswer = userAnswer.replace("<html>", "");
				userAnswer = userAnswer.replace("</html>", "");

				if(questionActualAnswers.contains(userAnswer)) {
					userScore += ((float) 1 / (float) questionActualAnswers.size());
				}else {
					// if the answer is not correct, add the question to the incorrect question set
					incorrectQuestionSet.add(ques);
					incorrectAnswer.add(userAnswer);
				}
			}
			
			if(incorrectAnswer.size() > 0) {
				incorrectUserAnswers.add(incorrectAnswer);
			}
		
		}

		userScore = (userScore / generatedQuizSet.size()) * 100;

		return userScore;

	}
}
