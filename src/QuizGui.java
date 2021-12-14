import javax.swing.*;
import java.awt.*;

public class QuizGui extends JFrame {

	private JPanel mainPanel = new JPanel();
	private JPanel qSelectDetailPanel = new JPanel();
	private JPanel previewPanel = new JPanel();


	QuizGui() {
		super("QuizMaker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(60, 60);
		setLayout(new FlowLayout());
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		qSelectDetailPanel.setLayout(new BoxLayout(qSelectDetailPanel, BoxLayout.LINE_AXIS));
		previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.PAGE_AXIS));

		setSize(600, 480);
	}

	public void display() {
		setVisible(true);
	}
}
