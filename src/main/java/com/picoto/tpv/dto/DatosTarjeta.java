package com.picoto.tpv.dto;

public class DatosTarjeta {

	private String pan;
	private String caducidad;
	private String cvv;

	public DatosTarjeta(String pan, String caducidad, String cvv) {
		super();
		this.pan = pan;
		this.caducidad = caducidad;
		this.cvv = cvv;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(String caducidad) {
		this.caducidad = caducidad;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return "DatosTarjeta [pan=" + pan + ", caducidad=" + caducidad + ", cvv=" + cvv + "]";
	}

}
