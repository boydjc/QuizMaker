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

	private JPanel qSelectSubPanelTwo = new JPanel(); // contains q bank buttons

	private JButton qSelectButton = new JButton("Start");

	private JPanel qDetailPanel = new JPanel();

	private JPanel previewPanel = new JPanel();


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
		qSelectSubPanelTwo.setLayout(new BoxLayout(qSelectSubPanelTwo, BoxLayout.PAGE_AXIS));
		
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.PAGE_AXIS));

		// qSelectDetailPanel and subpanels

		qBankTable.setFillsViewportHeight(true);
		qSelectSubPanelOne.add(new JScrollPane(qBankTable));
		qSelectPanel.add(qSelectSubPanelOne);

		qSelectSubPanelTwo.add(qSelectButton);
		qSelectPanel.add(qSelectSubPanelTwo);

		qSelectDetailPanel.add(qSelectPanel);

		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(previewPanel);

		add(mainPanel);

		setSize(600, 480);
	}

	public void display() {
		setVisible(true);
	}
}
