package com.picoto.tpv.service.intf;
import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.exceptions.TPVException;

public interface PostTpvIntf extends RedirectTpvIntf {

	public DetallesPagoIntf post(DatosPagoTpvRedsys datosPago) throws TPVException;
	
	public void open();
	
	public void close();

}