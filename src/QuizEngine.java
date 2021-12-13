import java.util.ArrayList;
import java.util.Random;

class QuizEngine {
	
	private ArrayList<Question> questionSet;
	private Random rand = new Random();

	QuizEngine() {

	}

	public void setQuestionSet(ArrayList<Question> qSetIn) {
		this.questionSet = qSetIn;
	}

	public ArrayList<Question> getQuestionSet() {
		return this.questionSet;
	}

	// generates a random array of numbers 1 - x
	public void getRandQuesSeq(int numOfQuestions) {
		ArrayList<Integer> qSequence = new ArrayList<Integer>();

		while(!(qSequence.size() >= numOfQuestions)){
			if(qSequence.size() >= numOfQuestions) {
				break;
			}else{
				// roll a random number
				int randNum = rand.nextInt((numOfQuestions)) + 1;
				if(!(qSequence.contains(randNum))) {
					qSequence.add(randNum);
					System.out.println("Number added");
				}
			}
		}

		for(int i=0; i<qSequence.size(); i++) {
			System.out.print(qSequence.get(i) + ", ");
		}
	}

}
