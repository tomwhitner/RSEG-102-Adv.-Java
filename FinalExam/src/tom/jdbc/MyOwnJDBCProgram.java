package tom.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

public class MyOwnJDBCProgram {

	/*
	 * This program creates, populates and lists two database tables (DEPT and EMPLOYEE).
	 */
	public static void main(String[] args) {

		Connection conn = null;

		try {
			// create the connection
			conn = getConnection();
			try {

				// drop the tables if they happen to exist from a prior execution
				dropEmployeeTable(conn);
				dropDeptTable(conn);
	
				// create the tables
				createDeptTable(conn);
				createEmployeeTable(conn);
	
				// insert the department data
				insertDept(conn, 1, "Engineering");
				insertDept(conn, 2, "Sales Marketing");
				insertDept(conn, 3, "Human Resources");
	
				// insert the employee data
				insertEmployee(conn, 1, "Raghu Verabelli", "Burlington", 1);
				insertEmployee(conn, 2, "Joe Chase", "Santa Clara", 3);
				insertEmployee(conn, 3, "David Korbel", "Chicago", 2);
				insertEmployee(conn, 4, "John Maina", "New York", 2);
				insertEmployee(conn, 5, "Ivan Krylov ", "St. Petersburg", 1);
	
				// list the data in both tables to output
				listDept(conn);
				listEmployee(conn);
				
			} finally {
				// close and open prepared statements
				close();
				// close the connection
				conn.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	// DEPT SQL Statements
	private static final String DEPT_DROP_SQL = "DROP TABLE IF EXISTS DEPT;";

	private static final String DEPT_CREATE_SQL = "CREATE TABLE DEPT (" + "DEPT_ID INTEGER NOT NULL PRIMARY KEY, "
			+ "DEPT_NAME VARCHAR(40));";

	private static final String DEPT_INSERT_SQL = "INSERT INTO DEPT VALUES (?, ?);";

	private static final String DEPT_SELECT_SQL = "SELECT * FROM DEPT;";

	// EMPLOYEE SQL Statements
	private static final String EMPLOYEE_DROP_SQL = "DROP TABLE IF EXISTS EMPLOYEE;";

	private static final String EMPLOYEE_CREATE_SQL = "CREATE TABLE EMPLOYEE ("
			+ "EMP_ID INTEGER NOT NULL PRIMARY KEY, " + "NAME VARCHAR(40), " + "LOCATION VARCHAR(40), "
			+ "DEPT INTEGER, " + "CONSTRAINT FK_DEPT_ID FOREIGN KEY (DEPT) REFERENCES DEPT(DEPT_ID));";

	private static final String EMPLOYEE_INSERT_SQL = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?);";

	private static final String EMPLOYEE_SELECT_SQL = "SELECT * FROM EMPLOYEE;";
	
	private static PreparedStatement deptInsertStatement;
	private static PreparedStatement employeeInsertStatement;

	/*
	 * Retrieves database connect properties from the db.properties file,
	 * creates and returns a database connection.
	 */
	private static Connection getConnection() throws IOException, SQLException {
		// open the property file - in same folder as this class
		InputStream in = MyOwnJDBCProgram.class.getResourceAsStream ("db.properties");

		// Create and load the properties
		Properties props = new Properties();
		props.load(in);
		
		// close the stream
		in.close();

		// retrieve the driver property from the file		
		String drivers = props.getProperty("jdbc.drivers");
		// if property value is null, no driver was specified
		if (drivers != null) {
			// set the drivers
			System.setProperty("jdbc.drivers", drivers);
		}
		
		// get connection properties
		String url = props.getProperty("db.url");
		String username = props.getProperty("db.username");
		String password = props.getProperty("db.password");

		// create and return a new connection
		return DriverManager.getConnection(url, username, password);
	}
	
	private static void close() throws SQLException {
		if (deptInsertStatement != null) {
			deptInsertStatement.close();
			deptInsertStatement = null;
		}
		if (employeeInsertStatement != null) {
			employeeInsertStatement.close();
			employeeInsertStatement = null;
		}
	}

	/*
	 * Drops the DEPT table if it exists; otherwise, does nothing.
	 */
	private static void dropDeptTable(Connection conn) throws SQLException {
		executeSql(conn, DEPT_DROP_SQL);
	}

	/*
	 * Creates the DEPT table.
	 */
	private static void createDeptTable(Connection conn) throws SQLException {
		executeSql(conn, DEPT_CREATE_SQL);
	}

	/*
	 * Insert a DEPT record into the database; uses a prepare statement
	 */
	private static void insertDept(Connection conn, int id, String name) throws SQLException {
		// Create the prepared statement if necessary
		if (deptInsertStatement == null) {
			deptInsertStatement = conn.prepareStatement(DEPT_INSERT_SQL);
		}
		
		// log the SQL being executed
		System.out.println(String.format("Executing: '%s'", DEPT_INSERT_SQL));

		// bind the parameters
		deptInsertStatement.setInt(1, id);
		deptInsertStatement.setString(2, name);		
		
		// execute the statement
		deptInsertStatement.executeUpdate();
	}

	/*
	 * Select all DEPT records from the database and list to output.
	 */
	private static void listDept(Connection conn) throws SQLException {
		ResultSet rs = executeQuery(conn, DEPT_SELECT_SQL);
		displayResultSet(rs);
		// close the result set (also closes statement)
		rs.close();
	}

	/*
	 * Drops the EMPLOYEE table if it exists; otherwise, does nothing.
	 */
	private static void dropEmployeeTable(Connection conn) throws SQLException {
		executeSql(conn, EMPLOYEE_DROP_SQL);
	}

	/*
	 * Creates the EMPLOYEE table.
	 */
	private static void createEmployeeTable(Connection conn) throws SQLException {
		executeSql(conn, EMPLOYEE_CREATE_SQL);
	}

	/*
	 * Insert a EMPLOYEE record into the database; uses a prepare statement
	 */
	private static void insertEmployee(Connection conn, int id, String name, String location, int deptId)
			throws SQLException {
		// Create the prepared statement if necessary
		if (employeeInsertStatement == null) {
			employeeInsertStatement = conn.prepareStatement(EMPLOYEE_INSERT_SQL);
		}
		
		// log the SQL being executed
		System.out.println(String.format("Executing: '%s'", EMPLOYEE_INSERT_SQL));
		
		// bind the parameters
		employeeInsertStatement.setInt(1, id);
		employeeInsertStatement.setString(2, name);
		employeeInsertStatement.setString(3, location);
		employeeInsertStatement.setInt(4, deptId);
		
		// execute the statement
		employeeInsertStatement.executeUpdate();
	}

	/*
	 * Select all EMPLOYEE records from the database and list to output.
	 */
	private static void listEmployee(Connection conn) throws SQLException {
		ResultSet rs = executeQuery(conn, EMPLOYEE_SELECT_SQL);
		displayResultSet(rs);
		// close the result set (also closes statement)
		rs.close();
	}

	/*
	 * Execute any SQL statement that does not return a ResultSet.
	 * Also logs the SQL statement to output.
	 */
	private static int executeSql(Connection conn, String sql) throws SQLException {
		// log the statement
		System.out.println(String.format("Executing: '%s'", sql));
		// create the statement
		Statement stat = conn.createStatement();
		// execute the statement
		int count = stat.executeUpdate(sql);
		// close the statement
		stat.close();
		// return the count of affected rows
		return count;
	}

	/* 
	 * Executes and query that returns a ResultSet and display ResultSet (including column names) to output
	 * Also logs the SQL statement to output.
	 * The caller is responsible for closing the result set.  This will also close the statement.
	 */
	private static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
		// log the statement
		System.out.println(String.format("Querying:  '%s'", sql));
		// create the statement
		Statement stat = conn.createStatement();
		// execute the query and return the result set
		ResultSet resultSet = stat.executeQuery(sql);
		// return the result set.
		return resultSet;
	}
		
	/*
	 * The expected max column width of data.  Data will be trimmed to fit within this space.
	 */
	private static int COLUMN_WIDTH = 15;
	
	/*
	 * Formats and displays the result set to the screen.
	 */
	private static void displayResultSet(ResultSet result) throws SQLException {

		// retrieve result set meta data
		ResultSetMetaData metaData = result.getMetaData();
		
		// create an array of column names
		int colCount = metaData.getColumnCount();
		String[] columns = new String[colCount];
		for (int c=1; c<=colCount; c++) {
			columns[c-1] = metaData.getColumnLabel(c);
		}
		
		// initialize string builders for header and separator rows
		StringBuilder header = new StringBuilder(100);
		StringBuilder separator = new StringBuilder(100);
		
		// initialize column separator string 
		char[] colSepArray = new char[COLUMN_WIDTH];
		Arrays.fill(colSepArray, '-');
		String columnSeparator = new String(colSepArray);
		
		// prepare format strings - this allows column width to be changed more easily.
		String headerFormat = String.format("%%-%d.%ds | " , COLUMN_WIDTH, COLUMN_WIDTH);
		String separatorFormat = String.format("%%-%d.%ds + " , COLUMN_WIDTH, COLUMN_WIDTH);
		String dataRowFormat = headerFormat;  // same as header for now.

		// add initial characters to header and separator builders
		header.append("| ");
		separator.append("+ ");
		
		// loop through the columns
		for (String column : columns) {
			// add each column to header and separator builders
			header.append(String.format(headerFormat, column));
			separator.append(String.format(separatorFormat, columnSeparator));
		}
		
		// output the heading (3 lines)
		System.out.println(separator.toString());
		System.out.println(header.toString());
		System.out.println(separator.toString());

		// initialize string builder for data rows
		StringBuilder dataRow = new StringBuilder(100);
		
		// for each record in the result set
		while (result.next()) {
			// clear the builder
			dataRow.setLength(0);
			// add initial characters to data row
			dataRow.append("| ");
			// for each column
			for (String column : columns) {
				// append the column's value
				dataRow.append(String.format(dataRowFormat, result.getObject(column)));
			}
			// output the data row
			System.out.println(dataRow.toString());
		}
		// output final separator
		System.out.println(separator.toString());
	}
}
