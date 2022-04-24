import java.util.ArrayList;

public class QuizSet implements java.io.Serializable {
	
	private String name;
	private String fileName;
	private int qNum = 0;
	private String createdDate;

	private ArrayList<Question> qSet = new ArrayList<Question>();	

	private float lastGrade = 0;
	private ArrayList<Float> allGrades = new ArrayList<Float>();

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

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String n) {
		this.fileName = n;
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

	public ArrayList<Question> getAllQuestions() {
		return this.qSet;
	}

	public void addQuestion(Question q) {
		this.qSet.add(q);
	}

	public void saveQuestion(Question qIn) {
		for(int i=0; i<qSet.size(); i++) {
			if(qIn.getQId() == qSet.get(i).getQId()) {
				System.out.println("FOUND QUESTION!");
				qSet.set(i, qIn);
			}
		}
	}

	public void remQuestion(int qIndex) {
		this.qSet.remove(qIndex);
	}

	public Question getQuestion(int qIndex) {
		return this.qSet.get(qIndex);
	}

	public float getLastGrade() {
		return this.lastGrade;
	}

	public void addGrade(float g) {
		allGrades.add(g);
		this.lastGrade = g;
	}

	public float getAveGrade() {
		float aveGrade = 0;

		if(allGrades.size() > 0 ) {
			for(int i=0; i<this.allGrades.size(); i++) {
				aveGrade += allGrades.get(i);
			}

			aveGrade /= allGrades.size();

			return aveGrade;
		}

		return 0;
	}
}
