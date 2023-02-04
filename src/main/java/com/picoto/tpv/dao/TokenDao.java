package com.picoto.tpv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.picoto.tpv.exceptions.DAOException;
import com.picoto.tpv.util.Utils;


public class TokenDao {

	Connection con;
	
	public void destruir() throws SQLException {
		getConnection();
		
        String sql = "drop table tpv_tokens if exists";
         
        Statement statement = con.createStatement();
         
        statement.execute(sql);
        
        Utils.debug("Tabla eliminada");
        
        closeConnection(statement);

		
	}
	
	public void preparar() throws SQLException {
		 
				getConnection();
		
		        String sql = "create table tpv_tokens (nif varchar(9) primary key, token varchar(200), fecha date)";
		         
		        Statement statement = con.createStatement();
		         
		        statement.execute(sql);
		        
		        Utils.debug("Tabla creada");
		        
		        closeConnection(statement);
		 
	}
	
	
	public void registrarToken(String nif, String token, Date fecha) {
		
		try {
			getConnection();
			String sql = "insert into tpv_tokens (nif, token, fecha) values (?, ?, ?)";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nif);
			statement.setString(2, token);
			statement.setDate(3, new java.sql.Date(fecha.getTime()));
			int rows = statement.executeUpdate();
			if (rows > 0) {
				Utils.debug(String.format("Registro creado para NIF %s", nif));
			}
			closeConnection(statement);
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido registrar el token para %s", nif));
		}

	}
	
	public String getToken(String nif) {
		try {
			getConnection();
			String sql = "select token from tpv_tokens where nif = ? and fecha >= ?";
			String token = null;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nif);
			statement.setDate(2, new java.sql.Date(new Date().getTime()));
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				token = rs.getString(1);
				Utils.debug(String.format("Registro encontrado: %s", token));
			}
			rs.close();
			statement.close();
			closeConnection(statement); // finally
			if (token == null) {
				throw new DAOException(String.format("No se ha encontrado el token para %s", nif));
			} else {
				return token;
			} 
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido recuperar el token para %s", nif));
		}
	}
	
	public String getFecha(String nif) {
		try {
			getConnection();
			String sql = "select fecha from tpv_tokens where nif = ?";
			String fecha = null;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nif);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				fecha = rs.getString(1);
				Utils.debug(String.format("Registro encontrado: %s", fecha));
			}
			rs.close();
			statement.close();
			closeConnection(statement); // finally
			if (fecha == null) {
				throw new DAOException(String.format("No se ha encontrado la fecha para %s", nif));
			} else {
				return fecha;
			} 
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido recuperar la fecha para %s", nif));
		}
	}
	

	private void getConnection() throws SQLException {
        String jdbcURL = "jdbc:h2:./test";
		 
        con = DriverManager.getConnection(jdbcURL);

	}
	
	private void closeConnection(Statement statement) throws SQLException {
		statement.close();
		con.close();
	}
	
	public static void main (String args[]) throws SQLException {
		TokenDao t = new TokenDao();
		t.destruir();
		t.preparar();
		t.registrarToken("89890001K","asdfasdfasdfsadfasdfasf", new Date());
		t.getToken("89890001K");
		t.getFecha("89890001K");
	}
}
