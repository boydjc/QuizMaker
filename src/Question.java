import java.util.ArrayList;

class Question {
	private int qId;
	private int qType;
	private String quesText;
	private ArrayList<String> quesAns = new ArrayList<String>();

	Question(int qIdIn, int qTypeIn, String quesTxtIn, ArrayList<String> quesAnsIn) {	

	}

	public int getQId() {
		return this.qId;
	}

	public int getQType() {
		return this.qType;
	}

	public String getQuesText() {
		return this.quesText;
	}

	public ArrayList<String> getQuesAns() {
		return this.quesAns;
	}	
}
