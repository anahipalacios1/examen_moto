package com.pruebas.springboot.di.app.controller;

import com.pruebas.springboot.di.app.models.Moto;
import com.pruebas.springboot.di.app.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MotoManager {

	private static final String SQL_INSERT = "INSERT INTO moto (idproveedor, monto) VALUES (?,?)";
	private static final String SQL = "SELECT * FROM moto";
	private static final String SQL_DELETE = "DELETE FROM moto WHERE idmoto=?";
	private static final String SQL_MODIFY = "UPDATE moto SET monto=? WHERE idmoto=?";
	private static final int Monto = 300000;

	public List<Moto> getAllMoto() {

		try (Connection conn = ConnectionManager.getConnection(); Statement statement = conn.createStatement()) {
			List<Moto> lista = new ArrayList<>();

			ResultSet resultSet = statement.executeQuery(SQL);

			while (resultSet.next()) {
				Moto moto = new Moto();
				moto.setIdmoto(resultSet.getInt("idmoto"));
				moto.setIdproveedor(resultSet.getInt("idproveedor"));
				moto.setMonto(resultSet.getInt("monto"));
				lista.add(moto);
			}
			resultSet.close();
			return lista;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}

		return Collections.EMPTY_LIST;
	}

	public Moto add(int idproveedor) throws SQLException {
		try (Connection conn = ConnectionManager.getConnection();
	             PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
	                     Statement.RETURN_GENERATED_KEYS)) {
				int aumento = Monto*5;
	            preparestatement.setInt(1, idproveedor);
	            preparestatement.setInt(2, aumento);

	            Moto moto = new Moto();

	            int affectedRows = preparestatement.executeUpdate();

	            if (affectedRows == 0) {
	                throw new SQLException("Creating user failed, no rows affected.");
	            }

	            try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
	                if (generatedKeys.next()) {

	                    moto.setIdproveedor(idproveedor);
	                    moto.setMonto(aumento);

	                    moto.setIdmoto(generatedKeys.getInt(1));
	                    return moto;
	                } else {
	                    throw new SQLException("Creating user failed, no ID obtained.");
	                }
	            }
	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	            throw e;
	        }
	}

	public boolean delete(int idmoto) {

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {

			preparestatement.setInt(1, idmoto);

			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			return false;
		}
		return true;
	}

	public void modify(int idmoto, int monto) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)) {

			preparestatement.setInt(1, monto);
			preparestatement.setInt(2, idmoto);

			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
	}

	public Moto getByid(int idmoto) {

		try (Connection conn = ConnectionManager.getConnection(); Statement statement = conn.createStatement()) {

			ResultSet resultSet = statement.executeQuery(SQL);

			while (resultSet.next()) {

				if (resultSet.getInt("idmoto") == idmoto) {
					Moto moto = new Moto();
					moto.setIdmoto(resultSet.getInt("idmoto"));
					moto.setIdproveedor(resultSet.getInt("idproveedor"));
					moto.setMonto(resultSet.getInt("monto"));

					resultSet.close();
					return moto;
				}

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return null;
	}
}