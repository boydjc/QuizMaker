import java.util.ArrayList;

public class QuizSet implements java.io.Serializable {
	
	private String name;
	private int qNum;
	private String createdDate;

	private ArrayList<Question> qSet;	

	private int lastGrade = 0;
	private ArrayList<Integer> allGrades = new ArrayList<Integer>();

	QuizSet(String nameIn, String createDateIn) {
		this.name = nameIn;
		this.createdDate = createDateIn;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	// gets the number of questions in the set
	public int getQNum() {
		return qSet.size();
	}

	// returns the formatted version of the created date
	public String getCreatedDate() {
		return this.createdDate;	
	}

	public void setCreatedDate(String createDateIn) {
		this.createdDate = createDateIn;
	}

	public void addQuestion(Question q) {
		this.qSet.add(q);
	}

	public Question getQuestion(int qIndex) {
		return this.qSet.get(qIndex);
	}

	public int getLastGrade() {
		return this.lastGrade;
	}

	public void setLastGrade(int g) {
		this.lastGrade = g;
	}

	public void addGrade() {
		allGrades.add(this.lastGrade);
	}

	public int getAveGrade() {
		int aveGrade = 0;

		for(int i=0; i<this.allGrades.size(); i++) {
			aveGrade += allGrades.get(i);
		}

		aveGrade /= allGrades.size();

		return aveGrade;
	}
}
