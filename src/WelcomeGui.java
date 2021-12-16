import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class WelcomeGui extends JFrame implements ActionListener {

	private JPanel mainPanel = new JPanel();

	private JPanel qSelectDetailPanel = new JPanel();

	private JPanel qSelectPanel = new JPanel();

	private JPanel qSelectSubPanelOne = new JPanel(); // contains q bank table

	private String[] testTableColNames = {"Quiz Banks"};

	private Object[][] testTableData = {
		{"Apple"},
		{"Banana"},
		{"Grape"},
		{"Orange"}
	};

	private JTable qBankTable = new JTable(testTableData, testTableColNames);

	private JScrollPane qBankTablePane = new JScrollPane(qBankTable);

	private JPanel qSelectSubPanelTwo = new JPanel(); // contains q bank buttons

	private JButton qSelectStartButton = new JButton("Start");
	private JButton qSelectCreateButton = new JButton("Create New");

	private JPanel qDetailPanel = new JPanel();
	private JLabel qBankNameLabel = new JLabel("Name: Random Questions");
	private JLabel qBankCreatedLabel = new JLabel("Created: 2021-12-14 12:05:45");
	private JLabel qBankQNumLabel = new JLabel("Number of Questions: 20");
	private JLabel qBankLastGradeLabel = new JLabel("Last Grade: 95%");
	private JLabel qBankAveGradeLabel = new JLabel("Average Grade: 60%");

	private JPanel previewPanel = new JPanel();

	private String[] previewTestTableColNames = {"Preview"};

	private Object[][] previewTestTableData = {
		{"Who likes question?"},
		{"What is your favorite color?"},
		{"Who is the President of the United States?"},
		{"Where is Waldo?"},
		{"Coffee or Tea?"}
	};

	private JTable previewTable = new JTable(previewTestTableData, previewTestTableColNames);
	private JScrollPane previewScrollPane = new JScrollPane(previewTable);


	WelcomeGui() {

		// JFrame configuration
		super("QuizMaker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(60, 60);
		setLayout(new FlowLayout());
		
		// set layouts for panels
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		qSelectDetailPanel.setLayout(new BoxLayout(qSelectDetailPanel, BoxLayout.LINE_AXIS));

		qSelectPanel.setLayout(new BoxLayout(qSelectPanel, BoxLayout.PAGE_AXIS));
		qSelectSubPanelOne.setLayout(new BoxLayout(qSelectSubPanelOne, BoxLayout.PAGE_AXIS));
		qSelectSubPanelTwo.setLayout(new BoxLayout(qSelectSubPanelTwo, BoxLayout.LINE_AXIS));

		qDetailPanel.setLayout(new BoxLayout(qDetailPanel, BoxLayout.PAGE_AXIS));
		
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.PAGE_AXIS));

		// qSelectDetailPanel and subpanels

		// qSelectPanel
		qBankTablePane.setPreferredSize(new Dimension(200, 75));
		qSelectSubPanelOne.add(qBankTablePane);
		qSelectPanel.add(qSelectSubPanelOne);

		qSelectStartButton.addActionListener(this);
		qSelectSubPanelTwo.add(qSelectStartButton);
		// spacing between 'start' and 'create new' buttons 
		qSelectSubPanelTwo.add(Box.createRigidArea(new Dimension(25, 0)));

		qSelectCreateButton.addActionListener(this);
		qSelectSubPanelTwo.add(qSelectCreateButton);

		qSelectPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qSelectPanel.add(qSelectSubPanelTwo);

		// qDetailPanel
		// add a little bit of spacing between each of the labels
		qDetailPanel.add(qBankNameLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qDetailPanel.add(qBankCreatedLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qDetailPanel.add(qBankQNumLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qDetailPanel.add(qBankLastGradeLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qDetailPanel.add(qBankAveGradeLabel);

		qSelectDetailPanel.add(qSelectPanel);
		// spacing between the question bank panel and the details panel
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		qSelectDetailPanel.add(qDetailPanel);


		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

		previewScrollPane.setPreferredSize(new Dimension(175, 100));
		previewPanel.add(previewScrollPane);

		mainPanel.add(previewPanel);

		add(mainPanel);

		setSize(500, 310);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if(source instanceof JButton) {
			if(((JButton) source).getText().equals("Start")) {
				System.out.println("Start button clicked.");
			}else if(((JButton) source).getText().equals("Create New")) {
				String newSetName = JOptionPane.showInputDialog(this, "New Quiz Bank Name?", null);

				// create the new quiz set with the name
				QuizSet newQSet = new QuizSet(newSetName);

				// go ahead and save the set
				this.saveQuizSet(newQSet);
			}
		}
	}

	// serializes the QuizSet object and saves it 
	public void saveQuizSet(QuizSet qSet) {
		try {

			// make a filename out of the set name 
			// replace spaces with dash
			String fileName = qSet.getName().replace(' ', '-');

			FileOutputStream fileOut = new FileOutputStream("./data/" + fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(qSet);
			out.close();
			fileOut.close();
			System.out.println("Quiz Set Serialized successfully");
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void display() {
		setVisible(true);
	}
}
