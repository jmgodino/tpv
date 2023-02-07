package com.picoto.tpv.dto;

import java.math.BigDecimal;

public interface DetallesPagoIntf {


	public String getNrc();
	
	public String getNif();

	// Importe en euros, sin tasa de descuento
	public BigDecimal getImporte();

	// Importe en céntimos de euro con tasa de descuento incluida
	public String getImporteTpv();

	public String getDetalles();

	public boolean isPagoSeguro();

	public String getPagoSeguroTexto();

	public String getMetodoPago();
	
	public String getDetalleMetodoPago();

	public String getMarcaTarjeta();
	
	public String getDetalleMarcaTarjeta();

	public String getToken();

	public boolean isPagoBizum();

	public String getPaisTarjeta();
	
	public String getDetallePaisTarjeta();
	
	public String getTipoTarjeta();
	
	public String getDetalleTipoTarjeta();

	public String getTarjetaPSD2();


	public String getError();

	public String getCodigoError();

	public boolean isNecesitaConfirmacion();
	
	public boolean isConfirmacionCorrecta();

	public void setToken(String token);

	public void setError(String string);

	public void setCodigoError(String codResp);

	public void setNrc(String nrc);

	public void setImporteTpv(String importeTpv);

	public void setDetalles(String detalles);

	public void setPagoSeguro(String campo);

	public void setMetodoPago(String campo);

	public void setPaisTarjeta(String string);

	public void setMarcaTarjeta(String campo);

	public void setTipoTarjeta(String campo);

	public void setTarjetaPSD2(String campo);

}
