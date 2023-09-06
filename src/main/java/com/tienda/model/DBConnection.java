package com.tienda.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.sql.PreparedStatement;


public class DBConnection {
	private Connection connection;

	public DBConnection(String databaseConnection) throws SQLException {
		connection = DriverManager.getConnection(databaseConnection);
		
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void closeConnection() throws SQLException {

		if (this.connection != null)
			this.connection.close();
	};

	public ResultSet query(Map<String, Object> listaParametros, String query) throws SQLException {
		ResultSet result;
		int index = 1;

		PreparedStatement preparedStatement = this.connection.prepareStatement(query);

		for (Object paramValue : listaParametros.values()) {
			preparedStatement.setObject(index, paramValue);
			index += 1;
		}

		result = preparedStatement.executeQuery(query);
		preparedStatement.close();

		return result;
	}

	public boolean insert(Map<String, Object> listaParametros, String query) throws SQLException {
		boolean registrosAfectados = false;
		int index = 1;

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = this.connection.prepareStatement(query);

			for (Object paramValue : listaParametros.values()) {
				preparedStatement.setObject(index, paramValue);
				index += 1;
			}

			registrosAfectados = preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return registrosAfectados;
	}

	public int update(Map<String, Object> listaParametros, String query) throws SQLException {
		int registrosAfectados = 0;
		int index = 1;

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = this.connection.prepareStatement(query);

			for (Object paramValue : listaParametros.values()) {
				preparedStatement.setObject(index, paramValue);
				index += 1;
			}

			registrosAfectados = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return registrosAfectados;
	}

	public boolean delete(int id, String query) throws SQLException {
		boolean registrosAfectados = false;		

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = this.connection.prepareStatement(query);
			preparedStatement.setInt(1, id);

			registrosAfectados = preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return registrosAfectados;
	}

}
