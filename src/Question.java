import java.util.ArrayList;

public class Question implements java.io.Serializable {
	private int qId;
	private int qType;
	private String quesText;
	private ArrayList<String> choices = new ArrayList<String>();
	private ArrayList<String> answers = new ArrayList<String>();

	Question() {

	}

	Question(int qIdIn, int qTypeIn, String quesTxtIn, ArrayList<String> choicesIn, ArrayList<String> ansIn) {	
		this.qId = qIdIn;
		this.qType = qTypeIn;
		this.quesText = quesTxtIn;
		this.choices = choicesIn;
		this.answers = ansIn;
	}

	public void setQType(int qTypeIn) {
		this.qType = qTypeIn;
	}

	public int getQType() {
		return this.qType;
	}

	public int getQId() {
		return this.qId;
	}

	public void setQuesText(String quesTxtIn) {
		this.quesText = quesTxtIn;
	}

	public String getQuesText() {
		return this.quesText;
	}

	public void setChoices(ArrayList<String> choicesIn) {
		this.choices = choicesIn;
	}

	public ArrayList<String> getChoices() {
		return this.choices;
	}

	public void setAnswers(ArrayList<String> ansIn) {
		this.answers = ansIn;
	}

	public ArrayList<String> getAnswers() {
		return this.answers;
	}	
}
