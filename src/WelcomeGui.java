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

public class WelcomeGui extends JFrame implements ActionListener, MouseListener {

	private String savedQSetPath = "./data/";

	private ArrayList<QuizSet> savedQSets = new ArrayList<QuizSet>();

	private QuizSet selectedSet;

	private DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

		// get the list of saved quiz set files from the directory

		File savedQSetsNames = new File(savedQSetPath);

		String[] pathnames = savedQSetsNames.list();

		// deserialize each filename
		for(String pathname : pathnames) {
			this.loadQuizSet(savedQSetPath + pathname);
		}

		// create table for Quiz Banks
		this.createTable(this.savedQSets, "set");

		qBankTable.addMouseListener(this);

		// settings for the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(60, 60);
		setLayout(new CardLayout(30, 15));
		
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
		qDetailPanel.add(qBankNameLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		qDetailPanel.add(qBankCreatedLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		qDetailPanel.add(qBankQNumLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		qDetailPanel.add(qBankLastGradeLabel);
		qDetailPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		qDetailPanel.add(qBankAveGradeLabel);
		qDetailPanel.add(Box.createVerticalGlue());
	
		qSelectDetailPanel.add(qSelectPanel);
		// spacing between the question bank panel and the details panel
		qSelectDetailPanel.add(Box.createRigidArea(new Dimension(25, 0)));
		qSelectDetailPanel.add(qDetailPanel);


		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		previewScrollPane.setPreferredSize(new Dimension(175, 150));
		previewPanel.add(previewScrollPane);

		mainPanel.add(previewPanel);

		add(mainPanel);

		setSize(500, 420);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if(source instanceof JButton) {
			if(((JButton) source).getText().equals("Start")) {
				System.out.println("Start button clicked.");

			}else if(((JButton) source).getText().equals("Edit")) {
	
				System.out.println("Edit button clicked");

			}else if(((JButton) source).getText().equals("Delete")) {

				System.out.println("Delete button clicked");

			}else if(((JButton) source).getText().equals("Create New")) {
				String newSetName = JOptionPane.showInputDialog(this, "New Quiz Bank Name?", null);

				String createdDate = LocalDateTime.now().format(dtFormat);
			
				// create the new quiz set with the name
				QuizSet newQSet = new QuizSet(newSetName, createdDate);

				// go ahead and save the set
				this.saveQuizSet(newQSet);
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
			}		

			System.out.println("Quiz Set " + selectedSet.getName() + " selected.");
		}	
	}

	// serializes the QuizSet object and saves it 
	private void saveQuizSet(QuizSet qSet) {
		try {

			// make a filename out of the set name 
			// replace spaces with dash
			String fileName = qSet.getName().replace(' ', '-');

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
	private void createTable(ArrayList<QuizSet> qSets, String type) {

		// make a new table model
		DefaultTableModel tModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		if(type.equals("set")) {	
			// set the headers
			String[] header = {"Quiz Banks"};
			tModel.setColumnIdentifiers(header);

			// set the table rows
			for(int i=0; i<qSets.size(); i++) {
				String[] tableRow = {qSets.get(i).getName()};
				tModel.addRow(tableRow);
			}

			qBankTable = new JTable(tModel);
			qBankTablePane.setViewportView(qBankTable);
			qBankTablePane.repaint();
		}


	}

	public void display() {
		setVisible(true);
	}
}
