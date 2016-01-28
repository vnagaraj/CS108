package assign3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

public class MetropolisFrame extends JFrame {

	private MetropolisTableModel tableModel;
	private JTable jtable;
	private JTextField metTxtField;
	private JTextField contTxtField;
	private JTextField popTxtField;
	private JButton addBtn;
	private JButton srchBtn;
	private JComboBox popCombo;
	private String[] matchTypeStrings;
	private String[] popStrings;
	private JComboBox matchTypeCombo;

	public MetropolisFrame() {
		super("Metropolis Viewer");
		tableModel = new MetropolisTableModel();
		jtable = new JTable(tableModel);
		setControls();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.tableModel.closeConnection();
		pack();
		setVisible(true);
	}

	private void setControls() {
		JScrollPane scroller = new JScrollPane(jtable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setLayout(new BorderLayout(4, 4));
		metTxtField = new JTextField(10);
		JLabel metLabel = new JLabel("Metropolis:");
		contTxtField = new JTextField(10);
		JLabel contLabel = new JLabel("Continent:");
		popTxtField = new JTextField(10);
		JLabel popLabel = new JLabel("Population:");
		JPanel panelTextBox = new JPanel();
		panelTextBox.add(metLabel);
		panelTextBox.add(metTxtField);
		panelTextBox.add(contLabel);
		panelTextBox.add(contTxtField);
		panelTextBox.add(popLabel);
		panelTextBox.add(popTxtField);
		JPanel panelJTable = new JPanel();
		panelJTable.add(scroller);
		JPanel panelAddSearch = new JPanel();
		addBtn = new JButton("Add");
		srchBtn = new JButton("Search");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});
		srchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getRowBasedOnQuery();
			}
		});
		panelAddSearch
				.setLayout(new BoxLayout(panelAddSearch, BoxLayout.Y_AXIS));
		panelAddSearch.add(addBtn);
		panelAddSearch.add(srchBtn);
		JLabel srcOptionsLabel = new JLabel("Search Options");
		panelAddSearch.add(srcOptionsLabel);
		popStrings = new String[] { getDisplayPop(Population.GREATER),
				getDisplayPop(Population.LESS), getDisplayPop(Population.EQUAL) };
		popCombo = new JComboBox(popStrings);
		popCombo.setSelectedIndex(0);
		panelAddSearch.add(popCombo);
		matchTypeStrings = new String[] { getDisplayMatch(MatchType.EXACT),
				this.getDisplayMatch(MatchType.PARTIAL) };
		matchTypeCombo = new JComboBox(matchTypeStrings);
		matchTypeCombo.setSelectedIndex(0);
		panelAddSearch.add(matchTypeCombo);
		this.getContentPane().add(panelTextBox, BorderLayout.NORTH);
		this.getContentPane().add(panelJTable, BorderLayout.CENTER);
		this.getContentPane().add(panelAddSearch, BorderLayout.EAST);
	}

	public enum MatchType {
		EXACT, PARTIAL
	}

	public String getDisplayMatch(MatchType match) {
		String st = null;
		switch (match) {
		case EXACT:
			st = "Exact Match";
			break;
		case PARTIAL:
			st = "Partial Match";
			break;
		}
		return st;
	}

	public enum Population {
		GREATER, LESS, EQUAL
	}

	public String getDisplayPop(Population pop) {
		String st = null;
		switch (pop) {
		case GREATER:
			st = "Population Larger Than";
			break;
		case LESS:
			st = "Population Less Than";
			break;
		case EQUAL:
			st = "Population equal to";
			break;
		}
		return st;
	}

	public String getQueryPop(String popStr) {
		if (popStr.equals("Population Larger Than")) {
			return ">";
		} else if (popStr.equals("Population Less Than")) {
			return "<";
		} else if (popStr.equals("Population equal to")) {
			return "=";
		}
		return null;
	}

	public boolean getQueryMatch(String matchStr) {
		if (matchStr.equals("Exact Match")) {
			return true;
		}
		return false;

	}

	private void addRow() {
		String metropolis = this.metTxtField.getText();
		String continent = this.contTxtField.getText();
		String population = this.popTxtField.getText();
		int pop = Integer.parseInt(population);
		this.tableModel.insertRow(metropolis, continent, pop);
	}

	private void getRowBasedOnQuery() {
		String population = this.popTxtField.getText();
		String met = this.metTxtField.getText();
		String cont = this.contTxtField.getText();
		int indexPop = this.popCombo.getSelectedIndex();
		String pop = this.popStrings[indexPop];
		String symbol = this.getQueryPop(pop);
		int matchType = this.matchTypeCombo.getSelectedIndex();
		String matchStr = this.matchTypeStrings[matchType];
		boolean match = this.getQueryMatch(matchStr);
		this.tableModel.getSearchQuery(symbol, population, met, cont, match);
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		MetropolisFrame frame = new MetropolisFrame();
	}

}
