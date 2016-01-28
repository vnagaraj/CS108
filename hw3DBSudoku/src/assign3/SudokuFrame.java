package assign3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


 public class SudokuFrame extends JFrame {
	



	private JTextArea source;
	private JTextArea result;
	private JCheckBox autoCheck;


	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		
		// Could do this:
		// setLocationByPlatform(true);
		this.setLayout(new BorderLayout(4,4));
		source = new JTextArea(15,20);
		source.setBorder(new TitledBorder("Puzzle"));
		result = new JTextArea(15,20);
		result.setBorder(new TitledBorder("Solution"));
		JPanel panel = new JPanel();
		JButton checkButton = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		panel.add(checkButton);
		checkButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				constructSudoku();
			}
		});
		Document document = source.getDocument();
		document.addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				//not do anything

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (autoCheck.isSelected())
				constructSudoku();

			}
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (autoCheck.isSelected())
				constructSudoku();

			}
		});
		panel.add(autoCheck);
		this.getContentPane().add(source,BorderLayout.CENTER);
		this.getContentPane().add(result,BorderLayout.EAST);
		this.getContentPane().add(panel,BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void constructSudoku() {

		//get text from user input
		String text = source.getText();
		try{			
		int[][] grids = Sudoku.textToGrid(text);		
		Sudoku sudoku = new Sudoku(grids);
		int solutions = sudoku.solve();
		long timeElapsed = sudoku.getElapsed();
		result.setText(sudoku.getSolutionText());
		result.append("solutions:"+solutions+"\n");
		result.append("elapsed"+timeElapsed + "ms");

		}
		catch(Exception e){
			result.setText("Parsing Problem");
		}
		
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
