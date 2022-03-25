package com.pruebas.springboot.di.app.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.pruebas.springboot.di.app.models.Proveedor;
import com.pruebas.springboot.di.app.utils.ConnectionManager;

public class ProveedorManager {

	private static final String SQL_INSERT = "INSERT INTO proveedor (nombre) VALUES (?)";
	private static final String SQL = "SELECT * FROM proveedor";
	private static final String SQL_DELETE = "DELETE FROM proveedor WHERE idproveedor=?";
	private static final String SQL_MODIFY = "UPDATE proveedor SET nombre=? WHERE idproveedor=?";

	public List<Proveedor> getAllProveedores() {
		try (Connection conn = ConnectionManager.getConnection(); 
				Statement statement = conn.createStatement()) {
			List<Proveedor> listaProveedores = new ArrayList<>();
			
			ResultSet resultSet = statement.executeQuery(SQL);
			
			while (resultSet.next()) {
				Proveedor proveedor = new Proveedor();
				proveedor.setIdproveedor(resultSet.getInt("idproveedor"));
				proveedor.setNombre(resultSet.getString("nombre"));
				listaProveedores.add(proveedor);
			}
			resultSet.close();
			
			return listaProveedores;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return Collections.EMPTY_LIST;
	}

	public Proveedor add(String nombre) throws SQLException {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
						Statement.RETURN_GENERATED_KEYS)) {
			preparestatement.setString(1, nombre);

			Proveedor proveedor = new Proveedor();
			int affectedRows = preparestatement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
			try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
				
				if (generatedKeys.next()) {
					proveedor.setNombre(nombre);
					proveedor.setIdproveedor(generatedKeys.getInt(1));
					return proveedor;
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw e;
		}

	}

	public boolean delete(int idproveedor) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {
			preparestatement.setInt(1, idproveedor);
			
			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			return false;
		}
		return true;
	}

	public void modify(int idproveedor, String nombre) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)) {
			preparestatement.setString(1, nombre);
			preparestatement.setInt(2, idproveedor);

			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
	}

	public Proveedor getByid(int idproveedor) {
		try (Connection conn = ConnectionManager.getConnection(); Statement statement = conn.createStatement()) {
			
			ResultSet resultSet = statement.executeQuery(SQL);
			while (resultSet.next()) {
				if (resultSet.getInt("idproveedor") == idproveedor) {
					Proveedor proveedor = new Proveedor();
					proveedor.setIdproveedor(resultSet.getInt("idproveedor"));
					proveedor.setNombre(resultSet.getString("nombre"));
					resultSet.close();
					return proveedor;
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return null;
	}
}
