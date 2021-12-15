import javax.swing.*;
import java.awt.*;

public class QuizGui extends JFrame {

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


	QuizGui() {

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

		qSelectSubPanelTwo.add(qSelectStartButton);
		// spacing between 'start' and 'create new' buttons 
		qSelectSubPanelTwo.add(Box.createRigidArea(new Dimension(25, 0)));
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

		mainPanel.add(qSelectDetailPanel);

		previewScrollPane.setPreferredSize(new Dimension(200, 125));
		previewPanel.add(previewScrollPane);

		mainPanel.add(previewPanel);

		add(mainPanel);

		setSize(800, 480);
	}

	public void display() {
		setVisible(true);
	}
}
