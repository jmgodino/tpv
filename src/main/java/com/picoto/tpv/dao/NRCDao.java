package com.picoto.tpv.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.picoto.tpv.exceptions.DAOException;
import com.picoto.tpv.util.Utils;


public class NRCDao {

	Connection con;
	
	public void destruir() throws SQLException {
		getConnection();
		
        String sql = "drop table tpv_nrcs if exists";
         
        Statement statement = con.createStatement();
         
        statement.execute(sql);
        
        Utils.debug("Tabla NRC eliminada");
        
        closeConnection(statement);

		
	}
	
	public void preparar() throws SQLException {
		 
				getConnection();
		
		        String sql = "create table tpv_nrcs (nrc varchar(22) primary key, nif varchar(9), importe decimal(13,2), fecha date, estado integer)";
		         
		        Statement statement = con.createStatement();
		         
		        statement.execute(sql);
		        
		        Utils.debug("Tabla NRC creada");
		        
		        closeConnection(statement);
		 
	}
	
	
	public void registrarNRC(String nif, String nrc, BigDecimal importe, Date fecha) {
		
		try {
			getConnection();
			String sql = "insert into tpv_nrcs (nif, nrc, importe, fecha, estado) values (?, ?, ?, ?, ?)";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nif);
			statement.setString(2, nrc);
			statement.setBigDecimal(3, importe);
			statement.setDate(4, new java.sql.Date(fecha.getTime()));
			statement.setInt(5, 1);
			int rows = statement.executeUpdate();
			if (rows > 0) {
				Utils.debug(String.format("Registro creado para NRC %s", nrc));
			}
			closeConnection(statement);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(String.format("No se ha podido registrar el NRC para %s", nrc));
		}

	}
	
	
	public void consolidarNRC(String nrc) {
		
		try {
			getConnection();
			String sql = "update tpv_nrcs set estado = 2 where nrc = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nrc);
			int rows = statement.executeUpdate();
			if (rows > 0) {
				Utils.debug(String.format("***** Registro consolidado para NRC %s *****", nrc));
			}
			closeConnection(statement);
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido consolidar el NRC para %s", nrc));
		}

	}
	
	public String getNRC(String nrc) {
		try {
			getConnection();
			String sql = "select nrc from tpv_nrcs where nrc = ?";
			String token = null;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nrc);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				token = rs.getString(1);
				Utils.debug(String.format("NRC encontrado: %s", token));
			}
			rs.close();
			statement.close();
			closeConnection(statement); // finally
			if (token == null) {
				throw new DAOException(String.format("No se ha encontrado el NRC para %s", nrc));
			} else {
				return token;
			} 
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido recuperar el token para %s", nrc));
		}
	}


	public List<String> getPagos(String nif) {
		try {
			getConnection();
			List<String> lista = new ArrayList<>();
			String sql = "select nrc, importe from tpv_nrcs where nif = ? order by fecha";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nif);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				lista.add("NRC: "+rs.getString(1)+" Importe: "+rs.getBigDecimal(2));
			}
			rs.close();
			statement.close();
			closeConnection(statement); // finally
			return lista;
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido recuperar la lista de NRCs para el NIF %s", nif));
		}
	}

	
	public boolean isConsolidado(String nrc) {
		try {
			getConnection();
			String sql = "select estado from tpv_nrcs where nrc = ?";
			int estado = 1;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nrc);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				estado = rs.getInt(1);
				Utils.debug(String.format("NRC encontrado para consultar su estado: %s", nrc));
			}
			rs.close();
			statement.close();
			closeConnection(statement); 
			if (estado == 1) {
				return false;
			} else {
				return true;
			} 
		} catch (Exception e) {
			throw new DAOException(String.format("No se ha podido recuperar el token para %s", nrc));
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
		NRCDao t = new NRCDao();
		t.destruir();
		t.preparar();
		String nrc1 = "9991234567890123456789";
		String nrc2 = "9991234567890123456788";
		t.registrarNRC("12345678Z",nrc1, new BigDecimal("123.45"), new Date());
		t.registrarNRC("12345678Z",nrc2, new BigDecimal("54.32"), new Date());
		t.getNRC(nrc1);
		Utils.debug("Consolidado? "+ t.isConsolidado(nrc1));
		t.consolidarNRC(nrc1);
		Utils.debug("Consolidado? "+ t.isConsolidado(nrc1));
		List<String> pagos = t.getPagos("12345678Z");
		for (String pago : pagos) {
			Utils.debug(pago);
		}
	}
}
