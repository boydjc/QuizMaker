import javax.swing.*;
import java.awt.*;

public class QuizGui extends JFrame {

	private JPanel mainPanel = new JPanel();

	private JPanel qSelectDetailPanel = new JPanel();
	private JPanel qSelectPanel = new JPanel();
	private JLabel qSelectLabel = new JLabel("Question Banks");

	private JPanel previewPanel = new JPanel();;


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
		
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.PAGE_AXIS));

		// qSelectDetailPanel components and subpanels

		// qSelectPanel components and subpanels
		qSelectPanel.add(qSelectLabel);
		// end qSelectPanel components and subpanels
		qSelectDetailPanel.add(qSelectPanel);

		// end qSelectDetailPanel components and sub panels
		mainPanel.add(qSelectDetailPanel);

		mainPanel.add(previewPanel);

		add(mainPanel);

		setSize(600, 480);
	}

	public void display() {
		setVisible(true);
	}
}
