package tom.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MyOwnJDBCProgram {

	public static void main(String[] args) {

		Connection conn = null;

		try {
			conn = getConnection();

			dropEmployeeTable(conn);
			dropDeptTable(conn);

			createDeptTable(conn);
			createEmployeeTable(conn);

			insertDept(conn, 1, "Engineering");
			insertDept(conn, 2, "Sales Marketing");
			insertDept(conn, 3, "Human Resources");

			insertEmployee(conn, 1, "Raghu Verabelli", "Burlington", 1);
			insertEmployee(conn, 2, "Joe Chase", "Santa Clara", 3);
			insertEmployee(conn, 3, "David Korbel", "Chicago", 2);
			insertEmployee(conn, 4, "John Maina", "New York", 2);
			insertEmployee(conn, 5, "Ivan Krylov ", "St. Petersburg", 1);

			listDept(conn);

			listEmployee(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// DEPT SQL Statements
	private static final String DEPT_DROP_SQL = "DROP TABLE IF EXISTS DEPT;";

	private static final String DEPT_CREATE_SQL = "CREATE TABLE DEPT (" + "DEPT_ID INTEGER NOT NULL PRIMARY KEY, "
			+ "DEPT_NAME VARCHAR(40));";

	private static final String DEPT_INSERT_SQL = "INSERT INTO DEPT VALUES (%d,'%s');";

	private static final String DEPT_SELECT_SQL = "SELECT * FROM DEPT;";

	// EMPLOYEE SQL Statements
	private static final String EMPLOYEE_DROP_SQL = "DROP TABLE IF EXISTS EMPLOYEE;";

	private static final String EMPLOYEE_CREATE_SQL = "CREATE TABLE EMPLOYEE ("
			+ "EMP_ID INTEGER NOT NULL PRIMARY KEY, " + "NAME VARCHAR(40), " + "LOCATION VARCHAR(40), "
			+ "DEPT INTEGER, " + "CONSTRAINT FK_DEPT_ID FOREIGN KEY (DEPT) REFERENCES DEPT(DEPT_ID));";

	private static final String EMPLOYEE_INSERT_SQL = "INSERT INTO EMPLOYEE VALUES (%d,'%s', '%s', %d);";

	private static final String EMPLOYEE_SELECT_SQL = "SELECT * FROM EMPLOYEE;";

	private static Connection getConnection() throws IOException, SQLException {
		Properties props = new Properties();
		InputStream in = MyOwnJDBCProgram.class.getResourceAsStream ("db.properties");

		props.load(in);
		in.close();

		String url = props.getProperty("db.url");
		String username = props.getProperty("db.username");
		String password = props.getProperty("db.password");

		return DriverManager.getConnection(url, username, password);
	}

	private static void dropDeptTable(Connection conn) throws SQLException {
		executeSql(conn, DEPT_DROP_SQL);
	}

	private static void createDeptTable(Connection conn) throws SQLException {
		executeSql(conn, DEPT_CREATE_SQL);
	}

	private static void insertDept(Connection conn, int id, String name) throws SQLException {
		executeSql(conn, String.format(DEPT_INSERT_SQL, id, name));
	}

	private static void listDept(Connection conn) throws SQLException {
		executeQuery(conn, DEPT_SELECT_SQL, "DEPT_ID", "DEPT_NAME");
	}

	private static void dropEmployeeTable(Connection conn) throws SQLException {
		executeSql(conn, EMPLOYEE_DROP_SQL);
	}

	private static void createEmployeeTable(Connection conn) throws SQLException {
		executeSql(conn, EMPLOYEE_CREATE_SQL);
	}

	private static void insertEmployee(Connection conn, int id, String name, String location, int deptId)
			throws SQLException {
		executeSql(conn, String.format(EMPLOYEE_INSERT_SQL, id, name, location, deptId));
	}

	private static void listEmployee(Connection conn) throws SQLException {
		executeQuery(conn, EMPLOYEE_SELECT_SQL, "EMP_ID", "NAME", "LOCATION", "DEPT");
	}

	private static void executeSql(Connection conn, String sql) throws SQLException {
		System.out.println(String.format("Executing: '%s'", sql));
		Statement stat = conn.createStatement();
		stat.execute(sql);
	}

	private static void executeQuery(Connection conn, String sql, String... columns) throws SQLException {
		System.out.println(String.format("Querying:  '%s'", sql));
		Statement stat = conn.createStatement();
		ResultSet result = stat.executeQuery(sql);

		StringBuilder header = new StringBuilder(100);
		StringBuilder separator = new StringBuilder(100);
		StringBuilder columnUnder = new StringBuilder(20);

		header.append("| ");
		separator.append("+ ");
		for (String column : columns) {
			header.append(String.format("%-20s | ", column));

			columnUnder.setLength(0);
			for (int i = 0; i < 20; i++) {
				columnUnder.append('-');
			}
			separator.append(String.format("%-20s + ", columnUnder.toString()));
		}
		System.out.println(separator.toString());
		System.out.println(header.toString());
		System.out.println(separator.toString());

		StringBuilder row = new StringBuilder(100);
		while (result.next()) {
			row.setLength(0);
			row.append("| ");
			for (String column : columns) {
				row.append(String.format("%-20s | ", result.getObject(column)));
			}
			System.out.println(row.toString());
		}
		System.out.println(separator.toString());
		result.close();
	}
}
