package tom.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyOwnJDBCProgram {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost:3306/RSEG_102";
		String username = "dev";
		String password = "dev";
		Connection conn = null;

		try {

			conn = DriverManager.getConnection(url, username, password);

			Statement stat = conn.createStatement();
			stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
			stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");
			ResultSet result = stat.executeQuery("SELECT * FROM Greetings");
			if (result.next())
				System.out.println(result.getString(1));
			result.close();
			stat.executeUpdate("DROP TABLE Greetings");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

}
