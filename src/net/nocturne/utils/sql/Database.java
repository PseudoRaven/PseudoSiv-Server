package net.nocturne.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private Connection conn;
	private Statement stmt;

	private String host;
	private String user;
	private String pass;
	private String database;

	public Connection getConn() {
		return conn;
	}

	public Database(String host, String user, String pass, String data) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = data;
	}

	public boolean init() {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host
					+ ":3306/" + database, user, pass);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean initBatch() {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://" + host
					+ ":3306/" + database + "?rewriteBatchedStatements=true",
					user, pass);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int executeUpdate(String query) throws SQLException {
		this.stmt = this.conn.createStatement(1005, 1008);
		int results = stmt.executeUpdate(query);
		return results;
	}

	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}

	public ResultSet executeQuery(String query) {
		try {
			this.stmt = this.conn.createStatement(1005, 1008);
			ResultSet results = stmt.executeQuery(query);
			return results;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void destroyAll() {
		try {
			conn.close();
			stmt.close();
			conn = null;
			stmt = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
