/**
 * 
 */
package com.sas.mom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author gerkna
 * @about Provides Connection To MOM Live System
 * 
 */

public class DBConnectionProvider {
	private static final String dbUser = "sa";
	private static final String dbPass = "SASpw123";
	private static final String dbHost = "skopockmt";
	private static final String dbPort = "1433";
	private static final String jdbcString = "jdbc:sqlserver://" + dbHost
			+ ";port=" + dbPort + ";user=" + dbUser + ";password=" + dbPass
			+ ";databaseName=REWEMOM;";

	private static Connection connection = null;

	public DBConnectionProvider() {
	}

	public static Connection connect() {
		if (connection == null) {
			try {
				Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
				connection = DriverManager.getConnection(jdbcString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	public static boolean disconnect() {
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				return false;
			}
		connection = null;

		return true;
	}

	public static boolean isValid() {
		boolean result = false;
		if (connection != null) {
			try {
				result = connection.isValid(0);
			} catch (SQLException e) {
			}
		}

		return result;
	}
}
