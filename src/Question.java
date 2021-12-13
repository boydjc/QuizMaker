import java.util.ArrayList;

class Question {
	private int qId;
	private int qType;
	private String quesText;
	private ArrayList<String> quesAns = new ArrayList<String>();

	Question() {

	}

	Question(int qIdIn, int qTypeIn, String quesTxtIn, ArrayList<String> quesAnsIn) {	
		this.qId = qIdIn;
		this.qType = qTypeIn;
		this.quesText = quesTxtIn;
		this.quesAns = quesAnsIn;
	}

	public void setQId(int qIdIn) {
		this.qId = qIdIn;
	}

	public int getQId() {
		return this.qId;
	}

	public void setQType(int qTypeIn) {
		this.qType = qTypeIn;
	}

	public int getQType() {
		return this.qType;
	}

	public void setQuesText(String quesTxtIn) {
		this.quesText = quesTxtIn;
	}

	public String getQuesText() {
		return this.quesText;
	}

	public void setQuesAns(ArrayList<String> quesAnsIn) {
		this.quesAns = quesAnsIn;
	}

	public ArrayList<String> getQuesAns() {
		return this.quesAns;
	}	
}
