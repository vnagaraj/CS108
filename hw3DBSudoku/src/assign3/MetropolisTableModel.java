package assign3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * a table model based on the abstract table model to retrieve information from the 
 * database
 */
public class MetropolisTableModel extends AbstractTableModel {
	Object[][] contents;
	String[] columnNames;
	Class[] columnClasses;
	private Connection conn;
	private boolean insert;
	private ArrayList rowList = new ArrayList();
	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	/**
	 * Constructor to set the table model .
	 * Initialized with columnNames set for the display .
	 */
	public MetropolisTableModel() {
		super();
		setColumnNames();
		setColumnClassList();
	}
	
	/**
	 * Create a jdbc connection to the database.
	 * @return
	 */
	private Connection createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + server;
			Connection con = DriverManager
					.getConnection(url, account, password);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Set the column names of the resulting table based on the query retrieved from the database.
	 * Populate the columnNames array with the result. 
	 * @return
	 */
	private void setColumnNames() {
		Statement statement;
		try {
			this.conn = this.createConnection();
			statement = conn.createStatement();
			statement.executeQuery("USE " + database);
			ResultSet results = statement
					.executeQuery("SELECT * FROM metropolises");
			ResultSetMetaData meta = results.getMetaData();
			ArrayList colNamesList = new ArrayList();
			ArrayList colClassesList = new ArrayList();
			int columnIndex = 1;
			try {
				while (meta.getColumnName(columnIndex) != null) {
					String columnName = meta.getColumnName(columnIndex);
					colNamesList.add(columnName);
					columnIndex++;
				}
			} catch (SQLException e) {
			}
			columnNames = new String[colNamesList.size()];
			colNamesList.toArray(columnNames);
			results.close();
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Set the datatype of the columns and add it to the columnClasses array .
	 * @return
	 */
	private void setColumnClassList() {
		ArrayList colClassesList = new ArrayList();
		colClassesList.add(String.class);
		colClassesList.add(String.class);
		colClassesList.add(Integer.class);
		columnClasses = new Class[colClassesList.size()];
		colClassesList.toArray(columnClasses);
	}

	/**
	 * Query for fetching all the rows in the database .
	 * Populating the rowList array and filling all the cells in the table.
	 * @return
	 */
	private void getAllRows() {
		try {
			rowList = new ArrayList();
			this.conn = this.createConnection();
			Statement statement = conn.createStatement();
			statement.executeQuery("USE " + database);
			ResultSet results = statement
					.executeQuery("SELECT * FROM metropolises");
			while (results.next()) {
				ArrayList cellList = new ArrayList();
				for (int i = 0; i < columnClasses.length; i++) {
					Object cellValue = null;
					if (columnClasses[i] == String.class)
						cellValue = results.getString(columnNames[i]);
					else if (columnClasses[i] == Integer.class)
						cellValue = new Integer(results.getInt(columnNames[i]));
					else
						System.out.println("Can't assign " + columnNames[i]);
					cellList.add(cellValue);
				}// for
				Object[] cells = cellList.toArray();
				rowList.add(cells);
				contents = new Object[rowList.size()][];
				for (int i = 0; i < contents.length; i++)
					contents[i] = (Object[]) rowList.get(i);
			}
			results.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Query for fetching only the last row, called after an insert query .
	 * Reassigning the rowList to only contain the last row .
	 * @return
	 */
	private void getLastRow() {
		try {
			rowList = new ArrayList();
			conn = this.createConnection();
			Statement statement = conn.createStatement();
			statement.executeQuery("USE " + database);
			ResultSet results = statement
					.executeQuery("SELECT * FROM metropolises");
			results.last();
			results.previous();
			while (results.next()) {
				ArrayList cellList = new ArrayList();
				for (int i = 0; i < columnClasses.length; i++) {
					Object cellValue = null;
					if (columnClasses[i] == String.class)
						cellValue = results.getString(columnNames[i]);
					else if (columnClasses[i] == Integer.class)
						cellValue = new Integer(results.getInt(columnNames[i]));
					else
						System.out.println("Can't assign " + columnNames[i]);
					cellList.add(cellValue);
				}// for
				Object[] cells = cellList.toArray();
				rowList.add(cells);
				contents = new Object[rowList.size()][];
				for (int i = 0; i < contents.length; i++)
					contents[i] = (Object[]) rowList.get(i);
			}
			results.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get the rowCount from the rowList arrayList.
	 * @return no of rows in the given result query .
	 */
	public int getRowCount() {
		return this.rowList.size();
	}

	/**
	 * Get the columnCount from the columnNames array.
	 * @return no of columns based on the database.
	 */
	public int getColumnCount() {
		return this.columnNames.length;
	}

	/**
	 * Fetch the value at the given cell based on row and column location.
	 * @return value of the cell.
	 */
	public Object getValueAt(int row, int column) {
		return contents[row][column];
	}
   
	/**
	 * Get the columnClass based on the column location
	 * @return type of the column.
	 */
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	/**
	 * Get the column name based on the column location .
	 * @return name of the column .
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Query to insert a new row into the database based on the following values.
	 * @param metropolis the metropolis value.
	 * @param continent the continent value .
	 * @param population the population value .
	 */
	public void insertRow(String metropolis, String continent, long population) {
		try {
			this.conn = this.createConnection();
			Statement stmt = conn.createStatement();
			stmt.executeQuery("USE " + database);
			stmt.executeUpdate("INSERT INTO metropolises VALUES(\""
					+ metropolis + "\"" + "," + "\"" + continent + "\"" + ","
					+ population + ");");
			conn.close();
			getLastRow();
			fireTableRowsInserted(rowList.size() - 1, rowList.size() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Utility method to construct the query based on the following parameters.
	 * This query is used during the search operations .
	 * @param symbol ">","<" or "=" for the population combo box.
	 * @param pop value of the population field.
	 * @param met value of the metropolis field.
	 * @param cont value of the continent field.
	 * @param match "Exact" or "Partial Match" based on the combo box.
	 * @return the constructed query .
	 */
	private String getQuery(String symbol, String pop, String met, String cont,
			boolean match) {
		String query = null;
		if (met.equals("") && cont.equals("")) {
			query = "SELECT * FROM metropolises where population" + symbol + ""
					+ pop;
		} else if (pop.equals("") && cont.equals("") && match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"" + met
					+ "\"";
		} else if (pop.equals("") && met.equals("") && match) {
			query = "SELECT * FROM metropolises where continent LIKE \"" + cont
					+ "\"";
		} else if (pop.equals("") && match) {
			query = "SELECT * FROM metropolises where continent LIKE \"" + cont
					+ "\" AND metropolis LIKE \"" + met + "\"";
		} else if (met.equals("") && match) {
			query = "SELECT * FROM metropolises where continent LIKE \"" + cont
					+ "\" AND population" + symbol + "" + pop;
		} else if (cont.equals("") && match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"" + met
					+ "\" AND population" + symbol + "" + pop;
		} else if (match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"" + met
					+ "\" AND continent LIKE \"" + cont + "\" AND population"
					+ symbol + "" + pop;
		} else if (pop.equals("") && cont.equals("") && !match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"%"
					+ met + "%\"";
		} else if (pop.equals("") && met.equals("") && !match) {
			query = "SELECT * FROM metropolises where continent LIKE \"%"
					+ cont + "%\"";
		} else if (pop.equals("") && !match) {
			query = "SELECT * FROM metropolises where continent LIKE \"%"
					+ cont + "%\" AND metropolis LIKE \"%" + met + "%\"";
		} else if (met.equals("") && !match) {
			query = "SELECT * FROM metropolises where continent LIKE \"%"
					+ cont + "%\" AND population" + symbol + "" + pop;
		} else if (cont.equals("") && !match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"%"
					+ met + "%\" AND population" + symbol + "" + pop;
		} else if (!match) {
			query = "SELECT * FROM metropolises where metropolis LIKE \"%"
					+ met + "%\" AND continent LIKE \"%" + cont
					+ "%\" AND population" + symbol + "" + pop;
		}
		return query;
	}

	/**
	 * Search query to search the database based on the given parameters.
	 * @param symbol ">","<" or "=" for the population combo box.
	 * @param pop value of the population field.
	 * @param met value of the metropolis field.
	 * @param cont value of the continent field.
	 * @param match "Exact" or "Partial Match" based on the combo box.
	 * @return
	 */
	public void getSearchQuery(String symbol, String pop, String met,
			String cont, boolean match) {
		try {
			if (pop.equals("") && met.equals("") && cont.equals("")) {
				this.getAllRows();
				fireTableDataChanged();
				return;
			}
			rowList = new ArrayList();
			this.conn = this.createConnection();
			Statement statement = conn.createStatement();
			String query = this.getQuery(symbol, pop, met, cont, match);
			statement.executeQuery("USE " + database);
			ResultSet results = statement.executeQuery(query);
			while (results.next()) {
				ArrayList cellList = new ArrayList();
				for (int i = 0; i < columnClasses.length; i++) {
					Object cellValue = null;
					if (columnClasses[i] == String.class)
						cellValue = results.getString(columnNames[i]);
					else if (columnClasses[i] == Integer.class)
						cellValue = new Integer(results.getInt(columnNames[i]));
					else
						System.out.println("Can't assign " + columnNames[i]);
					cellList.add(cellValue);
				}// for
				Object[] cells = cellList.toArray();
				rowList.add(cells);
				contents = new Object[rowList.size()][];
				for (int i = 0; i < contents.length; i++)
					contents[i] = (Object[]) rowList.get(i);
			}
			results.close();
			statement.close();
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
