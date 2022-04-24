import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public class QuizGui extends JFrame implements ActionListener, MouseListener, DocumentListener {

	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private String savedQSetPath = "./data/";

	private QuizEngine qEng = new QuizEngine();

	private ArrayList<QuizSet> savedQSets = new ArrayList<QuizSet>();

	// the index in savedQSets of the currently selected quiz set
	private int selectedSet = -1;

	// index of the currently selected question from the selected set
	private int selectedQuestion = -1;

	// if this variable is true then we aren't going to add the question as a 
	// new one but instead we will overwrite it in the currently selected quiz set
	private boolean editingQuestion = false;

	private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private JPanel containerPanel = new JPanel(); // used for the CardLayout

	// keeps track of what panel is currently being shown in the CardLayout
	// used to control what action is taken when a button is being pressed on the shown panel
	private String currentlyShownPanel = "main"; 

	// this holds the components dynamcially created for each quiz
	// we hold them in here so that they will retain their values 
	// (e.g. checked, clicked, words typed) after being removed and readded to the quiz choice panel
	private ArrayList<ArrayList<Component>> quizChoiceComponents;

	// a collection of button groups that we need for quiz JRadioButton components
	private ArrayList<ButtonGroup> quizButtonGroups;

	// START MAIN PANEL COMPONENTS

	private JPanel mainPanel = new JPanel();

	private JPanel qSelectDetailPanel = new JPanel();

	private JPanel qSelectPanel = new JPanel();

	private JPanel qSelectSubPanelOne = new JPanel(); // contains q bank table

	private JTable qBankTable;

	private JScrollPane qBankTablePane = new JScrollPane();

	private JPanel qSelectSubPanelTwo = new JPanel(); // contains q bank start edit and delete

	private JButton qSelectStartButton = new JButton("Start");
	private JButton qSelectEditButton = new JButton("Edit");
	private JButton qSelectDeleteButton = new JButton("Delete");
	
	private JPanel qSelectSubPanelThree = new JPanel(); // contains q bank create new button

	private JButton qSelectCreateButton = new JButton("Create New");


	private JPanel qDetailPanel = new JPanel();

	private Font detailLabelFont = new Font("Serif", Font.PLAIN, 17);

	private JLabel qBankNameLabel = new JLabel("Name: ");
	private JLabel qBankCreatedLabel = new JLabel("Created: ");
	private JLabel qBankQNumLabel = new JLabel("Number of Questions: ");
	private JLabel qBankLastGradeLabel = new JLabel("Last Grade: ");
	private JLabel qBankAveGradeLabel = new JLabel("Average Grade: ");

	private JPanel previewPanel = new JPanel();

	private JTable previewTable;
	private JScrollPane previewScrollPane = new JScrollPane();

	// END MAIN PANEL COMPONENTS

	// START EDIT PANEL COMPONENTS

	private JPanel editPanel = new JPanel();

	private JScrollPane editPanelTablePane = new JScrollPane();
		
	private JTable editPanelTable;

	private JPanel editPanelButtonPanel = new JPanel();

	private JButton editPanelAddButton = new JButton("Add");
	private JButton editPanelEditButton = new JButton("Edit");
	private JButton editPanelDeleteButton = new JButton("Delete");
	private JButton editPanelSaveButton = new JButton("Save");

	// END EDIT PANEL COMPONENTS

	// START NEW QUESTION PANEL COMPONENTS

	private JPanel newQuestionPaneView = new JPanel();

	private JScrollPane newQuestionPane = new JScrollPane(newQuestionPaneView);

	// Question type panel
	private JPanel newQuestionQTypeLabelPanel = new JPanel();
	private JLabel newQuestionQTypeLabel = new JLabel("Question Type");

	private JPanel newQuestionQTypeSelPanel = new JPanel();
	private JRadioButton multiChoiceOneAns = new JRadioButton();
	private JLabel multiChoiceOneAnsLabel = new JLabel("Multiple Choice");
	private JRadioButton multiChoiceMultiAns = new JRadioButton();
	private JLabel multiChoiceMultiAnsLabel = new JLabel("Many Answers");
	private JRadioButton fillInBlank = new JRadioButton();
	private JLabel fillInBlankLabel = new JLabel("Fill in the Blank");

	private ButtonGroup qTypeButtonGroup = new ButtonGroup();

	// Question text panel
	private JPanel newQuestionQTextLabelPanel = new JPanel();
	private JLabel newQuestionQTextLabel = new JLabel("Question Text");

	private JTextArea newQuestionQText = new JTextArea(10, 10);
	private JScrollPane newQuestionQTextPane = new JScrollPane(newQuestionQText);
	
	// Question choice panel
	private JPanel newQuestionQChoiceLabelPanel = new JPanel();
	private JLabel newQuestionQChoiceLabel = new JLabel("Question Choices");

	private JPanel newQuestionQChoicePanel = new JPanel();
	private JTextField newQuestionQChoice = new JTextField(20);

	private JPanel newQuestionQChoiceButtonPanel = new JPanel();
	private JButton newQuestionQChoiceAddButton = new JButton("Add");
	private JButton newQuestionQChoiceRemButton = new JButton("Remove");

	// Question answer panel
	private JPanel newQuestionQAnsLabelPanel = new JPanel();
	private JLabel newQuestionQAnsLabel = new JLabel("Question Answers");

	private JPanel newQuestionQAnsPanel = new JPanel();
	private JTextField newQuestionQAns = new JTextField(20);

	private JPanel newQuestionQAnsButtonPanel = new JPanel();
	private JButton newQuestionQAnsAddButton = new JButton("Add");
	private JButton newQuestionQAnsRemButton = new JButton("Remove");

	// Question Save button panel
	private JPanel newQuestionQSavePanel = new JPanel();
	private JButton newQuestionQSaveButton = new JButton("Save");
	private JButton newQuestionQExitButton = new JButton("Exit");

	// END NEW QUESTION PANEL COMPONENTS

	// START QUIZ PANEL COMPONENTS

	private JPanel quizPanel = new JPanel();

	private JPanel quizLabelPanel = new JPanel();
	private JLabel quizLabel = new JLabel("Test Quiz");

	private JPanel quizQuestionLabelPanel = new JPanel();

	private JPanel quizQuestionPanel = new JPanel();
	private JLabel quizQuestionLabel = new JLabel("Question 1");
	private JScrollPane quizQuestionScrollPane = new JScrollPane(quizQuestionPanel);
	private JLabel quizQuestionText = new JLabel();

	private JPanel quizChoicePanel = new JPanel();
	private JScrollPane quizChoiceScrollPane = new JScrollPane(quizChoicePanel);

	private JPanel quizPrevNextButtonPanel = new JPanel();
	private JButton quizExitButton = new JButton("Exit");
	private JButton quizPrevButton = new JButton("Previous");
	private JButton quizNextButton = new JButton("Next");

	// END QUIZ PANEL COMPONENTS

	// START QUIZ RESULT COMPONENTS
		
	private JPanel quizResultPanel = new JPanel();

	private JPanel quizResultQuizLabelPanel = new JPanel();
	private JLabel quizResultQuizLabel = new JLabel("Test Quiz");

	private JPanel quizResultLabelPanel = new JPanel();
	private JLabel quizResultLabel = new JLabel("Quiz Results");

	private JPanel quizResultScoreLabelPanel = new JPanel();
	private JLabel quizResultScoreLabel = new JLabel("Score: ");
	private JLabel quizResultAveScoreLabel = new JLabel("Average Score: ");

	private JPanel quizResultMissedQuestionLabelPanel = new JPanel();
	private JLabel quizResultMissedQuestionLabel = new JLabel("Missed Questions");
	private JPanel quizResultMissedQuestionPanel = new JPanel();
	private JScrollPane quizResultMissedQuestionScrollPane = new JScrollPane(quizResultMissedQuestionPanel);

	private JPanel quizResultButtonPanel = new JPanel();
	private JButton restartButton = new JButton("Restart");
	private JButton mainMenuButton = new JButton("Main Menu");
	
	// END QUIZ RESULT COMPONENTS

	QuizGui() {

		// JFrame configuration
		super("QuizMaker");

		// set the look and feel
		try {
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
				}
			}
		}catch (Exception ex) {
			// go with default
		}

		// settings for the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(60, 60);
		setLayout(new FlowLayout());

		containerPanel.setLayout(new CardLayout());


		// START MAIN PANEL CONFIGURATION

		// create table for Quiz Banks
		this.createTable("set");

		qBankTable.addMouseListener(this);
	
		// set layouts for panels
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		qSelectDetailPanel.setLayout(new BoxLayout(qSelectDetailPanel, BoxLayout.LINE_AXIS));

		qSelectPanel.setLayout(new BoxLayout(qSelectPanel, BoxLayout.PAGE_AXIS));
		qSelectSubPanelOne.setLayout(new BoxLayout(qSelectSubPanelOne, BoxLayout.PAGE_AXIS));
		qSelectSubPanelTwo.setLayout(new BoxLayout(qSelectSubPanelTwo, BoxLayout.LINE_AXIS));
		qSelectSubPanelThree.setLayout(new BoxLayout(qSelectSubPanelThree, BoxLayout.LINE_AXIS));

		qDetailPanel.setLayout(new BoxLayout(qDetailPanel, BoxLayout.PAGE_AXIS));
		
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.PAGE_AXIS));

		// qSelectDetailPanel and subpanels

		// qSelectPanel
		qBankTablePane.setPreferredSize(new Dimension(300, 175));
		qBankTablePane.setMaximumSize(new Dimension(300, 175));
		qSelectSubPanelOne.add(qBankTablePane);
		qSelectPanel.add(qSelectSubPanelOne);

		qSelectStartButton.addActionListener(this);
		qSelectSubPanelTwo.add(qSelectStartButton);
		qSelectSubPanelTwo.add(Box.createRigidArea(new Dimension(5, 0)));

		qSelectEditButton.addActionListener(this);
		qSelectSubPanelTwo.add(qSelectEditButton);
		qSelectSubPanelTwo.add(Box.createRigidArea(new Dimension(5, 0)));

		qSelectDeleteButton.addActionListener(this);
		qSelectSubPanelTwo.add(qSelectDeleteButton);

		qSelectPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		qSelectPanel.add(qSelectSubPanelTwo);

		qSelectCreateButton.addActionListener(this);
		qSelectSubPanelThree.add(qSelectCreateButton);
		qSelectPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		qSelectPanel.add(qSelectSubPanelThree);

		// qDetailPanel
		// add a little bit of spacing between each of the labels	
		

		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		qBankNameLabel.setFont(detailLabelFont);
		qDetailPanel.add(qBankNameLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		qBankCreatedLabel.setFont(detailLabelFont);
		qDetailPanel.add(qBankCreatedLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		qBankQNumLabel.setFont(detailLabelFont);
		qDetailPanel.add(qBankQNumLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		qBankLastGradeLabel.setFont(detailLabelFont);
		qDetailPanel.add(qBankLastGradeLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		qBankAveGradeLabel.setFont(detailLabelFont);
		qDetailPanel.add(qBankAveGradeLabel);
		qDetailPanel.add(Box.createVerticalGlue());
		
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		qSelectDetailPanel.add(qSelectPanel);
		// spacing between the question bank panel and the details panel
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		qSelectDetailPanel.add(new JSeparator(SwingConstants.VERTICAL));
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		qSelectDetailPanel.add(qDetailPanel);
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(150, 0)));


		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		previewScrollPane.setPreferredSize(new Dimension(175, 150));
		previewPanel.add(previewScrollPane);

		mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		mainPanel.add(previewPanel);

		// END MAIN PANEL CONFIGURATION

		// START EDIT PANEL CONFIGURATION

		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.PAGE_AXIS));
		editPanelButtonPanel.setLayout(new BoxLayout(editPanelButtonPanel, BoxLayout.LINE_AXIS));

		editPanel.add(editPanelTablePane);
		
		// adding the buttons to the edit panel
		editPanelAddButton.addActionListener(this);
		editPanelButtonPanel.add(editPanelAddButton);

		editPanelButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		editPanelEditButton.addActionListener(this);
		editPanelButtonPanel.add(editPanelEditButton);

		editPanelButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		editPanelDeleteButton.addActionListener(this);
		editPanelButtonPanel.add(editPanelDeleteButton);

		editPanelButtonPanel.add(Box.createRigidArea(new Dimension(180, 0)));

		editPanelSaveButton.addActionListener(this);
		editPanelButtonPanel.add(editPanelSaveButton);
		editPanel.add(Box.createRigidArea(new Dimension(0, 15))); // some spacing between table and buttons
		editPanel.add(editPanelButtonPanel);

		// END EDIT PANEL CONFIGURATION

		// START NEW QUESTION PANEL CONFIGURATION

		// set layouts

		newQuestionQTypeLabelPanel.setLayout(new BoxLayout(newQuestionQTypeLabelPanel,
											 BoxLayout.LINE_AXIS));
		
		newQuestionQTypeSelPanel.setLayout(new BoxLayout(newQuestionQTypeSelPanel,
											BoxLayout.LINE_AXIS));

		newQuestionQTextLabelPanel.setLayout(new BoxLayout(newQuestionQTextLabelPanel,
											 BoxLayout.LINE_AXIS));

		newQuestionQChoiceLabelPanel.setLayout(new BoxLayout(newQuestionQChoiceLabelPanel,
												BoxLayout.LINE_AXIS));

		newQuestionQChoicePanel.setLayout(new BoxLayout(newQuestionQChoicePanel,
											BoxLayout.PAGE_AXIS));

		newQuestionQChoiceButtonPanel.setLayout(new BoxLayout(newQuestionQChoiceButtonPanel,
												BoxLayout.LINE_AXIS));

		newQuestionQAnsLabelPanel.setLayout(new BoxLayout(newQuestionQAnsLabelPanel,
												BoxLayout.LINE_AXIS));

		newQuestionQAnsPanel.setLayout(new BoxLayout(newQuestionQAnsPanel,
											BoxLayout.PAGE_AXIS));

		newQuestionQAnsButtonPanel.setLayout(new BoxLayout(newQuestionQAnsButtonPanel,
												BoxLayout.LINE_AXIS));

		newQuestionQSavePanel.setLayout(new BoxLayout(newQuestionQSavePanel,
										BoxLayout.LINE_AXIS));

		newQuestionPaneView.setLayout(new BoxLayout(newQuestionPaneView, BoxLayout.PAGE_AXIS));

		// add components

		newQuestionQTypeLabel.setFont(detailLabelFont);
		newQuestionQTypeLabelPanel.add(newQuestionQTypeLabel);

		multiChoiceOneAns.setName("Multiple Choice");
		multiChoiceOneAns.addActionListener(this);
		qTypeButtonGroup.add(multiChoiceOneAns);

		multiChoiceMultiAns.setName("Many Answers");
		multiChoiceMultiAns.addActionListener(this);
		qTypeButtonGroup.add(multiChoiceMultiAns);

		fillInBlank.setName("Fill in the blank");
		fillInBlank.addActionListener(this);
		qTypeButtonGroup.add(fillInBlank);

		multiChoiceOneAns.setSelected(true);
		newQuestionQTypeSelPanel.add(multiChoiceOneAns);
		newQuestionQTypeSelPanel.add(multiChoiceOneAnsLabel);
		newQuestionQTypeSelPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		newQuestionQTypeSelPanel.add(multiChoiceMultiAns);
		newQuestionQTypeSelPanel.add(multiChoiceMultiAnsLabel);
		newQuestionQTypeSelPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		newQuestionQTypeSelPanel.add(fillInBlank);
		newQuestionQTypeSelPanel.add(fillInBlankLabel);

		newQuestionQTextLabel.setFont(detailLabelFont);
		newQuestionQTextLabelPanel.add(newQuestionQTextLabel);

		newQuestionQText.setLineWrap(true);
		newQuestionQText.setWrapStyleWord(true);

		newQuestionQChoiceLabel.setFont(detailLabelFont);
		newQuestionQChoiceLabelPanel.add(newQuestionQChoiceLabel);

		newQuestionQChoicePanel.add(newQuestionQChoice);

		newQuestionQChoiceButtonPanel.add(Box.createRigidArea(new Dimension(300, 0)));

		// we're setting the name on these so that we can target the right one in the 
		// action listener when they are clicked
		newQuestionQChoiceAddButton.setName("Choice Add");
		newQuestionQChoiceAddButton.addActionListener(this);
		newQuestionQChoiceButtonPanel.add(newQuestionQChoiceAddButton);
		newQuestionQChoiceButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		newQuestionQChoiceRemButton.setName("Choice Remove");
		newQuestionQChoiceRemButton.addActionListener(this);
		newQuestionQChoiceRemButton.setEnabled(false);
		newQuestionQChoiceButtonPanel.add(newQuestionQChoiceRemButton);

		newQuestionQAnsLabel.setFont(detailLabelFont);
		newQuestionQAnsLabelPanel.add(newQuestionQAnsLabel);

		newQuestionQAnsPanel.add(newQuestionQAns);

		newQuestionQAnsButtonPanel.add(Box.createRigidArea(new Dimension(300, 0)));
		newQuestionQAnsAddButton.setName("Answer Add");
		newQuestionQAnsAddButton.addActionListener(this);
		newQuestionQAnsAddButton.setEnabled(false);
		newQuestionQAnsButtonPanel.add(newQuestionQAnsAddButton);
		newQuestionQAnsButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		newQuestionQAnsRemButton.setName("Answer Remove");
		newQuestionQAnsRemButton.addActionListener(this);
		newQuestionQAnsRemButton.setEnabled(false);
		newQuestionQAnsButtonPanel.add(newQuestionQAnsRemButton);

		newQuestionQSaveButton.addActionListener(this);
		newQuestionQSavePanel.add(newQuestionQSaveButton);
		newQuestionQSavePanel.add(Box.createRigidArea(new Dimension(10, 0)));

		newQuestionQExitButton.addActionListener(this);
		newQuestionQSavePanel.add(newQuestionQExitButton);
		newQuestionQSavePanel.add(Box.createRigidArea(new Dimension(300, 0)));

		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 10)));
		newQuestionPaneView.add(newQuestionQTypeLabelPanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 10)));
		newQuestionPaneView.add(newQuestionQTypeSelPanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 20)));
		newQuestionPaneView.add(newQuestionQTextLabelPanel);
		newQuestionPaneView.add(newQuestionQTextPane);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 20)));
		newQuestionPaneView.add(newQuestionQChoiceLabelPanel);
		newQuestionPaneView.add(newQuestionQChoicePanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 10)));
		newQuestionPaneView.add(newQuestionQChoiceButtonPanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 20)));
		newQuestionPaneView.add(newQuestionQAnsLabelPanel);
		newQuestionPaneView.add(newQuestionQAnsPanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 10)));
		newQuestionPaneView.add(newQuestionQAnsButtonPanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 20)));
		newQuestionPaneView.add(newQuestionQSavePanel);
		newQuestionPaneView.add(Box.createRigidArea(new Dimension(0, 20)));

		// END NEW QUESTION PANEL CONFIGURATION

		// START QUIZ PANEL CONFIGURATION 
		
		// set layouts
		quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.PAGE_AXIS));
		quizLabelPanel.setLayout(new BoxLayout(quizLabelPanel, BoxLayout.LINE_AXIS));
		quizQuestionLabelPanel.setLayout(new BoxLayout(quizQuestionLabelPanel, BoxLayout.LINE_AXIS));
		quizQuestionPanel.setLayout(new BoxLayout(quizQuestionPanel, BoxLayout.LINE_AXIS));
		quizChoicePanel.setLayout(new GridLayout(6, 2));
		((GridLayout) quizChoicePanel.getLayout()).setVgap(10);
		quizPrevNextButtonPanel.setLayout(new BoxLayout(quizPrevNextButtonPanel, BoxLayout.LINE_AXIS));

		// adding components
		quizLabel.setFont(new Font("Serif", Font.BOLD, 21));
		quizLabelPanel.add(quizLabel);

		quizQuestionLabel.setFont(new Font("Serif", Font.BOLD, 17));
		quizQuestionLabelPanel.add(quizQuestionLabel);
		quizQuestionLabelPanel.add(Box.createRigidArea(new Dimension(400, 0)));
		quizQuestionText.setFont(new Font("Serif", Font.BOLD, 15));
		quizQuestionPanel.add(quizQuestionText);

		quizExitButton.addActionListener(this);
		quizPrevNextButtonPanel.add(quizExitButton);
		quizPrevNextButtonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
		quizPrevButton.addActionListener(this);
		quizPrevNextButtonPanel.add(quizPrevButton);
		quizPrevNextButtonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		quizNextButton.addActionListener(this);
		quizPrevNextButtonPanel.add(quizNextButton);

		quizPanel.add(quizLabelPanel);
		quizPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		quizPanel.add(quizQuestionLabelPanel);
		quizQuestionPanel.setPreferredSize(new Dimension(450, 100));
		quizQuestionPanel.setMaximumSize(new Dimension(450, 30));
		quizQuestionScrollPane.setPreferredSize(new Dimension(500, 500));
		quizQuestionScrollPane.setMaximumSize(new Dimension(500, 500));
		quizQuestionScrollPane.setBorder(BorderFactory.createEmptyBorder());
		quizPanel.add(quizQuestionScrollPane);
		quizPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		quizChoicePanel.setPreferredSize(new Dimension(450, 100));
		quizChoicePanel.setMaximumSize(new Dimension(450, 100));
		quizChoiceScrollPane.setPreferredSize(new Dimension(500, 500));
		quizChoiceScrollPane.setMaximumSize(new Dimension(500, 500));
		quizPanel.add(quizChoiceScrollPane);
		quizPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		quizPanel.add(quizPrevNextButtonPanel);
		

		// END QUIZ PANEL CONFIGURATION

		// START QUIZ RESULT PANEL CONFIGURATION

		// set layouts
		quizResultPanel.setLayout(new BoxLayout(quizResultPanel, BoxLayout.PAGE_AXIS));
		quizResultQuizLabelPanel.setLayout(new BoxLayout(quizResultQuizLabelPanel, BoxLayout.LINE_AXIS));
		quizResultLabelPanel.setLayout(new BoxLayout(quizResultLabelPanel, BoxLayout.LINE_AXIS));
		quizResultScoreLabelPanel.setLayout(new BoxLayout(quizResultScoreLabelPanel, BoxLayout.LINE_AXIS));
		quizResultMissedQuestionLabelPanel.setLayout(new BoxLayout(quizResultMissedQuestionLabelPanel, BoxLayout.LINE_AXIS));
		quizResultMissedQuestionPanel.setLayout(new BoxLayout(quizResultMissedQuestionPanel, BoxLayout.PAGE_AXIS));
		quizResultButtonPanel.setLayout(new BoxLayout(quizResultButtonPanel, BoxLayout.LINE_AXIS));

		quizResultQuizLabel.setFont(new Font("Serif", Font.BOLD, 21));
		quizResultQuizLabelPanel.add(quizResultQuizLabel);

		quizResultLabel.setFont(new Font("Serif", Font.BOLD, 18));
		quizResultLabelPanel.add(quizResultLabel);
		quizResultLabelPanel.add(Box.createRigidArea(new Dimension(400, 0)));
		quizResultScoreLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		quizResultScoreLabelPanel.add(quizResultScoreLabel);
		quizResultScoreLabelPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		quizResultAveScoreLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		quizResultScoreLabelPanel.add(quizResultAveScoreLabel);

		quizResultMissedQuestionLabel.setFont(new Font("Serif", Font.BOLD, 18));
		quizResultMissedQuestionLabelPanel.add(quizResultMissedQuestionLabel);
		quizResultMissedQuestionLabelPanel.add(Box.createRigidArea(new Dimension(400, 0)));

		restartButton.addActionListener(this);
		quizResultButtonPanel.add(restartButton);
		quizResultButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		mainMenuButton.addActionListener(this);
		quizResultButtonPanel.add(mainMenuButton);
		
		quizResultPanel.add(quizResultQuizLabelPanel);
		quizResultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		quizResultPanel.add(quizResultLabelPanel);
		quizResultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		quizResultPanel.add(quizResultScoreLabelPanel);
		quizResultPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		quizResultPanel.add(quizResultMissedQuestionLabelPanel);

		quizResultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		quizResultPanel.add(quizResultMissedQuestionScrollPane);
		quizResultPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		quizResultPanel.add(quizResultButtonPanel);
		
		

		// END QUIZ RESULT PANEL CONFIGURATION

		mainPanel.setPreferredSize(new Dimension(600, 500));
		mainPanel.setMaximumSize(new Dimension(600, 500));
		containerPanel.add(mainPanel, "main");

		editPanel.setPreferredSize(new Dimension(600, 500));
		editPanel.setMaximumSize(new Dimension(600, 500));
		containerPanel.add(editPanel, "edit");

		newQuestionPane.setPreferredSize(new Dimension(600, 500));
		newQuestionPane.setMaximumSize(new Dimension(600, 500));
		containerPanel.add(newQuestionPane, "newQuestion");

		quizPanel.setPreferredSize(new Dimension(600, 500));
		quizPanel.setMaximumSize(new Dimension(600, 500));
		containerPanel.add(quizPanel, "quiz");

		quizResultPanel.setPreferredSize(new Dimension(600, 500));
		quizResultPanel.setMaximumSize(new Dimension(600, 500));
		containerPanel.add(quizResultPanel, "results");
		
		add(containerPanel);

		setSize(650, 575);
	}

	// action listeners

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// process JButton events
		if(source instanceof JButton) {

			if(currentlyShownPanel.equals("main")) {

				if(((JButton) source).getText().equals("Start")) {

					if(selectedSet != -1 && savedQSets.get(selectedSet).getAllQuestions().size() != 0) {
						// since we are starting at the beginning of the quiz, there
						// is no reason for the "previous" button to be enabled

						quizPrevButton.setEnabled(false);

						qEng.setQuestionSet(savedQSets.get(selectedSet).getAllQuestions());

						qEng.generateQuiz();

						quizLabel.setText(savedQSets.get(selectedSet).getName());

						// create the components to add and remove
						createQuizComponents();
						
						configureQuizComponents();

						CardLayout cl = (CardLayout) containerPanel.getLayout();

						// switch to the quiz panel
						currentlyShownPanel = "quiz";

						cl.show(containerPanel, currentlyShownPanel);

						setTitle(savedQSets.get(selectedSet).getName() + " Quiz");
					}else if(selectedSet == -1) {
						JOptionPane.showMessageDialog(this, "You must select a quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}else if(savedQSets.get(selectedSet).getAllQuestions().size() == 0) {
						JOptionPane.showMessageDialog(this, "You selected an empty quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}

				}else if(((JButton) source).getText().equals("Edit")) {

					if(selectedSet != -1) {
						CardLayout cl = (CardLayout) containerPanel.getLayout();

						// switch to the edit panel
						currentlyShownPanel = "edit";
						cl.show(containerPanel, currentlyShownPanel);

						// change the title for the newly shown panel
						setTitle(savedQSets.get(selectedSet).getName() + " (Editing)");

						// draw the edit table
						createTable("edit");
					}else{
						JOptionPane.showMessageDialog(this, "You must select a quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}

				}else if(((JButton) source).getText().equals("Delete")) {

					if(selectedSet != -1) {

						int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this set?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

						if(n == 0) {
							File fileObj = new File(savedQSetPath + savedQSets.get(selectedSet).getFileName());
				
							if(fileObj.delete()) {
								JOptionPane.showMessageDialog(this, "File Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
								// set the selected set back to null so the user can't click the edit/delete on a set that doesn't exist
								selectedSet = -1;
							}else {
								JOptionPane.showMessageDialog(this, "Unable to delete file", "ERROR", JOptionPane.ERROR_MESSAGE);
							}
						}

						// recreate the table
						this.createTable("set");

						// clear the detail labels
						qBankNameLabel.setText("Name: ");
						qBankCreatedLabel.setText("Created: ");
						qBankQNumLabel.setText("Number of Questions: ");
						qBankLastGradeLabel.setText("Last Grade: ");
						qBankAveGradeLabel.setText("Average Grade: ");

					}else {
						JOptionPane.showMessageDialog(this, "You must select a quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}

				}else if(((JButton) source).getText().equals("Create New")) {
					String newSetName = null;

					newSetName = JOptionPane.showInputDialog(this, "New Quiz Bank Name?", null);

					if(newSetName != null) {
						if(!(newSetName.equals(""))) {

							String createdDate = LocalDateTime.now().format(dtFormat);
			
							// create the new quiz set with the name
							QuizSet newQSet = new QuizSet(newSetName, createdDate);

							// go ahead and save the set
							this.saveQuizSet(newQSet);

							// recreate the table with the new created set
							this.createTable("set");
						}else if(newSetName.equals("")) {
							JOptionPane.showMessageDialog(this, "Your quiz set must have a name.", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}else if(currentlyShownPanel.equals("edit")) {
				if(((JButton) source).getText().equals("Add")) {

					// clear any text that might be in the new question components from 
					// a previously added question

					newQuestionQText.setText("");
					
					Component[] newQuestionQChoiceComps = newQuestionQChoicePanel.getComponents();

					for(Component comp : newQuestionQChoiceComps) {
						if(comp instanceof JTextField) {
							((JTextField) comp).setText("");
						}
					}

					Component[] newQuestionQAnsComps = newQuestionQAnsPanel.getComponents();

					for(Component comp : newQuestionQAnsComps) {
						if(comp instanceof JTextField) {
							((JTextField) comp).setText("");
						}
					}

					CardLayout cl = (CardLayout) containerPanel.getLayout();
					currentlyShownPanel = "newQuestion";
					cl.show(containerPanel, currentlyShownPanel);
					setTitle(savedQSets.get(selectedSet).getName() + " New Question");

				}else if(((JButton) source).getText().equals("Edit")) {
					
					if(selectedQuestion != -1) {
						editingQuestion = true;

						int qType = savedQSets.get(selectedSet).getQuestion(selectedQuestion).getQType();

						Component[] radioComps = newQuestionQTypeSelPanel.getComponents();

						switch(qType) {
							case 1:
								((JRadioButton) radioComps[0]).setSelected(true);
								break;
							case 2:
								((JRadioButton) radioComps[3]).setSelected(true);
								break;
							case 3:
								((JRadioButton) radioComps[6]).setSelected(true);
								break;
							default:
								break;
						}

						newQuestionQText.setText(savedQSets.get(selectedSet).getQuestion(selectedQuestion).getQuesText());

						// get the number of choice fields currently on the edit panel
						// we do this because there is a chance the user added a question with multiple 
						// choices and then later goes back to edit it
						Component[] choiceFields = newQuestionQChoicePanel.getComponents();

						// the actual number of question choices we have for the selected question
						ArrayList<String> quesChoice = savedQSets.get(selectedSet).getQuestion(selectedQuestion).getChoices();

						// remove the components that we have right now
						while(choiceFields.length != 0) {
							if(choiceFields.length == 0) {
								break;
							}else {
								newQuestionQChoicePanel.remove(choiceFields[choiceFields.length-1]);
								choiceFields = newQuestionQChoicePanel.getComponents();
							}
						}

						// now that we have a clean empty panel, add the correct number of choice textfields
						for(int i=0; i<quesChoice.size(); i++) {
							newQuestionQChoicePanel.add(Box.createRigidArea(new Dimension(0, 5)));
							newQuestionQChoicePanel.add(new JTextField(20));
							newQuestionPane.validate();
						}

						// add the text from the question choice to the correct component
						choiceFields = newQuestionQChoicePanel.getComponents();

						int quesChoiceCount = 0;
						for(int i=0; i<choiceFields.length; i++) {
							if(choiceFields[i] instanceof JTextField) {
								((JTextField) choiceFields[i]).setText(quesChoice.get((quesChoiceCount)));
								quesChoiceCount++;
							}
						}

						// if we have more than one choice then make sure the remove button is enabled
						if(quesChoice.size() > 1) {
							newQuestionQChoiceRemButton.setEnabled(true);
						}


						// get the number of answer fields currently on the edit panel
						// we do this because there is a chance the user added a question with multiple 
						// answer and then later goes back to edit it
						Component[] answerFields = newQuestionQAnsPanel.getComponents();

						// the actual number of question answer we have for the selected question
						ArrayList<String> quesAnswer = savedQSets.get(selectedSet).getQuestion(selectedQuestion).getAnswers();

						// remove the components what we have right now
						while(answerFields.length != 0) {
							if(answerFields.length == 0) {
								break;
							}else {
								newQuestionQAnsPanel.remove(answerFields[answerFields.length-1]);
								answerFields = newQuestionQAnsPanel.getComponents();
							}
						}

						// now that we have a clean empty panel, add the correct number of answer textfields
						for(int i=0; i<quesAnswer.size(); i++) {
							newQuestionQAnsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
							newQuestionQAnsPanel.add(new JTextField(20));
							newQuestionPane.validate();
						}

						answerFields = newQuestionQAnsPanel.getComponents();

						int quesAnsCount = 0;
						for(int i=0; i<answerFields.length; i++) {
							if(answerFields[i] instanceof JTextField) {
								((JTextField) answerFields[i]).setText(quesAnswer.get((quesAnsCount)));
								quesAnsCount++;
							}
						}

						// if we have more than one answer then make sure the remove button is enabled
						if(quesAnswer.size() > 1) {
							newQuestionQAnsRemButton.setEnabled(true);
						}
										
						CardLayout cl = (CardLayout) containerPanel.getLayout();
						currentlyShownPanel = "newQuestion";
						cl.show(containerPanel, currentlyShownPanel);
						setTitle(savedQSets.get(selectedSet).getName() + " Edit Question");

					}else {
						JOptionPane.showMessageDialog(this, "You do not have a question selected", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}else if(((JButton) source).getText().equals("Delete")) {	

					if(selectedQuestion != -1) {
						savedQSets.get(selectedSet).remQuestion(selectedQuestion);
						saveQuizSet(savedQSets.get(selectedSet));
						loadAllQuizSets();
						selectedQuestion = -1;
						setQSetLabels();
						createTable("edit");
					}


				}else if(((JButton) source).getText().equals("Save")) {

					// save the quiz set
					saveQuizSet(savedQSets.get(selectedSet));

					CardLayout cl = (CardLayout) containerPanel.getLayout();
					currentlyShownPanel = "main";
					cl.show(containerPanel, currentlyShownPanel);
					setTitle("QuizMaker");
				}	
			}else if(currentlyShownPanel.equals("newQuestion")) {

				if(((JButton) source).getText().equals("Add")){

					if(((JButton) source).getName().equals("Choice Add")) {
						
						// adding a new choice text field if there is at least one already existing
						Component[] newQuesQChoicePanComponents = newQuestionQChoicePanel.getComponents();

						newQuestionQChoicePanel.add(Box.createRigidArea(new Dimension(0, 5)));
						newQuestionQChoicePanel.add(new JTextField(20));
						newQuestionPane.validate();

						if(!(newQuestionQChoiceRemButton.isEnabled())) {
							newQuestionQChoiceRemButton.setEnabled(true);
						}

					}else if(((JButton) source).getName().equals("Answer Add")) {

						Component[] newQuesQAnsPanComponents = newQuestionQAnsPanel.getComponents();

						newQuestionQAnsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
						newQuestionQAnsPanel.add(new JTextField(20));
						newQuestionPane.validate();

						if(!(newQuestionQAnsRemButton.isEnabled())) {
							newQuestionQAnsRemButton.setEnabled(true);
						}


					}

				}else if(((JButton) source).getText().equals("Remove")) {

					if(((JButton) source).getName().equals("Choice Remove")) {
						
						Component[] newQuesQChoicePanComponents = newQuestionQChoicePanel.getComponents();

						// make sure we have at least 1 choice avaiable
						if(newQuesQChoicePanComponents.length > 1) {
							// remove the most recent two components
							for(int i=0; i<2; i++) {
								newQuestionQChoicePanel.remove(newQuesQChoicePanComponents[newQuesQChoicePanComponents.length-1]);
								// refresh component list
								newQuesQChoicePanComponents = newQuestionQChoicePanel.getComponents();
								newQuestionPane.validate();
							}
						};

						if(newQuesQChoicePanComponents.length == 1) {
							newQuestionQChoiceRemButton.setEnabled(false);
						}

					}else if(((JButton) source).getName().equals("Answer Remove")) {

						Component[] newQuesQAnsPanComponents = newQuestionQAnsPanel.getComponents();

						// make sure we have at least 1 choice avaiable
						if(newQuesQAnsPanComponents.length > 1) {
							// remove the most recent two components
							for(int i=0; i<2; i++) {
								newQuestionQAnsPanel.remove(newQuesQAnsPanComponents[newQuesQAnsPanComponents.length-1]);
								// refresh component list
								newQuesQAnsPanComponents = newQuestionQAnsPanel.getComponents();
								newQuestionPane.validate();
							}
						};

						if(newQuesQAnsPanComponents.length == 1) {
							newQuestionQAnsRemButton.setEnabled(false);
						}
					}

				}else if(((JButton) source).getText().equals("Save")) {
			

					// only add the question if this is true once we are done checking everything
					boolean blankQuestion = false;
					boolean blankChoice = false;
					boolean blankAnswer = false;
					boolean choiceMatch = false;
					
					// get all of the values to make a new question

					// get which radio button is selected

					String radioSelText = null;
					int qType = 0;

					Component[] newQuestionQTypeSelComps = newQuestionQTypeSelPanel.getComponents();

					for(int i=0; i<newQuestionQTypeSelComps.length; i++) {
						if(newQuestionQTypeSelComps[i] instanceof JRadioButton) {
							if(((JRadioButton) newQuestionQTypeSelComps[i]).isSelected()) {
								// get the next component over which will be the label for the radio button
								radioSelText = ((JLabel) newQuestionQTypeSelComps[i+1]).getText();
								if(radioSelText.equals("Multiple Choice")) {
									qType = 1;
								}else if(radioSelText.equals("Many Answers")) {
									qType = 2;
								}else if(radioSelText.equals("Fill in the Blank")) {
									qType = 3;
								}
							}
						}
					}

					// get question text and make sure that it is not blank

					String questionText = newQuestionQText.getText();

					if(questionText.trim().length() == 0) {
						JOptionPane.showMessageDialog(this, "Question text must contain at least one alphaneumeric character.", "ERROR", JOptionPane.ERROR_MESSAGE);
						questionText = null;
						blankQuestion = true;
					}else {
						blankQuestion = false;
					}

					// get question choices and check for empty fields

					ArrayList<String> questionChoices = new ArrayList<String>();

					Component[] newQuestionQChoiceComps = newQuestionQChoicePanel.getComponents();

					for(Component comp : newQuestionQChoiceComps) {

						if(comp instanceof JTextField) {
							if(((JTextField) comp).getText().trim().length() == 0) {
								JOptionPane.showMessageDialog(this, "One of your choices is blank or has no alphaneumeric characters", "ERROR", JOptionPane.ERROR_MESSAGE);	
								blankChoice = true;
								break;
							}else {
								questionChoices.add(((JTextField) comp).getText());
								blankChoice = false;
							}
						}
					}

					// get question answers and check for empty fields

					ArrayList<String> questionAnswers = new ArrayList<String>();

					Component[] newQuestionQAnsComps = newQuestionQAnsPanel.getComponents();

					for(Component comp : newQuestionQAnsComps) {

						if(comp instanceof JTextField) {
							if(((JTextField) comp).getText().trim().length() == 0) {
								JOptionPane.showMessageDialog(this, "One of your answers is blank or has no alphaneumeric characters", "ERROR", JOptionPane.ERROR_MESSAGE);	
								blankAnswer = true;
								break;
							}else {
								questionAnswers.add(((JTextField) comp).getText());
								blankAnswer = false;
							}
						}
					}

					// go through the answers and make sure that they match up with the choices
					// what is trying to be prevented here is making sure that the user doesn't get 
					// the wrong answer for something like a '.' in the choice when it is not in the answer
					
					if(!(qType == 3)) {
						for(Component ansComp : newQuestionQAnsComps) {

							// get the choice string values
							ArrayList<String> choiceString = new ArrayList<String>();

							for(Component choiceComp : newQuestionQChoiceComps) {
								if(choiceComp instanceof JTextField) {
									choiceString.add(((JTextField) choiceComp).getText());
								}
							}
						
							if(ansComp instanceof JTextField) {
								if(choiceString.contains(((JTextField) ansComp).getText())) {
									choiceMatch = true;
								}else {
									choiceMatch = false;
									JOptionPane.showMessageDialog(this, "One of your answers does not match one of your choices. Choices and answers are case sensitive.",
																  "ERROR", JOptionPane.ERROR_MESSAGE);		 
									break;
								}
							}
						}
					}
					

					if(!(blankQuestion) && !(blankChoice) && !(blankAnswer) && choiceMatch) {

						// the ID for the new question will be the number question that it is in the set
						
						int newQuesId;
						
						if(editingQuestion) {
							newQuesId = savedQSets.get(selectedSet).getQuestion(selectedQuestion).getQId();
						}else {
							newQuesId = (savedQSets.get(selectedSet).getAllQuestions().size())+1;
						}

						// make a new question
						Question newQuestion = new Question(newQuesId, qType, questionText, questionChoices, questionAnswers);

						if(editingQuestion) {
							// if we are editing then just overwrite the question
							savedQSets.get(selectedSet).saveQuestion(newQuestion);
						}else {
							// add this question to the set
							savedQSets.get(selectedSet).addQuestion(newQuestion);
						}

						// we are actually going to secretly save the quiz set here, reload it, and recreate both
						// the edit panel question list table and the quiz set table back on the main screen 
						// so that we can display the updated question list back on the edit screen and updated
						// question numbers for the quiz set
					
						saveQuizSet(savedQSets.get(selectedSet));
						loadAllQuizSets();
						createTable("edit");
						createTable("set");

						// update the labels on main screen
						setQSetLabels();

						// take us back to the edit screen

						CardLayout cl = (CardLayout) containerPanel.getLayout();

						currentlyShownPanel = "edit";

						cl.show(containerPanel, currentlyShownPanel);

						setTitle(savedQSets.get(selectedSet).getName() + "(Editing)");
					}
					
				}else if(((JButton) source).getText().equals("Exit")) {

					editingQuestion = false;

					CardLayout cl = (CardLayout) containerPanel.getLayout();

					currentlyShownPanel = "edit";

					cl.show(containerPanel, currentlyShownPanel);

					setTitle(savedQSets.get(selectedSet).getName() + "(Editing)");
				}
			}else if(currentlyShownPanel.equals("quiz")) {

				if(source instanceof JButton) {
					
					if(((JButton) source).getText().equals("Exit")) {
						
						if(quizNextButton.getText().equals("Grade Quiz")) {
							quizNextButton.setText("Next");
						}
						CardLayout cl = (CardLayout) containerPanel.getLayout();
						currentlyShownPanel = "main";
						cl.show(containerPanel, currentlyShownPanel);
						setTitle("QuizMaker");
		
					}else if(((JButton) source).getText().equals("Previous")) {

						if(qEng.getCurQuesNum() > 0) {
							qEng.decrementCurQuesNum();
							configureQuizComponents();
							if(quizNextButton.getText().equals("Grade Quiz")) {
								quizNextButton.setText("Next");
							}
						}

						if(qEng.getCurQuesNum() == 0) {
							quizPrevButton.setEnabled(false);
						}

					}else if(((JButton) source).getText().equals("Next")) {

						if(qEng.getCurQuesNum() < qEng.getQuizSet().size()-1) {
							qEng.incrementCurQuesNum();
							configureQuizComponents();
							if(!(quizPrevButton.isEnabled())) {
								quizPrevButton.setEnabled(true);
							}
						}

						if(qEng.getCurQuesNum() == qEng.getQuizSet().size()-1) {
							quizNextButton.setText("Grade Quiz");
						}

					}else if(((JButton) source).getText().equals("Grade Quiz")){
							
						ArrayList<ArrayList<String>> allUserAnswers = new ArrayList<ArrayList<String>>();

						// go through each component and extract the selected answers
						for(ArrayList<Component> choiceComponents : quizChoiceComponents) {

							ArrayList<String> questionUserAnswers = new ArrayList<String>();

							for(Component comp : choiceComponents) {
								if(comp instanceof JRadioButton) {
									if(((JRadioButton) comp).isSelected()) {
										questionUserAnswers.add(((JRadioButton) comp).getText());
									}
								}else if(comp instanceof JCheckBox) {
									if(((JCheckBox) comp).isSelected()) {
										questionUserAnswers.add(((JCheckBox) comp).getText());
									}
								}else if(comp instanceof JTextField) {
									questionUserAnswers.add(((JTextField) comp).getText());
								}
							}

							allUserAnswers.add(questionUserAnswers);
						}

						float userScore = qEng.gradeQuiz(allUserAnswers);

						// send this score to the selected quiz set
						savedQSets.get(selectedSet).addGrade(userScore);
					
						// save the set
						saveQuizSet(savedQSets.get(selectedSet));
						loadAllQuizSets();
						createTable("edit");
						createTable("set");
						setQSetLabels();


						quizResultScoreLabel.setText("Score: " + decimalFormat.format(userScore));

						quizResultAveScoreLabel.setText("Average Score: " + decimalFormat.format(savedQSets.get(selectedSet).getAveGrade()));

						CardLayout cl = (CardLayout) containerPanel.getLayout();

						currentlyShownPanel = "results";

						cl.show(containerPanel, currentlyShownPanel);

						setTitle(savedQSets.get(selectedSet).getName() + " Results");

						// get the answer that the user got incorrect
						ArrayList<Question> incorrectQuestions = qEng.getIncorrectQuestionSet();
						ArrayList<ArrayList<String>> incorrectUserAnswers = qEng.getIncorrectUserAnswers();

						quizResultMissedQuestionPanel.removeAll();
						quizResultMissedQuestionPanel.revalidate();
						quizResultMissedQuestionPanel.repaint();

						for(int i=0; i<incorrectQuestions.size(); i++) {
							JPanel missedPanel = new JPanel();
							JPanel missedLabelPanel = new JPanel(); // hold question number
							JLabel missedLabel = new JLabel("Question " + incorrectQuestions.get(i).getQId());
							missedLabel.setFont(new Font("Serif", Font.BOLD, 17));
							missedLabelPanel.add(missedLabel);
						
							JPanel missedTextPanel = new JPanel(); // holds question text
							JLabel missedText = new JLabel();
							missedText.setText("<html>" + incorrectQuestions.get(i).getQuesText() + "</html>");
							missedText.setFont(new Font("Serif", Font.PLAIN, 14));
							missedTextPanel.add(missedText);

							JPanel missedUserLabelPanel = new JPanel(); // holds incorrect answer label
							JLabel missedUserLabel = new JLabel("Your Answer");
							missedUserLabel.setFont(new Font("Serif", Font.BOLD, 17));
							missedUserLabelPanel.add(missedUserLabel);
							
							JPanel missedUserPanel = new JPanel(); // hold incorrect answer
							
							if(i < incorrectUserAnswers.size()) {
								ArrayList<String> userAnswer = incorrectUserAnswers.get(i);
				
								for(int h=0; h<userAnswer.size(); h++) {
									JLabel missedUser = new JLabel("<html>" + userAnswer.get(h) + "</html>");
									missedUser.setFont(new Font("Serif", Font.PLAIN, 14));
									missedUserPanel.add(missedUser);
								}
							}

							JPanel missedCorrectLabelPanel = new JPanel(); // holds correct answer label
							JLabel missedCorrectLabel = new JLabel("Correct Answer");
							missedCorrectLabel.setFont(new Font("Serif", Font.BOLD, 17));
							missedCorrectLabelPanel.add(missedCorrectLabel);

							JPanel missedCorrectPanel = new JPanel(); // holds correct answer

							for(int h=0; h<incorrectQuestions.get(i).getAnswers().size(); h++) {
								JLabel missedCorrect = new JLabel("<html>" + incorrectQuestions.get(i).getAnswers().get(h) + "</html>");
								missedCorrect.setFont(new Font("Serif", Font.PLAIN, 14));
								missedCorrectPanel.add(missedCorrect);
							}							
							
							// set layouts
							missedPanel.setLayout(new BoxLayout(missedPanel, BoxLayout.PAGE_AXIS));
							missedLabelPanel.setLayout(new BoxLayout(missedLabelPanel, BoxLayout.LINE_AXIS));
							missedTextPanel.setLayout(new BoxLayout(missedTextPanel, BoxLayout.LINE_AXIS));
							missedUserLabelPanel.setLayout(new BoxLayout(missedUserLabelPanel, BoxLayout.LINE_AXIS));
							missedUserPanel.setLayout(new BoxLayout(missedUserPanel, BoxLayout.LINE_AXIS));
							missedCorrectLabelPanel.setLayout(new BoxLayout(missedCorrectLabelPanel, BoxLayout.LINE_AXIS));
							missedCorrectPanel.setLayout(new BoxLayout(missedCorrectPanel, BoxLayout.LINE_AXIS));

							missedPanel.add(missedLabelPanel);
							missedTextPanel.setPreferredSize(new Dimension(450, 150));
							missedTextPanel.setMaximumSize(new Dimension(450, 150));
							missedPanel.add(missedTextPanel);
							missedPanel.add(Box.createRigidArea(new Dimension(300, 0)));

							missedPanel.add(missedUserLabelPanel);
							missedUserPanel.setPreferredSize(new Dimension(450, 100));
							missedUserPanel.setMaximumSize(new Dimension(450, 100));
							missedPanel.add(missedUserPanel);
							missedPanel.add(missedCorrectLabelPanel);
							missedCorrectPanel.setPreferredSize(new Dimension(450, 100));
							missedCorrectPanel.setMaximumSize(new Dimension(450, 100));
							missedPanel.add(missedCorrectPanel);

							missedPanel.setPreferredSize(new Dimension(425, 300));
							missedPanel.setMaximumSize(new Dimension(425, 300));
							quizResultMissedQuestionPanel.add(Box.createRigidArea(new Dimension(0, 30)));
							quizResultMissedQuestionPanel.add(missedPanel);
							quizResultMissedQuestionPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
						}


					}
				}
			}else if(currentlyShownPanel.equals("results")) {
				if(((JButton) source).getText().equals("Restart")) {
					quizPrevButton.setEnabled(false);
					quizNextButton.setText("Next");

					qEng.setQuestionSet(savedQSets.get(selectedSet).getAllQuestions());

					qEng.generateQuiz();

					// create the components to add and remove
					createQuizComponents();
						
					configureQuizComponents();

					CardLayout cl = (CardLayout) containerPanel.getLayout();

					// switch to the quiz panel
					currentlyShownPanel = "quiz";

					cl.show(containerPanel, currentlyShownPanel);

					setTitle(savedQSets.get(selectedSet).getName() + " Quiz");
				}else if(((JButton) source).getText().equals("Main Menu")) {

					quizNextButton.setText("Next");

					CardLayout cl = (CardLayout) containerPanel.getLayout();
					currentlyShownPanel = "main";
					cl.show(containerPanel, currentlyShownPanel);
					setTitle("QuizMaker");
				}
			}
		}else if(source instanceof JRadioButton) {
			if(((JRadioButton) source).getName().equals("Multiple Choice")) {
				// if it is multiple choice then we should only have one answer
				// so limit the amount of answer fields the user has and disable
				// the add and remove answer buttons

				Component[] newQuestionChoiceComps = newQuestionQChoicePanel.getComponents();

				newQuestionQAnsPanel.removeAll();
				newQuestionQAnsPanel.add(newQuestionQAns);

				newQuestionQAnsAddButton.setEnabled(false);
				newQuestionQAnsRemButton.setEnabled(false);

			}else if(((JRadioButton) source).getName().equals("Many Answers")) {
				
				newQuestionQAnsAddButton.setEnabled(true);

				if(newQuestionQAnsPanel.getComponents().length > 2) {
					newQuestionQAnsRemButton.setEnabled(true);
				}else{
					newQuestionQAnsRemButton.setEnabled(false);
				}
			

			}else if(((JRadioButton) source).getName().equals("Fill in the blank")) {

				newQuestionQAnsAddButton.setEnabled(true);
					
				if(newQuestionQAnsPanel.getComponents().length > 2) {
					newQuestionQAnsRemButton.setEnabled(true);
				}else{
					newQuestionQAnsRemButton.setEnabled(false);
				}
			}
		}
	}

	// mouse listner functions

	public void mousePressed(MouseEvent e) {
		// not used
	}

	public void mouseReleased(MouseEvent e) {
		// not used
	}

	public void mouseEntered(MouseEvent e) {
		// not used
	}

	public void mouseExited(MouseEvent e) {
		// not used
	}

	public void mouseClicked(MouseEvent e) {
		
		Object source = e.getSource();

		if(source instanceof JTable) {

			JTable tableClicked = (JTable) source;

			int rowSelected = tableClicked.getSelectedRow();

			String selectedText = tableClicked.getValueAt(rowSelected, 0).toString();

			// get the name of the selected bank
			if(tableClicked.getName().equals("Main Table")) {

				// go through the saved quiz bank sets and get the correct one
				for(int i=0; i<savedQSets.size(); i++) {
					if(savedQSets.get(i).getName().equals(selectedText)) {
						selectedSet = i;
					}
				}

				setQSetLabels();

				createTable("preview");

			}else if(tableClicked.getName().equals("Edit Table")) {
				for(int i=0; i<savedQSets.get(selectedSet).getQNum(); i++) {
					if(savedQSets.get(selectedSet).getQuestion(i).getQuesText().equals(selectedText)) {
						selectedQuestion = i;
					}
				}
			}
		}	
	}

	// document listener functions
	public void insertUpdate(DocumentEvent e) {
		System.out.println("Insert Update Fired!");
	}

	public void removeUpdate(DocumentEvent e) {
		System.out.println("Remove Update Fired!");
	}

	public void changedUpdate(DocumentEvent e) {
		System.out.println("Changed Update Fired!");
	}

	private void setQSetLabels() {

		// change all of the detail labels to information for the selected set
		qBankNameLabel.setText("Name:    " + savedQSets.get(selectedSet).getName());
		qBankCreatedLabel.setText("Created:    " + savedQSets.get(selectedSet).getCreatedDate());
		qBankQNumLabel.setText("Number of Questions:    " + String.valueOf(savedQSets.get(selectedSet).getQNum()));
		qBankLastGradeLabel.setText("Last Grade:    " + decimalFormat.format(savedQSets.get(selectedSet).getLastGrade()));
		qBankAveGradeLabel.setText("Average Grade:    " + decimalFormat.format(savedQSets.get(selectedSet).getAveGrade()));

	}

	// serializes the QuizSet object and saves it 
	private void saveQuizSet(QuizSet qSet) {
		try {

			// make a filename out of the set name 
			// replace spaces with dash
			String fileName = qSet.getName().replace(' ', '-');
			qSet.setFileName(fileName + ".ser");

			FileOutputStream fileOut = new FileOutputStream("./data/" + fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(qSet);
			out.close();
			fileOut.close();

		}catch(IOException e) {
			e.printStackTrace();
		}

		System.out.println("Currently shown panel: " + currentlyShownPanel);
		
		if(currentlyShownPanel.equals("edit")) {
			JOptionPane.showMessageDialog(this, "Quiz Set Saved Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
		}
	}

	// deserialize the QuizSet object
	private void loadQuizSet(String fileName) {
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			QuizSet loadedSet = (QuizSet) in.readObject();

			savedQSets.add(loadedSet);

			in.close();
			fileIn.close();
		} catch(IOException i) {
			i.printStackTrace();
		} catch(ClassNotFoundException c) {
			System.out.println("QuizSet class not found");
			c.printStackTrace();
		}
	}

	// load all quiz sets in the savedQSetPath
	private void loadAllQuizSets() {

		savedQSets = new ArrayList<QuizSet>();

		// get the list of saved quiz set files from the directory

		File savedQSetsNames = new File(savedQSetPath);

		String[] pathnames = savedQSetsNames.list();

		// deserialize each filename
		for(String pathname : pathnames) {
			this.loadQuizSet(savedQSetPath + pathname);
		}
	}

	// recreates either the QuizSet bank table or the preview table with
	// new data. if param type = 'set' we do the QuizSet table, if it is 
	// 'preview' we do the preview table
	private void createTable(String tableType) {

		loadAllQuizSets();

		// make a new table model
		DefaultTableModel tModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		if(tableType.equals("set")) {	

			// set the headers
			String[] header = {"Quiz Banks"};
			tModel.setColumnIdentifiers(header);

			// set the table rows
			for(int i=0; i<savedQSets.size(); i++) {
				String[] tableRow = {savedQSets.get(i).getName()};
				tModel.addRow(tableRow);
			}

			qBankTable = new JTable(tModel);
			qBankTable.setRowHeight(25);
			qBankTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
			// read the mouse listener if we are refreshing
			qBankTable.addMouseListener(this);
			qBankTable.setName("Main Table");
			qBankTablePane.setViewportView(qBankTable);
			qBankTablePane.repaint();
		}else if(tableType.equals("preview")) {
			
			String[] header = {"Preview"};
			tModel.setColumnIdentifiers(header);

			for(int i=0; i<savedQSets.get(selectedSet).getQNum(); i++) {
				String[] tableRow = {savedQSets.get(selectedSet).getQuestion(i).getQuesText()};
				tModel.addRow(tableRow);
			}

			previewTable = new JTable(tModel);
			previewTable.setRowHeight(25);
			previewTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
			previewScrollPane.setViewportView(previewTable);
			previewScrollPane.repaint();


		}else if(tableType.equals("edit")) {

			// get all of the questions for the selected set
			ArrayList<Question> selSetQuestions = savedQSets.get(selectedSet).getAllQuestions();
			
			// set the header for the table
			String[] header = {savedQSets.get(selectedSet).getName() + " Questions"};
			tModel.setColumnIdentifiers(header);

			// set the table rows
			for(int i=0; i<selSetQuestions.size(); i++) {
				String[] tableRow = {selSetQuestions.get(i).getQuesText()};
				tModel.addRow(tableRow);
			}

			editPanelTable = new JTable(tModel);
			editPanelTable.setRowHeight(25);
			editPanelTable.addMouseListener(this);
			editPanelTable.setName("Edit Table");
			editPanelTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
			editPanelTablePane.setViewportView(editPanelTable);
			editPanelTablePane.repaint();
		}
	}


	// takes the selected quiz set and iterated through the choices,
	// creating components for the choices based on the question type
	// and storing them inside the ArrayList
	private void createQuizComponents() {

		quizChoiceComponents = new ArrayList<ArrayList<Component>>();
		quizButtonGroups = new ArrayList<ButtonGroup>();

		for(int i=0; i<qEng.getQuestionSet().size(); i++) {
			ArrayList<Component> compList = new ArrayList<Component>();
	
			Question ques = qEng.getQuestion(i);

			int quesType = ques.getQType();

			ArrayList<String> questionChoices = ques.getChoices();

			if(quesType == 1) {
				ButtonGroup quizButtonGroup = new ButtonGroup();
				for(String choice : questionChoices) {
					// type 1 is JRadioButtons
					JRadioButton radioChoice = new JRadioButton("<html>" + choice + "</html>");
					// add the component to the component ArrayList and button group
					compList.add(radioChoice);
					quizButtonGroup.add(radioChoice);
				}
			}else if(quesType == 2) {
				for(String choice : questionChoices) {
					// type 2 is JCheckBoxes
					JCheckBox checkBoxChoice = new JCheckBox("<html>" + choice + "</html>");
					compList.add(checkBoxChoice);
				}
			}else if(quesType == 3) {
				for(String choice : questionChoices) {
					// type 3 is JTextFields
					JLabel choiceLabel = new JLabel("<html>" + choice + "</html>");
					JTextField textFieldChoice = new JTextField(10);
					JPanel textFieldChoicePanel = new JPanel();
					textFieldChoicePanel.setLayout(new BoxLayout(textFieldChoicePanel, BoxLayout.LINE_AXIS));
					textFieldChoicePanel.add(textFieldChoice);
					compList.add(choiceLabel);
					compList.add(textFieldChoicePanel);
				}
			}	
			// add the ArrayList of components to the global ArrayList to retrieve later
			quizChoiceComponents.add(compList);
		}
	}

	private void configureQuizComponents() {

		// we've got three components that we need to configure
		// 1. Question number label (quizQuestionLabel)
		// 2. The question text (quizQuestionText)
		// 3. The question choice (quizChoicePanel)

		ArrayList<Integer> qSequence = qEng.getQuestionSequence();

		// get the current question from the generated quiz

		Question curQuestion = qEng.getQuestion(qEng.getCurQuesNum());

		// set question number label
		quizQuestionLabel.setText("Question " + (qEng.getCurQuesNum()+1));

		// set the question text
		quizQuestionText.setText("<html>" + curQuestion.getQuesText() + "</html>");

		// remove all of the components in the quizChoicePanel
		quizChoicePanel.removeAll();
		quizChoicePanel.validate();
		quizChoiceScrollPane.validate();

		int questionType = curQuestion.getQType();

		// get the choices for the question
		ArrayList<String> quesChoices = curQuestion.getChoices();

		// set the grid layout to match the amount of choices we have
		// the tradional layout is a x/2 layout where x is an even number of
		// rows. Of course if we have an odd number of choices then we will not use both 
		// column slots for the last row
		if(questionType == 3) {
			quizChoicePanel.setLayout(new BoxLayout(quizChoicePanel, BoxLayout.PAGE_AXIS));
		}else {
			int layoutRows = (int) Math.ceil(quesChoices.size() / 2);
			quizChoicePanel.setLayout(new GridLayout(layoutRows, 2));
		}

		quizChoiceScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		quizChoicePanel.validate();
		quizChoiceScrollPane.validate();
		repaint();

		// get the created components for the given question and add them to the panel

		ArrayList<Component> quesComps = quizChoiceComponents.get(qEng.getCurQuesNum());

		for(Component comp : quesComps) {
			if(comp instanceof JRadioButton) {
				comp = (JRadioButton) comp;
			}else if(comp instanceof JCheckBox) {
				comp = (JCheckBox) comp;
			}else if(comp instanceof JTextField) {
				comp = (JTextField) comp;
			}else if(comp instanceof JLabel) {
				comp = (JLabel) comp;
			}
			
			if((questionType == 3) && (comp instanceof JLabel || comp instanceof JTextField)) {
				quizChoicePanel.add(Box.createRigidArea(new Dimension(0, 5)));
			}

			quizChoicePanel.add(comp);
		}

		// we don't want the scroll pane to be too large for multiple choice
		// but it might have to be lengthy if there are multiple fill in the blank 
		// so set the height of the panel here depending on the number of components we have
		// and question type
		if(questionType == 3) {	
			Component[] choiceComps = quizChoicePanel.getComponents();

			quizChoicePanel.setPreferredSize(new Dimension(450, (18*choiceComps.length)));
			quizChoicePanel.setMaximumSize(new Dimension(450, (18*choiceComps.length)));
		}else{
			quizChoicePanel.setPreferredSize(new Dimension(450, 100));
			quizChoicePanel.setMaximumSize(new Dimension(450, 100));
		}

		quizChoicePanel.validate();
		quizChoiceScrollPane.validate();
		
	}

	public void display() {
		setVisible(true);
	}
}
