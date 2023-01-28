package com.picoto.tpv.service.intf;

import com.picoto.tpv.dto.DatosPagoTpvIntf;
import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.exceptions.TPVException;

public interface RedirectTpvIntf {

	public void procesarPeticionTPV(DatosPagoTpvIntf datosPago) throws TPVException;
	public DetallesPagoIntf procesarRespuestaTPV(String datos, String firma) throws TPVException;

}