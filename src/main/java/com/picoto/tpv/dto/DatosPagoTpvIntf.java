package com.picoto.tpv.dto;

public interface DatosPagoTpvIntf {

	public String getNrc();

	public String getImporteCentimos();

	public boolean isPagoDirecto();

	public String getConcepto();

	public String recuperaToken();

	public String getTitular();
	
	public String getDireccionIp();

	public String getIdioma();

	public String getOperacion();

	public String getMediopago();

	public boolean isPagoBizum();

	public boolean isPagoTarjeta();
	
	public boolean isPreautorizacion();
	
	public String getToken();
	
	public boolean isPagoInseguro();

	public String toString();

	public boolean isRedireccion();
	
}
