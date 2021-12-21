import javax.swing.*;
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

public class QuizGui extends JFrame implements ActionListener, MouseListener {

	private String savedQSetPath = "./data/";

	private ArrayList<QuizSet> savedQSets = new ArrayList<QuizSet>();

	private QuizSet selectedSet = null;

	private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private JPanel containerPanel = new JPanel(); // used for the CardLayout

	// keeps track of what panel is currently being shown in the CardLayout
	// used to control what action is taken when a button is being pressed on the shown panel
	private String currentlyShownPanel = "main"; 

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
	private JLabel multiChoiceMultiAnsLabel = new JLabel("Multiple Choice");
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
	private JLabel newQuestionQChoiceLabel = new JLabel("Question Choice 1");

	private JPanel newQuestionQChoicePanel = new JPanel();
	private JTextField newQuestionQChoice = new JTextField(20);

	private JPanel newQuestionQChoiceButtonPanel = new JPanel();
	private JButton newQuestionQChoiceAddButton = new JButton("Add");
	private JButton newQuestionQChoiceRemButton = new JButton("Remove");

	// Question answer panel
	private JPanel newQuestionQAnsLabelPanel = new JPanel();
	private JLabel newQuestionQAnsLabel = new JLabel("Question Answer 1");

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

	QuizGui() {

		// JFrame configuration
		super("QuizMaker");

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
		qBankTablePane.setPreferredSize(new Dimension(200, 125));
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
	
		qSelectDetailPanel.add(qSelectPanel);
		// spacing between the question bank panel and the details panel
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		qSelectDetailPanel.add(qDetailPanel);
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(100, 0)));


		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		previewScrollPane.setPreferredSize(new Dimension(175, 150));
		previewPanel.add(previewScrollPane);

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

		qTypeButtonGroup.add(multiChoiceOneAns);
		qTypeButtonGroup.add(multiChoiceMultiAns);
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
		newQuestionQChoiceButtonPanel.add(newQuestionQChoiceAddButton);
		newQuestionQChoiceButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		newQuestionQChoiceButtonPanel.add(newQuestionQChoiceRemButton);

		newQuestionQAnsLabel.setFont(detailLabelFont);
		newQuestionQAnsLabelPanel.add(newQuestionQAnsLabel);

		newQuestionQAnsPanel.add(newQuestionQAns);

		newQuestionQAnsButtonPanel.add(Box.createRigidArea(new Dimension(300, 0)));
		newQuestionQAnsButtonPanel.add(newQuestionQAnsAddButton);
		newQuestionQAnsButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		newQuestionQAnsButtonPanel.add(newQuestionQAnsRemButton);

		newQuestionQSavePanel.add(newQuestionQSaveButton);
		newQuestionQSavePanel.add(Box.createRigidArea(new Dimension(10, 0)));
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

		mainPanel.setPreferredSize(new Dimension(500, 400));
		mainPanel.setMaximumSize(new Dimension(500, 400));
		containerPanel.add(mainPanel, "main");

		editPanel.setPreferredSize(new Dimension(500, 400));
		editPanel.setMaximumSize(new Dimension(500, 400));
		containerPanel.add(editPanel, "edit");

		newQuestionPane.setPreferredSize(new Dimension(500, 400));
		newQuestionPane.setMaximumSize(new Dimension(500, 400));
		containerPanel.add(newQuestionPane, "newQuestion");

		
		add(containerPanel);

		setSize(550, 475);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// process JButton events
		if(source instanceof JButton) {

			if(currentlyShownPanel.equals("main")) {

				if(((JButton) source).getText().equals("Start")) {

					if(selectedSet != null) {
						System.out.println("Start button clicked.");
					}else {
						JOptionPane.showMessageDialog(this, "You must select a quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}	

				}else if(((JButton) source).getText().equals("Edit")) {

					if(selectedSet != null) {
						System.out.println("Edit button clicked");
						CardLayout cl = (CardLayout) containerPanel.getLayout();

						// switch to the edit panel
						currentlyShownPanel = "edit";
						cl.show(containerPanel, currentlyShownPanel);

						// change the title for the newly shown panel
						setTitle(selectedSet.getName() + "(Editing)");

						// draw the edit table
						createTable("edit");
					}else{
						JOptionPane.showMessageDialog(this, "You must select a quiz set.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}

				}else if(((JButton) source).getText().equals("Delete")) {

					if(selectedSet != null) {

						int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this set?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

						if(n == 0) {
							File fileObj = new File(savedQSetPath + selectedSet.getFileName());
				
							if(fileObj.delete()) {
								JOptionPane.showMessageDialog(this, "File Deleted Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
								// set the selected set back to null so the user can't click the edit/delete on a set that doesn't exist
								selectedSet = null;
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

					System.out.println("Edit Panel Add Button Pressed");

					CardLayout cl = (CardLayout) containerPanel.getLayout();
					currentlyShownPanel = "newQuestion";
					cl.show(containerPanel, currentlyShownPanel);
					setTitle(selectedSet.getName() + " New Question");

				}else if(((JButton) source).getText().equals("Edit")) {

					System.out.println("Edit Panel Edit Button Pressed");

				}else if(((JButton) source).getText().equals("Delete")) {
					
					System.out.println("Edit Panel Delete Button Pressed");

				}else if(((JButton) source).getText().equals("Save")) {
					CardLayout cl = (CardLayout) containerPanel.getLayout();
					currentlyShownPanel = "main";
					cl.show(containerPanel, currentlyShownPanel);
					setTitle("QuizMaker");
				}	
			}
		}
	}

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

			// get the name of the selected bank
			if(tableClicked.getColumnName(0).equals("Quiz Banks")) {
				
				int rowSelected = tableClicked.getSelectedRow();

				String bankName = tableClicked.getValueAt(rowSelected, 0).toString();

				// go through the saved quiz bank sets and get the correct one
				for(int i=0; i<savedQSets.size(); i++) {
					if(savedQSets.get(i).getName().equals(bankName)) {
						selectedSet = savedQSets.get(i);
					}
				}

				// change all of the detail labels to information for the selected set
				qBankNameLabel.setText("Name:    " + selectedSet.getName());
				qBankCreatedLabel.setText("Created:    " + selectedSet.getCreatedDate());
				qBankQNumLabel.setText("Number of Questions:    " + String.valueOf(selectedSet.getQNum()));
				qBankLastGradeLabel.setText("Last Grade:    " + String.valueOf(selectedSet.getLastGrade()));
				qBankAveGradeLabel.setText("Average Grade:    " + String.valueOf(selectedSet.getAveGrade()));

			}		
		}	
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

		JOptionPane.showMessageDialog(this, "Quiz Set Saved Successfully", "Success", JOptionPane.PLAIN_MESSAGE);
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

	// recreates either the QuizSet bank table or the preview table with
	// new data. if param type = 'set' we do the QuizSet table, if it is 
	// 'preview' we do the preview table
	private void createTable(String tableType) {

		// make a new table model
		DefaultTableModel tModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		if(tableType.equals("set")) {	

			savedQSets = new ArrayList<QuizSet>();

			// get the list of saved quiz set files from the directory

			File savedQSetsNames = new File(savedQSetPath);

			String[] pathnames = savedQSetsNames.list();

			// deserialize each filename
			for(String pathname : pathnames) {
				this.loadQuizSet(savedQSetPath + pathname);
			}

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
			qBankTablePane.setViewportView(qBankTable);
			qBankTablePane.repaint();
		}else if(tableType.equals("edit")) {

			// get all of the questions for the selected set
			ArrayList<Question> selSetQuestions = selectedSet.getAllQuestions();
			
			// set the header for the table
			String[] header = {selectedSet.getName() + " Questions"};
			tModel.setColumnIdentifiers(header);

			// set the table rows
			for(int i=0; i<selSetQuestions.size(); i++) {
				String[] tableRow = {selSetQuestions.get(i).getQuesText()};
				tModel.addRow(tableRow);
			}

			editPanelTable = new JTable(tModel);
			editPanelTable.setRowHeight(25);
			editPanelTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
			editPanelTablePane.setViewportView(editPanelTable);
			editPanelTablePane.repaint();
		}
	}

	public void display() {
		setVisible(true);
	}
}
