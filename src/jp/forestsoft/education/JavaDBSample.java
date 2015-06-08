package jp.forestsoft.education;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JavaDBSample {

	public static void main(String[] args) throws SQLException {

		Connection conn = DriverManager.getConnection("jdbc:derby:/tmp/javadb/sample;create=true");

		try {
			drop(conn, "sample");

			Statement stat = conn.createStatement();
			stat.execute("create table sample (id numeric(3), str varchar(5))");
			stat.close();

			conn.setAutoCommit(false);
			stat = conn.createStatement();
			stat.execute("insert into sample values (1, 'a')");
			stat.execute("insert into sample values (2, 'b')");
			stat.execute("insert into sample values (3, 'c')");
			stat.close();

			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from sample");
			while (rs.next()) {
				System.out.println(rs.getInt("ID") + ", " + rs.getString("STR"));
			}
			rs.close();
			stat.close();
			conn.commit();

			conn.close();
		} finally {
			try {
				shutdown();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	public static void drop(Connection conn, String table) {
		try {
			Statement stat = conn.createStatement();
			stat.execute("drop table " + table);
			stat.close();
		} catch (SQLException e) {
			// ignore
		}
	}

	public static void shutdown() throws SQLException {
		DriverManager.getConnection("jdbc:derby:;shutdown=true");
	}

}
