package com.pruebas.springboot.di.app.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.pruebas.springboot.di.app.models.Alumno;
import com.pruebas.springboot.di.app.utils.ConnectionManager;

public class AlumnoManager {
	/*
	 * Sentencias SQL para ejecutar alguna sentencia en la base de datos se declara
	 * como una constante estatica
	 */
	private static final String SQL_INSERT = "INSERT INTO alumno (nombre, apellido) VALUES (?, ?)";
	private static final String SQL = "SELECT * FROM alumno";
	private static final String SQL_DELETE = "DELETE FROM alumno WHERE idalumno=?";
	private static final String SQL_MODIFY = "UPDATE alumno SET nombre=?, apellido=? WHERE idalumno=?";

	// funcion que retorna una coleccion, hace una lista de clase alumno
	public List<Alumno> getAllAlumnos() {// nombre de la funcion es getAllAlumnos
		/*
		 * try para excepcion sobre el error de conexion Forma de conexion con la base
		 * de datos SQL, le llama a connection manager y el metodo getConnection de el.
		 */
		try (Connection conn = ConnectionManager.getConnection(); 
				Statement statement = conn.createStatement()) {
			List<Alumno> listaAlumnos = new ArrayList<>();
			/*
			 * statement.executeQuery(sql)– Ejecute la consulta SELECT y devuelva un
			 * ResultSet
			 */
			ResultSet resultSet = statement.executeQuery(SQL);
			/* como es una lista entonces va a recorrer lo que encontro en la bd */
			while (resultSet.next()) {
				/*
				 * Se instancia la clase de alumno para ir asignando un valor inicial a los
				 * atributos, saca la info que hay en la bd saca de la base de datos con el
				 * resultset y obtiene con el get cada campo
				 */
				Alumno alumno = new Alumno();
				alumno.setIdalumno(resultSet.getInt("idalumno"));
				alumno.setApellido(resultSet.getString("apellido"));
				alumno.setNombre(resultSet.getString("nombre"));
				/* add es un metodo lista que agrega el objeto */
				listaAlumnos.add(alumno);
			}
			// cierra la consulta de la bd
			resultSet.close();
			// retorna la lista
			return listaAlumnos;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		// devuelve colecciones
		return Collections.EMPTY_LIST;
	}

	// Metodo para agregar alumno
	public Alumno add(String nombre, String apellido) throws SQLException {
		/*
		 * RETURN_GENERATED_KEYS para recuperar las claves que se han generado
		 * automáticamente al ejecutar una sentencia INSERT. Entonces se debe decalarar
		 * eso al conectar https://code-examples.net/es/q/1d391e
		 */
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_INSERT,
						Statement.RETURN_GENERATED_KEYS)) {
			// PreparedStatementpara insertar una fila en la base de datos.
			preparestatement.setString(1, nombre);
			preparestatement.setString(2, apellido);

			Alumno alumno = new Alumno();
			/*
			 * preparedStatement.executeUpdate() Normalmente para DML como INSERTAR,
			 * ACTUALIZAR, ELIMINAR la función executeUpdate() de Preparedstatement devuelve
			 * un número entero que proporciona el número de filas afectadas por una
			 * actualización particular de MySQL
			 */
			int affectedRows = preparestatement.executeUpdate();
			// si se cargo 0 filas entonces es error
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}

			/*
			 * getGeneratedKeys Recupera las claves generadas automáticamente creadas como
			 * resultado de la ejecución de este Statementobjeto. Si este Statementobjeto no
			 * generó ninguna clave, ResultSet se devuelve un objeto vacío. Lanza un error
			 * de sqlexception ESTE ERROR ES PARA EL RETURN_GENERATED_KEYS QUE SE DECLARO
			 * https://www.tabnine.com/code/java/methods/java.sql.Statement/getGeneratedKeys
			 */
			try (ResultSet generatedKeys = preparestatement.getGeneratedKeys()) {
				// Los generatedKeys.next()retornos true si el DB devolvió una clave generada.
				if (generatedKeys.next()) {
					alumno.setApellido(apellido);
					alumno.setNombre(nombre);
					// obtiene el int de la primera columna autogenerada e inicializa idalumno
					alumno.setIdalumno(generatedKeys.getInt(1));
					return alumno;
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw e;
		}

	}

	public boolean delete(int idalumno) {
		/* Hacer la conexion y leer el sql delete */
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_DELETE)) {
			/*
			 * el 1 señala el primer campo escrito en el SQL_DELETE y tiene que coincidir
			 * con el parametro que se le pasa para que sepa de que le estamos hablando
			 */
			preparestatement.setInt(1, idalumno);
			// para actualizar el sql
			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			return false; // retorna false si da error
		}
		return true; // retorna true si todo esta bien
	}

	public void modify(int idalumno, String nombre, String apellido) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement preparestatement = conn.prepareStatement(SQL_MODIFY)) {
			// los parametros que se le pasa tiene que coincidir en orden con el SQL
			// respectivamente
			preparestatement.setString(1, nombre);
			preparestatement.setString(2, apellido);
			preparestatement.setInt(3, idalumno);

			preparestatement.executeUpdate();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
	}

	// metodo para buscar el alumno
	public Alumno getByid(int idalumno) {
		// createStatement tenemos que componer nosotros la SQL
		try (Connection conn = ConnectionManager.getConnection(); Statement statement = conn.createStatement()) {
			// pasarle por executeQuery el SQL a leer
			ResultSet resultSet = statement.executeQuery(SQL);
			/*
			 * se utiliza para recuperar el siguiente resultado de un secuencia de múltiples
			 * resultados.
			 */
			while (resultSet.next()) {
				/*
				 * Si lo que se obtuvo es idalumno y coincide con lo que se le paso en parametro
				 * entonces crea un objeto alumno y asigna lo que se obtiene de cada select
				 */
				if (resultSet.getInt("idalumno") == idalumno) {
					Alumno alumno = new Alumno();
					alumno.setIdalumno(resultSet.getInt("idalumno"));
					alumno.setNombre(resultSet.getString("nombre"));
					alumno.setApellido(resultSet.getString("apellido"));
					// se cierra la sentencia del SQL abierto
					resultSet.close();
					// se retorna el objeto
					return alumno;
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return null;
	}
}
