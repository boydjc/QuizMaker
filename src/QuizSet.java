import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuizSet implements java.io.Serializable {
	
	private String name;
	private int qNum;
	private LocalDateTime createdDate;

	private ArrayList<Question> qSet;

	private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private int lastGrade;
	private ArrayList<Integer> allGrades = new ArrayList<Integer>();

	QuizSet(String nameIn) {
		this.name = nameIn;
		this.setCreatedDate();
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
		String formattedDate = this.createdDate.format(this.dtFormat);
		return formattedDate;
	}

	public void setCreatedDate() {
		this.createdDate = LocalDateTime.now();
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
