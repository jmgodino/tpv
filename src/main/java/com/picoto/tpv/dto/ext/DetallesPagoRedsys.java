package com.picoto.tpv.dto.ext;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.util.Utils;

public class DetallesPagoRedsys implements DetallesPagoIntf {

	public static final String TEXTO_CONFIRMAR_PREAUTORIZACION = "CONFIRMAR:";
	public static final String TEXTO_AUTORIZADO_PREAUTORIZACION = "AUTORIZADO:";
	public static final ResourceBundle paisesTarjeta = ResourceBundle.getBundle("paisesTarjeta");
	public static final ResourceBundle mediosPago = ResourceBundle.getBundle("mediosPago");
	public static final ResourceBundle marcasTarjeta = ResourceBundle.getBundle("marcasTarjeta");
	public static final ResourceBundle tiposTarjeta = ResourceBundle.getBundle("tiposTarjeta");

	private String nrc;
	private BigDecimal importe;
	private String importeTpv;
	private String detalles;
	private boolean pagoSeguro;
	private String metodoPago;
	private String marcaTarjeta;
	private String paisTarjeta;
	private String tipoTarjeta;
	private String tarjetaPSD2;
	private String token;
	private String error;
	private String codigoError;

	@Override
	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	@Override
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporteTpv(String importeTpv) {
		this.importeTpv = importeTpv;
		this.importe = new java.math.BigDecimal(importeTpv).divide(new java.math.BigDecimal("100"));
	}

	@Override
	public String getImporteTpv() {
		return importeTpv;
	}

	@Override
	public String getDetalles() {
		return detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	@Override
	public boolean isPagoSeguro() {
		return pagoSeguro;
	}

	public void setPagoSeguro(String pagoSeguro) {
		this.pagoSeguro = "1".equals(pagoSeguro);
	}

	@Override
	public String getPagoSeguroTexto() {
		return pagoSeguro ? "Si" : "No";
	}

	@Override
	public String getMetodoPago() {
		return metodoPago;
	}

	@Override
	public String getDetalleMetodoPago() {
		return mediosPago.getString(metodoPago);
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	@Override
	public String getMarcaTarjeta() {
		return marcaTarjeta;
	}

	@Override
	public String getDetalleMarcaTarjeta() {
		if (isPagoBizum()) {
			return "BIZUM";
		} else {
			return marcasTarjeta.getString(marcaTarjeta);
		}
	}

	public void setMarcaTarjeta(String marcaTarjeta) {
		this.marcaTarjeta = marcaTarjeta;
	}

	@Override
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public boolean isPagoBizum() {
		return "68".equals(metodoPago);
	}

	@Override
	public String getPaisTarjeta() {
		return paisTarjeta;
	}

	@Override
	public String getDetallePaisTarjeta() {
		if (isPagoBizum()) {
			return "Spain";
		} else {
			return paisesTarjeta.getString(paisTarjeta);
		}
	}

	public void setPaisTarjeta(String paisTarjeta) {
		this.paisTarjeta = paisTarjeta;
	}

	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	@Override
	public boolean necesitaConfirmacion() {
		return !Utils.esVacio(detalles) && detalles.indexOf(TEXTO_CONFIRMAR_PREAUTORIZACION) >= 0;
	}

	public boolean isConfirmacionCorrecta() {
		// 0000 para operacion 8 y 0900 para operacion 1
		return "0000".equals(codigoError) || "0900".equals(codigoError);
	}

	public String getNif() {
		return getSubcadena(getDetalles(), ":", 2, 9);
	}

	private String getSubcadena(String cadena, String separador, int posicion, int longitud) {
		String[] trozos = cadena.split(separador);
		if (trozos.length >= posicion) {
			return trozos[posicion - 1].substring(0,longitud);
		} else
			return null;
	}

	@Override
	public String toString() {
		return "DetallesPagoRedsys [nrc=" + nrc + ", importe=" + importe + ", importeTpv=" + importeTpv + ", detalles="
				+ detalles + ", pagoSeguro=" + pagoSeguro + ", metodoPago=" + metodoPago + ", marcaTarjeta="
				+ marcaTarjeta + ", paisTarjeta=" + paisTarjeta + ", token=" + token + ", error=" + error
				+ ", codigoError=" + codigoError + "]";
	}

	@Override
	public String getTipoTarjeta() {
		return tipoTarjeta;
	}

	@Override
	public String getTarjetaPSD2() {
		return tarjetaPSD2;
	}

	@Override
	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;

	}

	@Override
	public void setTarjetaPSD2(String tarjetaPSD2) {
		this.tarjetaPSD2 = tarjetaPSD2;
	}
	
	@Override
	public String getDetalleTipoTarjeta() {
		if (Utils.esVacio(tipoTarjeta)) {
			return tiposTarjeta.getString("X");
		}
		return tiposTarjeta.getString(tipoTarjeta);
	}

}
